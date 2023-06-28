package com.gamebox.msusers.service;

import com.gamebox.msusers.dto.BillDto;
import com.gamebox.msusers.dto.UserDto;
import com.gamebox.msusers.repository.IBillsRepository;
import com.gamebox.msusers.repository.IUserRepository;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("UserServiceImpl")
public class UserServiceImpl implements IUserService {

    final IUserRepository userRepository;
    final IBillsRepository billsRepository;

    public UserServiceImpl(IUserRepository userRepository, IBillsRepository billsRepository) {
        this.userRepository = userRepository;
        this.billsRepository = billsRepository;
    }

    @Override
    public UserDto getById(String id) {
        List<BillDto> billsByUser = billsRepository.getAllByCustomerId(id);
        Optional<UserRepresentation> userSearched = userRepository.getById(id);

        return userSearched
                .map(userRepresentation -> UserDto.build(userRepresentation, billsByUser))
                .orElse(null);
    }
}
