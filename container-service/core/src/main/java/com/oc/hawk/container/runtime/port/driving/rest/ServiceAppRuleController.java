package com.oc.hawk.container.runtime.port.driving.rest;

import com.oc.hawk.common.spring.mvc.BooleanWrapper;
import com.oc.hawk.container.api.dto.AddAppRuleDTO;
import com.oc.hawk.container.api.dto.ServiceAppRuleDTO;
import com.oc.hawk.container.runtime.application.app.service.ServiceAppRuleManager;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ServiceAppRuleController {
    private final ServiceAppRuleManager serviceAppRuleManager;

    @PostMapping("/apps/{id}/rule")
    public BooleanWrapper addAppRule(
        @PathVariable long id,
        @RequestBody AddAppRuleDTO addRule) {
        serviceAppRuleManager.addAppRule(id, addRule);
        return BooleanWrapper.TRUE;
    }

    @PutMapping("/apps/rule/{id}/status")
    public BooleanWrapper updateRuleStatus(@PathVariable("id") long id, @RequestParam Boolean enable) {
        serviceAppRuleManager.updateAppRuleStatus(id, enable);
        return BooleanWrapper.TRUE;
    }


    @PutMapping("/apps/{id}/rule//apply")
    public BooleanWrapper applyAppRule(@PathVariable("id") long id) {
        serviceAppRuleManager.applyAppRule(id);
        return BooleanWrapper.TRUE;
    }

    @PostMapping("/apps/rule/{id}/exchange")
    public BooleanWrapper exchangeAppRulePosition(@PathVariable("id") long id, @RequestParam Long target) {
        serviceAppRuleManager.exchangeAppRuleOrder(id, target);
        return BooleanWrapper.TRUE;
    }

    @DeleteMapping("/apps/{app}/rule/{rule}")
    public BooleanWrapper deleteAppRule(@PathVariable("app") long app,
                                        @PathVariable("rule") long rule) {
        serviceAppRuleManager.deleteAppRule(app, rule);
        return BooleanWrapper.TRUE;
    }

    @GetMapping("/apps/{id}/rule")
    public List<ServiceAppRuleDTO> getAppRule(@PathVariable long id) {
        return serviceAppRuleManager.getAppRule(id);
    }
}
