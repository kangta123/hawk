package com.oc.hawk.transfer.entrypoint.port.driven.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.oc.hawk.transfer.entrypoint.port.driven.persistence.po.EntryPointGroupManagerPO;

@Repository
public interface EntryPointGroupManagerPoRepository extends CrudRepository<EntryPointGroupManagerPO, Long>{
	
	EntryPointGroupManagerPO findByUserId(Long userId);
	
//	@Modifying(clearAutomatically = true)
//	@Query(value = "update ApiGroupManagerPO r set r.groupids=?1 where r.userId=?2")
//	int updateGroupids(String groupids,Long userId);
	
}
