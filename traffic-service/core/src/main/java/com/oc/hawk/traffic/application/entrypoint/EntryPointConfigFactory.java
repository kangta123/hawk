package com.oc.hawk.traffic.application.entrypoint;

import com.oc.hawk.traffic.entrypoint.api.command.CreateEntryPointCommand;
import com.oc.hawk.traffic.entrypoint.api.command.UploadTraceInfoCommand;
import com.oc.hawk.traffic.entrypoint.api.dto.ImportGroupDTO.ImportApiDTO;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.*;
import com.oc.hawk.traffic.entrypoint.domain.model.execution.request.*;
import com.oc.hawk.traffic.entrypoint.domain.model.execution.response.HttpResponse;
import com.oc.hawk.traffic.entrypoint.domain.model.execution.response.HttpResponseBody;
import com.oc.hawk.traffic.entrypoint.domain.model.execution.response.HttpResponseHeader;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.Trace;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EntryPointConfigFactory {

    private final EntryPointConfigRepository entryPointConfigRepository;

    public EntryPointConfig create(CreateEntryPointCommand command) {
        EntryPointGroupID groupId = new EntryPointGroupID(command.getGroupId());

        EntryPointDesign design = new EntryPointDesign(command.getName(), command.getDesc());

        EntryPointPath apiPath = new EntryPointPath(command.getPath());
        EntryPointMethod apiMethod = EntryPointMethod.valueOf(command.getMethod());
        EntryPointTarget apiTarget = new EntryPointTarget(command.getApp(), command.getProjectId());

        EntryPointHttpResource apiHttpResource = new EntryPointHttpResource(apiPath, apiMethod, apiTarget);

        return EntryPointConfig.builder()
            .groupId(groupId)
            .design(design)
            .httpResource(apiHttpResource)
            .build();
    }

    public List<EntryPointConfig> create(List<ImportApiDTO> importApiList) {
        List<EntryPointConfig> baseApiConfigList = new ArrayList<EntryPointConfig>();
        for (ImportApiDTO importApiDTO : importApiList) {
            EntryPointConfig apiConfig = EntryPointConfig.builder()
                .design(new EntryPointDesign(importApiDTO.getName(), importApiDTO.getDescription()))
                .httpResource(new EntryPointHttpResource(new EntryPointPath(handleImportApiUrl(importApiDTO.getUrl())), EntryPointMethod.valueOf(importApiDTO.getMethod()), new EntryPointTarget()))
                .build();
            baseApiConfigList.add(apiConfig);
        }
        return baseApiConfigList;
    }

    private String handleImportApiUrl(String url) {
        String replaceStr = url.replaceAll("http://", "").replaceAll("https://", "");
        int pos = replaceStr.indexOf("/");
        if (pos >= 0) {
            return replaceStr.substring(pos);
        }
        return url;
    }

    public List<EntryPointGroupID> createGroupInvisibility(List<Long> groupIdList) {
        List<EntryPointGroupID> idList = groupIdList.stream().map(obj -> new EntryPointGroupID(obj)).collect(Collectors.toList());
        return idList;
    }

    public Trace createHistory(UploadTraceInfoCommand command) {

        HttpRequest httpRequest = createHttpRequest(command);
        HttpResponse httpResponse = createHttpResponse(command);

        Long configId = matchPath(httpRequest.getRequestAddr(), httpRequest.getRequestMethod().name());
        Long start = command.getStart();
        Long end = command.getEnd();

        return Trace.builder()
            .start(start)
            .build();
    }

    private Long matchPath(String path, String method) {
        if (StringUtils.isBlank(path)) {
            return null;
        }
        int n = path.indexOf("?");
        if (n >= 0) {
            path = path.substring(0, n);
        }
        if (path.endsWith("/")) {
            n = path.lastIndexOf("/");
            if (n > 0) {
                path = path.substring(0, n);
            }
        }

        //查找path 与 method匹配项
        EntryPointConfig entryPointConfig = entryPointConfigRepository.findByPathAndMethod(new EntryPointPath(path), EntryPointMethod.valueOf(method));
        if (Objects.nonNull(entryPointConfig)) {
            return entryPointConfig.getConfigId().getId();
        }

        //查找method 与 restful匹配项
        List<EntryPointConfig> configList = entryPointConfigRepository.findByMethodAndRestfulPath(EntryPointMethod.valueOf(method));
        for (EntryPointConfig config : configList) {
            String pathValue = config.getHttpResource().getPath().getPath();
            boolean result = checkPath(pathValue);
            if (result) {
                return config.getConfigId().getId();
            }
        }
        return null;
    }

    private boolean checkPath(String path) {
        String tempPath = path.replaceAll("\\/", "\\\\/");
        String replacePath = tempPath.replaceAll("\\{[a-zA-Z\\d]+\\}", "[a-zA-Z\\\\d]+");
        Pattern p = Pattern.compile(replacePath, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(path);
        boolean resultFlag = m.find();
        return resultFlag;
    }

    private HttpResponse createHttpResponse(UploadTraceInfoCommand command) {
        List<List<String>> responseHeaderList = command.getResponseHeaders();
        String responseCode = null;
        Map<String, String> headerMap = new HashMap<String, String>();
        for (List<String> headers : responseHeaderList) {
            String key = headers.get(0);
            String value = headers.get(1);
            if (":status".equalsIgnoreCase(key)) {
                responseCode = value;
            }
            headerMap.put(key, value);
        }
        String responseBody = command.getResponseBody();
        return HttpResponse.builder()
            .responseCode(responseCode)
            .responseHeader(HttpResponseHeader.createHttpResponseHeader(headerMap))
            .responseBody(HttpResponseBody.createHttpResponseBody(responseBody))
            .build();
    }

    private HttpRequest createHttpRequest(UploadTraceInfoCommand command) {
        List<List<String>> requestHeaderList = command.getRequestHeaders();
        String path = null;
        HttpRequestMethod method = null;
        String host = "http://";
        Map<String, String> headerMap = new HashMap<String, String>();
        for (List<String> headers : requestHeaderList) {
            String key = headers.get(0);
            String value = headers.get(1);
            if (":authority".equalsIgnoreCase(key)) {
                host += value;
            } else if (":path".equalsIgnoreCase(key)) {
                path = value;
            } else if (":method".equalsIgnoreCase(key)) {
                method = HttpRequestMethod.valueOf(value);
            } else if ("content-type".equalsIgnoreCase(key)) {
                headerMap.put("Content-Type", value);
            } else {
                headerMap.put(key, value);
            }
        }

        HttpBody body = null;
        if (StringUtils.equals("GET", method.name())) {
            int n = path.indexOf("?");
            if (n > 0) {
                String params = path.substring(n + 1);
                body = new JsonHttpBody(params);
            }
        }

        return HttpRequest.builder()
            .httpHeader(new HttpHeader(headerMap))
            .requestAddr(path)
            .requestBody(body)
            .requestMethod(method)
            .host(host)
            .build();
    }
}
