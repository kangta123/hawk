package com.oc.hawk.container.runtime.port.driven.persistence;

import java.time.LocalDate;

public interface RunnerStatByDate {
    LocalDate getDate();
    Long getRunnerCount();
    Long getConfigurationCount();
}
