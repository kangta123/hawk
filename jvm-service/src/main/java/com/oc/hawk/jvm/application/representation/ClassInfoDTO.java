package com.oc.hawk.jvm.application.representation;

import lombok.Data;

import java.util.List;

/**
 * @author kangta123
 */
@Data
public class ClassInfoDTO {
    private String name;
    private List<String> classLoaders;
    private String location;
    private String source;
}
