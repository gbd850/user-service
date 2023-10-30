package dev.peter.springsecurityclient.service;

import dev.peter.springsecurityclient.dto.UserDto;
import dev.peter.springsecurityclient.event.RegistrationCompleteEvent;
import dev.peter.springsecurityclient.model.Role;
import dev.peter.springsecurityclient.model.User;
import dev.peter.springsecurityclient.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private ApplicationEventPublisher eventPublisher;
    public User registerUser(UserDto userDto) {
        User user = User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role(Role.USER)
                .build();
        eventPublisher.publishEvent(new RegistrationCompleteEvent(user, "url"));
        return userRepository.save(user);
    }
}
