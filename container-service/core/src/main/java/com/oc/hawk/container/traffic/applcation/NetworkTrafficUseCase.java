package com.oc.hawk.container.traffic.applcation;

import com.oc.hawk.container.api.dto.InstanceConfigDTO;
import com.oc.hawk.container.api.dto.QueryServiceTrafficDTO;
import com.oc.hawk.container.domain.facade.InfrastructureLifeCycleFacade;
import com.oc.hawk.container.runtime.application.instance.InstanceConfigUseCase;
import com.oc.hawk.container.traffic.domain.QueryTraffic;
import com.oc.hawk.kubernetes.api.dto.MetricResultDTO;
import com.oc.hawk.kubernetes.api.dto.QueryTrafficDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class NetworkTrafficUseCase {
    final private InfrastructureLifeCycleFacade infrastructureLifeCycleFacade;
    final private InstanceConfigUseCase instanceConfigUseCase;

    public MetricResultDTO queryTraffic(long id, QueryServiceTrafficDTO traffic) {
        InstanceConfigDTO configuration = instanceConfigUseCase.getConfiguration(id);

        QueryTrafficDTO queryTrafficDTO = new QueryTraffic(configuration).getQueryTrafficDTO(traffic);

//        return infrastructureLifeCycleFacade.queryServiceTraffic(queryTrafficDTO);
        return null;
    }
}
