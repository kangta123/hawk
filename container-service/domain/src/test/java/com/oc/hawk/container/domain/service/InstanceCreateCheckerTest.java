package com.oc.hawk.container.domain.service;

import com.oc.hawk.container.domain.ContainerBaseTest;
import com.oc.hawk.container.domain.model.runtime.config.InstanceId;
import com.oc.hawk.container.domain.model.runtime.config.InstanceName;
import com.oc.hawk.test.TestHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class InstanceCreateCheckerTest extends ContainerBaseTest {

    private InstanceName getName() {
        String name = TestHelper.anyString();
        InstanceName instanceName = new InstanceName(name);
        return instanceName;
    }

    @Test
    void isDuplicate_createNewInstanceConfigAlreadyExisted() {

        InstanceName instanceName = getName();

        InstanceId id = new InstanceId(TestHelper.anyLong());

        when(instanceConfigRepository.existed(any(), any())).thenReturn(id);

        boolean duplicate = new InstanceCreateChecker(instanceConfigRepository).isDuplicate("default", instanceName, null);
        Assertions.assertThat(duplicate).isEqualTo(true);
    }

    @Test
    void isDuplicate_updateInstanceConfigNameAlreadyExist() {

        InstanceName instanceName = getName();
        InstanceId id = new InstanceId(TestHelper.anyLong());

        when(instanceConfigRepository.existed(any(), any())).thenReturn(id);

        boolean duplicate = new InstanceCreateChecker(instanceConfigRepository).isDuplicate("default", instanceName, id);
        Assertions.assertThat(duplicate).isEqualTo(false);
    }

    @Test
    void isDuplicate_createNewInstanceConfigNotExisted() {

        InstanceName instanceName = getName();

        when(instanceConfigRepository.existed(any(), any())).thenReturn(null);

        boolean duplicate = new InstanceCreateChecker(instanceConfigRepository).isDuplicate("default", instanceName, null);
        Assertions.assertThat(duplicate).isEqualTo(false);
    }


}
