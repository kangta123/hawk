package com.oc.hawk.container.runtime.port.driven.persistence;

import com.oc.hawk.container.runtime.port.driven.persistence.po.BusinessServiceStat;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BusinessServiceStatRepository extends CrudRepository <BusinessServiceStat, Long> {

    void deleteByDate(LocalDate now);

    @Query("select date as date, sum(runnerCount) as runnerCount, sum(configurationCount) as configurationCount from BusinessServiceStat group by date")
    List<RunnerStatByDate> queryByDate();
}
