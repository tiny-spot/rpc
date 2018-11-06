package com.tiny.spot.test;

import com.tiny.spot.server.RpcServer;

public class RpcServerTest {
	public static void main(String[] args) {
		RpcServer rpcServer = new RpcServer(8891);
		rpcServer.start("applicationContext.xml");
		while (!rpcServer.isAlive()) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("rpc server has started!");
	}
}
