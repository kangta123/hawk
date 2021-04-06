package com.oc.hawk.traffic.entrypoint.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfig;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfigID;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfigRepository;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointMethod;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointPath;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.Trace;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.TraceId;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EntryPointTraces {
    
    private final EntryPointConfigRepository entryPointConfigRepository;
    
    public List<Trace> queryTraceInfoList(Integer page,Integer size,String path,String instanceName,List<String> visibleInstances) {
        Trace traceParam = Trace.builder()
                .path(path)
                .dstWorkload(instanceName)
                .build();
        List<Trace> traceList = entryPointConfigRepository.queryTraceInfoList(page,size,traceParam,visibleInstances);
        for(Trace trace : traceList) {
            Trace traceInfo = queryTraceInfo(trace.getEntryPointId(),path,trace.getMethod());
            updateTrace(traceInfo,trace);
        }
        return traceList;
    }
    
    public List<Trace> queryTraceNodeList(Trace traceParam) {
        Trace traceNode = entryPointConfigRepository.findBySpanId(traceParam);
        if(Objects.isNull(traceNode)) {
            return new ArrayList<>();
        }
        List<Trace> traceList = entryPointConfigRepository.findByTraceId(traceNode);
        for(Trace trace : traceList) {
           Trace traceInfo = queryTraceInfo(trace.getEntryPointId(),trace.getPath(),trace.getMethod());
           updateTrace(traceInfo,trace);
        }
        return traceList;
    }
    
    public Trace queryApiHistoryInfo(TraceId traceId) {
        return entryPointConfigRepository.byTraceId(traceId);
    }
    
    public Trace queryTraceNameAndId(Long entryPointId) {
        EntryPointConfig entryPointConfig = entryPointConfigRepository.byId(new EntryPointConfigID(entryPointId));
        String name = "";
        if(Objects.nonNull(entryPointConfig)) {
            name = entryPointConfig.getDesign().getName();
        }
        return Trace.builder().entryPointName(name).build();
    }
    
    public Long matchPath(String path, String method) {
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
            String targetPath = config.getHttpResource().getPath().getPath();
            boolean result = checkPath(targetPath,path);
            if (result) {
                return config.getConfigId().getId();
            }
        }
        return null;
    }
    
    public List<Trace> queryApiHistoryList(Integer page,Integer size,EntryPointConfigID entryPointId){
        return entryPointConfigRepository.queryApiHistoryList(page,size,entryPointId);
    }
    
    public Long queryApiHistoryCount(EntryPointConfigID entryPointId) {
        return entryPointConfigRepository.queryApiHistoryCount(entryPointId);
    }
    
    private boolean checkPath(String targetPath,String path) {
        String tempPath = targetPath.replaceAll("\\/", "\\\\/");
        String replacePath = tempPath.replaceAll("\\{[a-zA-Z\\d]+\\}", "[a-zA-Z\\\\d]+");
        Pattern p = Pattern.compile(replacePath, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(path);
        boolean resultFlag = m.find();
        return resultFlag;
    }
    
    private Trace queryTraceInfo(Long entryPointId,String path,String method) {
        if(Objects.isNull(entryPointId)) {
            Long matchId = matchPath(path, method);
            if(Objects.nonNull(matchId)) {
                return queryTraceNameAndId(matchId);
            }
        }else{
            return queryTraceNameAndId(entryPointId);
        }
        return null;
    }
    
    private void updateTrace(Trace traceInfo,Trace trace) {
        if(Objects.nonNull(traceInfo)) {
            trace.updateEntryPointId(traceInfo.getEntryPointId());
            trace.updateEntryPointName(traceInfo.getEntryPointName());
        }
    }
    
    
}
