package com.oc.hawk.project.port.driven.persistence;

import com.google.common.collect.Lists;
import com.oc.hawk.api.exception.AppBusinessException;
import com.oc.hawk.project.domain.model.codebase.git.CodeBaseIdentity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

@Slf4j
public class GitRepo {
    private static final String TAG_REF = "refs/tags";
    private static final String HEAD_REF = "HEAD";
    private final String url;
    private final String username;
    private final String key;
    private final File localDir;

    public GitRepo(String url, CodeBaseIdentity identity, String localDir) {
        Assert.notNull(localDir, "dir cannot be null");

        this.url = url;
        if (identity != null) {
            this.username = identity.getUsername();
            this.key = identity.getKey();
            log.info("get gitRepo for {}, with auth {}@{}", url, identity.getUsername(), identity.getKey());
        } else {
            this.username = "";
            this.key = "";
            log.info("get gitRepo for {}, with empty auth ", url);
        }

        Path path = Paths.get(localDir);
        this.localDir = path.toFile();
        if (!this.localDir.exists()) {
            if (!this.localDir.mkdirs()) {
                throw new RuntimeException("Cannot create dir " + localDir);
            }
        }
    }

    public List<String> branch() {
        Collection<Ref> refs = null;
        try {
            refs = getGit().lsRemote()
                    .setCredentialsProvider(getUsernameCredentialsProvider())
                    .call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        List<String> branchList = Lists.newArrayList();
        if (refs != null) {
            for (Ref ref : refs) {
                String refName = ref.getName();
                String name = refName.replace("refs/heads/", "").replace("remotes/origin/", "");
                if (StringUtils.equalsIgnoreCase(HEAD_REF, name)) {
                    continue;
                }
                if (StringUtils.startsWith(name, TAG_REF)) {
                    continue;
                }
                branchList.add(name);
            }
        }
        return branchList;
    }

    public RevCommit log(String branch) {
        try {
            sync();
            Git git = getGit();
            final Repository repository = git.getRepository();
            Iterable<RevCommit> call = git.log().add(repository.resolve("remotes/origin/" + branch)).setMaxCount(1).call();

            if (call != null) {
                return call.iterator().next();
            }

        } catch (Exception e) {
            log.error("Cannot open a connection to git repo", e);
            throw new AppBusinessException("连接git仓库失败");
        }
        return null;
    }


    public Git getGit() {
        try (Git git = Git.open(localDir)) {
            return git;
        } catch (RepositoryNotFoundException e) {
            try {
                FileUtils.deleteDirectory(localDir);
            } catch (IOException ioException) {
                log.error("Delete director failed for dir {}", localDir, ioException);
            }
            cloneRepo();
            return getGit();
        } catch (IOException e) {
            log.error("Cannot open a connection to git repo", e);
            throw new AppBusinessException("连接git仓库失败");
        }
    }

    public void sync() {
        if (localDir.exists()) {
            final String[] list = localDir.list();
            if (list == null || list.length == 0) {
                cloneRepo();
            } else {
                pull();
            }
        } else {
            boolean mkdir = localDir.mkdir();
            if (!mkdir) {
                throw new AppBusinessException("无法创建目录:" + localDir);
            }

            cloneRepo();
        }
    }

    private void cloneRepo() {
        if (localDir.exists()) {
            localDir.delete();
        }
        try {
            CloneCommand cloneCommand = Git.cloneRepository()
                    .setURI(url)
                    .setCloneAllBranches(true)
                    .setCredentialsProvider(getUsernameCredentialsProvider())
                    .setDirectory(localDir)
                    .setProgressMonitor(new GitProgressMonitor());

            cloneCommand.call();
        } catch (Exception e) {
            log.error("Clone git repo error", e);
            throw new AppBusinessException(e.getMessage());
        }
    }

    private UsernamePasswordCredentialsProvider getUsernameCredentialsProvider() {
        if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(key)) {
            return new UsernamePasswordCredentialsProvider(username, key);
        }
        return null;
    }

    private void pull() {
        try {
            Git git = getGit();
            git.pull().setCredentialsProvider(getUsernameCredentialsProvider()).call();
        } catch (Exception e) {
            log.error("git repo pull error", e);
            throw new AppBusinessException("Git仓库更新失败");
        }

    }
}
