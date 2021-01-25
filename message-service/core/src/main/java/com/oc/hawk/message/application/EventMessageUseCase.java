package com.oc.hawk.message.application;

import com.oc.hawk.api.utils.JsonUtils;
import com.oc.hawk.container.api.command.CreateRuntimeInfoSpecCommand;
import com.oc.hawk.container.api.command.StopRuntimeInfoCommand;
import com.oc.hawk.container.api.dto.InstanceConfigDTO;
import com.oc.hawk.container.api.dto.InstanceDeploymentDTO;
import com.oc.hawk.ddd.event.DomainEvent;
import com.oc.hawk.ddd.web.DomainPage;
import com.oc.hawk.message.api.dto.EventMessageDTO;
import com.oc.hawk.message.application.representation.EventMessageRepresentation;
import com.oc.hawk.message.domain.facade.ProjectBuildFacade;
import com.oc.hawk.message.domain.facade.UserInfoFacade;
import com.oc.hawk.message.domain.model.*;
import com.oc.hawk.project.api.dto.ProjectBuildJobDTO;
import com.oc.hawk.project.api.dto.ProjectDetailDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventMessageUseCase {
    private final String DEFAULT_USERNAME = "系统";
    private final EventMessageRepository repository;
    private final EventMessageRepresentation eventMessageRepresentation;
    private final UserInfoFacade userInfoFacade;
    private final ProjectBuildFacade projectBuildFacade;

    public DomainPage<EventMessageDTO> getEventMessageByType(int page, int size, Long projectId, Long instanceId) {
        DomainPage<EventMessage> resultPage = null;
        if (instanceId != null) {
            resultPage = repository.byInstanceId(instanceId, page, size);
        } else {
            resultPage = repository.byProjectId(projectId, page, size);
        }
        List<EventMessageDTO> eventMessages = eventMessageRepresentation.toEventMessageDTO(resultPage.getContent());
        return resultPage.map(eventMessages);
    }


    public void createEventMessage(EventType eventType, DomainEvent domainEvent) {
        Object data = domainEvent.getData();
        Long userId = domainEvent.getUserId();

        UserInfo userInfo = null;
        if (userId != null) {
            userInfo = userInfoFacade.getUserInfo(userId);
        }

        String title = "";
        Long projectId = null;
        Long instanceId = null;
        final String userName = getUserName(userInfo);

        switch (eventType) {
            case RUNTIME_AUTH_DEPLOYMENT_BY_PROJECT_BUILD:
                InstanceDeploymentDTO deployment = (InstanceDeploymentDTO) data;
                ProjectBuildCreator creator = projectBuildFacade.getBuildJobCreator(deployment.getProjectBuildJobId());
                userId = creator.getUserId();
                InstanceConfigDTO instance = deployment.getInstance();
                projectId = instance.getProjectId();
                title = eventType.getTitle(creator.getUserName(), instance.getName());
                instanceId = instance.getId();
                break;
            case PROJECT_BUILD_FAILED:
            case PROJECT_BUILD:
                ProjectBuildJobDTO buildJobDTO = (ProjectBuildJobDTO) data;
                projectId = buildJobDTO.getProjectId();
                title = eventType.getTitle(buildJobDTO.getCreatorName());
                break;
            case RUNTIME_START:
                CreateRuntimeInfoSpecCommand command = (CreateRuntimeInfoSpecCommand) data;
                projectId = command.getProjectId();
                if (StringUtils.isEmpty(userName)) {
                    // ignored event created by system
                    return;
                }
                title = eventType.getTitle(userName, command.getName());
                instanceId = command.getInstanceId();
                break;
            case RUNTIME_STOP:
                if (StringUtils.isEmpty(userName)) {
                    // ignored event created by system
                    return;
                }
                StopRuntimeInfoCommand stopRuntimeInfoCommand = (StopRuntimeInfoCommand) data;
                projectId = stopRuntimeInfoCommand.getProjectId();
                title = eventType.getTitle(userName, stopRuntimeInfoCommand.getName());
                instanceId = stopRuntimeInfoCommand.getId();
                break;
            case RUNTIME_ADD_CONFIG:
            case RUNTIME_UPDATE_CONFIG:
            case RUNTIME_DELETE_CONFIG:
                InstanceConfigDTO configDTO = (InstanceConfigDTO) data;
                projectId = configDTO.getProjectId();
                title = eventType.getTitle(userName, configDTO.getName());
                instanceId = configDTO.getId();
                break;
            case PROJECT_ADD:
                ProjectDetailDTO projectDetailDTO = (ProjectDetailDTO) data;
                projectId = projectDetailDTO.getId();
                title = eventType.getTitle(userName, projectDetailDTO.getName());
                break;

            default:
                break;
        }
        EventMessage eventMessage = EventMessage.builder()
            .departmentId(userInfo != null ? userInfo.getDepartmentId() : null)
            .message(JsonUtils.object2Json(data))
            .instance(instanceId)
            .title(title)
            .type(eventType)
            .project(projectId)
            .time(domainEvent.getCreatedAt())
            .userId(userId)
            .build();
        repository.save(eventMessage);
    }

    public String getUserName(UserInfo userInfo) {
        if (userInfo == null) {
            return "";
        }
        return userInfo.getName();
    }
}
