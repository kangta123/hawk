package com.oc.hawk.project.domain.model.codebase.git.service;

import com.oc.hawk.project.domain.model.codebase.git.CodeBaseIdentity;

public interface GitPasswordEncryptStrategy {
    CodeBaseIdentity getPlainText(String username, String password);

    CodeBaseIdentity getCipherText(String username, String password);
}
