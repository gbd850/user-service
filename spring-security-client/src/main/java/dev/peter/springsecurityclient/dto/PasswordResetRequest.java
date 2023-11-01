package dev.peter.springsecurityclient.dto;

import lombok.Data;

@Data
public class PasswordResetRequest {
    private String email;
}
