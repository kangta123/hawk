package com.oc.hawk.traffic.entrypoint.domain.service;

import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfig;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfigGroup;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfigRepository;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointGroupID;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class EntryPointGroupImportance {

    private final EntryPointConfigRepository apiConfigRepository;

    public void importPostmanJson(EntryPointConfigGroup entryPointGroup, List<EntryPointConfig> entryPointList) {
        //保存导入分组
        EntryPointGroupID apiConfigGroupID = apiConfigRepository.save(entryPointGroup);
        //保存导入api
        apiConfigRepository.batchSave(apiConfigGroupID, entryPointList);
    }

}
