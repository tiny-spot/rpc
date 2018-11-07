package com.tiny.spot.server;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;

import com.tiny.spot.common.RpcRequest;
import com.tiny.spot.common.RpcResponse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

	private SpringContainer springContainer;
	private static int CPUCORES = Runtime.getRuntime().availableProcessors();
	private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(CPUCORES, CPUCORES * 2, 60, TimeUnit.SECONDS,
			new ArrayBlockingQueue<>(CPUCORES * 1000), new ThreadPoolExecutorFactoryBean(),
			new ThreadPoolExecutor.DiscardPolicy());

	public RpcServerHandler() {
		super();
	}

	public RpcServerHandler(SpringContainer springContainer) {
		this.springContainer = springContainer;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcRequest rpcRequest) throws Exception {
		threadPoolExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					RpcResponse rpcResponse = new RpcResponse();
					rpcResponse.setRequestId(rpcRequest.getRequestId());
					try {
						rpcResponse.setResult(springContainer.invoker(rpcRequest));
					} catch (Throwable e) {
						rpcResponse.setErrorMsg(e.getMessage());
					}
					ctx.writeAndFlush(rpcResponse);
				} finally {
					ReferenceCountUtil.release(rpcRequest);
				}
			}
		});
	}

}
