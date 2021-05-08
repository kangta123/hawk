package com.oc.hawk.traffic.application;

import com.oc.hawk.traffic.application.entrypoint.representation.EntryPointConfigRepresentation;
import com.oc.hawk.traffic.application.entrypoint.representation.facade.ProjectFacade;
import com.oc.hawk.traffic.entrypoint.api.dto.TraceItemPageDTO;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.*;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.*;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.Trace;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.TraceId;
import com.oc.hawk.traffic.port.driven.facade.RemoteProjectFacade;
import com.oc.hawk.traffic.port.driven.facade.feign.ProjectGateway;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class EntryPointRepresentationTest extends TrafficBaseTest {
    
    EntryPointConfigRepresentation entryPointConfigRepresentation;
    ProjectFacade projectFacade;
    ProjectGateway projectGateway;
    
    @BeforeEach
    public void setup() {
        projectFacade = new RemoteProjectFacade(projectGateway);
        entryPointConfigRepresentation = new EntryPointConfigRepresentation(projectFacade);
    }
    
    @Test
    void testToTraceDetailList_traceListNotNull() {
        List<Trace> traceList = List.of(getTrace());
        TraceItemPageDTO dto = entryPointConfigRepresentation.toTraceDetailList(traceList,10);
        Assertions.assertThat(dto.isHasNext()).isEqualTo(false);
    }
    
    EntryPointConfig getEntryPointConfig() {
        EntryPointDescription design = new EntryPointDescription(str(),str());
        HttpResource resource = new HttpResource(new HttpPath(str()),HttpMethod.GET);
        return EntryPointConfig.builder()
                .configId(new EntryPointConfigID(along()))
                .groupId(new EntryPointGroupID(1L))
                .description(design)
                .httpResource(resource)
                .build();
    }
    
    EntryPointConfigGroup getEntryPointConfigGroup() {
        return EntryPointConfigGroup.builder()
                .groupId(new EntryPointGroupID(1L))
                .groupName(str())
                .build();
    }
    
    Trace getTrace() {
        return Trace.builder()
                .id(new TraceId(along()))
                .destination(new Destination(str(),str(),str()))
                .entryPointId(along())
                .entryPointName(str())
                .host(str())
                .timestamp(System.currentTimeMillis())
                .responseCode(new HttpResponseCode(1))
                .spanContext(new SpanContext(str(),str(),str(), "client"))
                .latency(new Latency(along()))
                .httpResource(new HttpResource(new HttpPath(),HttpMethod.GET))
                .build();
    }
    
}
