package dev.peter.springsecurityclient.event;

import dev.peter.springsecurityclient.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {

    private User user;
    private String token;
    private String applicationUrl;

    public RegistrationCompleteEvent(User user, String token, String applicationUrl) {
        super(user);
        this.user = user;
        this.token = token;
        this.applicationUrl = applicationUrl;

    }
}
