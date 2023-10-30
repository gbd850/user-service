package dev.peter.springsecurityclient.event.listener;

import dev.peter.springsecurityclient.event.RegistrationCompleteEvent;
import dev.peter.springsecurityclient.model.User;
import org.springframework.context.ApplicationListener;

import java.util.UUID;

public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
    }
}
