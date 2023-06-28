package com.gamebox.keycloakadminclient.configuration;

import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class KeycloakConfig {

    @Value("${app.keycloak.server_url}")
    private String serverUrl;
    @Value("${app.keycloak.realm}")
    private String realm;
    @Value("${app.keycloak.username}")
    private String username;
    @Value("${app.keycloak.password}")
    private String password;
    @Value("${app.keycloak.client_id}")
    private String clientId;


    @Bean
    public Keycloak getInstance() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .username(username)
                .password(password)
                .clientId(clientId)
                .resteasyClient(
                        new ResteasyClientBuilderImpl()
                                .connectionPoolSize(10).build()
                )
                .build();
    }
}
