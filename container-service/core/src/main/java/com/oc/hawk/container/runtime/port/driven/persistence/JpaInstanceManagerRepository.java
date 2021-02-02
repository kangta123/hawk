package com.oc.hawk.container.runtime.port.driven.persistence;

import com.oc.hawk.container.domain.model.runtime.config.InstanceManager;
import com.oc.hawk.container.domain.model.runtime.config.InstanceManagerRepository;
import com.oc.hawk.container.runtime.port.driven.persistence.po.InstanceConfigPO;
import com.oc.hawk.container.runtime.port.driven.persistence.po.InstanceManagerPO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;


@Repository
interface InstanceManagerPORepository extends CrudRepository<InstanceManagerPO, Long> {
    @Query("delete from InstanceManagerPO where config.id=?1")
    @Modifying
    void delete(long instanceId);

}

@Component
@RequiredArgsConstructor
public class JpaInstanceManagerRepository implements InstanceManagerRepository {
    private final InstanceManagerPORepository instanceManagerPORepository;

    @Override
    public void update(long instanceId, List<InstanceManager> instanceMangers) {
        instanceManagerPORepository.delete(instanceId);
        if (instanceMangers != null) {
            List<InstanceManagerPO> managers = instanceMangers.stream().map(m -> {
                InstanceManagerPO po = new InstanceManagerPO();
                po.setUserId(m.getUserId());
                InstanceConfigPO config = new InstanceConfigPO();
                config.setId(instanceId);
                po.setConfig(config);
                po.setUsername(m.getUsername());
                return po;
            }).collect(Collectors.toList());
            if (managers.size() > 0) {
                instanceManagerPORepository.saveAll(managers);
            }
        }
    }
}
