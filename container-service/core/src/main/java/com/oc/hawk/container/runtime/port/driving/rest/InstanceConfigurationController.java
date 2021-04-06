package com.oc.hawk.container.runtime.port.driving.rest;

import com.oc.hawk.common.spring.mvc.BooleanWrapper;
import com.oc.hawk.container.api.command.ChangeInstanceConfigCommand;
import com.oc.hawk.container.api.command.CreateInstanceConfigCommand;
import com.oc.hawk.container.api.dto.InstanceConfigDTO;
import com.oc.hawk.container.api.dto.InstanceProjectDTO;
import com.oc.hawk.container.domain.model.runtime.config.InstanceId;
import com.oc.hawk.container.runtime.application.instance.InstanceConfigUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/instance")
@RequiredArgsConstructor
@Slf4j
public class InstanceConfigurationController {

    private final InstanceConfigUseCase instanceConfigUseCase;

    @PutMapping("/{id}")
    public InstanceConfigDTO updateInstanceConfig(@PathVariable long id, @RequestBody ChangeInstanceConfigCommand command) {
        return instanceConfigUseCase.updateInstanceConfig(id, command);
    }

    @PostMapping
    public InstanceConfigDTO addInstanceConfig(@RequestBody CreateInstanceConfigCommand createInstanceConfigCommand) {
        return instanceConfigUseCase.createInstanceConfig(createInstanceConfigCommand);
    }

    @GetMapping
    public List<InstanceConfigDTO> listInstanceConfig(@RequestParam Long projectId) {
        return instanceConfigUseCase.queryDefaultNsInstanceConfig(projectId);
    }

    @PutMapping("/{id}/version")
    public BooleanWrapper updateVersion(@PathVariable("id") long id, @RequestParam long buildJobId) {
        instanceConfigUseCase.updateInstanceVersionAndRestart(new InstanceId(id), buildJobId);
        return BooleanWrapper.TRUE;
    }
//    @GetMapping("/query")
//    public List<InstanceConfigDTO> query(@RequestParam(value = "serviceName", required = false, defaultValue = "") String serviceName,
//                                         @RequestParam(value = "publicIp", required = false, defaultValue = "") String publicIp) {
//        return configurationManager.query(serviceName, publicIp);
//    }


    @GetMapping("/{id}")
    public InstanceConfigDTO getConfig(@PathVariable long id) {
        return instanceConfigUseCase.getConfiguration(id);
    }

    @DeleteMapping("/{id}")
    public BooleanWrapper deleteConfig(@PathVariable long id) {
        instanceConfigUseCase.deleteConfiguration(id);
        return BooleanWrapper.TRUE;
    }

    @GetMapping("/names")
    public List<InstanceProjectDTO> getProjectInstances(@RequestParam List<Long> projectIds) {
        return instanceConfigUseCase.listProjectInstances(projectIds);
    }

}
