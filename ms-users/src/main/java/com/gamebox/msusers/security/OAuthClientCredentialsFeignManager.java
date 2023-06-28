package com.gamebox.msusers.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class OAuthClientCredentialsFeignManager {

    final OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;
    final Authentication authentication;
    final ClientRegistration clientRegistration;

    public OAuthClientCredentialsFeignManager(OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager,
                                              ClientRegistration clientRegistration) {
        this.oAuth2AuthorizedClientManager = oAuth2AuthorizedClientManager;
        this.authentication = createPrincipal();
        this.clientRegistration = clientRegistration;
    }

    private Authentication createPrincipal() {
        return new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return Collections.emptySet();
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }

            @Override
            public String getName() {
                return clientRegistration.getClientId();
            }
        };
    }

    public String getAccessToken() {
        try {
            OAuth2AuthorizeRequest oAuth2AuthorizeRequest = OAuth2AuthorizeRequest
                    .withClientRegistrationId(clientRegistration.getClientId())
                    .principal(authentication)
                    .build();

            OAuth2AuthorizedClient oAuth2AuthorizedClient =
                    oAuth2AuthorizedClientManager.authorize(oAuth2AuthorizeRequest);


            if (Objects.isNull(oAuth2AuthorizedClient))
                throw new IllegalStateException(String.format("Client credentials flow on %s failed, client is null.",
                        clientRegistration.getClientId()));

            return oAuth2AuthorizedClient.getAccessToken().getTokenValue();

        } catch (Exception exception) {
            System.out.printf("Client credentials error: %s%n", exception.getMessage());
        }

        return null;
    }
}
