package com.oc.hawk.traffic.entrypoint.domain.model.entrypoint;

import java.util.List;

public interface EntryPointResourceRepository {
    
    void loadEntryPointResource(List<EntryPointConfig> configList);

}
