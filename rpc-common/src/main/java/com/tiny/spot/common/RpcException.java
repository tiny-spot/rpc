package com.tiny.spot.common;

public class RpcException extends RuntimeException {

	private static final long serialVersionUID = 3166884864693923979L;

	public RpcException() {
		super();
	}

	public RpcException(String message, Throwable cause) {
		super(message, cause);
	}

	public RpcException(String message) {
		super(message);
	}

	public RpcException(Throwable cause) {
		super(cause);
	}

}
