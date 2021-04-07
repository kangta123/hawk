package com.oc.hawk.traffic.entrypoint.domain.service;

import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfig;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfigRepository;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointGroupID;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class EntryPointGroupImportance {

    private final EntryPointConfigRepository apiConfigRepository;

    public void importPostmanJson(EntryPointGroupID entryPointGroupID, List<EntryPointConfig> entryPointList) {
        //TODO 判断重复地址不导入
        //保存导入api
        apiConfigRepository.batchSave(entryPointGroupID, entryPointList);
    }

}
