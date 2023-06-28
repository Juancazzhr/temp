package com.gamebox.msusers.controller;

import com.gamebox.msusers.dto.UserDto;
import com.gamebox.msusers.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping(path = "/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable(name = "id")
                                           String id) {
        return ResponseEntity.ok().body(userService.getById(id));
    }
}
