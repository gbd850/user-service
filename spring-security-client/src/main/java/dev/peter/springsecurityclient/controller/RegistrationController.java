package dev.peter.springsecurityclient.controller;

import dev.peter.springsecurityclient.dto.NewPasswordRequest;
import dev.peter.springsecurityclient.dto.PasswordResetRequest;
import dev.peter.springsecurityclient.dto.UserRequestDto;
import dev.peter.springsecurityclient.model.PasswordResetToken;
import dev.peter.springsecurityclient.service.PasswordResetTokenService;
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
    private PasswordResetTokenService passwordResetTokenService;

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

    @GetMapping("/resend")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> resendVerificationToken(@RequestParam("token") String oldToken, HttpServletRequest httpServletRequest) {
        return verificationTokenService.resendToken(oldToken, httpServletRequest);
    }

    @PostMapping("/resetPassword")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> resetUserPassword(@RequestBody PasswordResetRequest passwordResetRequest, HttpServletRequest httpServletRequest) {
        return userService.resetPassword(passwordResetRequest, httpServletRequest);
    }

    @PostMapping("/changePassword")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> changeUserPassword(@RequestParam("token") String token, @RequestBody NewPasswordRequest newPasswordRequest) {
        return passwordResetTokenService.validateToken(token, newPasswordRequest);
    }
}
