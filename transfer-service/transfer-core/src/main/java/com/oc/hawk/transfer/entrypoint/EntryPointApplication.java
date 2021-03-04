package com.oc.hawk.transfer.entrypoint;

import com.oc.hawk.common.spring.config.BaseConfiguration;
import com.oc.hawk.common.spring.config.WebConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(value = {WebConfiguration.class, BaseConfiguration.class})
@Slf4j
public class EntryPointApplication {

    public static void main(String[] args) {
        SpringApplication.run(EntryPointApplication.class, args);
    }

}

