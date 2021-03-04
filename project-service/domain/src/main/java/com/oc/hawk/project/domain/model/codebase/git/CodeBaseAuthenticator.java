package com.oc.hawk.project.domain.model.codebase.git;


public interface CodeBaseAuthenticator {
    CodeBaseIdentity encode();

    CodeBaseIdentity decode();
}
