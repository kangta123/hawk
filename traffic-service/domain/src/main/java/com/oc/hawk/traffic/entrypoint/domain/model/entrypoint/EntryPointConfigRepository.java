package com.oc.hawk.traffic.entrypoint.domain.model.entrypoint;

import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpResource;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.Trace;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.TraceId;

import java.util.List;

public interface EntryPointConfigRepository {

    EntryPointConfigID save(EntryPointConfig config);

    EntryPointConfig byId(EntryPointConfigID id);

    EntryPointGroupID save(EntryPointConfigGroup group);

    List<EntryPointConfig> byKey(EntryPointConfig config, List<EntryPointConfigGroup> groupList);

    List<EntryPointConfigGroup> findAllGroup();

    EntryPointConfigGroup byId(EntryPointGroupID id);

    List<EntryPointConfigGroup> findGroups();

    void update(List<EntryPointGroupID> entryPointGroupIdList);

    List<EntryPointConfigGroup> byIdList(List<EntryPointGroupID> groupIdList);

    List<EntryPointConfig> byGroupIdList(List<EntryPointGroupID> groupIdList);
    List<Trace> queryTraceInfoList(Integer page, Integer size, String key) ;

    List<EntryPointConfig> batchSave(EntryPointGroupID entryPointConfigGroupID, List<EntryPointConfig> apiConfigList);

    void saveTrace(List<Trace> history);
    

    void deleteById(EntryPointConfigID entryPointConfigID);
    
    Trace byTraceId(TraceId traceId);
    
    Trace findBySpanId(Trace traceParam);
    
    List<Trace> findByTraceId(Trace trace);
    
    List<Trace> queryTrafficTraceList(Integer page,Integer size,EntryPointConfig entryPointConfig);
    
    Long queryTrafficTraceCount(EntryPointConfig entryPointConfig);
    
    List<EntryPointConfig> findAllEntryPointConfig();
    
    List<EntryPointConfig> findByHttpResource(HttpResource httpResource);
    
    void deleteAll();
    
    void deleteGroupById(EntryPointGroupID entryPointGroupID);
    
}
