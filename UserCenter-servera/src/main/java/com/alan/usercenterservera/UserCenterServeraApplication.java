package com.alan.usercenterservera;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author alan
 */
@SpringBootApplication
@MapperScan("com.alan.usercenterservera.mapper")
public class UserCenterServeraApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserCenterServeraApplication.class, args);
    }

}
