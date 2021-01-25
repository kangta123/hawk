package com.oc.hawk.container.domain.model.runtime.stat;

import java.util.List;
import java.util.Map;

public interface RuntimeStatRepository {
    void reset(Map<Long, Integer> projectRuntimeCount);

    int getTotalRuntimeCount();

    Map<Long, Integer> getRuntimeCountByProject(List<Long> projectIds);
}
