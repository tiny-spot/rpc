package com.tiny.spot.common;

import java.io.Serializable;

public class RpcRequest implements Serializable  {

	private static final long serialVersionUID = -8845745799907337640L;

	private long requestId;
	private String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;
    
	public long getRequestId() {
		return requestId;
	}
	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}
	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}
	public Object[] getParameters() {
		return parameters;
	}
	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}
	
}
