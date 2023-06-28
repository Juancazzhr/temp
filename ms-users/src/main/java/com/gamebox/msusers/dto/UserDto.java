package com.gamebox.msusers.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class UserDto {
    String id;
    String username;
    String email;
    String firstname;
    String lastName;
    List<BillDto> bills;

    public static UserDto build(UserRepresentation userRepresentation, List<BillDto> bills) {
        return new UserDto(userRepresentation.getId(),
                userRepresentation.getUsername(),
                userRepresentation.getEmail(),
                userRepresentation.getFirstName(),
                userRepresentation.getLastName(),
                bills);
    }
}
