package dev.peter.springsecurityclient.service;

import dev.peter.springsecurityclient.dto.NewPasswordRequest;
import dev.peter.springsecurityclient.model.PasswordResetToken;
import dev.peter.springsecurityclient.model.User;
import dev.peter.springsecurityclient.repository.PasswordResetTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PasswordResetTokenService {

    private PasswordResetTokenRepository passwordResetTokenRepository;
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    public void savePasswordResetToken(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(user, token);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    public ResponseEntity<String> validateToken(String token, NewPasswordRequest newPasswordRequest) {
        Optional<PasswordResetToken> passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if (passwordResetToken.isEmpty()) {
            return new ResponseEntity<>("Invalid token", HttpStatus.NOT_FOUND);
        }
        Calendar calendar = Calendar.getInstance();
        if (passwordResetToken.get().getExpirationDate().getTime() - calendar.getTime().getTime() <= 0) {
            passwordResetTokenRepository.delete(passwordResetToken.get());
            return new ResponseEntity<>("Expired token", HttpStatus.GONE);
        }
        User user = passwordResetToken.get().getUser();
        user.setPassword(passwordEncoder.encode(newPasswordRequest.getNewPassword()));
        userService.updateUser(user);
        return new ResponseEntity<>("Password changed successfully", HttpStatus.OK);
    }
}
