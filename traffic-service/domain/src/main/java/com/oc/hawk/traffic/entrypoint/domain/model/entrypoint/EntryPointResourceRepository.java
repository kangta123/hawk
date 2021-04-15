package com.oc.hawk.traffic.entrypoint.domain.model.entrypoint;

import java.util.List;

import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpMethod;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpPath;

public interface EntryPointResourceRepository {
    
    void deleteAllEntryPointResource(List<EntryPointConfig> configList);
    
    void loadEntryPointResource(List<EntryPointConfig> configList);
    
    EntryPointConfig findByPathAndMethod(HttpPath path, HttpMethod method);
    
    List<EntryPointConfig> findByMethodAndRestfulPath(HttpMethod method);
    
    void addConfig(EntryPointConfig config);

    void deleteConfig(EntryPointConfig config);
}
