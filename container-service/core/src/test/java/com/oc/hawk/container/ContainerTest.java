package com.oc.hawk.container;

import com.oc.hawk.container.domain.facade.ProjectFacade;
import com.oc.hawk.test.BaseTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author kangta123
 */
@ExtendWith(MockitoExtension.class)
public abstract class ContainerTest extends BaseTest {
    @Mock
    protected ProjectFacade projectFacade;
}
