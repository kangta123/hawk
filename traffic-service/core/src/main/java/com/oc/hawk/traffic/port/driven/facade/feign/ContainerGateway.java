package com.oc.hawk.traffic.port.driven.facade.feign;

import com.oc.hawk.container.api.dto.InstanceConfigDTO;
import com.oc.hawk.container.api.dto.InstanceProjectDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "container")
public interface ContainerGateway {
    
    @RequestMapping(method = RequestMethod.GET, value = "/instance/names")
    List<InstanceProjectDTO> getProjectInstances(@RequestParam("projectIds") String projectIds);
    
    @RequestMapping(method = RequestMethod.GET, value = "/instance/{id}")
    InstanceConfigDTO getById(@PathVariable(value="id") Long id);
}

