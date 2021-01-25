package com.oc.hawk.monitor.port.driving.rest;


import com.oc.hawk.monitor.application.MeasurementUseCase;
import com.oc.hawk.monitor.dto.MeasurementGroupDTO;
import com.oc.hawk.monitor.dto.QueryMeasurementDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/measurement")
@Slf4j
@RequiredArgsConstructor
public class MeasurementGroupController {
    private final MeasurementUseCase measurementUseCase;

    @PostMapping("/group")
    public MeasurementGroupDTO queryMeasurement(@RequestBody QueryMeasurementDTO queryMeasurementDTO) {
        return measurementUseCase.measure(queryMeasurementDTO);
    }
}
