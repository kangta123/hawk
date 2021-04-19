package com.oc.hawk.traffic.entrypoint.domain.service;

import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfig;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfigRepository;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointGroupID;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointResourceRepository;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpResource;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class EntryPointGroupImportance {

    private final EntryPointConfigRepository apiConfigRepository;
    private final EntryPointResourceRepository entryPointResourceRepository;

    public void importPostmanJson(EntryPointGroupID entryPointGroupID, List<EntryPointConfig> entryPointList) {
        
        List<EntryPointConfig> resultList = savedEntryPointList(entryPointList);
        //保存导入api
        List<EntryPointConfig> entryPointConfigList = apiConfigRepository.batchSave(entryPointGroupID, resultList);
        if(Objects.isNull(entryPointConfigList) || entryPointConfigList.isEmpty()) {
            return ;
        }
        entryPointConfigList.forEach(item -> {
            entryPointResourceRepository.addConfig(item);
        });
    }
    
    
    private List<EntryPointConfig> savedEntryPointList(List<EntryPointConfig> entryPointList){
        List<EntryPointConfig> resultList = new ArrayList<>();
        entryPointList.forEach(item -> {
            List<EntryPointConfig> list = apiConfigRepository.findByHttpResource(item.getHttpResource());
            if(Objects.isNull(list) || list.isEmpty()) {
                resultList.add(item);
            }
        });
        return resultList;
    }
}
