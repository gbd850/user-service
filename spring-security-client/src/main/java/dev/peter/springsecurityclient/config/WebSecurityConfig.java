package dev.peter.springsecurityclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    //whitelist
    @Bean
    @Order(0)
    public SecurityFilterChain whitelistFilterChain(HttpSecurity http) throws Exception {

        String[] endpoints = new String[] {"/register", "/verify", "/resend", "/resetPassword", "/changePassword"};

        return http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .securityMatcher(endpoints)
                .authorizeHttpRequests(auth -> auth.requestMatchers(endpoints).permitAll())
                .authorizeHttpRequests(auth -> {
                    try {
                        auth.requestMatchers("/api/**").authenticated()
                                .and()
                                .oauth2Login(oauth2Login -> oauth2Login.loginPage("/oauth2/authorization/api-client-oidc"))
                                .oauth2Client(Customizer.withDefaults());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .build();
    }

    // catch-all
    @Bean
    @Order(1)
    public SecurityFilterChain catchAllFilterChain(HttpSecurity http) throws Exception {

        return http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .build();
    }
}
