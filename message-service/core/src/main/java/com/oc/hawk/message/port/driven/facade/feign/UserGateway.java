package com.oc.hawk.message.port.driven.facade.feign;

import com.oc.hawk.base.api.dto.QueryUserDTO;
import com.oc.hawk.base.api.dto.UserDTO;
import com.oc.hawk.message.domain.model.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "base")
public interface UserGateway {

    @RequestMapping(method = RequestMethod.POST, path = "/user")
    List<UserDTO> getUserInfo(@RequestBody QueryUserDTO queryUserDTO);

    @RequestMapping(method = RequestMethod.GET, path = "/user/{id}")
    UserInfo getUser(@PathVariable Long id);
}
