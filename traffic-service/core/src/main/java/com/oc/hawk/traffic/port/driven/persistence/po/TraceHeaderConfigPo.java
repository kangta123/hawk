package com.oc.hawk.traffic.port.driven.persistence.po;

import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;

import com.oc.hawk.common.hibernate.BaseEntity;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.TraceHeaderConfig;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "traffic_trace_header_config")
@Entity
@DynamicUpdate
public class TraceHeaderConfigPo extends BaseEntity{
    
    private String keyName;
    private Integer keyType;
    
    public static TraceHeaderConfigPo createBy(TraceHeaderConfig traceHeaderConfig) {
        TraceHeaderConfigPo traceHeaderConfigPo = new TraceHeaderConfigPo();
        traceHeaderConfigPo.setKeyName(traceHeaderConfig.getKeyName());
        traceHeaderConfigPo.setKeyType(traceHeaderConfig.getKeyType());
        return traceHeaderConfigPo;
    }
    
    public TraceHeaderConfig toTraceHeaderConfig() {
        return TraceHeaderConfig
                .builder()
                .keyName(this.keyName)
                .keyType(this.keyType)
                .build();
    }
    
}
