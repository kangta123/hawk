package com.oc.hawk.message.application.representation;

import com.oc.hawk.message.api.dto.EventMessageDTO;
import com.oc.hawk.message.domain.facade.UserInfoFacade;
import com.oc.hawk.message.domain.model.EventMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventMessageRepresentation {
    private final UserInfoFacade userInfoFacade;

    public List<EventMessageDTO> toEventMessageDTO(List<EventMessage> eventMessageList) {
        List<Long> userIds = eventMessageList.stream().map(EventMessage::getUserId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<Long, String> userName = userInfoFacade.getUserName(userIds);

        return eventMessageList.stream().map(e -> {
            EventMessageDTO eventMessageDTO = new EventMessageDTO();
            eventMessageDTO.setTime(e.getTime());
            eventMessageDTO.setUserName(userName.getOrDefault(e.getUserId(), ""));
            eventMessageDTO.setTitle(e.getTitle());
            eventMessageDTO.setTypeDesc(e.getType().getType());
            eventMessageDTO.setType(e.getType().name());
            return eventMessageDTO;
        }).collect(Collectors.toList());
    }
}
