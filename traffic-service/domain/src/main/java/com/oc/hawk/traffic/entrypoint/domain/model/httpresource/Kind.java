package com.oc.hawk.traffic.entrypoint.domain.model.httpresource;

import org.apache.commons.lang3.StringUtils;

/**
 * @author kangta123
 */
public enum Kind {
    client, server;


    public static Kind of(String str){
       if(StringUtils.isEmpty(str)){
           return null;
       }else{
           return Kind.valueOf(str);
       }
    }
}
