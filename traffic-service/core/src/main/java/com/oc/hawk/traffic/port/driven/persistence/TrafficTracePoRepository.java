package com.oc.hawk.traffic.port.driven.persistence;

import com.oc.hawk.traffic.port.driven.persistence.po.TrafficTracePo;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TrafficTracePoRepository extends JpaRepositoryImplementation<TrafficTracePo, Long> {

    TrafficTracePo findBySpanIdOrderByStartTimeAsc(String spanId);
    
    List<TrafficTracePo> findByTraceIdOrderByStartTimeAsc(String traceId);
    
    void deleteByStartTimeLessThan(LocalDateTime startTime);
}
