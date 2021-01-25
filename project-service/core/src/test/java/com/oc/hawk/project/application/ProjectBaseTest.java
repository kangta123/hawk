package com.oc.hawk.project.application;

import com.oc.hawk.project.domain.facade.UserFacade;
import com.oc.hawk.project.domain.model.buildjob.ProjectBuildJobRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProjectBaseTest {
    @Mock
    protected UserFacade userFacade;
    @Mock
    protected ProjectBuildJobRepository projectBuildJobRepository;
}
