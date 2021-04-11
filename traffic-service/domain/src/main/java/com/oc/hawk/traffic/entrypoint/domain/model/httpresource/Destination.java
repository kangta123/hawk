package com.oc.hawk.traffic.entrypoint.domain.model.httpresource;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Destination {
    
    
    private String dstWorkload;
    private String destAddr;
    private String dstNamespace;
    
    public Destination(String dstWorkload,String destAddr,String dstNamespace) {
        this.dstWorkload=dstWorkload;
        this.destAddr=dstWorkload;
        this.dstNamespace=dstNamespace;
    }

}
