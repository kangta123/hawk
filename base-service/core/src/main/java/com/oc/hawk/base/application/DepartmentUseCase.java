package com.oc.hawk.base.application;

import com.google.common.collect.Lists;
import com.oc.hawk.api.exception.AppBusinessException;
import com.oc.hawk.base.port.driven.persistence.DepartmentRepository;
import com.oc.hawk.base.port.driven.persistence.po.DepartmentPo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentUseCase {
    private final DepartmentRepository departmentRepository;

    public List<DepartmentPo> queryAll() {
        return Lists.newArrayList(departmentRepository.findAll());
    }

    public DepartmentPo getDepartment(long id) {
        return departmentRepository.findById(id).orElseThrow(() -> new AppBusinessException("部门不存在, id:" + id));
    }

}
