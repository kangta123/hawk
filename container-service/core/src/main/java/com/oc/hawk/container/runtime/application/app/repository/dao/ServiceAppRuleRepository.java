package com.oc.hawk.container.runtime.application.app.repository.dao;

import com.oc.hawk.container.runtime.port.driven.persistence.po.app.ServiceAppRulePO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceAppRuleRepository extends CrudRepository<ServiceAppRulePO, Long> {
    @Query("select max(r.order) from ServiceAppRulePO r where r.appId=?1")
    Optional<Integer> getMaxAppRuleOrder(Long appId);

    List<ServiceAppRulePO> findByAppIdAndEnabled(long id, boolean enabled);

    List<ServiceAppRulePO> findByAppId(long id);

    boolean existsByAppId(Long appId);

    Long countByAppId(long id);
}
