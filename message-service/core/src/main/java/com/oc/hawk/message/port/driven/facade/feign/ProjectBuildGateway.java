package com.oc.hawk.message.port.driven.facade.feign;

import com.oc.hawk.project.api.dto.ProjectBuildJobDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "project")
public interface ProjectBuildGateway {
    @RequestMapping(method = RequestMethod.GET, path = "/build/job/{id}")
    ProjectBuildJobDTO getProjectBuildJob(@PathVariable Long id);
}
