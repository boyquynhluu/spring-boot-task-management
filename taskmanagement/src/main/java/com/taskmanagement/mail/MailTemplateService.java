package com.taskmanagement.mail;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.exceptions.TemplateInputException;
import org.thymeleaf.spring6.SpringTemplateEngine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "MAIL TEMPLATE SERVICE")
public class MailTemplateService {

    private final SpringTemplateEngine templateEngine;

    public String buildVerifyEmail(String fullName, String verifyUrl, String expiredAtStr) {
        try {
            Context context = new Context();
            context.setVariable("fullName", Objects.requireNonNullElse(fullName, ""));
            context.setVariable("verifyUrl", Objects.requireNonNullElse(verifyUrl, "#"));
            context.setVariable("expiredAtStr", Objects.requireNonNullElse(expiredAtStr, ""));

            return templateEngine.process("verify-email", context);
        } catch (TemplateInputException e) {
            log.error("Template 'verify-email' not found: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Error building verification email: {}", e.getMessage(), e);
            throw e;
        }
    }
}
