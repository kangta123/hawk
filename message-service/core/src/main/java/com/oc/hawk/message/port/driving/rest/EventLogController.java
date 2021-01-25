package com.oc.hawk.message.port.driving.rest;

import com.oc.hawk.ddd.web.DomainPage;
import com.oc.hawk.message.api.dto.EventMessageDTO;
import com.oc.hawk.message.application.EventMessageUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventLogController {
    private final EventMessageUseCase eventMessageUseCase;

    @GetMapping
    public DomainPage<EventMessageDTO> queryEventMessage(@RequestParam long projectId,
                                                         @RequestParam(required = false) Long instanceId,
                                                         @RequestParam int page,
                                                         @RequestParam int size) {
        return eventMessageUseCase.getEventMessageByType(page, size, projectId, instanceId);
    }
}
