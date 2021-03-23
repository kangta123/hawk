package com.oc.hawk.traffic.port.driven.persistence;

import com.oc.hawk.traffic.port.driven.persistence.po.EntryPointHistoryManagerPo;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface EntryPointHistoryManagerPoRepository extends JpaRepositoryImplementation<EntryPointHistoryManagerPo, Long> {

}
