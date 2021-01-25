package com.oc.hawk.kubernetes.runtime.port.driven.facade;

import com.oc.hawk.project.api.dto.ProjectDetailDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("project")
public interface BaseGateway {
    @RequestMapping(method = RequestMethod.GET, path = "/project/{id}")
    ProjectDetailDTO getProject(@PathVariable long id);
}
