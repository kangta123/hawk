package com.oc.hawk.common.hibernate;

import com.oc.hawk.api.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import java.util.List;

@Slf4j
public class ArrayConverter implements AttributeConverter<List, String> {

    @Override
    public String convertToDatabaseColumn(List attribute) {
        return JsonUtils.object2Json(attribute);
    }

    @Override
    public List convertToEntityAttribute(String json) {
        if (json == null) {
            return null;
        }
        return JsonUtils.json2List(json, Object.class);

    }
}
