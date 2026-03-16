package com.taskmanagement.mail;

import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailTemplateService {

    private final SpringTemplateEngine templateEngine;

    public String buildVerifyEmail(String fullName, String verifyUrl, String expiredAtStr) {
        Context context = new Context();
        context.setVariable("fullName", fullName);
        context.setVariable("verifyUrl", verifyUrl);
        context.setVariable("expiredAtStr", expiredAtStr);

        return templateEngine.process("verify-email", context);
    }
}
