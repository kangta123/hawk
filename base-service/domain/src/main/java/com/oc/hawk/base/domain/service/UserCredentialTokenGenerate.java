package com.oc.hawk.base.domain.service;

import com.google.common.collect.ImmutableMap;
import com.oc.hawk.api.constant.AuthKey;
import com.oc.hawk.base.domain.model.user.User;
import com.oc.hawk.base.domain.model.user.UserId;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Map;

public class UserCredentialTokenGenerate {
    private final SecretKey secretKey = Keys.hmacShaKeyFor(AuthKey.KEY.getBytes());

    public String generate(User user) {
        return generateToken(user.getId(), ImmutableMap.of(
            "name", user.getName(),
            "deptId", user.getDepartment().getId(),
            "email", user.getEmail(),
            "time", LocalDateTime.now().toString(),
            "id", user.getId()
        ));
    }

    private String generateToken(UserId id, Map<String, Object> claims) {
        return Jwts.builder().addClaims(claims)
            .setSubject(String.valueOf(id))
            .signWith(secretKey).compact();
    }
}
