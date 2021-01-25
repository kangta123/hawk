package com.oc.hawk.container.runtime.port.driven.facade.feign;


import com.oc.hawk.base.api.dto.DepartmentDTO;
import com.oc.hawk.base.api.dto.QueryUserDTO;
import com.oc.hawk.base.api.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "base")
public interface BaseGateway {

    @RequestMapping(method = RequestMethod.GET, path = "/user/{id}")
    UserDTO getUser(@PathVariable("id") long id);


    @RequestMapping(method = RequestMethod.POST, path = "/user")
    List<UserDTO> queryUsers(QueryUserDTO queryUserDTO);

    @RequestMapping(method = RequestMethod.GET, path = "/department/{id}")
    DepartmentDTO getDepartment(@PathVariable("id") Long id);

    @RequestMapping(method = RequestMethod.GET, path = "/department")
    List<DepartmentDTO> queryDepartments();

}
