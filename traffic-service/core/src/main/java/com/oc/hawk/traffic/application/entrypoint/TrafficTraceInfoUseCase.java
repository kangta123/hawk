package com.oc.hawk.traffic.application.entrypoint;

import com.oc.hawk.traffic.entrypoint.api.command.UploadTraceInfoCommand;
import com.oc.hawk.traffic.entrypoint.domain.config.TrafficTraceHeaderFilterConfig;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfigRepository;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.Trace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class TrafficTraceInfoUseCase {
	
    private final EntryPointConfigFactory entryPointConfigFactory;
    private final EntryPointConfigRepository entryPointConfigRepository;
    private final TrafficTraceHeaderFilterConfig trafficTraceHeaderFilterConfig;
    
    @Transactional(rollbackFor = Exception.class)
    public void createTrace(List<UploadTraceInfoCommand> commandList) {
        List<Trace> traceList = new ArrayList<>();
    	for(UploadTraceInfoCommand command : commandList) {
    		Trace trace = entryPointConfigFactory.createTrace(command);
    		traceList.add(trace);
    	}
    	entryPointConfigRepository.saveTrace(traceList);
    }
    
    public List<String> getTraceRequestHeaderConfig(){
        return trafficTraceHeaderFilterConfig.getRequestFilterKey();
    }
    
    public List<String> getTraceResponseHeaderConfig(){
        return trafficTraceHeaderFilterConfig.getResponseFilterKey();
    }
    
}
