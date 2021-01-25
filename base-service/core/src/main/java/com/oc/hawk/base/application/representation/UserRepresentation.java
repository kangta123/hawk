package com.oc.hawk.base.application.representation;

import com.oc.hawk.base.api.dto.AuthDTO;
import com.oc.hawk.base.api.dto.DepartmentDTO;
import com.oc.hawk.base.api.dto.UserDTO;
import com.oc.hawk.base.domain.model.department.Department;
import com.oc.hawk.base.domain.model.user.User;
import com.oc.hawk.base.domain.model.user.UserId;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserRepresentation {
    public UserDTO toUserDTO(User user) {
        final UserDTO userDTO = new UserDTO();
        userDTO.setAuthName(user.getAuthName());
        userDTO.setEmail(user.getEmail());
        userDTO.setId(user.getId().getId());
        userDTO.setName(user.getName());
        userDTO.setPhone(user.getPhone());

        final Department department = user.getDepartment();
        userDTO.setDepartment(toDepartmentDto(department));

        return userDTO;
    }

    public List<UserDTO> toUserDTO(List<User> users) {
        if (CollectionUtils.isEmpty(users)) {
            return null;
        }
        return users.stream().map(this::toUserDTO).collect(Collectors.toList());
    }

    public DepartmentDTO toDepartmentDto(Department department) {
        if (department != null) {
            final DepartmentDTO departmentDTO = new DepartmentDTO();
            departmentDTO.setId(department.getId().getId());
            departmentDTO.setMaster(department.isMaster());
            departmentDTO.setName(department.getName());
            departmentDTO.setProjectName(department.getProjectName());
            return departmentDTO;
        }
        return null;

    }

    public AuthDTO toAuthDto(User user, String token) {
        final UserId id = user.getId();
        AuthDTO authDTO = new AuthDTO();
        authDTO.setToken(token);
        authDTO.setId(id.getId());
        authDTO.setName(user.getName());
        authDTO.setEmail(user.getEmail());
        authDTO.setDepartment(toDepartmentDto(user.getDepartment()));
        authDTO.setEmail(user.getEmail());

        return authDTO;
    }
}
