package com.tiny.spot.common;

import java.io.Serializable;

public class RemoteAddress implements Serializable {

	private static final long serialVersionUID = -3255579596011131416L;

	private String host;
	private int port;
	
	public RemoteAddress() {
		super();
	}
	public RemoteAddress(String host, int port) {
		super();
		this.host = host;
		this.port = port;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
}
