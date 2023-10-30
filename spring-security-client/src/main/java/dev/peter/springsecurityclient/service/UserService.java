package dev.peter.springsecurityclient.service;

import dev.peter.springsecurityclient.dto.UserDto;
import dev.peter.springsecurityclient.event.RegistrationCompleteEvent;
import dev.peter.springsecurityclient.model.Role;
import dev.peter.springsecurityclient.model.User;
import dev.peter.springsecurityclient.model.VerificationToken;
import dev.peter.springsecurityclient.repository.UserRepository;
import dev.peter.springsecurityclient.repository.VerificationTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private VerificationTokenRepository verificationTokenRepository;
    private PasswordEncoder passwordEncoder;
    private ApplicationEventPublisher eventPublisher;
    public ResponseEntity<String> registerUser(UserDto userDto, HttpServletRequest httpServletRequest) {
        User user = User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role(Role.USER.getValue())
                .build();
        userRepository.save(user);
        eventPublisher.publishEvent(new RegistrationCompleteEvent(user, generateApplicationUrl(httpServletRequest)));
        return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
    }

    private String generateApplicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    public void saveVerificationToken(User user, String token) {
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationTokenRepository.save(verificationToken);
    }
}
