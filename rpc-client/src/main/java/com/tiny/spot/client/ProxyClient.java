package com.tiny.spot.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.tiny.spot.common.IdWorker;
import com.tiny.spot.common.RemoteAddress;
import com.tiny.spot.common.RpcException;
import com.tiny.spot.common.RpcRequest;
import com.tiny.spot.common.RpcResponse;

import io.netty.channel.Channel;

public class ProxyClient {

	private static IdWorker idWorker = new IdWorker(0);

	@SuppressWarnings("all")
	public static <T> T proxyClass(Class<T> clazz, List<RemoteAddress> remoteAddresses) {
		return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[] { clazz },
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						RpcRequest rpcRequest = new RpcRequest();
						rpcRequest.setClassName(clazz.getCanonicalName());
						rpcRequest.setMethodName(method.getName());
						rpcRequest.setParameters(args);
						rpcRequest.setParameterTypes(method.getParameterTypes());
						rpcRequest.setRequestId(idWorker.nextId());
						DefaultRouterStrategy defaultRouterStrategy = new DefaultRouterStrategy();
						Channel channel = RpcClient.connect(defaultRouterStrategy.route(remoteAddresses));
						channel.writeAndFlush(rpcRequest);
						RpcResponse rpcResponse = DefaultRpcInvoker.putRpcRequest(rpcRequest.getRequestId()).wait(5,
								TimeUnit.SECONDS);
						if (rpcResponse == null) {
							throw new RpcException("TimeoutException");
						}
						if (rpcResponse.getErrorMsg() != null) {
							throw new RpcException(rpcResponse.getErrorMsg());
						}
						return rpcResponse.getResult();
					}
				});
	}

}
