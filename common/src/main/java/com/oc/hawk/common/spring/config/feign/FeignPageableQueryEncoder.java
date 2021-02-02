package com.oc.hawk.common.spring.config.feign;

import com.oc.hawk.api.utils.JsonUtils;
import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FeignPageableQueryEncoder implements Encoder {
    private final Encoder delegate;

    public FeignPageableQueryEncoder() {
        HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter(JsonUtils.OBJECT_MAPPER);
        ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(jacksonConverter);
        delegate = new SpringEncoder(objectFactory);
    }

    @Override
    public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
        if (object instanceof Pageable) {
            Pageable pageable = (Pageable) object;
            template.query("page", pageable.getPageNumber() + "");
            template.query("size", pageable.getPageSize() + "");

            Collection<String> existingSorts = template.queries().get("sort");
            List<String> sortQueries = existingSorts != null ? new ArrayList<>(existingSorts) : new ArrayList<>();
            for (Sort.Order order : pageable.getSort()) {
                sortQueries.add(order.getProperty() + "," + order.getDirection());
            }
            template.query("sort", sortQueries);
        } else {
            delegate.encode(object, bodyType, template);
        }
    }
}
