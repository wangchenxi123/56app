package com.mierro.common.common;

import com.alibaba.fastjson.parser.deserializer.Jdk8DateCodec;
import com.alibaba.fastjson.parser.deserializer.OptionalCodec;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * Created by Simba on 2017/2/21.
 */
public class FastJsonConfig {

    public static void init() {
        SerializeConfig.getGlobalInstance().put(Long.class, longSerializer);
        SerializeConfig.getGlobalInstance().put(Date.class, dateSerializer);

        try { // 下面每一行都不能删除，只能改变put进去的Serializer
            SerializeConfig.getGlobalInstance().put(Class.forName("java.time.LocalDateTime"), localDateTimeSerializer);
            SerializeConfig.getGlobalInstance().put(Class.forName("java.time.LocalDate"), Jdk8DateCodec.instance);
            SerializeConfig.getGlobalInstance().put(Class.forName("java.time.LocalTime"), localTimeSerializer);
            SerializeConfig.getGlobalInstance().put(Class.forName("java.time.ZonedDateTime"), Jdk8DateCodec.instance);
            SerializeConfig.getGlobalInstance().put(Class.forName("java.time.OffsetDateTime"), Jdk8DateCodec.instance);
            SerializeConfig.getGlobalInstance().put(Class.forName("java.time.OffsetTime"), Jdk8DateCodec.instance);
            SerializeConfig.getGlobalInstance().put(Class.forName("java.time.ZoneOffset"), Jdk8DateCodec.instance);
            SerializeConfig.getGlobalInstance().put(Class.forName("java.time.ZoneRegion"), Jdk8DateCodec.instance);
            SerializeConfig.getGlobalInstance().put(Class.forName("java.time.Period"), Jdk8DateCodec.instance);
            SerializeConfig.getGlobalInstance().put(Class.forName("java.time.Duration"), Jdk8DateCodec.instance);
            SerializeConfig.getGlobalInstance().put(Class.forName("java.time.Instant"), Jdk8DateCodec.instance);

            SerializeConfig.getGlobalInstance().put(Class.forName("java.util.Optional"), OptionalCodec.instance);
            SerializeConfig.getGlobalInstance().put(Class.forName("java.util.OptionalDouble"), OptionalCodec.instance);
            SerializeConfig.getGlobalInstance().put(Class.forName("java.util.OptionalInt"), OptionalCodec.instance);
            SerializeConfig.getGlobalInstance().put(Class.forName("java.util.OptionalLong"), OptionalCodec.instance);
        } catch (ClassNotFoundException e) {}
    }

    /**json Long类型序列化为string*/
    public final static ObjectSerializer longSerializer = (serializer, object, fieldName, fieldType, features) -> {
        SerializeWriter out = serializer.getWriter();
        if ( object == null ) {
            if ( out.isEnabled(SerializerFeature.WriteNullNumberAsZero) ) {
                out.write("0");
            } else {
                out.writeNull();
            }
            return;
        }
        out.writeString(object.toString());
    };

    /**json Date类型序列化为string*/
    public final static ObjectSerializer dateSerializer = (serializer, object, fieldName, fieldType, features) -> {
        SerializeWriter out = serializer.getWriter();
        if ( object == null ) {
            out.writeNull();
        } else {
            out.writeString(Long.toString(((Date)object).getTime()));
        }
    };

    public static DateTimeFormatter localDateTimeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**json LocalDateTime 类型序列化为string*/
    public final static ObjectSerializer localDateTimeSerializer = (serializer, object, fieldName, fieldType, features) -> {
        SerializeWriter out = serializer.getWriter();
        if ( object == null ) {
            out.writeNull();
        } else {
            out.writeString(localDateTimeformatter.format((TemporalAccessor) object));
        }
    };
    public static DateTimeFormatter localTimeformatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**json LocalTime 类型序列化为string*/
    public final static ObjectSerializer localTimeSerializer = (serializer, object, fieldName, fieldType, features) -> {
        SerializeWriter out = serializer.getWriter();
        if ( object == null ) {
            out.writeNull();
        } else {
            out.writeString(localTimeformatter.format((TemporalAccessor) object));
        }
    };

}
