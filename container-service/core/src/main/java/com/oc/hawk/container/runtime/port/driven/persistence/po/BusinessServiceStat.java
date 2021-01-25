package com.oc.hawk.container.runtime.port.driven.persistence.po;

import com.oc.hawk.common.hibernate.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Getter
@Setter
@Table(name = "container_service_runner_stat")
@Entity
@NoArgsConstructor
public class BusinessServiceStat extends BaseEntity  {
    private long departmentId;
    private long projectId;
    private long runnerCount;
    private long configurationCount;
    private LocalDate date =  LocalDate.now();

    public BusinessServiceStat(long projectId, long departmentId, long runnerCount, long configurationCount) {
        this.departmentId = departmentId;
        this.projectId = projectId;
        this.runnerCount = runnerCount;
        this.configurationCount = configurationCount;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
