package com.oc.hawk.traffic.port.driving.facade.grpc;

import com.oc.hawk.api.utils.JsonUtils;
import com.oc.hawk.trace_logging.LoggingServiceGrpc;
import com.oc.hawk.traffic.application.entrypoint.EntryPointTraceInfoUseCase;
import com.oc.hawk.traffic.entrypoint.api.command.RequestHeaderCommand;
import com.oc.hawk.traffic.entrypoint.api.command.UploadTraceInfoCommand;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            System.out.println("host:" + logEntry.getHost());
            System.out.println("path:" + logEntry.getPath());
            System.out.println("dest addr:" + logEntry.getDestinationAddress());
            System.out.println("source addr:" + logEntry.getSourceAddress());
            System.out.println("dst workload:" + logEntry.getDestinationWorkload());
            System.out.println("dst ns:" + logEntry.getDestinationNamespace());
            System.out.println("timestamp :" + logEntry.getTimestamp());
            System.out.println("latency:" + logEntry.getLatency());
            System.out.println("request id:" + logEntry.getRequestId());
            System.out.println("protocol: " + logEntry.getProtocol());
            System.out.println("method: " + logEntry.getMethod());
            System.out.println("request headers: " + logEntry.getRequestHeaders());
            System.out.println("response headers: " + logEntry.getResponseHeaders());
            System.out.println("response body: " + logEntry.getResponseBody());
            System.out.println("request body: " + logEntry.getRequestBody());
            
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
