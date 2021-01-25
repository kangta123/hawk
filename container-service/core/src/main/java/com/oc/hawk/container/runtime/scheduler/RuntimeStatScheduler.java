package com.oc.hawk.container.runtime.scheduler;

import com.oc.hawk.container.runtime.application.stat.ProjectRuntimeStatUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
@RequiredArgsConstructor
public class RuntimeStatScheduler {
    private final ProjectRuntimeStatUseCase projectRuntimeStatUseCase;

    @PostConstruct
    public void setup() {
        resetRuntimeStat();
    }

    @Scheduled(fixedRate = 1000 * 60 * 10L)
    public void resetRuntimeStat() {
        log.info("Reset runtime stat...");
        projectRuntimeStatUseCase.resetRuntimeStat();
    }

}
