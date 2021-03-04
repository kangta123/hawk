package com.oc.hawk.project.domain.model.project;

import com.oc.hawk.project.ProjectBaseTest;
import com.oc.hawk.project.domain.model.codebase.git.CodeBaseIdentity;
import com.oc.hawk.project.domain.model.codebase.git.service.AesEncryptStrategy;
import com.oc.hawk.test.TestHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class AesEncryptStrategyTest extends ProjectBaseTest {
    static String username;
    static String password;

    @BeforeAll
    public static void startup() {
        username = TestHelper.anyString();
        password = TestHelper.anyString();
    }


    @Test
    void testAesEncryptStrategy() {
        AesEncryptStrategy aesEncryptStrategy = new AesEncryptStrategy();
        CodeBaseIdentity cipherText = aesEncryptStrategy.getCipherText(username, password);
        Assertions.assertThat(cipherText.getKey()).isNotEmpty();
        Assertions.assertThat(cipherText.getUsername()).isEqualTo(username);


        aesEncryptStrategy = new AesEncryptStrategy();
        CodeBaseIdentity plain = aesEncryptStrategy.getPlainText(cipherText.getUsername(), cipherText.getKey());
        Assertions.assertThat(plain.getKey()).isEqualTo(password);
        Assertions.assertThat(plain.getUsername()).isEqualTo(username);
    }
}
