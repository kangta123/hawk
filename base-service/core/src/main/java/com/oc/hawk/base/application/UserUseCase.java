package com.oc.hawk.base.application;

import com.oc.hawk.base.api.dto.AddUserDTO;
import com.oc.hawk.base.api.dto.QueryUserDTO;
import com.oc.hawk.base.api.dto.UserDTO;
import com.oc.hawk.base.api.event.UserDomainEventType;
import com.oc.hawk.base.application.representation.UserRepresentation;
import com.oc.hawk.base.domain.model.user.User;
import com.oc.hawk.base.domain.model.user.UserId;
import com.oc.hawk.base.port.driven.persistence.JpaUserRepository;
import com.oc.hawk.ddd.event.DomainEvent;
import com.oc.hawk.ddd.event.EventPublisher;
import com.oc.hawk.ddd.web.DomainPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor

public class UserUseCase {

    private final JpaUserRepository userRepository;
    private final UserFactory userFactory;
    private final UserRepresentation userRepresentation;
    private final EventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public List<UserDTO> queryUsers(QueryUserDTO queryUserDTO) {
        List<User> users = userRepository.queryUser(queryUserDTO.getKey(), queryUserDTO.getIds(), queryUserDTO.getDepartmentIgnore());
        return userRepresentation.toUserDTO(users);

    }

    @Transactional(readOnly = true)
    public DomainPage<UserDTO> queryUserPage(int page, int size, String key) {
        return userRepository.queryUserPage(page, size, key).map(userRepresentation::toUserDTO);
    }

    @Transactional(readOnly = true)
    public UserDTO getUser(long userId) {
        final User user = userRepository.getUser(new UserId(userId));
        return userRepresentation.toUserDTO(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(long id) {
        log.info("delete user with id {}", id);
        try {
            final User user = userRepository.getUser(new UserId(id));
            userRepository.deleteUser(user);

            final UserDTO userDTO = userRepresentation.toUserDTO(user);
            eventPublisher.publishDomainEvent(DomainEvent.byData(id, UserDomainEventType.USER_DELETED, userDTO));
        } catch (Exception e) {
            log.warn("delete user failed {}, {}", id, e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void registerUser(AddUserDTO addUserDTO) {
        log.info("register user with email {}", addUserDTO.getEmail());
        User user = userFactory.createNewUser(addUserDTO);

        user = userRepository.save(user);

        final UserDTO userDTO = userRepresentation.toUserDTO(user);
        eventPublisher.publishDomainEvent(DomainEvent.byData(userDTO.getId(), UserDomainEventType.USER_REGISTERED, userDTO));
    }
}
