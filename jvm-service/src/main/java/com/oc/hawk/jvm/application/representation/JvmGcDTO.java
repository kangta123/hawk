package com.oc.hawk.jvm.application.representation;

import lombok.Data;

/**
 * @author kangta123
 */
@Data
public class JvmGcDTO {
    private String name;
    private long collectionCount;
    private long collectionTime;
}
