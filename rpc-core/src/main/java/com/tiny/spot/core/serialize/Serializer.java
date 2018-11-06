package com.tiny.spot.core.serialize;

public interface Serializer {

	<T> byte[] serialize(T obj);
	<T> Object deserialize(byte[] bytes, Class<T> clazz);
	
}
