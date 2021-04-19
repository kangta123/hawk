package com.oc.hawk.project.domain.model.codebase.git;

/**
 * @author kangta123
 */
public class TokenCodeBaseAuthenticator implements CodeBaseAuthenticator {
    private final String token;
    private final String username;

    public TokenCodeBaseAuthenticator(String username, String token) {
        this.token = token;
        this.username = username;
    }

    @Override
    public CodeBaseIdentity encode() {
        return new CodeBaseIdentity(username, token);
    }

    @Override
    public CodeBaseIdentity decode() {
        return new CodeBaseIdentity(username, token);
    }
}
