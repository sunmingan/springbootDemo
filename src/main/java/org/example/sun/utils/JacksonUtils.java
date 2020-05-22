package org.example.sun.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: suanjin
 * @date: 18/12/2017 1:38 PM
 *
 */
public class JacksonUtils {

    private static class MapperHolder {

        private static ObjectMapper mapper = createObjectMapper();
    }

    /**
     * 获取共享的单例 ObjectMapper 对象
     */
    public static ObjectMapper getObjectMapper() {
        return MapperHolder.mapper;
    }

    /**
     * 创建新的 ObjectMapper 对象
     */
    public static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper(getJsonFactory());
        //mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        //mapper.setPropertyNamingStrategy(PropertyNamingStrategy.);
        //mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);  //空也返回

        // 忽略字段变动
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 直接读取字段，不走 getter/setter 方法
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE);

        // for Java8 new date time apis
        SimpleModule module = new SimpleModule();
        module.addSerializer(Instant.class, new JsonSerializer<Instant>() {
            @Override
            public void serialize(Instant value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeNumber(value.toEpochMilli());
            }
        });
        module.addDeserializer(Instant.class, new JsonDeserializer<Instant>() {
            @Override
            public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return Instant.ofEpochMilli(p.getLongValue());
            }
        });

        mapper.registerModule(module);

        return mapper;
    }

    /**
     * 创建一个新的 JsonFactory 对象
     */
    public static JsonFactory getJsonFactory() {
        JsonFactory jsonFactory = new JsonFactory();
        jsonFactory.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        jsonFactory.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

        // 忽略不存在字段
        jsonFactory.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
        return jsonFactory;
    }

    /**
     * 将 json 转成 java bean
     */
    public static <T> T toBean(String json, Class<T> clazz) {
        try {
            return getObjectMapper().readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将 json 转成 java bean
     */
    public static <T> T toBean(String json, TypeReference type) {
        try {
            return getObjectMapper().readValue(json, type);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * 将 java bean 转成json
     */
    public static String toJson(Object bean) {
        try {
            return getObjectMapper().writeValueAsString(bean);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将 json 转成 JavaType
     */
    public static <T> T toBean(String json, JavaType valueType) {
        try {
            return getObjectMapper().readValue(json, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取泛型的Collection Type
     *
     * @param collectionClass 泛型的Collection
     * @param elementClasses 元素类
     * @return JavaType Java类型
     */
    public static JavaType getCollectionType(ObjectMapper mapper, Class<?> collectionClass, Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    /**
     * 将 json 转成 JavaType
     */
    public static <T> T toBean(String json, Class<?> collectionClass, Class<?>... elementClasses) {
        try {
            ObjectMapper objectMapper = getObjectMapper();
            return objectMapper.readValue(json, getCollectionType(objectMapper, collectionClass, elementClasses));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> jsonToList(String json, Class<T> tClass) throws IOException {
        JavaType javaType = getCollectionType(getObjectMapper(), ArrayList.class, tClass);
        return getObjectMapper().readValue(json, javaType);
    }

    /**
     * @desc: json-->java bean 驼峰
     * @date: 03/01/2018 9:52 AM
     * @param:
     *
     */
//    public static <T> T toLowerCamelBean(String json, Class<T> clazz) {
//        try {
//            ObjectMapper objectMapper = createObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE);
//
//            return objectMapper.readValue(json, clazz);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
}
