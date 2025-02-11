package com.github.paicoding.forum.core.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * JsonUtil 类提供了一些实用方法，用于将 Java 对象与 JSON 字符串进行相互转换，
 * 以及自定义一些特殊类型（如 long, BigDecimal, BigInteger 等）的 JSON 序列化规则。
 *
 * 主要功能：
 * 1. 提供对象与 JSON 字符串的相互转换；
 * 2. 针对大数字类型（如 Long、BigDecimal）提供自定义序列化，避免 JavaScript 中精度丢失；
 * 3. 支持自定义序列化模块，便于在项目中扩展自定义的序列化需求。
 */
public class JsonUtil {

    private static final ObjectMapper jsonMapper = new ObjectMapper();

    /**
     * 将 JSON 字符串转换为指定类型的 Java 对象
     *
     * @param str JSON 字符串
     * @param clz 要转换成的目标类型
     * @param <T> 目标类型
     * @return 转换后的 Java 对象
     * @throws UnsupportedOperationException 如果 JSON 解析失败，则抛出异常
     */
    public static <T> T toObj(String str, Class<T> clz) {
        try {
            return jsonMapper.readValue(str, clz);
        } catch (Exception e) {
            throw new UnsupportedOperationException("Error while converting JSON to object", e);
        }
    }

    /**
     * 将 Java 对象转换为 JSON 字符串
     *
     * @param t Java 对象
     * @param <T> Java 对象类型
     * @return 转换后的 JSON 字符串
     * @throws UnsupportedOperationException 如果对象序列化失败，则抛出异常
     */
    public static <T> String toStr(T t) {
        try {
            return jsonMapper.writeValueAsString(t);
        } catch (Exception e) {
            throw new UnsupportedOperationException("Error while converting object to JSON", e);
        }
    }

    /**
     * 创建一个 Jackson 序列化模块，用于将 Long、BigDecimal、BigInteger 等类型序列化为字符串，
     * 以避免在 JavaScript 中出现精度丢失的问题。
     *
     * @return 自定义的 SimpleModule
     */
    public static SimpleModule bigIntToStrsimpleModule() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, newSerializer(s -> String.valueOf(s)));
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        simpleModule.addSerializer(long[].class, newSerializer((Function<Long, String>) String::valueOf));
        simpleModule.addSerializer(Long[].class, newSerializer((Function<Long, String>) String::valueOf));
        simpleModule.addSerializer(BigDecimal.class, newSerializer(BigDecimal::toString));
        simpleModule.addSerializer(BigDecimal[].class, newSerializer(BigDecimal::toString));
        simpleModule.addSerializer(BigInteger.class, ToStringSerializer.instance);
        simpleModule.addSerializer(BigInteger[].class, newSerializer((Function<BigInteger, String>) BigInteger::toString));
        return simpleModule;
    }

    /**
     * 根据传入的函数创建一个通用的 JSON 序列化器，
     * 用于将指定类型的对象转换为字符串（可用于单一对象或数组）。
     *
     * @param func 转换函数，将对象转换为字符串
     * @param <T> 对象类型
     * @param <K> 被转换的目标类型
     * @return 自定义的 JsonSerializer
     */
    public static <T, K> JsonSerializer<T> newSerializer(Function<K, String> func) {
        return new JsonSerializer<T>() {
            @Override
            public void serialize(T t, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                if (t == null) {
                    jsonGenerator.writeNull();
                    return;
                }

                if (t.getClass().isArray()) {
                    jsonGenerator.writeStartArray();
                    Stream.of(t).forEach(s -> {
                        try {
                            jsonGenerator.writeString(func.apply((K) s));
                        } catch (IOException e) {
                            throw new RuntimeException("Error serializing array element", e);
                        }
                    });
                    jsonGenerator.writeEndArray();
                } else {
                    jsonGenerator.writeString(func.apply((K) t));
                }
            }
        };
    }
}
