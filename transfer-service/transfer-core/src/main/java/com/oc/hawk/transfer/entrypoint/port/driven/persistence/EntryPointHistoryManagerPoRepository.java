package com.oc.hawk.transfer.entrypoint.port.driven.persistence;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;
import com.oc.hawk.transfer.entrypoint.port.driven.persistence.po.EntryPointHistoryManagerPo;

@Repository
public interface EntryPointHistoryManagerPoRepository extends JpaRepositoryImplementation<EntryPointHistoryManagerPo, Long>{
	
}
