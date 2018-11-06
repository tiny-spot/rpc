package com.tiny.spot.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import com.tiny.spot.common.RpcRequest;
import com.tiny.spot.common.RpcResponse;
import com.tiny.spot.core.codec.NettyDecoder;
import com.tiny.spot.core.codec.NettyEncoder;
import com.tiny.spot.core.serialize.ProtostuffSerializer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class RpcServer {

	private ExecutorService executorService = Executors.newFixedThreadPool(1);
	private volatile AtomicBoolean startUp = new AtomicBoolean(false);
	private int port;
	private SpringContainer springContainer; 
	private NioEventLoopGroup bossGroup = null;
	private NioEventLoopGroup workerGroup = null;
	
	public RpcServer(int port) {
		super();
		this.port = port;
	}

	public void start(String configLocation) {
		if (startUp.get()) {
			return;
		}
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				bossGroup = new NioEventLoopGroup();
				workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);
				try {
					springContainer = new SpringContainer(configLocation);
					springContainer.init();
					
					ServerBootstrap serverBootstrap = new ServerBootstrap();
					serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
							.option(ChannelOption.SO_TIMEOUT, 100).option(ChannelOption.SO_BACKLOG, 128)
							.option(ChannelOption.TCP_NODELAY, true).option(ChannelOption.SO_REUSEADDR, true)
							.childOption(ChannelOption.SO_KEEPALIVE, true)
							.childHandler(new ChannelInitializer<SocketChannel>() {
								@Override
								protected void initChannel(SocketChannel ch) throws Exception {
									ch.pipeline().addLast(new NettyDecoder(RpcRequest.class, ProtostuffSerializer.INSTANCE))
											.addLast(new NettyEncoder(RpcResponse.class, ProtostuffSerializer.INSTANCE))
											.addLast(new RpcServerHandler(springContainer));
								}
							});
					ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
					startUp.compareAndSet(false, true);
					channelFuture.channel().closeFuture().sync();
				} catch (Throwable e) {
					bossGroup.shutdownGracefully();
					workerGroup.shutdownGracefully();
					throw new RuntimeException(e);
				}
			}
		});
	}
	
	public void shutdown() {
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
	}
	
	public boolean isAlive() {
		return startUp.get();
	}

}
