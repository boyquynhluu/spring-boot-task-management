package com.taskmanagement.mail;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.taskmanagement.entities.User;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "MAIL SERVICE")
public class MailService {

    private final JavaMailSender javaMailSender;
    private final MailTemplateService mailTemplateService;
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    /**
     * 
     * @param user
     * @param token
     */
    @Async("asyncExecutor")
    public void sendVerificationEmail(User user, String token) {

        if(Objects.isNull(user)) {
            log.error("User not exist!");
            return;
        }

        try {
            log.info("Start Send Mail Verify Token to user: {}", user.getEmail());

            // Creating a mime message
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            // Set info send mail
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(user.getEmail());
            mimeMessageHelper.setSubject("Kích hoạt tài khoản");

            String verifyUrl = frontendUrl + "/verify?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8);
            String expiredAtStr = LocalDateTime.now().plusMinutes(30).format(DATE_TIME_FORMATTER);

            String html = mailTemplateService.buildVerifyEmail(
                    user.getName(),
                    verifyUrl,
                    expiredAtStr
            );
            mimeMessageHelper.setText(html, true);

            // Sending the mail
            javaMailSender.send(mimeMessage);

            log.info("Verification email sent successfully to: {}", user.getEmail());
        } catch (MessagingException e) {
            log.error("Send mail has error: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error in sendVerificationEmail: {}", e.getMessage(), e);
        }
    }
}
