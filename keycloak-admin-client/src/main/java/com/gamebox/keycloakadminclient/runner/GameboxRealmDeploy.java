package com.gamebox.keycloakadminclient.runner;

import lombok.AllArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientScopeResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@AllArgsConstructor
@Component
public class GameboxRealmDeploy implements CommandLineRunner {

    final Keycloak keycloak;

    @Override
    public void run(String... args) {
        String gamebox_ecommerce = "gamebox_ecommerce";

        Optional<RealmRepresentation> realm =
                keycloak.realms()
                        .findAll()
                        .stream().filter(currentRealm -> currentRealm.getRealm().equals(gamebox_ecommerce)).findAny();
        if (realm.isPresent()) {
            keycloak.realm(gamebox_ecommerce).remove();
        }

        RealmRepresentation gameboxEcommerceRealm = new RealmRepresentation();
        gameboxEcommerceRealm.setRealm(gamebox_ecommerce);
        gameboxEcommerceRealm.setEnabled(true);
        gameboxEcommerceRealm.setAccessTokenLifespan(600);

        ClientRepresentation gatewayClient = new ClientRepresentation();
        gatewayClient.setClientId("ms-gateway");
        gatewayClient.setName("Ms Gateway");
        gatewayClient.setEnabled(true);
        gatewayClient.setClientAuthenticatorType("client-secret");
        gatewayClient.setSecret("A4NsizH9074OQFFJcuqJOlekSzIkS201");
        gatewayClient.setRootUrl("http://localhost:9090");
        gatewayClient.setAdminUrl("http://localhost:9090");
        gatewayClient.setRedirectUris(List.of("http://localhost:9090/*", "https://oauth.pstmn.io/v1/callback/*"));
        gatewayClient.setWebOrigins(List.of("http://localhost:9090", "https://oauth.pstmn.io"));

        ClientRepresentation resourcesClient = new ClientRepresentation();
        resourcesClient.setClientId("ms-resources");
        resourcesClient.setName("Ms Resources");
        resourcesClient.setEnabled(true);
        resourcesClient.setServiceAccountsEnabled(true);
        resourcesClient.setClientAuthenticatorType("client-secret");
        resourcesClient.setSecret("DDbAUbpjRRtAQnDpq1gzO8hQBud6ZBby");
        resourcesClient.setRedirectUris(List.of("/*"));
        resourcesClient.setWebOrigins(List.of("/*"));
        resourcesClient.setDefaultRoles(new String[]{"USER"});

        GroupRepresentation providersGroup = new GroupRepresentation();
        providersGroup.setName("PROVIDERS");

        UserRepresentation juancazz = new UserRepresentation();
        juancazz.setId("f9d54057-7fa0-4993-a6a8-4c2e92eac320");
        juancazz.setEnabled(true);
        juancazz.setCreatedTimestamp(new Date().getTime());
        juancazz.setUsername("juancazz");
        juancazz.setFirstName("Juan");
        juancazz.setLastName("Hernandez");
        juancazz.setEmail("juancazz@gmail.com");
        CredentialRepresentation juancazzCredential = new CredentialRepresentation();
        juancazzCredential.setType(CredentialRepresentation.PASSWORD);
        juancazzCredential.setValue("juancazz");
        juancazzCredential.setTemporary(false);
        juancazz.setCredentials(List.of(juancazzCredential));
        juancazz.setClientRoles(Map.of("ms-resources", List.of("USER")));

        UserRepresentation carl = new UserRepresentation();
        carl.setId("f9d54057-7fa0-4993-a6a8-4c2e92eac321");
        carl.setEnabled(true);
        carl.setCreatedTimestamp(new Date().getTime());
        carl.setUsername("carl");
        carl.setFirstName("Carl");
        carl.setLastName("Jones");
        carl.setEmail("carljones@gmail.com");
        CredentialRepresentation carlCredential = new CredentialRepresentation();
        carlCredential.setType(CredentialRepresentation.PASSWORD);
        carlCredential.setValue("carl");
        carlCredential.setTemporary(false);
        carl.setCredentials(List.of(carlCredential));
        carl.setClientRoles(Map.of("ms-resources", List.of("USER")));

        UserRepresentation thomas = new UserRepresentation();
        thomas.setId("f9d54057-7fa0-4993-a6a8-4c2e92eac120");
        thomas.setEnabled(true);
        thomas.setCreatedTimestamp(new Date().getTime());
        thomas.setUsername("thomas");
        thomas.setFirstName("Thomas");
        thomas.setLastName("Lin");
        thomas.setEmail("thomas.lin@gamebox.com");
        CredentialRepresentation thomasCredential = new CredentialRepresentation();
        thomasCredential.setType(CredentialRepresentation.PASSWORD);
        thomasCredential.setValue("thomas");
        thomasCredential.setTemporary(false);
        thomas.setCredentials(List.of(thomasCredential));
        thomas.setGroups(List.of("PROVIDERS"));
        thomas.setClientRoles(Map.of("ms-resources", List.of("USER")));

        gameboxEcommerceRealm.setClients(List.of(gatewayClient, resourcesClient));
        gameboxEcommerceRealm.setGroups(List.of(providersGroup));
        gameboxEcommerceRealm.setUsers(List.of(juancazz, carl, thomas));

        keycloak.realms().create(gameboxEcommerceRealm);

        addGroupMembershipToToken(gamebox_ecommerce, "profile");
        addAdminClientRoleToUser(gamebox_ecommerce);
    }

    public void addGroupMembershipToToken(String realm, String scope) {
        RealmResource realmResource = keycloak.realm(realm);

        List<ClientScopeRepresentation> clientScopes = realmResource.clientScopes().findAll();
        ClientScopeRepresentation clientScope = clientScopes.stream()
                .filter(currentClientScope -> currentClientScope.getName().equals(scope))
                .findFirst().orElse(null);

        String id = clientScope.getId();

        ProtocolMapperRepresentation groupMembershipMapper = new ProtocolMapperRepresentation();
        groupMembershipMapper.setName("group membership");
        groupMembershipMapper.setProtocol("openid-connect");
        groupMembershipMapper.setProtocolMapper("oidc-group-membership-mapper");
        groupMembershipMapper.getConfig().put("full.path", "false");
        groupMembershipMapper.getConfig().put("access.token.claim", "true");
        groupMembershipMapper.getConfig().put("id.token.claim", "true");
        groupMembershipMapper.getConfig().put("userinfo.token.claim", "true");
        groupMembershipMapper.getConfig().put("claim.name", "groups");

        ClientScopeResource clientScopeResource = realmResource.clientScopes().get(id);
        clientScopeResource.getProtocolMappers().createMapper(groupMembershipMapper).close();

        ClientScopeRepresentation updatedClientScope = clientScopeResource.toRepresentation();
        clientScopeResource.update(updatedClientScope);
    }

    private void addAdminClientRoleToUser(String gamebox_ecommerce) {
        UserRepresentation user = keycloak.realm(gamebox_ecommerce).users().search("service-account-ms-resources").get(0);
        String userId = user.getId();

        String realmManagementClientId = "realm-management";
        List<ClientRepresentation> clients = keycloak.realm(gamebox_ecommerce).clients().findByClientId(realmManagementClientId);
        String clientId = clients.get(0).getId();

        String[] roleNames = {"view-users"};
        List<RoleRepresentation> roles = new ArrayList<>();
        for (String roleName : roleNames) {
            RoleRepresentation role = keycloak.realm(gamebox_ecommerce).clients().get(clientId).roles().get(roleName).toRepresentation();
            roles.add(role);
        }
        keycloak.realm(gamebox_ecommerce).users().get(userId).roles().clientLevel(clientId).add(roles);
    }
}
