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

    public List<EntryPointConfig> create(EntryPointConfigGroup group,List<ImportApiDTO> importApiList) {
        List<EntryPointConfig> baseApiConfigList = new ArrayList<EntryPointConfig>();
        for (ImportApiDTO importApiDTO : importApiList) {
            EntryPointConfig apiConfig = EntryPointConfig.builder()
                .groupId(group.getGroupId())
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
    
    public Trace createTrace(UploadTraceInfoCommand command) {
        return Trace.builder()
    			.host(command.getHost())
    			.path(command.getPath())
    			.method(command.getMethod())
    			.destAddr(command.getDestAddr())
    			.dstNamespace(command.getDstNamespace())
    			.dstWorkload(command.getDstWorkload())
    			.sourceAddr(command.getSourceAddr())
    			.timestamp(command.getTimestamp())
    			.latency(command.getLatency())
    			.requestId(command.getRequestId())
    			.protocol(command.getProtocol())
    			.requestBody(command.getRequestBody())
    			.requestHeaders(command.getRequestHeaders())
    			.responseBody(command.getResponseBody())
    			.responseHeaders(command.getResponseHeaders())
    			.responseCode(command.getResponseCode())
    			.spanId(command.getSpanId())
    			.parentSpanId(command.getParentSpanId())
    			.traceId(command.getTraceId())
    			.build();
    }

    
}
