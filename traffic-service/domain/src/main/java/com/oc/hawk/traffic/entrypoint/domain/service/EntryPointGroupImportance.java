package com.oc.hawk.traffic.entrypoint.domain.service;

import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfig;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfigRepository;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointGroupID;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointResourceRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class EntryPointGroupImportance {

    private final EntryPointConfigRepository apiConfigRepository;
    private final EntryPointResourceRepository entryPointResourceRepository;

    public void importPostmanJson(EntryPointGroupID entryPointGroupID, List<EntryPointConfig> entryPointList) {
        //保存导入api
        List<EntryPointConfig> entryPointConfigList = apiConfigRepository.batchSave(entryPointGroupID, entryPointList);
        if(Objects.isNull(entryPointConfigList) || entryPointConfigList.isEmpty()) {
            return ;
        }
        entryPointConfigList.forEach(item -> {
            entryPointResourceRepository.addConfig(item);
        });
    }

}
