package com.oc.hawk.jvm.application.representation;

import lombok.Data;

/**
 * @author kangta123
 */
@Data
public class JvmThreadDTO {
    private long id;
    private String name;
    private String group;
    private int priority;
    private String state;
    private double cpu;
    private long deltaTime;
    private long time;
    private boolean interrupted;
    private boolean daemon;
}
