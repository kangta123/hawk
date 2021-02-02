package com.oc.hawk.project.domain.service;

import com.google.common.collect.Maps;
import com.oc.hawk.project.domain.model.buildjob.ProjectBuildExecutionEnv;
import com.oc.hawk.project.domain.model.buildjob.ProjectBuildJob;
import com.oc.hawk.project.domain.model.codebase.CodeBase;
import com.oc.hawk.project.domain.model.codebase.git.GitCommitLog;
import com.oc.hawk.project.domain.model.gitrecord.GitCommitLogRepository;
import com.oc.hawk.project.domain.model.project.Project;
import com.oc.hawk.project.domain.model.project.ProjectAppRepository;
import com.oc.hawk.project.domain.model.project.ProjectBuild;
import com.oc.hawk.project.domain.model.projectapp.ProjectApp;
import com.oc.hawk.project.domain.model.projectapp.ProjectAppID;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class DockerProjectBuildEnvService implements ProjectBuildEnv {

    private final ProjectAppRepository projectAppRepository;
    private final GitCommitLogRepository recordRepository;

    @Override
    public ProjectBuildExecutionEnv generateEnvVariable(ProjectBuildJob projectBuildJob, Project project, CodeBase codebase) {
        return new ProjectBuildExecutionEnv(this.getDockerEnvList(projectBuildJob, project, codebase));
    }

    private Map<String, Object> getDockerEnvList(ProjectBuildJob projectBuildJob, Project project, CodeBase codebase) {
        Map<String, Object> env = Maps.newHashMap();

        GitCommitLog gitCommitLog = recordRepository.byId(projectBuildJob.getGitCommitLogId().getId());
        env.put("REMOTE_BRANCH", gitCommitLog.getBranch());

        env.put("TAG", projectBuildJob.imageTagStr());

        env.put("GROUP", project.getGroupName());
        env.put("MODE", project.getMode());
        env.put("GIT_URL", codebase.urlWithAuthentication());

        env.put("PROJECT_ID", project.getProjectId().toString());
        env.put("PROJECT_NAME", project.getName().toString());

        ProjectBuild projectBuild = project.getBuild();
        String command = project.buildCommand();
        if (StringUtils.isNotEmpty(command)) {
            env.put("BUILD_COMMAND", command);
        }
        if (StringUtils.isNotEmpty(projectBuild.getOutput())) {
            env.put("BUILD_OUT", projectBuild.getOutput());
        }
//
        String subPath = getSubPath(projectBuildJob.getSubApps());
        if (StringUtils.isNotEmpty(subPath)) {
            env.put("SUB_PATH", subPath);
        }
        return env;
    }

    private String getSubPath(List<ProjectAppID> appIds) {
        if (CollectionUtils.isEmpty(appIds)) {
            return null;
        }
        List<ProjectApp> projectApps = projectAppRepository.byIds(appIds);
        if (CollectionUtils.isEmpty(projectApps)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (ProjectApp projectApp : projectApps) {
            sb.append(projectApp.getAppPath()).append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }

}
