package com.oc.hawk.project.domain.service;

import com.oc.hawk.project.domain.model.buildjob.ProjectBuildJobRepository;
import com.oc.hawk.project.domain.model.buildjob.ProjectImageTag;
import com.oc.hawk.project.domain.model.buildjob.exception.ProjectImageTagValidateException;
import com.oc.hawk.project.domain.model.project.ProjectID;
import com.oc.hawk.project.domain.service.ProjectImageTagGenerator;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class CustomTagGenerator implements ProjectImageTagGenerator {
    private static final int TAG_MAX_LENGTH = 40;
    private final ProjectBuildJobRepository projectBuildJobRepository;
    private final Pattern compile = compile("[0-9a-zA-Z\\-—.]+");

    public CustomTagGenerator(ProjectBuildJobRepository projectBuildJobRepository) {
        this.projectBuildJobRepository = projectBuildJobRepository;
    }

    @Override
    public ProjectImageTag createImageTag(ProjectID projectId, String customTag) {
        if (!compile.matcher(customTag).find()) {
            throw new ProjectImageTagValidateException("[" + customTag + "]参数不符合规则，只接受输入【英文数字-.]");
        }
        if (customTag.length() > TAG_MAX_LENGTH) {
            throw new ProjectImageTagValidateException("[" + customTag + "]超长，长度小与" + TAG_MAX_LENGTH);
        }
        if (projectBuildJobRepository.isCustomTagExisted(customTag, projectId)) {
            throw new ProjectImageTagValidateException("[" + customTag + "]已存在，请更名");
        }

        return new ProjectImageTag(customTag);
    }
}
