package com.oc.hawk.container.runtime.application.representation;

import com.oc.hawk.container.ContainerTest;
import com.oc.hawk.container.api.command.CreateInstanceVolumeSpecCommand;
import com.oc.hawk.container.api.command.CreateRuntimeInfoSpecCommand;
import com.oc.hawk.container.domain.config.ContainerConfiguration;
import com.oc.hawk.container.domain.config.ContainerRuntimeConfig;
import com.oc.hawk.project.api.dto.ProjectBuildReadyDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author kangta123
 */
class InstanceRuntimeRepresentationTest extends ContainerTest {
    @Mock
    private ContainerRuntimeConfig containerRuntimeConfig;

    @Test
    public void testBuildRuntimeSpecCommand_volumeMountPathNotEmpty() {
        final InstanceRuntimeRepresentation instanceRuntimeRepresentation = newInstanceRuntimeRepresentation();
        final CreateRuntimeInfoSpecCommand createRuntimeInfoSpecCommand = instanceRuntimeRepresentation.buildRuntimeSpecCommand(along(), newInstance(ProjectBuildReadyDTO.class));


        for (CreateInstanceVolumeSpecCommand command : createRuntimeInfoSpecCommand.getVolume()) {
            Assertions.assertThat(command.getMountPath()).isNotEmpty();
        }
    }

    protected InstanceRuntimeRepresentation newInstanceRuntimeRepresentation() {
        when(containerRuntimeConfig.getConfig(anyString(), anyString())).thenReturn(newInstance(ContainerRuntimeConfig.ContainerConfig.class));
        return new InstanceRuntimeRepresentation(
            newInstance(ContainerConfiguration.class),
            containerRuntimeConfig,
            projectFacade
        );
    }
}
