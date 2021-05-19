package com.oc.hawk.traffic.port.driven.facade;

import com.oc.hawk.traffic.port.driven.facade.feign.RemoteHotswapException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


/**
 * @author kangta123
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class RemoteConnectJvmAgentFacade implements JvmAgentFacade {

    @Override
    public void hotswap(InputStream inputStream, String instanceName, String namespace) {

        String url = "http://" + instanceName + "." + namespace + ":4295/hotswap";
//        String url = "http://localhost:4295/hotswap";
        log.info("hotswap class info to url {}", url);

        final HttpClient httpClient = HttpClient.newHttpClient();
        final HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(url)).POST(HttpRequest.BodyPublishers.ofInputStream(() -> inputStream)).build();
        try {
            final HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            final String error = response.body();
            if (StringUtils.isNotEmpty(error)) {
                if (StringUtils.equals(error, NullPointerException.class.getSimpleName())) {
                    throw new RemoteHotswapException("服务需要先执行过任意http请求。");
                }
                if (StringUtils.equals(error, UnsupportedOperationException.class.getSimpleName())) {
                    throw new RemoteHotswapException("Hotswap仅支持方法代码内容的修改。");
                }
            }
        } catch (IOException | InterruptedException e) {
            log.error("can not connect to instance", e);
            throw new RemoteHotswapException("无法连接实例" + instanceName);
        }
    }
}
