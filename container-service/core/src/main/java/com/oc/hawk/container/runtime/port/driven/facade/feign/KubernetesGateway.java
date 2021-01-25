package com.oc.hawk.container.runtime.port.driven.facade.feign;

import com.oc.hawk.common.spring.mvc.BooleanWrapper;
import com.oc.hawk.kubernetes.api.constants.RuntimeInfoDTO;
import com.oc.hawk.kubernetes.api.dto.HawkHttpPolicyRequestDTO;
import com.oc.hawk.kubernetes.api.dto.MetricResultDTO;
import com.oc.hawk.kubernetes.api.dto.QueryTrafficDTO;
import com.oc.hawk.kubernetes.api.dto.ServiceEntryPointDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "kubernetes")
public interface KubernetesGateway {
    @RequestMapping(method = RequestMethod.GET, value = "/ns/runtime")
    List<RuntimeInfoDTO> queryRuntimeInfo(@RequestParam String namespace,
                                          @RequestParam Long projectId,
                                          @RequestParam String name,
                                          @RequestParam boolean ready);


    @RequestMapping(method = RequestMethod.PUT, value = "/ns/service/scale")
    BooleanWrapper scaleService(@RequestParam String name, @RequestParam String namespace, @RequestParam int value);


    @RequestMapping(method = RequestMethod.POST, value = "/istio/traffic")
    MetricResultDTO queryServiceTraffic(QueryTrafficDTO traffic);

    @RequestMapping(method = RequestMethod.POST, value = "/ns/entry")
    void createEntryPoint(ServiceEntryPointDTO serviceEntryPointDTO);

    @RequestMapping(method = RequestMethod.PUT, value = "/istio/traffic/policy")
    BooleanWrapper applyServiceAppRules(@RequestBody HawkHttpPolicyRequestDTO rules);
}
