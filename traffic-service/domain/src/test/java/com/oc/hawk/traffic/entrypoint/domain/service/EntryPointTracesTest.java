package com.oc.hawk.traffic.entrypoint.domain.service;

import com.oc.hawk.test.TestHelper;
import com.oc.hawk.traffic.entrypoint.EntryPointBaseTest;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfig;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfigID;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointDesign;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointHttpResource;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointMethod;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointPath;
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
        when(entryPointConfigRepository.queryTraceInfoList(any(),any(),any())).thenReturn(List.of(getTrace()));
        List<Trace> traceList = new EntryPointTraces(entryPointConfigRepository).queryTraceInfoList(integer(), integer(), null, str());
        Assertions.assertThat(traceList).isNotEmpty();
    }
    
    /**
     * 测试查询链路信息,实例名为空
     */
    @Test
    void testQueryTraceInfoList_instanceNameIsNull() {
        when(entryPointConfigRepository.queryTraceInfoList(any(),any(),any())).thenReturn(List.of(getTrace()));
        List<Trace> traceList = new EntryPointTraces(entryPointConfigRepository).queryTraceInfoList(integer(), integer(), str(), null);
        Assertions.assertThat(traceList).isNotEmpty();
    }
    
    /**
     * 测试查询链路信息,路径与实例名都为空
     */
    @Test
    void testQueryTraceInfoList_pathAndInstanceNameIsNull() {
        when(entryPointConfigRepository.queryTraceInfoList(any(),any(),any())).thenReturn(List.of(getTrace()));
        List<Trace> traceList = new EntryPointTraces(entryPointConfigRepository).queryTraceInfoList(integer(), integer(), null, null);
        Assertions.assertThat(traceList).isNotEmpty();
    }
    
    /**
     * 测试查询链路信息,路径与实例名都不为空
     */
    @Test
    void testQueryTraceInfoList_pathAndInstanceNameAlreadyExists() {
        Trace traceParam = Trace.builder().path("/a/b").dstWorkload("abcd").method(EntryPointMethod.GET.name()).build();
        when(entryPointConfigRepository.queryTraceInfoList(any(),any(),any())).thenReturn(List.of(traceParam));
        when(entryPointConfigRepository.findByPathAndMethod(any(),eq(EntryPointMethod.GET))).thenReturn(null);
        
        when(entryPointConfigRepository.findByMethodAndRestfulPath(eq(EntryPointMethod.GET))).thenReturn(List.of(getEntryPointConfig()));
        when(entryPointConfigRepository.byId(new EntryPointConfigID(any()))).thenReturn(getEntryPointConfig());
        
        List<Trace> traceList = new EntryPointTraces(entryPointConfigRepository).queryTraceInfoList(integer(), integer(), "/a/b", "abcd");
        Assertions.assertThat(traceList.get(0).getEntryPointName()).isNotBlank();
    }
    
    private Trace getTrace() {
        return TestHelper.newInstance(Trace.class);
    }
    
    private EntryPointConfig getEntryPointConfig() {
        return EntryPointConfig.builder()
                .configId(new EntryPointConfigID(1L))
                .design(new EntryPointDesign("name123",""))
                .httpResource(new EntryPointHttpResource(new EntryPointPath("/a/b"), EntryPointMethod.GET, null))
                .build();
    }
}
