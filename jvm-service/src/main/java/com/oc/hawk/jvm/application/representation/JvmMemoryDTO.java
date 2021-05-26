package com.oc.hawk.jvm.application.representation;

import lombok.Data;

/**
 * @author kangta123
 */
@Data
public class JvmMemoryDTO {
    private String type;
    private String name;
    private long used;
    private long total;
    private long max;
}
