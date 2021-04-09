package com.oc.hawk.traffic.port.driven.persistence;

import org.springframework.data.repository.CrudRepository;
import com.oc.hawk.traffic.port.driven.persistence.po.TraceHeaderConfigPo;


public interface TraceHeaderConfigPoRepository extends CrudRepository<TraceHeaderConfigPo, Long>{
    
}
