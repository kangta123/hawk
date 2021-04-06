package com.oc.hawk.traffic.port.driven.facade.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.oc.hawk.project.api.dto.ProjectDetailDTO;

import java.util.List;

@FeignClient(name = "project")
public interface ProjectGateway {

    @RequestMapping(method = RequestMethod.GET, path = "/ids")
    List<Long> queryVisibleProjectIds();
    
    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    ProjectDetailDTO getProject(@PathVariable Long id);
    
}
