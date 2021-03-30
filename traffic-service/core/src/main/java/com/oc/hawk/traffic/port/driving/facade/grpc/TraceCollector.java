package com.oc.hawk.traffic.port.driving.facade.grpc;

import com.oc.hawk.api.utils.JsonUtils;
import com.oc.hawk.trace_logging.LoggingServiceGrpc;
import com.oc.hawk.traffic.application.entrypoint.EntryPointTraceInfoUseCase;
import com.oc.hawk.traffic.entrypoint.api.command.RequestHeaderCommand;
import com.oc.hawk.traffic.entrypoint.api.command.UploadTraceInfoCommand;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

/**
 * @author kangta123
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TraceCollector extends LoggingServiceGrpc.LoggingServiceImplBase {

	private final EntryPointTraceInfoUseCase entryPointTraceInfoUseCase;

    @Override
    public void writeLog(com.oc.hawk.trace_logging.Trace.WriteLogRequest request, StreamObserver<com.oc.hawk.trace_logging.Trace.WriteLogResponse> responseObserver) {
    	List<UploadTraceInfoCommand> commandList = new ArrayList<UploadTraceInfoCommand>();
    	for (com.oc.hawk.trace_logging.Trace.WriteLogRequest.LogEntry logEntry : request.getLogEntriesList()) {
            UploadTraceInfoCommand command = new UploadTraceInfoCommand();
            command.setHost(logEntry.getHost());
            command.setPath(logEntry.getPath());
            command.setDestAddr(logEntry.getDestinationAddress());
            command.setSourceAddr(logEntry.getSourceAddress());
            command.setDstWorkload(logEntry.getDestinationWorkload());
            command.setDstNamespace(logEntry.getDestinationNamespace());
            command.setTimestamp(logEntry.getTimestamp().getSeconds()*1000L);
            command.setLatency(logEntry.getLatency().getNanos());
            command.setRequestId(logEntry.getRequestId());
            command.setProtocol(logEntry.getProtocol());
            command.setMethod(logEntry.getMethod());
            command.setRequestBody(logEntry.getRequestBody());
            command.setResponseBody(logEntry.getResponseBody());

            String requestHeader = logEntry.getRequestHeaders();
            Object[] reqHeaderList = JsonUtils.json2Object(requestHeader,Object[].class);
            Map<String,String> requestMap = new HashMap<String,String>();
            for(Object obj : reqHeaderList) {
            	List<String> list = (List<String>)obj;
            	if(list.get(0).startsWith(":")) {
            		continue;
            	}else{
            		if(list.get(0).equalsIgnoreCase("x-b3-spanid")) {
            			command.setTraceId(list.get(1));
            		}else if(list.get(0).equalsIgnoreCase("x-b3-traceid")) {
            			command.setSpanId(list.get(1));
            		}else if(list.get(0).equalsIgnoreCase("x-b3-parentspanid")) {
            			command.setParentSpanId(list.get(1));
            		}else {
            			requestMap.put((String)list.get(0), (String)list.get(1));
            		}
            	}
            }
            command.setRequestHeaders(requestMap);

            String responseHeader = logEntry.getResponseHeaders();
            Object[] respHeaderList = JsonUtils.json2Object(responseHeader,Object[].class);
            Map<String,String> responseMap = new HashMap<String,String>();
            for(Object obj : respHeaderList) {
            	List<String> list = (List<String>)obj;
            	if(list.get(0).startsWith(":")) {
            		if(list.get(0).equalsIgnoreCase(":status")) {
            			command.setResponseCode(list.get(1));
            		}
            		continue;
            	}else{
            		responseMap.put(list.get(0), list.get(1));
            	}
            }
            command.setResponseHeaders(responseMap);

            commandList.add(command);
        }

        entryPointTraceInfoUseCase.createTrace(commandList);
        log.info("complete");
        responseObserver.onNext(com.oc.hawk.trace_logging.Trace.WriteLogResponse.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
