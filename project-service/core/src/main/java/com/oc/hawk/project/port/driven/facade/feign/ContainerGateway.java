package com.oc.hawk.project.port.driven.facade.feign;


import com.oc.hawk.container.api.dto.ProjectRuntimeStatDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "container")
public interface ContainerGateway {

    @RequestMapping(method = RequestMethod.GET, value = "/project/runtime/summary")
    ProjectRuntimeStatDTO loadRuntimeCountByProjects(@RequestParam("projectIds") List<Long> projectIds);
}

