package dev.peter.springsecurityclient.event.listener;

import dev.peter.springsecurityclient.event.RegistrationCompleteEvent;
import dev.peter.springsecurityclient.model.User;
import dev.peter.springsecurityclient.service.UserService;
import dev.peter.springsecurityclient.service.VerificationTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    private VerificationTokenService verificationTokenService;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        verificationTokenService.saveVerificationToken(user, token);
        String url = event.getApplicationUrl() + "/verify?token=" + token;
        //log for development purposes / ideally should be sent email with link
        log.info("Verification link: {}", url);
    }
}
