package com.oc.hawk.project.port.driven.persistence.po;

import java.time.LocalDateTime;

public interface InstanceImageInfoPO {
    String getTag();

    String getImage();

    String getBranch();

    long getJobId();

    LocalDateTime getTime();
}
