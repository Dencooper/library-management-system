package com.library.commonservice.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender javaMailSender;

    private final Configuration config;

    /**
     * Sends an email with the given details.
     *
     * @param to         the recipient's email address
     * @param subject    the subject of the email
     * @param text       the body content of the email
     * @param isHtml     true if the email content is HTML, false if plain text
     * @param attachment a file to attach to the email (can be null if no attachment)
     */
    public void sendEmail(String to, String subject, String text, boolean isHtml, File attachment) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, isHtml);
            if(attachment != null){
                FileSystemResource fileSystemResource = new FileSystemResource(attachment);
                helper.addAttachment(fileSystemResource.getFilename(), fileSystemResource);
            }

            javaMailSender.send(message);
            log.info("Send successfully email to {}", to);

        } catch (MessagingException e) {
            log.error("Failed to send email to {}", to, e.getMessage());

        }
    }

    /**
     * Sends an email using a FreeMarker template and optional file attachment.
     *
     * @param to            recipient's email address
     * @param subject       subject of the email
     * @param templateName  name of the FreeMarker template file (e.g., "welcome.ftl")
     * @param placeholders  map containing dynamic data to fill in the template
     * @param attachment    file to attach (can be null if no attachment is needed)
     */
    public void sendEmailWithTemplate(String to, String subject, String templateName, Map<String, Object> placeholders,  File attachment) {
        try {
            Template template = config.getTemplate(templateName);
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, placeholders);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);

            if(attachment != null){
                FileSystemResource fileSystemResource = new FileSystemResource(attachment);
                helper.addAttachment(fileSystemResource.getFilename(), fileSystemResource);
            }

            javaMailSender.send(message);
            log.info("Send successfully email to {}", to);

        } catch (MessagingException | IOException | TemplateException e) {
            log.error("Failed to send email with template to {}", to, e.getMessage());

        }
    }
}
