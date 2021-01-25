package com.oc.hawk.base.port.driven.persistence;

import com.oc.hawk.base.port.driven.persistence.po.DepartmentPo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends CrudRepository<DepartmentPo, Long> {
}
