package com.oc.hawk.traffic.port.driven.persistence;

import com.oc.hawk.traffic.port.driven.persistence.po.TrafficTracePo;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrafficTracePoRepository extends CrudRepository<TrafficTracePo, Long> {

    TrafficTracePo findBySpanIdOrderByStartTimeAsc(String spanId);
    
    List<TrafficTracePo> findByTraceIdOrderByStartTimeAsc(String traceId);
    
}
