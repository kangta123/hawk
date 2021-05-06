package com.oc.hawk.traffic.application.entrypoint;

import com.oc.hawk.traffic.entrypoint.api.command.CreateEntryPointCommand;
import com.oc.hawk.traffic.entrypoint.api.command.UploadTraceInfoCommand;
import com.oc.hawk.traffic.entrypoint.api.dto.ImportGroupDTO.ImportApiDTO;
import com.oc.hawk.traffic.entrypoint.domain.config.TrafficTraceHeaderFilterConfig;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfig;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfigGroup;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointDescription;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointGroupID;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.*;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.Trace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EntryPointConfigFactory {
    
    private final TrafficTraceHeaderFilterConfig trafficTraceHeaderFilterConfig;
    

    public EntryPointConfig create(CreateEntryPointCommand command) {
        EntryPointGroupID groupId = new EntryPointGroupID(command.getGroupId());

        EntryPointDescription description = new EntryPointDescription(command.getName(), command.getDesc());
        
        HttpPath apiPath = new HttpPath(command.getPath());
                
        HttpMethod apiMethod = HttpMethod.valueOf(command.getMethod());
        HttpResource apiHttpResource = new HttpResource(apiPath.getUri(), apiMethod);
        
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
            EntryPointConfig apiConfig = EntryPointConfig.builder()
                .groupId(group.getGroupId())
                .description(new EntryPointDescription(importApiDTO.getName(), importApiDTO.getDescription()))
                .httpResource(new HttpResource(path.getUri(), HttpMethod.valueOf(importApiDTO.getMethod())))
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
        return groupIdList.stream().map(EntryPointGroupID::new).collect(Collectors.toList());
    }
    
    public Trace createTrace(UploadTraceInfoCommand command) {
        Map<String,String> requestHeaders = command.getRequestHeaders();
        List<String> requestFilterKeyList = getTraceRequestHeaderConfig();
        Map<String, String> requestHeaderMap = requestHeaders.entrySet().stream()
                .filter(map -> !requestFilterKeyList.contains(map.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        
        Map<String,String> responseHeaders = command.getResponseHeaders();
        List<String> responseFilterKeyList = getTraceResponseHeaderConfig();
        Map<String, String> responseHeaderMap = responseHeaders.entrySet().stream()
                .filter(map -> !responseFilterKeyList.contains(map.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        
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
