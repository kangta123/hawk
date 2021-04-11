package com.oc.hawk.traffic.entrypoint.domain.model.entrypoint;

import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpMethod;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpPath;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.Trace;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.TraceHeaderConfig;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.TraceId;

import java.util.List;

public interface EntryPointConfigRepository {

    EntryPointConfigID save(EntryPointConfig config);

    EntryPointConfig byId(EntryPointConfigID id);

    List<EntryPointConfig> byGroupId(Long id);

    EntryPointGroupID save(EntryPointConfigGroup group);

    List<EntryPointConfig> byKey(EntryPointConfig config, List<EntryPointConfigGroup> groupList);

    List<EntryPointConfigGroup> findAllGroup();

    EntryPointConfigGroup byId(EntryPointGroupID id);

    List<EntryPointConfigGroup> findGroups();

    void update(List<EntryPointGroupID> entryPointGroupIdList);

    List<EntryPointConfigGroup> byIdList(List<Long> groupIdList);

    List<EntryPointConfig> byGroupIdList(List<Long> groupIdList);

    void updateList(Long userId, List<EntryPointConfigGroup> groupList);

    void batchSave(EntryPointGroupID entryPointConfigGroupID, List<EntryPointConfig> apiConfigList);

    void saveTrace(List<Trace> history);

    EntryPointConfig findByPathAndMethod(HttpPath path, HttpMethod method);

    List<EntryPointConfig> findByMethodAndRestfulPath(HttpMethod method);
    
    List<Trace> queryTraceInfoList(Integer page,Integer size,Trace trace,List<String> visibleInstances);
    
    void deleteById(EntryPointConfigID entryPointConfigID);
    
    Trace byTraceId(TraceId traceId);
    
    Trace findBySpanId(Trace traceParam);
    
    List<Trace> findByTraceId(Trace trace);
    
    List<Trace> queryApiHistoryList(Integer page,Integer size,EntryPointConfig entryPointConfig);
    
    Long queryApiHistoryCount(EntryPointConfig entryPointConfig);
    
    List<TraceHeaderConfig> findTraceHeaderConfig();
}
