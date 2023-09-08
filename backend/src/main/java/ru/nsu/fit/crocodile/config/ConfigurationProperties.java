package ru.nsu.fit.crocodile.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationProperties {
    public String registrationSucceedEmailMessage = "Registration succeed";
    public String passwordResetSucceedEmailMessage = "Password reset succeed";
}
