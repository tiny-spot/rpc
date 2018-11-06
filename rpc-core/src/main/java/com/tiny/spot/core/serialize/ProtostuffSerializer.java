package com.tiny.spot.core.serialize;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import com.tiny.spot.common.RpcException;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

public class ProtostuffSerializer implements Serializer {

	private static Objenesis objenesis = new ObjenesisStd(true);
	private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<Class<?>, Schema<?>>();

	public static Serializer INSTANCE = new ProtostuffSerializer();

	private static <T> Schema<T> getSchema(Class<T> cls) {
		@SuppressWarnings("unchecked")
		Schema<T> schema = (Schema<T>) cachedSchema.get(cls);
		if (schema == null) {
			schema = RuntimeSchema.createFrom(cls);
			if (schema != null) {
				cachedSchema.put(cls, schema);
			}
		}
		return schema;
	}

	public <T> byte[] serialize(T obj) {
		@SuppressWarnings("unchecked")
		Class<T> cls = (Class<T>) obj.getClass();
		LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
		try {
			Schema<T> schema = getSchema(cls);
			return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
		} catch (Exception e) {
			throw new RpcException(e.getMessage(), e);
		} finally {
			buffer.clear();
		}
	}

	public <T> Object deserialize(byte[] bytes, Class<T> clazz) {
		try {
			T message = (T) objenesis.newInstance(clazz);
			Schema<T> schema = getSchema(clazz);
			ProtostuffIOUtil.mergeFrom(bytes, message, schema);
			return message;
		} catch (Exception e) {
			throw new RpcException(e.getMessage(), e);
		}
	}

}
