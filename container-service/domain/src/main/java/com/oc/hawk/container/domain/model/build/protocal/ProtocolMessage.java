package com.oc.hawk.container.domain.model.build.protocal;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Data
@Slf4j
public class ProtocolMessage {
    public final static String SUB_APP_SEPARATOR = "@";
    public static final int MIN_TAG_LENGTH = 2;

    private List<ProtocolTag> tags;
    private String text;
    private List<ProtocolSubApp> subApps;

    public ProtocolMessage() {
        tags = Lists.newArrayList();
        text = "";
        subApps = Lists.newArrayList();
    }

    public void setText(String s) {
        text = s;
    }

    public void addTag(String msg) {
        if (StringUtils.isNotEmpty(msg)) {
            ProtocolTag tag = new ProtocolTag();
            String[] splitData = msg.split(" ");
            if (splitData.length >= MIN_TAG_LENGTH) {
                try {
                    String title = splitData[0];
                    String status = splitData[1].trim();
                    if (splitData.length == 3) {
                        tag.setData(splitData[2].trim());
                    }
                    tag.setTag(title);
                    boolean success = Integer.parseInt(status) == 0;
                    tag.setSuccess(success);
                    this.getTags().add(tag);
                } catch (Exception e) {
                    log.error("Invalidate tag message [{}]", msg, e);
                }
            } else {
                log.error("Invalidate tag message [{}]", msg);
            }
        }
    }

    public void addProjects(String s) {
        if (StringUtils.isNotEmpty(s)) {
            ProtocolSubApp protocolSubApp = new ProtocolSubApp();
            s = s.trim();
            String[] splits = s.split(SUB_APP_SEPARATOR);
            Long projectId = Long.valueOf(splits[0]);
            String branch = splits[1];
            String appName = splits[2];
            String appPath = splits[3];
            protocolSubApp.setProjectId(projectId);
            protocolSubApp.setBranch(branch);
            protocolSubApp.setAppName(appName);
            protocolSubApp.setAppPath(appPath);
            this.subApps.add(protocolSubApp);
        }
    }


}
