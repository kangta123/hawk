package com.oc.hawk.traffic.entrypoint.domain.service;

import com.oc.hawk.test.TestHelper;
import com.oc.hawk.traffic.entrypoint.EntryPointBaseTest;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfig;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfigID;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointDescription;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.Destination;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpMethod;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpPath;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpResource;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.SpanContext;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.Trace;
import static org.mockito.Mockito.when;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class EntryPointTracesTest extends EntryPointBaseTest {
    
    @BeforeEach
    public void setup() {
        
    }
    
    /**
     * 测试查询链路信息,路径path为空
     */
    @Test
    void testQueryTraceInfoList_pathIsNull() {
        when(entryPointConfigRepository.queryTraceInfoList(any(),any(),any(),any())).thenReturn(List.of(getTrace()));
        List<Trace> traceList = new EntryPointTraces(entryPointConfigRepository).queryTraceInfoList(integer(), integer(), str(),null);
        Assertions.assertThat(traceList).isNotEmpty();
    }
    
    /**
     * 测试查询链路信息,实例名为空
     */
    @Test
    void testQueryTraceInfoList_instanceNameIsNull() {
        when(entryPointConfigRepository.queryTraceInfoList(any(),any(),any(),any())).thenReturn(List.of(getTrace()));
        List<Trace> traceList = new EntryPointTraces(entryPointConfigRepository).queryTraceInfoList(integer(), integer(), str(),null);
        Assertions.assertThat(traceList).isNotEmpty();
    }
    
    /**
     * 测试查询链路信息,路径与实例名都为空
     */
    @Test
    void testQueryTraceInfoList_pathAndInstanceNameIsNull() {
        when(entryPointConfigRepository.queryTraceInfoList(any(),any(),any(),any())).thenReturn(List.of(getTrace()));
        List<Trace> traceList = new EntryPointTraces(entryPointConfigRepository).queryTraceInfoList(integer(), integer(), null,null);
        Assertions.assertThat(traceList).isNotEmpty();
    }
    
    /**
     * 测试查询链路信息,路径与实例名都不为空
     */
    @Test
    void testQueryTraceInfoList_pathAndInstanceNameAlreadyExists() {
        Trace traceParam = Trace.builder()
                .httpResource(new HttpResource(new HttpPath("/a/b"),HttpMethod.GET))
                .destination(new Destination("abcd", null, null))
                .build();
        when(entryPointConfigRepository.queryTraceInfoList(any(),any(),any(),any())).thenReturn(List.of(traceParam));
        when(entryPointConfigRepository.findByPathAndMethod(any(),eq(HttpMethod.GET))).thenReturn(null);
        
        when(entryPointConfigRepository.findByMethodAndRestfulPath(eq(HttpMethod.GET))).thenReturn(List.of(getEntryPointConfig()));
        when(entryPointConfigRepository.byId(new EntryPointConfigID(any()))).thenReturn(getEntryPointConfig());
        
        List<Trace> traceList = new EntryPointTraces(entryPointConfigRepository).queryTraceInfoList(integer(), integer(), "/a/b",null);
        Assertions.assertThat(traceList.get(0).getEntryPointName()).isNotBlank();
    }
    
    /**
     * 测试查询链路节点信息,spanId存在
     */
    @Test
    void testQueryTraceNodeList_spanIdAlreadyExists() {
        Trace traceParam = Trace.builder().spanContext(new SpanContext("1",null,null)).build();
        when(entryPointConfigRepository.findBySpanId(any())).thenReturn(getTrace());
        when(entryPointConfigRepository.findByTraceId(any())).thenReturn(List.of(getTrace()));
        List<Trace> traceList = new EntryPointTraces(entryPointConfigRepository).queryTraceNodeList(traceParam);
        
        Assertions.assertThat(traceList).isNotEmpty();
    }
    
    /**
     * 测试查询链路节点信息,spanId不存在
     */
    @Test
    void testQueryTraceNodeList_spanIdIsNull() {
        Trace traceParam = Trace.builder().spanContext(new SpanContext(null,null,null)).build();
        when(entryPointConfigRepository.findBySpanId(eq(traceParam))).thenReturn(null);
        List<Trace> traceList = new EntryPointTraces(entryPointConfigRepository).queryTraceNodeList(traceParam);
        
        Assertions.assertThat(traceList).isEmpty();
    }
    
    private Trace getTrace() {
        return TestHelper.newInstance(Trace.class);
    }
    
    private EntryPointConfig getEntryPointConfig() {
        return EntryPointConfig.builder()
                .configId(new EntryPointConfigID(1L))
                .description(new EntryPointDescription("name123",""))
                .httpResource(new HttpResource(new HttpPath("/a/b"), HttpMethod.GET))
                .build();
    }
}
