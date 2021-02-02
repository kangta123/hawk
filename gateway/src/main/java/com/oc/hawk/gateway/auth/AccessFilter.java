package com.oc.hawk.gateway.auth;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oc.hawk.api.constant.AuthKey;
import com.oc.hawk.api.constant.Headers;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 权限认证过滤器
 *
 * @author kangta123
 */
@Slf4j
public class AccessFilter implements WebFilter, Ordered {
    public static final String WEBSOCKET_PROTOCOL = "websocket";
    public static final int MIN_TOKEN_LENGTH = 10;
    private static final String TAG_KEY = "Bearer";
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(AuthKey.KEY.getBytes());
    @Autowired
    private AuthProperties authProperties;

    private static Map<String, List<String>> getAuthentication(String token) {
        if (token != null) {
            try {
                Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token.replace(TAG_KEY, ""))
                    .getBody();

                String subject = claims.getSubject();
                Map<String, List<String>> headers = Maps.newHashMap();
                Object id = claims.get("id");

                if (Objects.isNull(id) || Objects.isNull(subject)) {
                    return null;
                }

                headers.put(Headers.AUTH_ID, Lists.newArrayList(subject));
                return headers;
            } catch (Exception e) {
                log.error("Auth failed with {}", e.getMessage());
                return null;
            }
        }
        return null;
    }

    @PostConstruct
    public void setup() {
        log.info("Authorization white list {}", authProperties.getWhiteList());
    }

    private Mono<Void> forward(ServerWebExchange exchange, WebFilterChain chain, Map<String, List<String>> authentication) {
        ServerHttpRequest forwardRequest = Objects.nonNull(authentication) ? exchange.getRequest()
            .mutate()
            .headers(httpHeaders -> httpHeaders.putAll(authentication))
            .build() : exchange.getRequest();

        return chain.filter(exchange.mutate().request(forwardRequest).build());
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        log.info("Request denied {}, ", exchange.getRequest().getPath().value());
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);

        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        exchange.getResponse().getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        log.info("Request url {} {}:", request.getMethod(), request.getURI());
        if (Objects.equals(request.getHeaders().getUpgrade(), WEBSOCKET_PROTOCOL)) {
            return forward(exchange, chain, null);
        }

        String requestPath = request.getURI().getPath();
        HttpHeaders headers = request.getHeaders();
        String token = headers.getFirst(Headers.AUTHORIZATION);

        if (StringUtils.isNotEmpty(token) && token.length() > MIN_TOKEN_LENGTH) {
            Map<String, List<String>> authentication = getAuthentication(token);
            if (authentication != null) {
                return forward(exchange, chain, authentication);
            }
        } else {
            boolean ignoreAuth = authProperties.getWhiteList()
                .stream()
                .anyMatch(requestPath::startsWith);
            if (ignoreAuth) {
                return forward(exchange, chain, null);
            }
        }

        return unauthorized(exchange);
    }
}
