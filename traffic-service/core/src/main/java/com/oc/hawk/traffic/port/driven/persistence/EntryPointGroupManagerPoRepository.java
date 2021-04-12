package com.oc.hawk.traffic.port.driven.persistence;

import com.oc.hawk.traffic.port.driven.persistence.po.EntryPointGroupManagerPO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntryPointGroupManagerPoRepository extends CrudRepository<EntryPointGroupManagerPO, Long> {

    EntryPointGroupManagerPO findByUserId(Long userId);

}
