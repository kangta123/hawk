package com.oc.hawk.traffic.port.driving.facade.grpc;

import com.oc.hawk.api.utils.JsonUtils;
import com.oc.hawk.trace_logging.LoggingServiceGrpc;
import com.oc.hawk.traffic.application.entrypoint.EntryPointTraceInfoUseCase;
import com.oc.hawk.traffic.entrypoint.api.command.UploadTraceInfoCommand;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

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
    private final EntryPointTraceInfoUseCase entryPointTraceInfoUseCase;
    private static final Long TIME_MILLIS = 1000L; 

    @Override
    public void writeLog(com.oc.hawk.trace_logging.Trace.WriteLogRequest request, StreamObserver<com.oc.hawk.trace_logging.Trace.WriteLogResponse> responseObserver) {
        List<UploadTraceInfoCommand> commandList = new ArrayList<UploadTraceInfoCommand>();
        for (com.oc.hawk.trace_logging.Trace.WriteLogRequest.LogEntry logEntry : request.getLogEntriesList()) {
            log.info("receive log: {} , {} to {}", logEntry.getMethod(), logEntry.getPath(), logEntry.getDestinationWorkload());
            UploadTraceInfoCommand command = getTraceInfoCommand(logEntry);
            buildRequestHeader(logEntry, command);
            buildResponseHeader(logEntry, command);
            commandList.add(command);
        }
        entryPointTraceInfoUseCase.createTrace(commandList);
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
        command.setLatency(logEntry.getLatency().getNanos());
        command.setRequestId(logEntry.getRequestId());
        command.setProtocol(logEntry.getProtocol());
        command.setMethod(logEntry.getMethod());
        command.setRequestBody(logEntry.getRequestBody());
        command.setResponseBody(logEntry.getResponseBody());
        return command;
    }

    private void buildRequestHeader(com.oc.hawk.trace_logging.Trace.WriteLogRequest.LogEntry logEntry, UploadTraceInfoCommand command) {
        Object[] reqHeaderList = JsonUtils.json2Object(logEntry.getRequestHeaders(), Object[].class);
        command.setRequestHeaders(buildHeader(reqHeaderList, (key, value) -> {
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

    private void buildResponseHeader(com.oc.hawk.trace_logging.Trace.WriteLogRequest.LogEntry logEntry, UploadTraceInfoCommand command) {
        Object[] respHeaderList = JsonUtils.json2Object(logEntry.getResponseHeaders(), Object[].class);
        command.setResponseHeaders(buildHeader(respHeaderList, (key, value) -> {
            if (key.startsWith(":")) {
                if (key.equalsIgnoreCase(KEY_STATUS)) {
                    command.setResponseCode(value);
                }
                return false;
            }
            return true;
        }));
    }

    private Map<String, String> buildHeader(Object[] headerList, BiFunction<String, String, Boolean> consumer) {
        Map<String, String> headerMap = new HashMap<String, String>(KEY_MAP_SIZE);
        for (Object obj : headerList) {
            List<String> list = (List<String>) obj;
            String key = list.get(0);
            String value = list.get(1);
            if (consumer.apply(key, value)) {
                headerMap.put(key, value);
            }
        }
        return headerMap;
    }

}
