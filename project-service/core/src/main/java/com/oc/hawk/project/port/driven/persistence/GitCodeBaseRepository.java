package com.oc.hawk.project.port.driven.persistence;

import com.oc.hawk.api.exception.DomainNotFoundException;
import com.oc.hawk.project.domain.model.codebase.CodeBase;
import com.oc.hawk.project.domain.model.codebase.CodeBaseID;
import com.oc.hawk.project.domain.model.codebase.CodeBaseRepository;
import com.oc.hawk.project.domain.model.codebase.git.GitCodeBase;
import com.oc.hawk.project.domain.model.project.ProjectID;
import com.oc.hawk.project.port.driven.persistence.po.GitCodeBasePO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
interface JpaCodeBasePoRepository extends JpaRepositoryImplementation<GitCodeBasePO, Long> {

    @Query("SELECT codebase FROM GitCodeBasePO codebase  JOIN ProjectPO p ON p.codeBaseId=codebase.id WHERE p.id=?1")
    GitCodeBasePO findByProjectId(Long projectId);

    @Query("SELECT id FROM GitCodeBasePO where url like ?1")
    List<Long> queryByUrl(String key);
}

@Component
@RequiredArgsConstructor
public class GitCodeBaseRepository implements CodeBaseRepository {
    private final JpaCodeBasePoRepository jpaCodeBasePORepository;

    @Override
    public CodeBaseID save(CodeBase codeBase) {
        if (codeBase instanceof GitCodeBase) {
            GitCodeBasePO gitCodeBasePo = GitCodeBasePO.createdBy((GitCodeBase) codeBase);

            jpaCodeBasePORepository.save(gitCodeBasePo);
            return new CodeBaseID(gitCodeBasePo.getId());
        }
        return null;
    }

    @Override
    public CodeBase byProjectId(ProjectID projectId) {
        return jpaCodeBasePORepository.findByProjectId(projectId.getId()).toGitCodeBase();
    }

    @Override
    public CodeBase byId(CodeBaseID codeBaseId) {
        return jpaCodeBasePORepository.findById(codeBaseId.getId()).orElseThrow(() -> new DomainNotFoundException(codeBaseId.getId())).toGitCodeBase();
    }

    @Override
    public void deleteByProjectId(long projectId) {
        // codebase id equivalent to project id
        try {
            jpaCodeBasePORepository.deleteById(projectId);
        } catch (EmptyResultDataAccessException e) {
            // codebase not exist
        }
    }

    @Override
    public List<CodeBaseID> getCodeBaseIdsByUrl(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        List<Long> ids = jpaCodeBasePORepository.queryByUrl("%" + key + "%");
        if (ids != null) {
            return ids.stream().map(CodeBaseID::new).collect(Collectors.toList());
        }
        return null;
    }
}
