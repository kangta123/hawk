package com.oc.hawk.base;

import com.oc.hawk.common.spring.config.BaseConfiguration;
import com.oc.hawk.common.spring.config.WebConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
@Import(value = {WebConfiguration.class, BaseConfiguration.class})
public class
BaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(BaseApplication.class, args);
    }

    /**
     * bcrypt算法是基于Blowfish加密算法改进得到，只用于密码存储，
     * 其好处通过设定一个参数work factor调整计算强度，同时work factor是包括在输出的摘要中的。
     * 随着攻击者计算能力的提高，使用者可以逐步增大work factor，而且不会影响已有用户的登陆，
     * 一般认为它比PBKDF2更能承受随着计算能力加强而带来的风险。
     * bcrypt也有广泛的函数库支持，因此我们建议使用这种方式存储密码。
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

