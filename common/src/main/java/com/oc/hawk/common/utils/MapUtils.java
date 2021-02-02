package com.oc.hawk.common.utils;

import com.google.common.collect.Maps;
import com.google.common.collect.Streams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MapUtils {
    public static Map<String, String> toMap(List<String> key, List<String> value) {
        Map<String, String> map = null;
        if (!CollectionUtils.isEmpty(key) && !CollectionUtils.isEmpty(value)) {
            map = Streams.zip(key.stream().filter(StringUtils::isNoneEmpty), value.stream().filter(StringUtils::isNoneEmpty), Maps::immutableEntry)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        return map;
    }

    public static <V, V2, K> Map<K, V2> transfer(Map<K, V> map, Function<V, V2> valueTransformer) {
        return transfer(map, Function.identity(), valueTransformer);
    }

    public static <V, V2, K, K2> Map<K2, V2> transfer(Map<K, V> map, Function<K, K2> keyTransformer, Function<V, V2> valueTransformer) {
        if (map == null) {
            return null;
        }
        Map<K2, V2> resultMap = Maps.newHashMapWithExpectedSize(map.size());
        map.forEach((k, v) -> resultMap.put(keyTransformer.apply(k), valueTransformer.apply(v)));
        return resultMap;
    }
}
