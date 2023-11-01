package dev.peter.springsecurityclient.event.listener;

import dev.peter.springsecurityclient.event.PasswordResetEvent;
import dev.peter.springsecurityclient.helper.TokenUtil;
import dev.peter.springsecurityclient.model.User;
import dev.peter.springsecurityclient.service.PasswordResetTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class PasswordResetEventListener implements ApplicationListener<PasswordResetEvent> {

    private PasswordResetTokenService passwordResetTokenService;
    @Override
    public void onApplicationEvent(PasswordResetEvent event) {
        User user = event.getUser();
        String token = TokenUtil.generateToken();
        passwordResetTokenService.savePasswordResetToken(user, token);
        String url = event.getApplicationUrl() + "/changePassword?token=" + token;
        //log for development purposes / ideally should be sent email with link
        log.info("Password reset link: {}", url);
    }
}
