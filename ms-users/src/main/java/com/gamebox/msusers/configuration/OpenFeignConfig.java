package com.gamebox.msusers.configuration;

import com.gamebox.msusers.security.OAuthClientCredentialsFeignManager;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@Configuration
public class OpenFeignConfig {

    final static String CLIENT_REGISTRATION_ID = "ms-resources";

    final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
    final ClientRegistrationRepository clientRegistrationRepository;

    public OpenFeignConfig(OAuth2AuthorizedClientService oAuth2AuthorizedClientService,
                           ClientRegistrationRepository clientRegistrationRepository) {
        this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        ClientRegistration clientRegistration =
                clientRegistrationRepository.findByRegistrationId(CLIENT_REGISTRATION_ID);
        OAuthClientCredentialsFeignManager clientCredentialsFeignManager =
                new OAuthClientCredentialsFeignManager(oAuth2AuthorizedClientManager(), clientRegistration);

        String accessToken = clientCredentialsFeignManager.getAccessToken();

        return requestTemplate -> {
            requestTemplate.header("Authorization", String.format("Bearer %s", accessToken));
        };
    }

    OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager() {
        OAuth2AuthorizedClientProvider authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder
                .builder()
                .clientCredentials()
                .build();
        AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, oAuth2AuthorizedClientService);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }
}
