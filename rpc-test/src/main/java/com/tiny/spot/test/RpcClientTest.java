package com.tiny.spot.test;

import java.util.ArrayList;
import java.util.List;

import com.tiny.spot.client.ProxyClient;
import com.tiny.spot.common.RemoteAddress;
import com.tiny.spot.test.service.HelloService;

public class RpcClientTest {

	public static void main(String[] args) {
		List<RemoteAddress> remoteAddresses = new ArrayList<>();
		RemoteAddress remoteAddress = new RemoteAddress("127.0.0.1", 8891);
		remoteAddresses.add(remoteAddress);
		HelloService helloService = ProxyClient.proxyClass(HelloService.class, remoteAddresses);
		System.out.println("client invoker:" + helloService.sayHai("fuck"));
	}

}
