package com.gamebox.msusers.repository;

import org.keycloak.representations.idm.UserRepresentation;

import java.util.Optional;

public interface IUserRepository {
    Optional<UserRepresentation> getById(String id);
}
