package com.oc.hawk.message.repository;

import com.oc.hawk.ddd.web.DomainPage;
import com.oc.hawk.message.domain.model.EventMessage;
import com.oc.hawk.message.domain.model.EventMessageRepository;
import com.oc.hawk.message.port.driven.persistence.po.EventMessagePO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
interface JpaEventMessagePORepository extends JpaRepositoryImplementation<EventMessagePO, Long> {

    Page<EventMessagePO> findByProjectId(Long project, Pageable pageable);

    Page<EventMessagePO> findByInstanceId(Long instanceId, Pageable pageable);
}

@Component
@RequiredArgsConstructor
public class JpaEventMessageRepository implements EventMessageRepository {
    private final JpaEventMessagePORepository jpaEventMessagePORepository;

    @Override
    public DomainPage<EventMessage> byInstanceId(Long instanceId, int page, int size) {
        Page<EventMessagePO> pageResult = jpaEventMessagePORepository.findByInstanceId(instanceId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "time")));
        return toDomainPage(pageResult);
    }

    @Override
    public DomainPage<EventMessage> byProjectId(long projectId, int page, int size) {
        Page<EventMessagePO> pageResult = jpaEventMessagePORepository.findByProjectId(projectId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "time")));
        return toDomainPage(pageResult);
    }

    public DomainPage<EventMessage> toDomainPage(Page<EventMessagePO> pageResult) {
        if (pageResult != null) {
            List<EventMessage> content = pageResult.getContent().stream().map(EventMessagePO::createEventMessage).collect(Collectors.toList());
            return new DomainPage<>(pageResult.getNumber(), pageResult.getSize(),
                pageResult.getNumberOfElements(), content,
                pageResult.isFirst(), pageResult.isLast(),
                pageResult.getTotalPages(), pageResult.getTotalElements());
        }
        return null;
    }


    @Override
    public void save(EventMessage eventMessage) {
        EventMessagePO eventMessagePo = EventMessagePO.create(eventMessage);
        jpaEventMessagePORepository.save(eventMessagePo);
    }
}
