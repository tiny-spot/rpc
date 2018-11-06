package com.tiny.spot.test.service;

import org.springframework.stereotype.Service;

import com.tiny.spot.common.ServiceRemote;

@Service
@ServiceRemote(serviceClazz = HelloService.class)
public class HelloServiceImpl implements HelloService {

	@Override
	public String sayHai(String hello) {
		return "sayHai:" + hello;
	}

}
