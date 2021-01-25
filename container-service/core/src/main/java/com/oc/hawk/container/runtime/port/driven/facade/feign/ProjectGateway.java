package com.oc.hawk.container.runtime.port.driven.facade.feign;

import com.oc.hawk.common.spring.mvc.BooleanWrapper;
import com.oc.hawk.project.api.dto.InstanceImageDTO;
import com.oc.hawk.project.api.dto.ProjectBuildJobDTO;
import com.oc.hawk.project.api.dto.ProjectDetailDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "project")
public interface ProjectGateway {
    @RequestMapping(method = RequestMethod.GET, path = "/count")
    BooleanWrapper queryProjectCount();

    @RequestMapping(method = RequestMethod.GET, path = "/ids")
    List<Long> queryVisibleProjectIds();

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    ProjectDetailDTO getProject(@PathVariable Long id);

    @RequestMapping(method = RequestMethod.GET, path = "/build/job/images")
    List<InstanceImageDTO> getInstanceImages(@RequestParam long projectId, @RequestParam String tag);

    @RequestMapping(method = RequestMethod.GET, path = "/build/job/{id}")
    ProjectBuildJobDTO getProjectBuildJob(@PathVariable long id);
}
