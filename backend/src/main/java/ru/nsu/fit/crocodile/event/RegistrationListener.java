package ru.nsu.fit.crocodile.event;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import ru.nsu.fit.crocodile.config.ConfigurationProperties;
import ru.nsu.fit.crocodile.model.UserData;
import ru.nsu.fit.crocodile.model.token.VerificationToken;
import ru.nsu.fit.crocodile.service.TokenService;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class RegistrationListener implements
        ApplicationListener<OnRegistrationCompleteEvent> {

    private final TokenService<VerificationToken> tokenService;

    private final ConfigurationProperties configurationProperties;

    @Value("${front.page.confirmation}")
    private String frontConfirmRegPage;

    @Value("${server.url}")
    private String serverUrl;

    @Value("${server.port}")
    private String serverPort;

    private final JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    public void confirmRegistration(OnRegistrationCompleteEvent event) {
        UserData user = event.getUser();
        String token = UUID.randomUUID().toString();
        tokenService.createToken(new VerificationToken(token, user));

        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl
                = serverUrl + ":" + serverPort + frontConfirmRegPage + "?token=" + token;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(configurationProperties.registrationSucceedEmailMessage + "\r\n" + confirmationUrl);

        mailSender.send(email);
    }
}
