package com.gamebox.msusers.configuration;

import org.keycloak.OAuth2Constants;
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
    @Value("${app.keycloak.client_id}")
    private String clientId;
    @Value("${app.keycloak.client_secret}")
    private String clientSecret;


    @Bean
    public Keycloak getInstance() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .build();
    }
}
