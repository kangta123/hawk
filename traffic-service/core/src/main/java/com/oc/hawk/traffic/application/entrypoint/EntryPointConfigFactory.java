package com.oc.hawk.traffic.application.entrypoint;

import com.oc.hawk.traffic.entrypoint.api.command.CreateEntryPointCommand;
import com.oc.hawk.traffic.entrypoint.api.command.UploadTraceInfoCommand;
import com.oc.hawk.traffic.entrypoint.api.dto.ImportGroupDTO.ImportApiDTO;
import com.oc.hawk.traffic.entrypoint.domain.config.TrafficTraceHeaderFilterConfig;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.*;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.Destination;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpMethod;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpPath;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpRequestBody;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpRequestHeader;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpResource;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpResponseBody;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpResponseCode;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpResponseHeader;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.Latency;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.SpanContext;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.Trace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EntryPointConfigFactory {
    
    private final TrafficTraceHeaderFilterConfig trafficTraceHeaderFilterConfig;
    

    public EntryPointConfig create(CreateEntryPointCommand command) {
        EntryPointGroupID groupId = new EntryPointGroupID(command.getGroupId());

        EntryPointDescription description = new EntryPointDescription(command.getName(), command.getDesc());
        
        HttpPath apiPath = new HttpPath(command.getPath());
        apiPath.handlePath();
        
        HttpMethod apiMethod = HttpMethod.valueOf(command.getMethod());
        HttpResource apiHttpResource = new HttpResource(apiPath, apiMethod);

        return EntryPointConfig.builder()
            .groupId(groupId)
            .description(description)
            .httpResource(apiHttpResource)
            .projectId(command.getProjectId())
            .build();
    }

    public List<EntryPointConfig> create(EntryPointConfigGroup group,List<ImportApiDTO> importApiList) {
        List<EntryPointConfig> baseApiConfigList = new ArrayList<>();
        for (ImportApiDTO importApiDTO : importApiList) {
            HttpPath path = new HttpPath(handleImportApiUrl(importApiDTO.getUrl()));
            path.handlePath();
            EntryPointConfig apiConfig = EntryPointConfig.builder()
                .groupId(group.getGroupId())
                .description(new EntryPointDescription(importApiDTO.getName(), importApiDTO.getDescription()))
                .httpResource(new HttpResource(path, HttpMethod.valueOf(importApiDTO.getMethod())))
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
    
    public Trace createTrace(UploadTraceInfoCommand command) {
        Map<String,String> reqeustHeaders = command.getRequestHeaders();
        List<String> requestFilterKeyList = getTraceRequestHeaderConfig();
        Map<String, String> requestHeaderMap = reqeustHeaders.entrySet().stream()
                .filter(map -> !requestFilterKeyList.contains(map.getKey()))
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
        
        Map<String,String> responseHeaders = command.getResponseHeaders();
        List<String> responseFilterKeyList = getTraceResponseHeaderConfig();
        Map<String, String> responseHeaderMap = responseHeaders.entrySet().stream()
                .filter(map -> !responseFilterKeyList.contains(map.getKey()))
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
        
        return Trace.builder()
    			.host(command.getHost())
    			.httpResource(new HttpResource(new HttpPath(command.getPath()),HttpMethod.valueOf(command.getMethod())))
    			.destination(new Destination(command.getDstWorkload(), command.getDestAddr(), command.getDstNamespace()))
    			.sourceAddr(command.getSourceAddr())
    			.timestamp(command.getTimestamp())
    			.latency(new Latency(command.getLatency()))
    			.requestId(command.getRequestId())
    			.protocol(command.getProtocol())
    			.requestBody(new HttpRequestBody(command.getRequestBody()))
    			.requestHeaders(new HttpRequestHeader(requestHeaderMap))
    			.responseBody(new HttpResponseBody(command.getResponseBody()))
    			.responseHeaders(new HttpResponseHeader(responseHeaderMap))
    			.responseCode(new HttpResponseCode(Integer.parseInt(command.getResponseCode())))
    			.spanContext(new SpanContext(command.getSpanId(),command.getParentSpanId(),command.getTraceId()))
    			.build();
    }

    private List<String> getTraceRequestHeaderConfig(){
        return trafficTraceHeaderFilterConfig.getRequestFilterKey();
    }
    
    private List<String> getTraceResponseHeaderConfig(){
        return trafficTraceHeaderFilterConfig.getResponseFilterKey();
    }
    
}
