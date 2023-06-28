package com.gamebox.msusers.service;

import com.gamebox.msusers.dto.UserDto;

public interface IUserService {
    UserDto getById(String id);
}
