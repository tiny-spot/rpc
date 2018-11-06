package com.tiny.spot.server;

import java.util.UUID;

import com.tiny.spot.common.RpcRequest;
import com.tiny.spot.common.RpcResponse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
		try {
			RpcResponse rpcResponse = new RpcResponse();
			rpcResponse.setRequestId(msg.getRequestId());
			rpcResponse.setResult("back from server:" + UUID.randomUUID().toString());
			ctx.writeAndFlush(rpcResponse);
		} finally {
			ReferenceCountUtil.release(msg);
		}
	}

}
