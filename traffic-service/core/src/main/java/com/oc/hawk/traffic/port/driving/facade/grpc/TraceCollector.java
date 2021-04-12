package com.oc.hawk.traffic.port.driving.facade.grpc;

import com.oc.hawk.api.utils.JsonUtils;
import com.oc.hawk.trace_logging.LoggingServiceGrpc;
import com.oc.hawk.traffic.application.entrypoint.TrafficTraceInfoUseCase;
import com.oc.hawk.traffic.entrypoint.api.command.UploadTraceInfoCommand;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.TraceHeaderConfig;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * @author kangta123
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TraceCollector extends LoggingServiceGrpc.LoggingServiceImplBase {

    private static final String KEY_SPAN_ID = "x-b3-spanid";
    private static final String KEY_TRACE_ID = "x-b3-traceid";
    private static final String KEY_PARENT_SPAN_ID = "x-b3-parentspanid";
    private static final String KEY_STATUS = ":status";
    private static final Integer KEY_MAP_SIZE = 8;
    private final TrafficTraceInfoUseCase trafficTraceInfoUseCase;
    private static final Long TIME_MILLIS = 1000L; 

    @Override
    public void writeLog(com.oc.hawk.trace_logging.Trace.WriteLogRequest request, StreamObserver<com.oc.hawk.trace_logging.Trace.WriteLogResponse> responseObserver) {
        List<UploadTraceInfoCommand> commandList = new ArrayList<UploadTraceInfoCommand>();
        
        List<TraceHeaderConfig> headerConfigList = trafficTraceInfoUseCase.findTraceHeaderConfig();
        for (com.oc.hawk.trace_logging.Trace.WriteLogRequest.LogEntry logEntry : request.getLogEntriesList()) {
            log.info("receive log: {} , {} to {}", logEntry.getMethod(), logEntry.getPath(), logEntry.getDestinationWorkload());
            UploadTraceInfoCommand command = getTraceInfoCommand(logEntry);
            buildRequestHeader(logEntry, command, headerConfigList);
            buildResponseHeader(logEntry, command, headerConfigList);
            commandList.add(command);
        }
        trafficTraceInfoUseCase.createTrace(commandList);
        log.info("complete");
        responseObserver.onNext(com.oc.hawk.trace_logging.Trace.WriteLogResponse.getDefaultInstance());
        responseObserver.onCompleted();
    }

    private UploadTraceInfoCommand getTraceInfoCommand(com.oc.hawk.trace_logging.Trace.WriteLogRequest.LogEntry logEntry) {
        UploadTraceInfoCommand command = new UploadTraceInfoCommand();
        command.setHost(logEntry.getHost());
        command.setPath(logEntry.getPath());
        command.setDestAddr(logEntry.getDestinationAddress());
        command.setSourceAddr(logEntry.getSourceAddress());
        command.setDstWorkload(logEntry.getDestinationWorkload());
        command.setDstNamespace(logEntry.getDestinationNamespace());
        command.setTimestamp(logEntry.getTimestamp().getSeconds() * TIME_MILLIS);
        command.setLatency(Long.valueOf(logEntry.getLatency().getNanos()));
        command.setRequestId(logEntry.getRequestId());
        command.setProtocol(logEntry.getProtocol());
        command.setMethod(logEntry.getMethod());
        command.setRequestBody(logEntry.getRequestBody());
        command.setResponseBody(logEntry.getResponseBody());
        return command;
    }
    
    private void buildRequestHeader(com.oc.hawk.trace_logging.Trace.WriteLogRequest.LogEntry logEntry, UploadTraceInfoCommand command,List<TraceHeaderConfig> headerConfigList) {
        Object[] reqHeaderList = JsonUtils.json2Object(logEntry.getRequestHeaders(), Object[].class);      
        List<TraceHeaderConfig> requestConfigList = headerConfigList.stream().filter(obj -> obj.getKeyType()==TraceHeaderConfig.REQUEST_TYPE).collect(Collectors.toList());
        List<String> requestKeyList = requestConfigList.stream().map(obj -> obj.getKeyName()).collect(Collectors.toList());
        command.setRequestHeaders(buildHeader(reqHeaderList, requestKeyList, (key, value) -> {
            switch (key) {
                case KEY_SPAN_ID:
                    command.setSpanId(value);
                    break;
                case KEY_TRACE_ID:
                    command.setTraceId(value);
                    break;
                case KEY_PARENT_SPAN_ID:
                    command.setParentSpanId(value);
                    break;
                default:
                    return true;
            }
            return false;
        }));
    }

    private void buildResponseHeader(com.oc.hawk.trace_logging.Trace.WriteLogRequest.LogEntry logEntry, UploadTraceInfoCommand command,List<TraceHeaderConfig> headerConfigList) {
        Object[] respHeaderList = JsonUtils.json2Object(logEntry.getResponseHeaders(), Object[].class);
        List<TraceHeaderConfig> responseConfigList = headerConfigList.stream().filter(obj -> obj.getKeyType()==TraceHeaderConfig.RESPONSE_TYPE).collect(Collectors.toList());
        List<String> responseKeyList = responseConfigList.stream().map(obj -> obj.getKeyName()).collect(Collectors.toList());
        command.setResponseHeaders(buildHeader(respHeaderList, responseKeyList, (key, value) -> {
            if (key.startsWith(":")) {
                if (key.equalsIgnoreCase(KEY_STATUS)) {
                    command.setResponseCode(value);
                }
                return false;
            }
            return true;
        }));
    }

    private Map<String, String> buildHeader(Object[] headerList, List<String> configList, BiFunction<String, String, Boolean> consumer) {
        Map<String, String> headerMap = new HashMap<String, String>(KEY_MAP_SIZE);
        for (Object obj : headerList) {
            List<String> list = (List<String>) obj;
            String key = list.get(0);
            if(configList.contains(key)) {
                continue;
            }
            String value = list.get(1);
            if (consumer.apply(key, value)) {
                headerMap.put(key, value);
            }
        }
        return headerMap;
    }

}
