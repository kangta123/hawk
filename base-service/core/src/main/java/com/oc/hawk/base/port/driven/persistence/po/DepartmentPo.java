package com.oc.hawk.base.port.driven.persistence.po;

import com.oc.hawk.base.api.dto.DepartmentDTO;
import com.oc.hawk.common.hibernate.BaseEntity;
import com.oc.hawk.common.spring.mvc.DTOConvert;
import com.oc.hawk.common.utils.BeanUtils;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "base_user_department")
public class DepartmentPo extends BaseEntity implements DTOConvert<DepartmentDTO> {
    private String name;
    private String projectName;
    private boolean master;

    @Override
    public DepartmentDTO convert() {
        return BeanUtils.transform(DepartmentDTO.class, this);
    }
}
