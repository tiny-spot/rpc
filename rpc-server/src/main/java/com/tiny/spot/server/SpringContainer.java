package com.tiny.spot.server;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tiny.spot.common.RpcRequest;
import com.tiny.spot.common.ServiceRemote;

@SuppressWarnings("all")
public class SpringContainer {
	
	private static Map<String, Object> serviceObjects = new ConcurrentHashMap<>();

	private String configLocation;
	
	public SpringContainer() {
		super();
	}
	
	public SpringContainer(String configLocation) {
		this.configLocation = configLocation;
	}
	
	public void init() {
		ApplicationContext context = new ClassPathXmlApplicationContext(configLocation);
		Map<String, Object> map = context.getBeansWithAnnotation(ServiceRemote.class);
		if (map != null && !map.isEmpty()) {
			for (Object object : map.values()) {
				String serviceClazz = object.getClass().getAnnotation(ServiceRemote.class).serviceClazz().getCanonicalName();
				serviceObjects.put(serviceClazz, object);
			}
		}
	}
	
	public Object invoker(RpcRequest rpcRequest) throws Throwable {
		Object object = serviceObjects.get(rpcRequest.getClassName());
		Method method = object.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
		return method.invoke(object, rpcRequest.getParameters());
	}
	
}
