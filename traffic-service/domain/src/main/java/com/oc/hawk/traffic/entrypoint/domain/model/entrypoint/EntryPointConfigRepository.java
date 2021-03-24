package com.oc.hawk.traffic.entrypoint.domain.model.entrypoint;

import com.oc.hawk.traffic.entrypoint.domain.model.trace.Trace;

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

    //EntryPointGroupVisibility byGroupManagerUserId(Long userId);

    void updateList(Long userId, List<EntryPointConfigGroup> groupList);

    void batchSave(EntryPointGroupID entryPointConfigGroupID, List<EntryPointConfig> apiConfigList);

    void saveTrace(List<Trace> history);

    EntryPointConfig findByPathAndMethod(EntryPointPath path, EntryPointMethod method);

    List<EntryPointConfig> findByMethodAndRestfulPath(EntryPointMethod method);
}
