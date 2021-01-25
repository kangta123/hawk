package com.oc.hawk.container.runtime.application.app.repository.dao;

import com.oc.hawk.container.runtime.port.driven.persistence.po.app.ServiceAppVersionPO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceAppVersionRepository extends CrudRepository<ServiceAppVersionPO, Long> {
    List<ServiceAppVersionPO> findByAppId(Long id);

    @Query("SELECT name FROM ServiceAppVersionPO where app.id=?1")
    List<String> getVersionName(long appId);
}
