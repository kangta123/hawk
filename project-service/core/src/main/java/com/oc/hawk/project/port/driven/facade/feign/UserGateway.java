package com.oc.hawk.project.port.driven.facade.feign;

import com.oc.hawk.base.api.dto.DepartmentDTO;
import com.oc.hawk.base.api.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "base")
public interface UserGateway {

    @RequestMapping(method = RequestMethod.GET, path = "/user/{id}")
    UserDTO getUserInfo(@PathVariable Long id);

    @RequestMapping(method = RequestMethod.GET, path = "/user/{id}/department")
    DepartmentDTO getUserDepartment(@PathVariable Long id);
}
