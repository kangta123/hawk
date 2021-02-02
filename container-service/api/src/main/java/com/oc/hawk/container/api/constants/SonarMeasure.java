package com.oc.hawk.container.api.constants;

import lombok.Getter;

@Getter
public enum SonarMeasure {
    COVERAGE("coverage", "测试覆盖率"), CODESMELLS("code_smells", "坏味道"),
    DUPLICATEDLINESDENSITY("duplicated_lines_density", "代码重复率"), BUGS("bugs", "bugs");

    private final String key;

    private final String title;

    SonarMeasure(String key, String title) {
        this.key = key;
        this.title = title;
    }

    public static String[] listKeys() {
        String[] keys = new String[SonarMeasure.values().length];
        for (SonarMeasure meaure : SonarMeasure.values()) {
        }
        return keys;
    }
}
