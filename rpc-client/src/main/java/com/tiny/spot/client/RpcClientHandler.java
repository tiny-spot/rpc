package com.tiny.spot.client;

import com.tiny.spot.common.RpcResponse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
		DefaultRpcInvoker.countDown(msg);
	}
	
}
