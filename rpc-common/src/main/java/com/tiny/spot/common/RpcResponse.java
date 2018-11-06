package com.tiny.spot.common;

import java.io.Serializable;

public class RpcResponse implements Serializable {

	private static final long serialVersionUID = 8259696813436797896L;

	private long requestId;
	private Object result;
	private String errorMsg;
	
	public long getRequestId() {
		return requestId;
	}
	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
}
