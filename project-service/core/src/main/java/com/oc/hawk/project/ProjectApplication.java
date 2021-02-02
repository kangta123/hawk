package com.oc.hawk.project;

import com.oc.hawk.common.spring.config.BaseConfiguration;
import com.oc.hawk.common.spring.config.WebConfiguration;
import com.oc.hawk.project.domain.config.GitCodeBaseConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;


@SpringBootApplication
@Import(value = {WebConfiguration.class, BaseConfiguration.class})
@Slf4j
public class ProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }

    @Bean
    @ConfigurationProperties("hawk.project.codebase.git")
    public GitCodeBaseConfig gitCodeBaseConfiguration() {
        return new GitCodeBaseConfig();
    }
}

