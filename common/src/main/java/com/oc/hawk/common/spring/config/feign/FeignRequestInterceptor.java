package com.oc.hawk.common.spring.config.feign;

import com.google.common.base.Joiner;
import com.oc.hawk.api.constant.Headers;
import com.oc.hawk.api.utils.JsonUtils;
import com.oc.hawk.common.utils.WebUtils;
import feign.Request;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@Slf4j
public class FeignRequestInterceptor implements feign.RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        template.header("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        template.header("x-forwarded-for", WebUtils.getClientIp());

        if (template.method().equalsIgnoreCase(HttpMethod.GET.name()) && template.requestBody() != null) {
            //params store in request body by default.
            byte[] bytes = template.requestBody().asBytes();

            if (Objects.nonNull(bytes)) {
                String queryBody = new String(bytes);
                try {

                    Map<String, Object> stringObjectMap = JsonUtils.OBJECT_MAPPER.readValue(queryBody, HashMap.class);
                    template.body(Request.Body.empty());
                    stringObjectMap.forEach((key, value) -> {
                        if (value instanceof Collection) {
                            template.query(key, Joiner.on(",").join((Collection) value));
                        } else {
                            template.query(key, String.valueOf(value));
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    log.warn("query param format maybe not json, query body: {}", queryBody);
                }
            }
        }
        try {
            WebUtils.getCurrentHttpRequest().ifPresent(httpServletRequest -> {
                template.header(Headers.AUTH_ID, httpServletRequest.getHeader(Headers.AUTH_ID));
            });
        } catch (BeanCreationException e) {
            log.error("no current account holder");
        }
    }

    public Optional<HttpServletRequest> getCurrentHttpRequest() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
            .filter(requestAttributes -> ServletRequestAttributes.class.isAssignableFrom(requestAttributes.getClass()))
            .map(requestAttributes -> ((ServletRequestAttributes) requestAttributes))
            .map(ServletRequestAttributes::getRequest);
    }
}
