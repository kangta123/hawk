package com.oc.hawk.container.runtime.application.app.repository.dao;

import com.oc.hawk.container.runtime.port.driven.persistence.po.app.ServiceAppPO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceAppRepository extends CrudRepository<ServiceAppPO, Long> {
    List<ServiceAppPO> findByAppIn(List<String> apps);

    Optional<ServiceAppPO> findByApp(String app);
}
