package com.oc.hawk.traffic.port.driving.facade.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

/**
 * @author kangta123
 */
@Component
@RequiredArgsConstructor
public class GrpcServer {
    private final GrpcTraceCollector grpcTraceCollector;
    private Server server;

    @PostConstruct
    public void setup() {
        try {
            server = ServerBuilder.forPort(6565)
                .addService(grpcTraceCollector)
                .build().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void destroy() {
        if (server != null) {
            server.shutdown();
            try {
                // Wait for RPCs to complete processing
                if (!server.awaitTermination(30, TimeUnit.SECONDS)) {
                    // That was plenty of time. Let's cancel the remaining RPCs
                    server.shutdownNow();
                    // shutdownNow isn't instantaneous, so give a bit of time to clean resources up
                    // gracefully. Normally this will be well under a second.
                    server.awaitTermination(5, TimeUnit.SECONDS);
                }
            } catch (InterruptedException ex) {
                server.shutdownNow();
            }
        }
    }
}
