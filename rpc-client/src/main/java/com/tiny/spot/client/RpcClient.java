package com.tiny.spot.client;

import java.util.HashMap;
import java.util.Map;

import com.tiny.spot.common.RemoteAddress;
import com.tiny.spot.common.RpcRequest;
import com.tiny.spot.common.RpcResponse;
import com.tiny.spot.core.codec.NettyDecoder;
import com.tiny.spot.core.codec.NettyEncoder;
import com.tiny.spot.core.serialize.ProtostuffSerializer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class RpcClient {

	private static Map<String, Channel> ConnectionManager = new HashMap<>();
	private static final String CodeSeperator = ":";

	private RpcClient() {}

	public static synchronized Channel connect(RemoteAddress remoteAddress) {
		String address = remoteAddress.getHost() + CodeSeperator + remoteAddress.getPort();
		if (ConnectionManager.containsKey(address)) {
			Channel channel = ConnectionManager.get(address);
			if (channel != null && channel.isActive()) {
				return channel;
			}
		}
		
		Bootstrap bootstrap = new Bootstrap();
		EventLoopGroup group = new NioEventLoopGroup();
		bootstrap.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(new NettyEncoder(RpcRequest.class, ProtostuffSerializer.INSTANCE))
						.addLast(new NettyDecoder(RpcResponse.class, ProtostuffSerializer.INSTANCE))
						.addLast(new RpcClientHandler());
			}

		}).option(ChannelOption.TCP_NODELAY, true).option(ChannelOption.SO_REUSEADDR, true)
				.option(ChannelOption.SO_KEEPALIVE, true);
		;
		Channel channel;
		try {
			channel = bootstrap.connect(remoteAddress.getHost(), remoteAddress.getPort()).sync().channel();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		ConnectionManager.put(address, channel);
		return channel;
	}

}