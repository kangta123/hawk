package com.oc.hawk.base.port.driving.facade.reset;

import com.oc.hawk.base.api.dto.DepartmentDTO;
import com.oc.hawk.base.port.driven.persistence.po.DepartmentPo;
import com.oc.hawk.base.application.DepartmentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentUseCase departmentUseCase;

    @GetMapping
    public List <DepartmentDTO> queryAll() {
        return departmentUseCase.queryAll().stream().map(DepartmentPo::convert).collect(Collectors.toList());
    }
    @GetMapping("/{id}")
    public DepartmentDTO getDepartment(@PathVariable long id) {
        return departmentUseCase.getDepartment(id).convert();
    }
}
