package com.oc.hawk.api.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class JsonUtils {

    public static final ObjectMapper OBJECT_MAPPER = createObjectMapper(false);
    public static final ObjectMapper SNAKE_OBJECT_MAPPER = createObjectMapper(true);

    /**
     * 初始化ObjectMapper
     *
     * @return
     */
    private static ObjectMapper createObjectMapper(boolean snake) {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setLocale(Locale.CHINA);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, new CustomLocalDateTimeDeserializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        javaTimeModule.addDeserializer(LocalDate.class, new CustomLocalDateDeserializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        objectMapper.registerModule(javaTimeModule);

        if (snake) {
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        }
        return objectMapper;
    }


    /**
     * 将 json 字段串转换为 对象.
     *
     * @param json  字符串
     * @param clazz 需要转换为的类
     * @return
     */
    public static <T> T json2Object(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException("将 Json 转换为对象时异常,数据是:" + json, e);
        }
    }


    public static <T> T map2Object(Map data, Class<T> cls) {
        return OBJECT_MAPPER.convertValue(data, cls);
    }

    public static String object2Json(Object o) {
        if (Objects.isNull(o)) {
            return "";
        }
        StringWriter sw = new StringWriter();
        JsonGenerator gen = null;
        try {
            gen = new JsonFactory().createGenerator(sw);
            OBJECT_MAPPER.writeValue(gen, o);
        } catch (IOException e) {
            throw new RuntimeException("不能序列化对象为Json", e);
        } finally {
            if (null != gen) {
                try {
                    gen.close();
                } catch (IOException e) {
                    throw new RuntimeException("不能序列化对象为Json", e);
                }
            }
        }
        return sw.toString();
    }

    public static Map<String, Object> object2Map(Object o) {
        return OBJECT_MAPPER.convertValue(o, Map.class);
    }


    /**
     * 将 json 字段串转换为 List.
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> List<T> json2List(String json, Class<T> clazz) {
        JavaType type = OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz);

        List<T> list = null;
        try {
            list = OBJECT_MAPPER.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("错误Json数据格式", e);
        }
        return list;
    }


    /**
     * 将 json 字段串转换为 数据.
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T[] json2Array(String json, Class<T[]> clazz) throws IOException {
        return OBJECT_MAPPER.readValue(json, clazz);

    }

    public static <T> T node2Object(JsonNode jsonNode, Class<T> clazz) {
        try {
            T t = OBJECT_MAPPER.treeToValue(jsonNode, clazz);
            return t;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("将 Json 转换为对象时异常,数据是:" + jsonNode.toString(), e);
        }
    }

    public static Map<String, Object> json2Map(String json) {
        try {
            if (StringUtils.isEmpty(json)) {
                return null;
            }
            return OBJECT_MAPPER.readValue(json, new TypeReference<>() {

            });
        } catch (IOException e) {
            throw new RuntimeException("将 Json 转换为MAP时异常,数据是:" + json, e);
        }
    }

    public static JsonNode object2Node(Object o) {
        try {
            if (o == null) {
                return OBJECT_MAPPER.createObjectNode();
            } else {
                return OBJECT_MAPPER.convertValue(o, JsonNode.class);
            }
        } catch (Exception e) {
            throw new RuntimeException("不能序列化对象为Json", e);
        }
    }

    private static String eraseText(JsonParser parser, String sign) throws IOException {
        String text = parser.getText().trim();
        if (text.contains(".")) {
            text = StringUtils.substringBeforeLast(text, ".");
        }
        if (text.contains(sign)) {
            text = StringUtils.substringBeforeLast(text, sign);
        }
        return text;
    }

    static class CustomLocalDateTimeDeserializer extends LocalDateTimeDeserializer {

        public CustomLocalDateTimeDeserializer(DateTimeFormatter formatter) {
            super(formatter);
        }

        @Override
        public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            try {
                if (NumberUtils.isDigits(parser.getText())) {
                    return new Timestamp(NumberUtils.createLong(parser.getText())).toLocalDateTime();
                } else {
                    String text = eraseText(parser, "+");

                    if (text.length() <= 20) {
                        if (text.contains("T")) {
                            return LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
                        } else {
                            return LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        }
                    }
                }
            } catch (Exception e) {
                //ignore
            }
            return super.deserialize(parser, context);
        }


    }

    static class CustomLocalDateDeserializer extends LocalDateDeserializer {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        public CustomLocalDateDeserializer(DateTimeFormatter formatter) {
            super(formatter);
        }

        @Override
        public LocalDate deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            if (parser.hasToken(JsonToken.VALUE_STRING)) {
                String text = eraseText(parser, "T");
                if (text.length() == 10) {
                    return LocalDate.parse(text, formatter);
                }
            } else {
                try {
                    if (parser.getText().length() < 8) {
                        return null;
                    }
                    if (parser.getNumberType().equals(JsonParser.NumberType.LONG)) {
                        return new Timestamp(NumberUtils.createLong(parser.getText())).toLocalDateTime().toLocalDate();
                    }
                } catch (Exception e) {
                    //ignore
                }
            }
            return super.deserialize(parser, context);
        }
    }

}
