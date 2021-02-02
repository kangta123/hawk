package com.oc.hawk.container.traffic.domain;

import com.oc.hawk.common.utils.BeanUtils;
import com.oc.hawk.container.api.dto.InstanceConfigDTO;
import com.oc.hawk.container.api.dto.QueryServiceTrafficDTO;
import com.oc.hawk.kubernetes.api.dto.QueryTrafficDTO;

public class QueryTraffic {
    private final InstanceConfigDTO configuration;

    public QueryTraffic(InstanceConfigDTO serviceConfiguration) {
        this.configuration = serviceConfiguration;
    }

    public QueryTrafficDTO getQueryTrafficDTO(QueryServiceTrafficDTO traffic) {
        if (traffic.getEndTime() != 0 && traffic.getStartTime() == 0) {
            throw new IllegalArgumentException("指定endTime，必须同时也要指定startTime");
        }
        QueryTrafficDTO queryTrafficDTO = BeanUtils.transform(QueryTrafficDTO.class, traffic);

        handleDirection(traffic, queryTrafficDTO);

        return queryTrafficDTO;
    }


    private void handleDirection(QueryServiceTrafficDTO traffic, QueryTrafficDTO queryTrafficDTO) {
        String serviceName = configuration.getServiceName();
        if (traffic.getDirection() != null) {
            switch (traffic.getDirection()) {
                case IN:
                    queryTrafficDTO.setDestinationService(serviceName);
                    queryTrafficDTO.setDestinationNamespace(configuration.getNamespace());
                    break;
                case OUT:
                    queryTrafficDTO.setSourceService(serviceName);
                    queryTrafficDTO.setSourceNamespace(configuration.getNamespace());
                    break;
                default:
                    break;
            }
        } else {
            queryTrafficDTO.setDestinationService(serviceName);
            queryTrafficDTO.setSourceService(serviceName);
        }
    }
}
