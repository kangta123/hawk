package com.oc.hawk.project.port.driven.persistence;

import com.google.common.collect.Lists;
import com.oc.hawk.common.utils.DomainPageUtils;
import com.oc.hawk.ddd.web.DomainPage;
import com.oc.hawk.project.domain.model.codebase.CodeBaseID;
import com.oc.hawk.project.domain.model.codebase.CodeBaseRepository;
import com.oc.hawk.project.domain.model.user.UserDepartment;
import com.oc.hawk.project.domain.model.projectApp.ProjectApp;
import com.oc.hawk.project.domain.model.project.Project;
import com.oc.hawk.project.domain.model.project.ProjectID;
import com.oc.hawk.project.domain.model.project.ProjectName;
import com.oc.hawk.project.domain.model.project.ProjectRepository;
import com.oc.hawk.project.domain.model.project.exception.ProjectNotFoundException;
import com.oc.hawk.project.port.driven.persistence.po.ProjectAppPO;
import com.oc.hawk.project.port.driven.persistence.po.ProjectPO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
interface ProjectPoRepository extends JpaRepositoryImplementation<ProjectPO, Long> {
    boolean existsByName(String name);

    @Query("Select id from ProjectPO")
    List<Long> queryAllIds();

    @Query("Select id from ProjectPO where departmentId=:deptId")
    List<Long> queryByDepartmentId(@Param("deptId") Long deptId);
}

@Component
@RequiredArgsConstructor
public class JpaProjectRepository implements ProjectRepository {
    private final ProjectPoRepository projectPORepository;
    private final ProjectAppPORepository projectAppPORepository;
    private final CodeBaseRepository codeBaseRepository;

    @Override
    public boolean existsProjectName(ProjectName name) {
        return projectPORepository.existsByName(name.getName());
    }

    @Override
    public ProjectID save(Project project) {
        ProjectPO projectPo = ProjectPO.createBy(project);

        projectPORepository.save(projectPo);

        if (project.getApps() != null) {
            List<ProjectAppPO> apps = project.getApps().stream().map(p -> {
                ProjectAppPO projectAppPo = ProjectAppPO.create(p);
                projectAppPo.setProjectId(project.getProjectId().getId());
                return projectAppPo;
            }).collect(Collectors.toList());
            projectAppPORepository.saveAll(apps);
        }

        return new ProjectID(projectPo.getId());
    }

    @Override
    public Project byId(ProjectID id) {
        List<ProjectAppPO> appPos = projectAppPORepository.findByProjectId(id.getId());
        Set<ProjectApp> apps = appPos.stream().map(ProjectAppPO::toProjectApp).collect(Collectors.toSet());

        return projectPORepository.findById(id.getId()).orElseThrow(ProjectNotFoundException::new).toProject(apps);
    }

    @Override
    public DomainPage<Project> projectPage(int page, int size, String key, Long departmentId) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.desc("id")));
        final List<CodeBaseID> codebaseIdList = codeBaseRepository.getCodeBaseIdsByUrl(key);

        Page<Project> result = projectPORepository.findAll((root, query, criteriaBuilder) -> {

            List<Predicate> predicates = Lists.newArrayList();
            if (departmentId != null) {
                predicates.add(criteriaBuilder.equal(root.get("departmentId"), departmentId));
            }

            if (StringUtils.isNotEmpty(key)) {
                List<Predicate> orPredicates =
                    Lists.newArrayList(criteriaBuilder.like(root.get("name"), '%' + key + '%'), criteriaBuilder.like(root.get("descn"), '%' + key + '%'));
                if (!CollectionUtils.isEmpty(codebaseIdList)) {
                    List<Long> ids = codebaseIdList.stream().map(CodeBaseID::getId).collect(Collectors.toList());
                    orPredicates.add(criteriaBuilder.in(root.get("id")).value(ids));
                }

                predicates.add(criteriaBuilder.or(orPredicates.toArray(new Predicate[0])));
            }


            if (predicates.isEmpty()) {
                return null;
            } else {
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        }, pageRequest).map(ProjectPO::toProject);
        return DomainPageUtils.create(result);
    }

    @Override
    public List<Project> byIds(List<Long> projectIds) {
        return projectPORepository.findAllById(projectIds).stream().map(ProjectPO::toProject).collect(Collectors.toList());
    }

    @Override
    public Map<ProjectID, ProjectName> getProjectNames(UserDepartment userDepartment, List<Long> ids) {
        List<ProjectPO> project = projectPORepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = Lists.newArrayList();
            if (userDepartment != null) {
                if (!userDepartment.isMaster()) {
                    predicates.add(criteriaBuilder.equal(root.get("department"), userDepartment.getId()));
                }
            }
            if (!CollectionUtils.isEmpty(ids)) {
                predicates.add(criteriaBuilder.in(root.get("id")).value(ids));
            }
            return query.where(predicates.toArray(new Predicate[0])).getRestriction();
        });
        return project.stream().collect(Collectors.toMap(p -> new ProjectID(p.getId()), p -> new ProjectName(p.getName())));

    }

    @Override
    public List<ProjectID> getProjectIds(Long deptId) {
        List<Long> ids;
        if (deptId == null) {
            ids = projectPORepository.queryAllIds();
        } else {
            ids = projectPORepository.queryByDepartmentId(deptId);
        }
        return ids.stream().map(ProjectID::new).collect(Collectors.toList());
    }

    @Override
    public void deleteProject(long id) {
        projectPORepository.deleteById(id);
        codeBaseRepository.deleteByProjectId(id);
    }

    @Override
    public int getProjectTotalCount() {
        return Math.toIntExact(projectPORepository.count());
    }

    public List<ProjectPO> getAllProjects() {
        return projectPORepository.findAll();
    }


}
