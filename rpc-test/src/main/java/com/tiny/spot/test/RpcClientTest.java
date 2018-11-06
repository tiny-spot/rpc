package com.tiny.spot.test;

import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;
import com.tiny.spot.client.DefaultRpcInvoker;
import com.tiny.spot.client.RpcClient;
import com.tiny.spot.common.IdWorker;
import com.tiny.spot.common.RemoteAddress;
import com.tiny.spot.common.RpcRequest;
import com.tiny.spot.common.RpcResponse;

import io.netty.channel.Channel;

public class RpcClientTest {
	public static void main(String[] args) {
		Channel channel = RpcClient.connect(new RemoteAddress("127.0.0.1", 8891));
		IdWorker idWorker = new IdWorker(0);
		for (int i = 0; i< 5; i++) {
			RpcRequest rpcRequest = new RpcRequest();
			rpcRequest.setRequestId(idWorker.nextId());
			channel.writeAndFlush(rpcRequest);
			RpcResponse rpcResponse = DefaultRpcInvoker.putRpcRequest(rpcRequest.getRequestId()).wait(5, TimeUnit.SECONDS);
			System.out.println(JSON.toJSONString(rpcResponse));
		}
	}
}
