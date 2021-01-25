package com.oc.hawk.common.hibernate;

import com.oc.hawk.api.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import java.util.Map;

@Slf4j
public class HashMapConverter implements AttributeConverter <Map <String, Object>, String> {
    @Override
    public String convertToDatabaseColumn(Map <String, Object> customerInfo) {
        return JsonUtils.object2Json(customerInfo);
    }

    @Override
    public Map <String, Object> convertToEntityAttribute(String customerInfoJson) {
        return JsonUtils.json2Map(customerInfoJson);
    }
}
