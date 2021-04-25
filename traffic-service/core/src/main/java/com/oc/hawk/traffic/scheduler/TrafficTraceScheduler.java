package com.oc.hawk.traffic.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.oc.hawk.traffic.application.entrypoint.TrafficTraceInfoUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class TrafficTraceScheduler {
    
    private final TrafficTraceInfoUseCase trafficTraceInfoUseCase;
    
    @Scheduled(cron = "0 30 0 * * ?")
    public void deleteTrace() {
        log.info("delete trace...");
        trafficTraceInfoUseCase.deleteTrace();
    }
    
}
