package com.dh.msbills.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.dh.msbills.security.PrefixUtil.*;

@Component("JwtAuthoritiesConverter")
public class JWTConverter implements Converter<Jwt, AbstractAuthenticationToken> {


    @Value("${app.keycloak.resource_id}")
    private String resourceId;

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        Collection<GrantedAuthority> authorities =
                Stream.concat(jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                        extractRoles(jwt).stream()).collect(Collectors.toSet());

        return new JwtAuthenticationToken(jwt, authorities);
    }


    private Collection<? extends GrantedAuthority> extractRoles(Jwt jwt) {

        Collection<GrantedAuthority> allRoles = new HashSet<>();

        allRoles.addAll(extractResourceRoles(jwt));
        allRoles.addAll(extractRealmRoles(jwt));
        allRoles.addAll(extractAud(jwt));
        allRoles.addAll(extractScopes(jwt));
        allRoles.addAll(extractGroups(jwt));

        return allRoles;
    }


    private Collection<GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");

        if (resourceAccess == null) return Set.of();

        Map<String, Object> resourceAccessByResourceId = (Map<String, Object>) resourceAccess.get(resourceId);
        Map<String, Object> resourceAccessByAccount = (Map<String, Object>) resourceAccess.get("account");

        if (resourceAccessByResourceId == null) return Set.of();

        Collection<String> resourceRolesByResourceId = (Collection<String>) resourceAccessByResourceId.get("roles");
        //Collection<String> resourceRolesByAccount = (Collection<String>) resourceAccessByAccount.get("roles");

        Collection<String> allResourceRoles = new HashSet<>();

        allResourceRoles.addAll(resourceRolesByResourceId);
        //allResourceRoles.addAll(resourceRolesByAccount);

        return allResourceRoles.stream().map(resourceRole ->
                new SimpleGrantedAuthority(addRolePrefix(resourceRole))).collect(Collectors.toSet());
    }

    private Collection<GrantedAuthority> extractRealmRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");

        if (realmAccess == null) return Set.of();

        Collection<String> allRealmRoles = (Collection<String>) realmAccess.get("roles");

        return allRealmRoles.stream().map(realmRole ->
                new SimpleGrantedAuthority(addRolePrefix(realmRole))).collect(Collectors.toSet());
    }

    private Collection<GrantedAuthority> extractAud(Jwt jwt) {
        Collection<String> aud = jwt.getClaim("aud");

        if (aud == null) return Set.of();

        return aud.stream().map(currentAud ->
                new SimpleGrantedAuthority(addAudPrefix(currentAud))).collect(Collectors.toSet());
    }

    private Collection<GrantedAuthority> extractScopes(Jwt jwt) {
        String scope = jwt.getClaim("scope");
        if (scope == null) return Set.of();

        Collection<String> allScopes = Arrays.stream(scope.split(" ")).collect(Collectors.toSet());

        return allScopes.stream().map(currentScope ->
                new SimpleGrantedAuthority(addScopePrefix(currentScope))).collect(Collectors.toSet());
    }

    private Collection<GrantedAuthority> extractGroups(Jwt jwt) {
        Collection<String> groups = jwt.getClaim("groups");

        if (groups == null) return Set.of();

        return groups.stream().map(currentGroup ->
                new SimpleGrantedAuthority(addGroupPrefix(currentGroup))).collect(Collectors.toSet());
    }
}
