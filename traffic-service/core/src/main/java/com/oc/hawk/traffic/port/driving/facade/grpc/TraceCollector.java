package com.oc.hawk.traffic.port.driving.facade.grpc;

import com.oc.hawk.trace_logging.LoggingServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author kangta123
 */
@Slf4j
@Component
public class TraceCollector extends LoggingServiceGrpc.LoggingServiceImplBase {
    @Override
    public void writeLog(com.oc.hawk.trace_logging.Trace.WriteLogRequest request, StreamObserver<com.oc.hawk.trace_logging.Trace.WriteLogResponse> responseObserver) {
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
        }

        log.info("complete");
        responseObserver.onNext(com.oc.hawk.trace_logging.Trace.WriteLogResponse.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
