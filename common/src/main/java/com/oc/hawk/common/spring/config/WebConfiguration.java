package com.oc.hawk.common.spring.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.oc.hawk.api.constant.AccountHolder;
import com.oc.hawk.api.constant.Headers;
import com.oc.hawk.api.utils.JsonUtils;
import com.oc.hawk.common.utils.AccountHolderUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebMvc
@Slf4j
@RequiredArgsConstructor
@IgnoreScan
public class WebConfiguration implements WebMvcConfigurer {
    private final ApplicationContext context;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setUseIsoFormat(true);
        registrar.registerFormatters(registry);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptorAdapter() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                String idHeader = request.getHeader(Headers.AUTH_ID);
                if (StringUtils.isNotEmpty(idHeader)) {
                    long id = Long.parseLong(idHeader);
                    AccountHolder accountHolder = new AccountHolder(id);
                    AccountHolderUtils.setCurrent(accountHolder);
                }

                return super.preHandle(request, response, handler);
            }

            @Override
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
                AccountHolderUtils.removeAccountHolder();
            }
        });
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter jackson2Convert = context.getBean(MappingJackson2HttpMessageConverter.class);
        ObjectMapper objectMapper = JsonUtils.OBJECT_MAPPER;
        objectMapper.addMixIn(Page.class, PageMixIn.class);

        jackson2Convert.setObjectMapper(objectMapper);
        converters.removeIf(converter -> converter instanceof MappingJackson2HttpMessageConverter);
        converters.add(jackson2Convert);

        converters.stream()
                .filter(c -> c instanceof StringHttpMessageConverter)
                .findFirst()
                .ifPresent(messageConverter -> ((StringHttpMessageConverter) messageConverter).setDefaultCharset(StandardCharsets.UTF_8));

    }

    @Bean
    public RestTemplate restTemplate() {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderRequestInterceptor("Accept", MediaType.APPLICATION_JSON_VALUE));


        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new ResourceHttpMessageConverter());
        restTemplate.setInterceptors(interceptors);

        return restTemplate;
    }

    @JsonDeserialize(as = RestPageImpl.class)
    private interface PageMixIn {
    }

    static class HeaderRequestInterceptor implements ClientHttpRequestInterceptor {

        private final String headerName;

        private final String headerValue;

        public HeaderRequestInterceptor(String headerName, String headerValue) {
            this.headerName = headerName;
            this.headerValue = headerValue;
        }

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            request.getHeaders().set(headerName, headerValue);
            return execution.execute(request, body);
        }
    }

}
