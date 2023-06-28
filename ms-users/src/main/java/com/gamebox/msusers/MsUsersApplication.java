package com.gamebox.msusers;

import jakarta.annotation.PreDestroy;
import org.keycloak.admin.client.Keycloak;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class MsUsersApplication {

    final Keycloak keycloak;

    public MsUsersApplication(Keycloak keycloak) {
        this.keycloak = keycloak;
    }


    public static void main(String[] args) {
        SpringApplication.run(MsUsersApplication.class, args);
    }

}
