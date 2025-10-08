package org.massmanagement.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.massmanagement.dto.MailStructure;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
    @Value("${spring.mail.username}")
    public String fromMail;
    private final JavaMailSender mailSender;

    public void send(MailStructure mail) {

        try (var service = Executors.newVirtualThreadPerTaskExecutor()) {
            List<String> mails = mail.toMails();

            String subject = mail.subject();
            String mailText = mail.mailText();
            for (String toMail : mails) {
                service.submit(() -> sendIndividualMail(toMail, subject, mailText));
            }
        }
    }

    public void sendIndividualMail(String toMail, String subject, String mailText) {
        if (toMail.isEmpty() || subject.isEmpty() || mailText.isEmpty()) {
            log.error("Invalid mail");
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

            helper.setTo(toMail);
            helper.setFrom(fromMail);
            helper.setSubject(subject);
            helper.setText(mailText, true);

            mailSender.send(message);
            log.info("HTML mail '{}' sent to {}", subject, toMail);

        } catch (Exception ex) {
            log.error("Failed to send mail '{}' to {}: {}", subject, toMail, ex.getMessage());
        }
    }
}
