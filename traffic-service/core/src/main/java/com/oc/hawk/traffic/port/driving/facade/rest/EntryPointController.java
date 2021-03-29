package com.oc.hawk.traffic.port.driving.facade.rest;

import com.oc.hawk.api.utils.JsonUtils;
import com.oc.hawk.common.spring.mvc.BooleanWrapper;
import com.oc.hawk.ddd.web.DomainPage;
import com.oc.hawk.traffic.application.entrypoint.EntryPointUseCase;
import com.oc.hawk.traffic.entrypoint.api.command.CreateEntryPointCommand;
import com.oc.hawk.traffic.entrypoint.api.command.ExecuteCommand;
import com.oc.hawk.traffic.entrypoint.api.dto.*;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.TraceId;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/entrypoint")
@RequiredArgsConstructor
@Slf4j
public class EntryPointController {

    private final EntryPointUseCase entryPointUseCase;
    
    private final String contentType = "application/octet-stream";

    /**
     * 创建API
     */
    @PostMapping("")
    public BooleanWrapper createApi(@RequestBody CreateEntryPointCommand apiCommand) {
        entryPointUseCase.createApi(apiCommand);
        return BooleanWrapper.TRUE;
    }

    /**
     * 创建分组
     */
    @PostMapping("/group")
    public BooleanWrapper createUserGroup(@RequestParam(required = false) String groupName) {
        entryPointUseCase.createGroup(groupName);
        return BooleanWrapper.TRUE;
    }

    /**
     * 查询用户所有可见分组及分组下的api
     */
    @GetMapping("")
    public List<UserGroupEntryPointDTO> queryUserGroupApiList() {
        return entryPointUseCase.queryGroupAndApiList();
    }

    /**
     * 查询用户所有分组及用户可见分组
     */
    @GetMapping("/group")
    public List<UserGroupDTO> queryUserAllGroupList() {
        return entryPointUseCase.queryUserAllGroupList();
    }

    /**
     * 设置分组可见度
     */
    @PutMapping("/group/visibility")
    public BooleanWrapper updateUserGroupVisibility(@RequestBody List<Long> groupIds) {
        entryPointUseCase.updateUserGroupVisibility(groupIds);
        return BooleanWrapper.TRUE;
    }

    /**
     * 根据api地址模糊查询
     */
    @GetMapping("/path")
    public List<UserEntryPointDTO> queryApiByPath(@RequestParam(required = false) String key) {
        return entryPointUseCase.queryApiByResource(key);
    }

    /**
     * 根据id查询api详情信息
     */
    @GetMapping("/info/{id}")
    public UserEntryPointDTO queryApiInfo(@PathVariable Long id) {
        return entryPointUseCase.queryApiInfo(id);
    }

    /**
     * postman内容导入
     */
    @PostMapping("/group/importance")
    public BooleanWrapper importGroup(@RequestBody String importJson) {
        ImportGroupDTO importGroupDTO = JsonUtils.json2Object(importJson, ImportGroupDTO.class);
        entryPointUseCase.importGroup(importGroupDTO);
        return BooleanWrapper.TRUE;
    }

    /**
     * 执行接口
     */
    @PostMapping("/execution")
    public ExecuteResponseDTO executor(@RequestBody ExecuteCommand executeCommand) {
        return entryPointUseCase.execute(executeCommand);
    }

    /**
     * 根据接口id,查询历史请求列表
     */
    @GetMapping("/history/page")
    public TraceResponseDTO queryApiHistoryList(@RequestParam(required=false) Integer page,
                                                @RequestParam(required=false) Integer size,
                                                @RequestParam(required=false) Long entryPointId) {
        return entryPointUseCase.queryApiHistoryList(page,size,entryPointId);
    }

    /**
     * 根据历史请求id,查询单个历史请求信息
     */
    @GetMapping("/history/{id}")
    public TraceDetailDTO queryApiHistoryInfo(@PathVariable Long id) {
        return entryPointUseCase.queryApiHistoryInfo(new TraceId(id));
    }
    
    /**
     * 删除接口
     */
    @DeleteMapping("/{id}")
    public BooleanWrapper deleteEntryPoint(@PathVariable Long id) {
        entryPointUseCase.deleteEntryPoint(id);
        return BooleanWrapper.TRUE;
    }
    
    /**
     * 链路列表信息查询
     */
    @GetMapping("/trace")
    public List<TraceDetailDTO> queryApiTraceInfoList(
            @RequestParam(required=false) Integer page,
            @RequestParam(required=false) Integer size,
            @RequestParam(required=false) String path,
            @RequestParam(required=false) String instanceName){
    	return entryPointUseCase.queryTraceInfoList(page,size,path,instanceName);
    }
    
    /**
     * 链路节点列表查询
     */
    @GetMapping("/trace/node")
    public List<TraceNodeDTO> queryTraceNodeList(@RequestParam(required=false) String spanId) {
        return entryPointUseCase.queryTraceNodeList(spanId);
    }
    
    @GetMapping("/file/wasmconfig")
    public ResponseEntity<Resource> wasmConfigFile(@RequestParam(required=false) String fileName) {
        String wasmFile = readWasmFile();
        return textToFile(wasmFile,fileName);
    }
    
    private ResponseEntity<Resource> textToFile(String text,String fileName) {
        if (StringUtils.isNotEmpty(text)) {
            Resource resource = new ByteArrayResource(text.getBytes());
            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileName)
                .body(resource);
        }
        return ResponseEntity.of(Optional.empty());
    }
    
    
    private String readWasmFile(){
        StringBuilder sb = new StringBuilder("");
        FileReader fileReader = null;
        BufferedReader br = null;
        try {
            Resource resource = new ClassPathResource("files/wasm");
            File file = resource.getFile();
            fileReader = new FileReader(file);
            br = new BufferedReader(fileReader);
            String temp = "";
            while ((temp = br.readLine()) != null) {
              sb.append(temp + "\n");
            }
        }catch(Exception e) {
            sb = new StringBuilder(""); 
        }finally {
            try {
                if(fileReader!=null) {
                    fileReader.close();
                }
                if(br!=null) {
                    br.close();
                }
            }catch(Exception e) {
                log.error("read wasm file exception");
            }
        }
        return sb.toString();
    }
}
