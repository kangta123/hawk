package com.oc.hawk.traffic.port.driven.persistence;

import com.oc.hawk.traffic.port.driven.persistence.po.EntryPointTracePo;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntryPointTracePoRepository extends CrudRepository<EntryPointTracePo, Long> {

    EntryPointTracePo findBySpanIdOrderByStartTimeAsc(String spanId);
    
    List<EntryPointTracePo> findByTraceIdOrderByStartTimeAsc(String traceId);
    
}
