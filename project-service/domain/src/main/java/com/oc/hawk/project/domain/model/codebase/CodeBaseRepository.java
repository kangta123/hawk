package com.oc.hawk.project.domain.model.codebase;


import com.oc.hawk.project.domain.model.project.ProjectID;

import java.util.List;

public interface CodeBaseRepository {
    CodeBaseID save(CodeBase codeBase);

    CodeBase byProjectId(ProjectID projectId);

    CodeBase byId(CodeBaseID codeBaseId);

    List<CodeBaseID> getCodeBaseIdsByUrl(String key);

    void deleteByProjectId(long id);
}
