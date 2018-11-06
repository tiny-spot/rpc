package com.tiny.spot.server;

import com.tiny.spot.common.RpcRequest;
import com.tiny.spot.common.RpcResponse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

	private SpringContainer springContainer;

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
		try {
			RpcResponse rpcResponse = new RpcResponse();
			rpcResponse.setRequestId(rpcRequest.getRequestId());
			try {
				Thread.sleep(10000);
				rpcResponse.setResult(springContainer.invoker(rpcRequest));
			} catch (Throwable e) {
				rpcResponse.setErrorMsg(e.getMessage());
			}
			ctx.writeAndFlush(rpcResponse);
		} finally {
			ReferenceCountUtil.release(rpcRequest);
		}
	}

}
