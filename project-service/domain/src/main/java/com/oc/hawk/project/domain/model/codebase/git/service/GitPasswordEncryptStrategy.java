package com.oc.hawk.project.domain.model.codebase.git.service;

import com.oc.hawk.project.domain.model.codebase.git.PasswordAuthentication;

public interface GitPasswordEncryptStrategy {
    PasswordAuthentication getPlainText(String username, String password);
    PasswordAuthentication getCipherText(String username, String password);
}
