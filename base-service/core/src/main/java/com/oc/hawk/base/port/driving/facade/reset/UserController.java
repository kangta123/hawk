package com.oc.hawk.base.port.driving.facade.reset;

import com.oc.hawk.base.api.dto.AddUserDTO;
import com.oc.hawk.base.api.dto.DepartmentDTO;
import com.oc.hawk.base.api.dto.QueryUserDTO;
import com.oc.hawk.base.api.dto.UserDTO;
import com.oc.hawk.base.application.UserUseCase;
import com.oc.hawk.common.spring.mvc.BooleanWrapper;
import com.oc.hawk.ddd.web.DomainPage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserUseCase userUseCase;

    @PostMapping
    public List<UserDTO> queryUsers(@RequestBody(required = false) QueryUserDTO queryUserDTO) {
        return userUseCase.queryUsers(queryUserDTO);
    }

    @GetMapping("/{id}/department")
    public DepartmentDTO getUserDepartment(@PathVariable("id") long id) {
        final UserDTO user = userUseCase.getUser(id);
        return user.getDepartment();
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable("id") long id) {
        return userUseCase.getUser(id);
    }

    @GetMapping("/page")
    public DomainPage<UserDTO> queryUserPage(
        @RequestParam(name = "size") int size,
        @RequestParam(name = "page") int page,
        @RequestParam(name = "key", required = false) String key) {
        return userUseCase.queryUserPage(page, size, key);

    }

    @PostMapping("/register")
    public BooleanWrapper registerUser(@RequestBody AddUserDTO addUserDTO) {
        userUseCase.registerUser(addUserDTO);
        return BooleanWrapper.TRUE;
    }

    @DeleteMapping("/{id}")
    public BooleanWrapper deleteUser(@PathVariable("id") long id) {
        userUseCase.deleteUser(id);
        return BooleanWrapper.TRUE;
    }
}
