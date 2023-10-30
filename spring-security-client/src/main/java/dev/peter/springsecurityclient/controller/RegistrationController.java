package dev.peter.springsecurityclient.controller;

import dev.peter.springsecurityclient.dto.UserDto;
import dev.peter.springsecurityclient.model.User;
import dev.peter.springsecurityclient.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class RegistrationController {

    private UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User registerUser(@RequestBody UserDto userDto, final HttpServletRequest httpServletRequest) {
        return userService.registerUser(userDto, httpServletRequest);
    }
}
