package com.oc.hawk.api.constant;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class KafkaTopic {
    public final static String DOMAIN_EVENT_TOPIC = "HAWK-DOMAIN-EVENT";
    public final static String RUNTIME_OUTPUT_TOPIC = "HAWK-RUNTIME-OUTPUT-EVENT";

    public final static String INFRASTRUCTURE_RESOURCE_TOPIC = "HAWK-INFRASTRUCTURE-RESOURCE-UPDATE-EVENT";

    public static List<String> getTopics() {
        return Arrays.stream(KafkaTopic.class.getFields()).map(f -> {
            try {
                return f.get(null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }).filter(Objects::nonNull).map(String::valueOf).collect(Collectors.toList());
    }

}
