package com.taskmanagement.mail;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    /**
     * 
     * @param user
     * @param token
     */
    @Async
    public void sendVerificationEmail(User user, String token) {

        if(Objects.isNull(user)) {
            log.error("User not exist!");
        }

        try {
            log.info("Start Send Mail Verify Token");
            // Creating a mime message
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            // Set info send mail
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(user.getEmail());
            mimeMessageHelper.setSubject("Kích hoạt tài khoản");

            String verifyUrl = frontendUrl + "/verify?token=" + token;
            String expiredAtStr = formatDateTimeToString(LocalDateTime.now().plusMinutes(30));
            String html = mailTemplateService.buildVerifyEmail(
                    user.getName(),
                    verifyUrl,
                    expiredAtStr
            );

            mimeMessageHelper.setText(html, true);

            log.info("Send Mail...");
            // Sending the mail
            javaMailSender.send(mimeMessage);

            log.info("Verification email sent to: {}", user.getEmail());
        } catch (MessagingException e) {
            log.error("Send mail has error: {}", e.getMessage(), e);
        }
    }

    /**
     * 
     * @param dateTime
     * @return
     */
    private String formatDateTimeToString(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

        try {
            return dateTime.format(formatter);
        } catch (DateTimeParseException e) {
            log.error("Format Date To String Has Error: {}", e.getMessage(), e);
        }
        return null;
    }
}
