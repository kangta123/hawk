package com.oc.hawk.traffic.port.driving.facade.rest;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oc.hawk.traffic.application.entrypoint.EntryPointUseCase;
import com.oc.hawk.traffic.entrypoint.api.dto.TraceDetailDTO;
import com.oc.hawk.traffic.entrypoint.api.dto.TraceItemPageDTO;
import com.oc.hawk.traffic.entrypoint.api.dto.TraceNodeDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TraceController {
    
    private final EntryPointUseCase entryPointUseCase;
    
    /**
     * 链路列表信息查询
     */
    @GetMapping("/trace")
    public TraceItemPageDTO queryApiTraceInfoList(
            @RequestParam(required=false) Integer page,
            @RequestParam(required=false) Integer size,
            @RequestParam(required=false) String key){
        return entryPointUseCase.queryTraceInfoList(page,size,key);
    }
    
    /**
     * 链路节点列表查询
     */
    @GetMapping("/trace/node")
    public List<TraceNodeDTO> queryTraceNodeList(@RequestParam(required=false) String spanId) {
        return entryPointUseCase.queryTraceNodeList(spanId);
    }
    
    /**
     * 链路详情信息查询
     */
    @GetMapping("/trace/{spanId}")
    public TraceDetailDTO queryApiTraceDetail(@PathVariable String spanId){
        return entryPointUseCase.queryApiTraceDetail(spanId);
    }
    
}
