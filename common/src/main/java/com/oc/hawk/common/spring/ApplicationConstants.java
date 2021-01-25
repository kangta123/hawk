package com.oc.hawk.common.spring;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class ApplicationConstants {

    @Value("${spring.application.name}")
    private String appName;

}
