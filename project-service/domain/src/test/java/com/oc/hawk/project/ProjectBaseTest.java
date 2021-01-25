package com.oc.hawk.project;

import com.oc.hawk.project.domain.model.codebase.CodeBaseRepository;
import com.oc.hawk.project.domain.model.project.ProjectRepository;
import com.oc.hawk.test.BaseTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class ProjectBaseTest extends BaseTest {
    @Mock
    protected CodeBaseRepository codeBaseRepository;
    @Mock
    protected ProjectRepository projectRepository;
}
