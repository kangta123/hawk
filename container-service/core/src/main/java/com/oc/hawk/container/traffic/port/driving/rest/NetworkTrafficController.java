package com.oc.hawk.container.traffic.port.driving.rest;

import com.oc.hawk.container.api.dto.QueryServiceTrafficDTO;
import com.oc.hawk.container.traffic.applcation.NetworkTrafficUseCase;
import com.oc.hawk.kubernetes.api.dto.MetricResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/service")
@RequiredArgsConstructor
public class NetworkTrafficController {
    private final NetworkTrafficUseCase networkTrafficUseCase;

    @PostMapping("/{id}/traffic")
    public MetricResultDTO queryTraffic(@PathVariable long id, @RequestBody QueryServiceTrafficDTO traffic) {
        return networkTrafficUseCase.queryTraffic(id, traffic);
    }
}
