package com.tiny.spot.client;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.tiny.spot.common.RemoteAddress;

public class DefaultRouterStrategy implements RouterStrategy {

	private AtomicInteger currIndex = new AtomicInteger(0);
	
	public static DefaultRouterStrategy INSTANCE = new DefaultRouterStrategy();
	
	@Override
	public RemoteAddress route(List<RemoteAddress> remoteAddress) {
		return remoteAddress.get(currIndex.incrementAndGet() % remoteAddress.size());
	}

}
