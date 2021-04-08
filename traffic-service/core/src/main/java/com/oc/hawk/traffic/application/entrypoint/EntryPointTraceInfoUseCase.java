package com.oc.hawk.traffic.application.entrypoint;

import com.oc.hawk.traffic.entrypoint.api.command.UploadTraceInfoCommand;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfig;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfigRepository;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointMethod;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointPath;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.Trace;
import com.oc.hawk.traffic.entrypoint.domain.service.EntryPointTraces;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
@Slf4j
public class EntryPointTraceInfoUseCase {
	
    private final EntryPointConfigFactory entryPointConfigFactory;
    private final EntryPointConfigRepository entryPointConfigRepository;
    
    @Transactional(rollbackFor = Exception.class)
    public void createTrace(List<UploadTraceInfoCommand> commandList) {
        List<Trace> traceList = new ArrayList<Trace>();
    	for(UploadTraceInfoCommand command : commandList) {
    		Trace trace = entryPointConfigFactory.createTrace(command);
    	    //Long matchId = new EntryPointTraces(entryPointConfigRepository).matchPath(command.getPath(), command.getMethod());
    	    //trace.updateEntryPointId(matchId);
    		traceList.add(trace);
    	}
    	entryPointConfigRepository.saveTrace(traceList);
    }

    
}
