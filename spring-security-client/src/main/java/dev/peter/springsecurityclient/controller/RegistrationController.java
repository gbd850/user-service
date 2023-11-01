package dev.peter.springsecurityclient.controller;

import dev.peter.springsecurityclient.dto.UserRequestDto;
import dev.peter.springsecurityclient.service.UserService;
import dev.peter.springsecurityclient.service.VerificationTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class RegistrationController {

    private UserService userService;
    private VerificationTokenService verificationTokenService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> registerUser(@RequestBody UserRequestDto userDto, final HttpServletRequest httpServletRequest) {
        return userService.registerUser(userDto, httpServletRequest);
    }

    @GetMapping("/verify")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> verifyRegistrationToken(@RequestParam("token") String token) {
        return verificationTokenService.validateToken(token);
    }
}
