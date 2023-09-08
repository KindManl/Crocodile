package ru.nsu.fit.crocodile.event;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import ru.nsu.fit.crocodile.config.ConfigurationProperties;
import ru.nsu.fit.crocodile.model.UserData;
import ru.nsu.fit.crocodile.model.token.PasswordResetToken;
import ru.nsu.fit.crocodile.service.TokenService;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class PasswordResetListener implements
        ApplicationListener<OnPasswordResetEvent> {
    private final TokenService<PasswordResetToken> tokenService;

    private final ConfigurationProperties configurationProperties;

    @Value("${front.page.password_reset}")
    private String frontResetPassPage;

    @Value("${front.url}")
    private String frontUrl;
    private final JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnPasswordResetEvent event) {
        this.confirmReset(event);
    }

    private void confirmReset(OnPasswordResetEvent event) {//TODO проверка на существующий токен
        UserData user = event.getUser();
        String token = UUID.randomUUID().toString();
        tokenService.createToken(new PasswordResetToken(token, user));

        String recipientAddress = user.getEmail();
        String subject = "Reset Password";
        String confirmationUrl
                = frontUrl + frontResetPassPage + "?token=" + token;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(configurationProperties.passwordResetSucceedEmailMessage + "\r\n" + confirmationUrl);

        mailSender.send(email);
    }
}
