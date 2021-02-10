package com.co.hawk.transfer.entrypoint.port.driven.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;
import com.co.hawk.transfer.entrypoint.port.driven.persistence.po.EntryPointConfigGroupPO;

@Repository
public interface EntryPointConfigGroupPoRepository extends JpaRepositoryImplementation<EntryPointConfigGroupPO, Long> {
	
	List<EntryPointConfigGroupPO> findAll();
	
	Optional<EntryPointConfigGroupPO> findById(Long groupId);
	
	List<EntryPointConfigGroupPO> findByIdIn(List<Long> idList);
	
}


