package com.oc.hawk.traffic.port.driven.persistence;

import com.oc.hawk.traffic.port.driven.persistence.po.EntryPointTracePo;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntryPointTracePoRepository extends CrudRepository<EntryPointTracePo, Long> {

}
