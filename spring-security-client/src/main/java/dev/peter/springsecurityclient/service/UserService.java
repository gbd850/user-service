package dev.peter.springsecurityclient.service;

import dev.peter.springsecurityclient.dto.PasswordResetRequest;
import dev.peter.springsecurityclient.dto.UserRequestDto;
import dev.peter.springsecurityclient.event.PasswordResetEvent;
import dev.peter.springsecurityclient.event.RegistrationCompleteEvent;
import dev.peter.springsecurityclient.helper.TokenUtil;
import dev.peter.springsecurityclient.helper.UrlUtil;
import dev.peter.springsecurityclient.model.Role;
import dev.peter.springsecurityclient.model.User;
import dev.peter.springsecurityclient.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private ApplicationEventPublisher eventPublisher;
    public ResponseEntity<String> registerUser(UserRequestDto userDto, HttpServletRequest httpServletRequest) {
        User user = User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role(Role.USER.getValue())
                .build();
        String token = TokenUtil.generateToken();
        userRepository.save(user);
        String appUrl = UrlUtil.generateApplicationUrl(httpServletRequest);
        eventPublisher.publishEvent(new RegistrationCompleteEvent(user, token, appUrl));
        String resendUrl = appUrl + "/resend?token=" + token;
        return new ResponseEntity<>("User created successfully\n" +
                "Verification link has been sent\n" +
                "To resend verification link click here: " +
                resendUrl, HttpStatus.CREATED);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public ResponseEntity<String> resetPassword(PasswordResetRequest passwordResetRequest, HttpServletRequest httpServletRequest) {
        Optional<User> user = userRepository.findByEmail(passwordResetRequest.getEmail());
        if (user.isEmpty()) {
            return new ResponseEntity<>("User with this email address does not exist", HttpStatus.NOT_FOUND);
        }
        String appUrl = UrlUtil.generateApplicationUrl(httpServletRequest);
        eventPublisher.publishEvent(new PasswordResetEvent(user.get(), appUrl));
        return new ResponseEntity<>("Password reset link has been sent", HttpStatus.OK);
    }
}
