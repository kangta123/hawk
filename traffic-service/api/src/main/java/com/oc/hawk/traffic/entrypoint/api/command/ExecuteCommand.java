package com.oc.hawk.traffic.entrypoint.api.command;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ExecuteCommand {
    /**
     * form表单参数
     */
    private List<Map<String, String>> requestParams;
    /**
     * header头
     */
    private List<Map<String, String>> requestHeaders;
    /**
     * 请求体
     */
    private String requestBody;
    /**
     * 请求路径参数
     */
    private List<Map<String, String>> uriParams;
    /**
     * 配置接口id
     */
    private Long entryPointId;
    /**
     * 实例id
     */
    private String instanceId;
    /**
     * 项目id
     */
    private Long projectId;
}
