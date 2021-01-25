package com.oc.hawk.monitor.port.driven.facade.feign;

import com.oc.hawk.kubernetes.api.constants.RuntimeInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author kangta123
 */
@FeignClient(name = "kubernetes")
public interface KubernetesGateway {
    @RequestMapping(method = RequestMethod.GET, value = "/ns/runtime")
    List<RuntimeInfoDTO> getAvailableRuntimeInfo(@RequestParam(required = false) String namespace,
                                                 @RequestParam(required = false) String app,
                                                 @RequestParam(required = false) String version,
                                                 @RequestParam(required = false) String name,
                                                 @RequestParam(required = false) Boolean ready);

}
