package dev.peter.springsecurityclient.service;

import dev.peter.springsecurityclient.model.User;
import dev.peter.springsecurityclient.model.VerificationToken;
import dev.peter.springsecurityclient.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VerificationTokenService {

    private VerificationTokenRepository verificationTokenRepository;
    private UserService userService;

    public void saveVerificationToken(User user, String token) {
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationTokenRepository.save(verificationToken);
    }

    public ResponseEntity<String> validateToken(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken.isEmpty()) {
            return new ResponseEntity<>("Invalid token", HttpStatus.NOT_FOUND);
        }
        User user = verificationToken.get().getUser();
        Calendar calendar = Calendar.getInstance();
        if (verificationToken.get().getExpirationDate().getTime() - calendar.getTime().getTime() <= 0) {
            verificationTokenRepository.delete(verificationToken.get());
            return new ResponseEntity<>("Expired token", HttpStatus.GONE);
        }
        user.setEnabled(true);
        userService.updateUser(user);
        return new ResponseEntity<>("User successfully verified", HttpStatus.OK);
    }
}
