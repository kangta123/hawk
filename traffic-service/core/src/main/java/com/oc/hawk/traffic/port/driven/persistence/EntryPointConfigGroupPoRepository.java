package com.oc.hawk.traffic.port.driven.persistence;

import com.oc.hawk.traffic.port.driven.persistence.po.EntryPointConfigGroupPO;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntryPointConfigGroupPoRepository extends JpaRepositoryImplementation<EntryPointConfigGroupPO, Long> {

    List<EntryPointConfigGroupPO> findAll();

    Optional<EntryPointConfigGroupPO> findById(Long groupId);

    List<EntryPointConfigGroupPO> findByIdIn(List<Long> idList);

}


