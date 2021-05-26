package com.oc.hawk.jvm.port.driven.facade;

import com.oc.hawk.api.utils.JsonUtils;
import com.oc.hawk.jvm.application.representation.ClassInfoDTO;
import com.oc.hawk.jvm.application.representation.JvmDashboardDTO;
import com.oc.hawk.jvm.application.representation.ThreadStackDTO;
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
import java.util.function.Function;


/**
 * @author kangta123
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class RemoteConnectJvmAgentFacade implements JvmAgentFacade {

    private String getUrl(String instanceName, String namespace) {
        //        String url = "http://" + instanceName + "." + namespace + ":4295/hotswap";
        return "http://localhost:4295";
    }

    private HttpRequest getHttpRequest(String url) {
        return HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
    }

    private <T> T execute(HttpRequest httpRequest, Function<String, T> handleResponse) {
        final HttpClient httpClient = HttpClient.newHttpClient();
        try {
            final HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            final String body = response.body();
            return handleResponse.apply(body);
        } catch (IOException | InterruptedException e) {
            log.error("can not connect to instance", e);
            throw new RemoteHotswapException("无法连接实例");
        }
    }

    @Override
    public void hotswap(InputStream inputStream, String instanceName, String namespace) {
        final String url = getUrl(instanceName, namespace) + "/hotswap";

        final HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(url)).POST(HttpRequest.BodyPublishers.ofInputStream(() -> inputStream)).build();
        this.execute(httpRequest, (error) -> {
            if (StringUtils.isNotEmpty(error)) {
                if (StringUtils.equals(error, NullPointerException.class.getSimpleName())) {
                    throw new RemoteHotswapException("服务需要先执行过任意http请求。");
                }
                if (StringUtils.equals(error, UnsupportedOperationException.class.getSimpleName())) {
                    throw new RemoteHotswapException("Hotswap仅支持方法代码内容的修改。");
                }
            }
            return null;
        });
    }

    @Override
    public JvmDashboardDTO dashboard(String instanceName, String namespace) {
        final String url = getUrl(instanceName, namespace) + "/dashboard";
        final HttpRequest httpRequest = getHttpRequest(url);

        return this.execute(httpRequest, response -> JsonUtils.json2Object(response, JvmDashboardDTO.class));
    }

    @Override
    public ClassInfoDTO decompileClass(String clz, String instanceName, String namespace) {
        final String url = getUrl(instanceName, namespace) + "/classes/decompile?clz=" + clz;
        final HttpRequest httpRequest = getHttpRequest(url);

        return this.execute(httpRequest, response -> JsonUtils.json2Object(response, ClassInfoDTO.class));
    }

    @Override
    public ThreadStackDTO getThreadTrace(long id, String instanceName, String namespace) {
        final String url = getUrl(instanceName, namespace) + "/thread/stack?id=" + id;
        final HttpRequest httpRequest = getHttpRequest(url);
        return this.execute(httpRequest, response -> {
            if(StringUtils.isEmpty(response)){
               return null;
            }
            return JsonUtils.json2Object(response, ThreadStackDTO.class);
        });
    }
}
