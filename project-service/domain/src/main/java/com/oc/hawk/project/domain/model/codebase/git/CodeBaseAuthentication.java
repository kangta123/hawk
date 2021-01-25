package com.oc.hawk.project.domain.model.codebase.git;


public interface CodeBaseAuthentication {
    PasswordAuthentication encode();

    PasswordAuthentication decode();
}
