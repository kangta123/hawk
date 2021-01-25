package com.oc.hawk.container.runtime.port.driving.rest;

import com.oc.hawk.common.spring.mvc.BooleanWrapper;
import com.oc.hawk.container.api.dto.AddServiceAppDTO;
import com.oc.hawk.container.api.dto.ServiceAppDTO;
import com.oc.hawk.container.api.dto.ServiceAppVersionDTO;
import com.oc.hawk.container.runtime.application.app.service.ServiceAppExecutor;
import com.oc.hawk.container.runtime.application.app.service.ServiceAppManager;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ServiceAppController {
    private final ServiceAppManager serviceAppManager;
    private final ServiceAppExecutor serviceAppExecutor;

    @GetMapping("/apps/{id}")
    public ServiceAppDTO getServiceApp(@PathVariable("id") Long id) {
        return serviceAppManager.getServiceApp(id);
    }

    @PostMapping("/project/apps")
    public BooleanWrapper createProjectApps(@RequestBody AddServiceAppDTO projectApp) {
        Long id = serviceAppManager.addAppVersion(projectApp);
        return new BooleanWrapper(true, id);
    }

    @PutMapping("/project/apps/versions/{version}/start")
    public BooleanWrapper startApp(@PathVariable("version") long versionId) {
        serviceAppExecutor.startAppVersion(versionId);
        return BooleanWrapper.TRUE;
    }

    @PutMapping("/project/apps/versions/{version}/stop")
    public BooleanWrapper stopApp(@PathVariable("version") long versionId) {
        serviceAppExecutor.stopAppVersion(versionId);
        return BooleanWrapper.TRUE;
    }

    @PutMapping("/project/apps/versions/{version}/scale")
    public ServiceAppVersionDTO scaleApp(@PathVariable("version") long versionId, @RequestParam("value") int value) {
        return serviceAppExecutor.scaleApp(versionId, value);
    }

    @DeleteMapping("/project/apps/versions/{version}")
    public BooleanWrapper deleteApp(@PathVariable("version") long versionId) {
        serviceAppExecutor.deleteApp(versionId);
        return BooleanWrapper.TRUE;
    }
}
