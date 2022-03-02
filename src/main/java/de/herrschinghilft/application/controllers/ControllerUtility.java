package de.herrschinghilft.application.controllers;

import de.herrschinghilft.application.entities.PersonalDataEntity;
import de.herrschinghilft.application.services.notification.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Component
public class ControllerUtility {

    private static final Logger log = LoggerFactory.getLogger(ControllerUtility.class);

    @Autowired
    private EmailService emailService;

    public void sendConfirmationEmail(PersonalDataEntity person) {
        String appUrl = createUrl(person.getEmailConfirmationToken());

        // send email
        try {
            MimeMessage message = emailService.prepareConfirmationMessage(person, appUrl);
            emailService.sendMimeMessage(message);
            log.info("Successfully sent the confirmation email to " + person.getEmail());
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Failed to send confirmation email. " + e.getMessage());
        }
    }

    private String createUrl(UUID token) {
        // create url for the email confirmation
        String appUrl = "https://herrschinghilft.de/confirm?token=" + token;
        log.info("Generated confirmation link: " + appUrl);
        return appUrl;
    }

    public void sendMatchingEmail(PersonalDataEntity person) {
        try {
            MimeMessage message = emailService.prepareMatchingMessage(person);
            emailService.sendMimeMessage(message);
            log.info("Successfully sent the matching email to " + person.getEmail());
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Failed to send matching email. " + e.getMessage());
        }
    }

    public void sendDeletionEmail(PersonalDataEntity person) {
        try {
            MimeMessage message = emailService.prepareDeletionMessage(person);
            emailService.sendMimeMessage(message);
            log.info("Successfully sent the deletion email to " + person.getEmail());
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Failed to send deletion email. " + e.getMessage());
        }
    }
}
