package com.oc.hawk.project.port.driven.persistence;

import com.google.common.collect.Lists;
import com.oc.hawk.api.constant.AccountHolder;
import com.oc.hawk.common.utils.AccountHolderUtils;
import com.oc.hawk.project.domain.model.project.Project;
import com.oc.hawk.project.domain.model.project.ProjectID;
import com.oc.hawk.project.domain.model.project.ProjectRepository;
import com.oc.hawk.project.domain.model.shared.ProjectSharedRepository;
import com.oc.hawk.project.port.driven.persistence.po.ProjectSharedPO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
interface ProjectSharedPoRepository extends CrudRepository<ProjectSharedPO, Long> {

    List<ProjectSharedPO> findByUser(Long userId);

    List<ProjectSharedPO> findByProject(long id);

    void deleteByProject(long id);
}

@Component
@RequiredArgsConstructor
public class JpaProjectSharedRepository implements ProjectSharedRepository {
    private final ProjectSharedPoRepository projectSharedPORepository;
    private final ProjectRepository projectRepository;

    @Override
    public List<Project> getProjectSharedToMe() {
        AccountHolder accountHolder = AccountHolderUtils.getAccountHolder();
        if (accountHolder == null || accountHolder.getId() == null) {
            return Lists.newArrayList();
        }

        Long userId = accountHolder.getId();
        List<ProjectSharedPO> list = projectSharedPORepository.findByUser(userId);
        List<Long> projectIds = list.stream().map(ProjectSharedPO::getProject).collect(Collectors.toList());

        return projectRepository.byIds(projectIds);
    }

    @Override
    public List<Long> projectSharedUsers(ProjectID projectId) {
        final List<ProjectSharedPO> poList = projectSharedPORepository.findByProject(projectId.getId());
        if (poList != null) {
            return poList.stream().map(ProjectSharedPO::getUser).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public void sharedToUser(ProjectID projectId, List<Long> users) {
        long id = projectId.getId();
        projectSharedPORepository.deleteByProject(id);
        LocalDateTime now = LocalDateTime.now();
        for (Long user : users) {
            ProjectSharedPO projectSharedPo = new ProjectSharedPO();
            projectSharedPo.setProject(id);
            projectSharedPo.setUser(user);
            projectSharedPo.setTime(now);
            projectSharedPORepository.save(projectSharedPo);
        }
    }
}
