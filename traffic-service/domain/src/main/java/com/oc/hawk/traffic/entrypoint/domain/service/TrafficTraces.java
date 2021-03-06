package com.oc.hawk.traffic.entrypoint.domain.service;

import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfig;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfigID;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfigRepository;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointResourceRepository;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpMethod;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpPath;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.Trace;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.TraceId;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class TrafficTraces {

    private final EntryPointConfigRepository entryPointConfigRepository;
    private final EntryPointResourceRepository entryPointResourceRepository;

    public List<Trace> queryTraceInfoList(Integer page, Integer size, String key) {
        List<Trace> traceList = entryPointConfigRepository.queryTraceInfoList(page, size, key);
        for (Trace trace : traceList) {
            Trace traceInfo = queryTraceInfo(trace.getHttpResource().getPath(), trace.getHttpResource().getMethod().name());
            updateTrace(traceInfo, trace);
        }
        return traceList;
    }

    public List<Trace> queryTraceNodeList(Trace traceParam) {
        Trace traceNode = entryPointConfigRepository.findBySpanId(traceParam);
        if (Objects.isNull(traceNode)) {
            return new ArrayList<>();
        }
        List<Trace> traceList = entryPointConfigRepository.findByTraceId(traceNode);
        for (Trace trace : traceList) {
            Trace traceInfo = queryTraceInfo(trace.getHttpResource().getPath(), trace.getHttpResource().getMethod().name());
            updateTrace(traceInfo, trace);
        }
        return traceList;
    }

    public Trace queryTrafficTraceInfo(EntryPointConfigID entryPointId, TraceId traceId) {
        EntryPointConfig entryPointConfig = entryPointConfigRepository.byId(entryPointId);
        Trace trace = entryPointConfigRepository.byTraceId(traceId);
        HttpPath httpPath = trace.getHttpResource().getPath();
        HttpPath pathUri = httpPath.getUri();
        pathUri.updatePathVariable(entryPointConfig.getHttpResource().getPath().getPath());
        pathUri.updatePath(httpPath.getPath());
        trace.getHttpResource().updateHttpPath(pathUri);
        return trace;
    }

    private EntryPointConfig matchPath(HttpPath path, String method) {
        if (StringUtils.isEmpty(path.getPath())) {
            return null;
        }
        HttpPath pathUri = path.getUri();

        //查找path 与 method匹配项
        EntryPointConfig entryPointConfig = entryPointResourceRepository.findByPathAndMethod(pathUri, HttpMethod.valueOf(method));
        if (Objects.nonNull(entryPointConfig)) {
            return entryPointConfig;
        }

        //查找method 与 restful匹配项
        List<EntryPointConfig> configList = entryPointResourceRepository.findByMethodAndRestfulPath(HttpMethod.valueOf(method));
        for (EntryPointConfig config : configList) {
            String targetPath = config.getHttpResource().getPath().getPath();
            boolean result = pathUri.matchPath(targetPath);
            if (result) {
                return config;
            }
        }
        return null;
    }

    public List<Trace> queryTrafficTraceList(Integer page, Integer size, EntryPointConfig entryPointConfig) {
        return entryPointConfigRepository.queryTrafficTraceList(page, size, entryPointConfig);
    }

    public Long queryTrafficTraceCount(EntryPointConfig entryPointConfig) {
        return entryPointConfigRepository.queryTrafficTraceCount(entryPointConfig);
    }

    private Trace queryTraceInfo(HttpPath path, String method) {
        EntryPointConfig entryPointConfig = matchPath(path, method);
        if (Objects.nonNull(entryPointConfig) && Objects.nonNull(entryPointConfig.getConfigId())) {
            return Trace.builder()
                    .entryPointId(entryPointConfig.getConfigId().getId())
                    .entryPointName(entryPointConfig.getDescription().getName())
                    .build();
        }
        return null;
    }

    private void updateTrace(Trace traceInfo, Trace trace) {
        if (Objects.nonNull(traceInfo)) {
            trace.updateEntryPointIdAndName(traceInfo.getEntryPointId(), traceInfo.getEntryPointName());
        }
    }
    
    public Trace queryApiTraceDetail(Trace traceParam) {
        Trace traceNode = entryPointConfigRepository.findBySpanId(traceParam);
        if (Objects.isNull(traceNode)) {
            return null;
        }
        Trace traceInfo = queryTraceInfo(traceNode.getHttpResource().getPath(), traceNode.getHttpResource().getMethod().name());
        updateTrace(traceInfo, traceNode);
        return traceNode;
    }

}
