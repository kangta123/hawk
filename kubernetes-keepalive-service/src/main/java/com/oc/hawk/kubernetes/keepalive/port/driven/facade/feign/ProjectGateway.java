package com.oc.hawk.kubernetes.keepalive.port.driven.facade.feign;

import com.oc.hawk.project.api.dto.ProjectBuildJobDetailDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "project")
public interface ProjectGateway {
    @RequestMapping(method = RequestMethod.GET, path = "/build/job/{id}")
    ProjectBuildJobDetailDTO getProjectBuildJob(@PathVariable long id);
}
