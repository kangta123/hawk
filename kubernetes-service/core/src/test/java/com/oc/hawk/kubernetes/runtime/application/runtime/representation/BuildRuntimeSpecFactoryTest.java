package com.oc.hawk.kubernetes.runtime.application.runtime.representation;

import com.oc.hawk.container.api.command.CreateRuntimeInfoSpecCommand;
import com.oc.hawk.container.domain.model.runtime.info.PerformanceLevel;
import com.oc.hawk.kubernetes.KubernetesBaseTest;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.BuildRuntimeSpecFactory;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.RuntimeConfigSpec;
import com.oc.hawk.test.TestHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class BuildRuntimeSpecFactoryTest extends KubernetesBaseTest {

    @Test
    void testToRuntimeConfiguration_UseDefaultWhenPerformanceLevelIsNull() {
        BuildRuntimeSpecFactory buildRuntimeSpecFactory = new BuildRuntimeSpecFactory();
        CreateRuntimeInfoSpecCommand spec = TestHelper.newInstance(CreateRuntimeInfoSpecCommand.class);
        spec.setPerformance(null);
        spec.setRuntimeCatalog("build");
        RuntimeConfigSpec configuration = buildRuntimeSpecFactory.toRuntimeConfiguration(spec);

        Assertions.assertThat(configuration.getPerformanceLevel()).isEqualTo(PerformanceLevel.NORMAL);
    }
}
