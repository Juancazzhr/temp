package com.gamebox.msusers.repository;

import com.gamebox.msusers.dto.UserDto;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("UserRepositoryImpl")
public class UserRepositoryImpl implements IUserRepository {

    @Value("${app.keycloak.realm}")
    String realm;

    final Keycloak keycloak;

    public UserRepositoryImpl(Keycloak keycloak) {
        this.keycloak = keycloak;
    }


    @Override
    public Optional<UserRepresentation> getById(String id) {
        return Optional.ofNullable(
                keycloak.realm(realm)
                        .users()
                        .get(id)
                        .toRepresentation()
        );
    }
}
