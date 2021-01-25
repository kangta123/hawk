package com.oc.hawk.project.api.dto;

import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProjectBuildJobDTO {
    private Long id;
    private Long projectId;
    private String tag;
    private String branch;
    private List<String> apps;
    private String creatorName;
    private Long creator;
    private String state;
    private LocalDateTime createdTime;
    private LocalDateTime endTime;
    private Long deployToId;
    private String deployToInstance;
    public void addApp(String i){
       if(apps == null){
           apps = Lists.newArrayList();
       }
        apps.add(i);
    }
}
