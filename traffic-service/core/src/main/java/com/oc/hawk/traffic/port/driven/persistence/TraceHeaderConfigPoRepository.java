package com.oc.hawk.traffic.port.driven.persistence;

import org.springframework.data.repository.CrudRepository;
import com.oc.hawk.traffic.port.driven.persistence.po.TrafficTraceHeaderConfigPo;


public interface TraceHeaderConfigPoRepository extends CrudRepository<TrafficTraceHeaderConfigPo, Long>{
    
}
