package com.tiny.spot.client;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.tiny.spot.common.RpcException;
import com.tiny.spot.common.RpcResponse;

public final class DefaultRpcInvoker implements Serializable {

	private static final long serialVersionUID = -1097571279327714896L;
	
	private static Map<Long, DefaultRpcInvoker> InvokerMaps = new ConcurrentHashMap<>();
	private CountDownLatch latch = new CountDownLatch(1);
	private Long requestId;
	private volatile RpcResponse rpcResponse;

	private DefaultRpcInvoker() {}
	
	public static void countDown(RpcResponse rpcResponse) {
		DefaultRpcInvoker defaultRpcInvoker = InvokerMaps.get(rpcResponse.getRequestId());
		if (defaultRpcInvoker != null) {
			defaultRpcInvoker.rpcResponse = rpcResponse;
			defaultRpcInvoker.latch.countDown();
		}
	}
	
	public static DefaultRpcInvoker putRpcRequest(long requestId) {
		DefaultRpcInvoker rpcInvoker = new DefaultRpcInvoker();
		rpcInvoker.requestId = requestId;
		InvokerMaps.put(requestId, rpcInvoker);
		return rpcInvoker;
	}

	public RpcResponse wait(long timeout, TimeUnit unit) {
		try {
			this.latch.await(timeout, unit);
			return rpcResponse;
		} catch (Throwable e) {
			throw new RpcException(e);
		} finally {
			InvokerMaps.remove(this.requestId);
		}
	}

}
