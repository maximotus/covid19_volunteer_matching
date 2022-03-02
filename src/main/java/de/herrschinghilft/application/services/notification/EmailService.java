package de.herrschinghilft.application.services.notification;

import de.herrschinghilft.application.entities.AssistantEntity;
import de.herrschinghilft.application.entities.PersonalDataEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    // TODO to application.properties
    private static final String SUBJECT_OF_CONFIRMATION = "HerrschingHilft: Griaß di!";
    private static final String SUBJECT_OF_SEEKER_MATCHING = "HerrschingHilft: Wir haben eine/n Helfer/in gefunden!";
    private static final String SUBJECT_OF_ASSISTANT_MATCHING = "HerrschingHilft: Jemand braucht deine Hilfe!";
    private static final String SUBJECT_OF_DELETION = "HerrschingHilft: Pfiat di!";
    private static final String HERRSCHING_HILFT_IMAGE_CONTENT_ID = "herrschingHilftImage";
    private static final String ASSISTANT_SEEKER_IMAGE_CONTENT_ID = "assistantSeekerImage";
    private static final String ASSISTANT_IMAGE_CONTENT_ID = "assistantImage";
    private static final String SEEKER_IMAGE_CONTENT_ID = "seekerImage";
    private static final String HERRSCHING_HILFT_IMAGE_PATH = "images/herrsching_hilft.png";
    private static final String ASSISTANT_SEEKER_IMAGE_PATH = "images/assistant_seeker.png";
    private static final String ASSISTANT_IMAGE_PATH = "images/assistant.png";
    private static final String SEEKER_IMAGE_PATH = "images/seeker.png";


    @Autowired
    JavaMailSender emailSender;

    @Autowired
    EmailContentBuilder emailContentBuilder;

    @Value("${spring.mail.username}")
    private String username;

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom(username);
        emailSender.send(message);
    }

    public void sendMimeMessage(MimeMessage message) {
        emailSender.send(message);
    }

    public MimeMessage prepareConfirmationMessage(PersonalDataEntity person, String url) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = prepareHelper(person.getEmail(), message);
        helper.setSubject(SUBJECT_OF_CONFIRMATION);
        helper.setText(emailContentBuilder.buildConfirmationMessage(person, url), true);

        tryInlineImage(HERRSCHING_HILFT_IMAGE_CONTENT_ID, HERRSCHING_HILFT_IMAGE_PATH, person.getEmail(), helper);
        tryInlineImage(ASSISTANT_SEEKER_IMAGE_CONTENT_ID, ASSISTANT_SEEKER_IMAGE_PATH, person.getEmail(), helper);

        return message;
    }

    public MimeMessage prepareMatchingMessage(PersonalDataEntity person) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = prepareHelper(person.getEmail(), message);

        helper.setText(emailContentBuilder.buildMatchingMessage(person), true);

        if (person instanceof AssistantEntity) {
            helper.setSubject(SUBJECT_OF_ASSISTANT_MATCHING);
            tryInlineImage(HERRSCHING_HILFT_IMAGE_CONTENT_ID, HERRSCHING_HILFT_IMAGE_PATH, person.getEmail(), helper);
            tryInlineImage(ASSISTANT_IMAGE_CONTENT_ID, ASSISTANT_IMAGE_PATH, person.getEmail(), helper);
        } else {
            helper.setSubject(SUBJECT_OF_SEEKER_MATCHING);
            tryInlineImage(HERRSCHING_HILFT_IMAGE_CONTENT_ID, HERRSCHING_HILFT_IMAGE_PATH, person.getEmail(), helper);
            tryInlineImage(SEEKER_IMAGE_CONTENT_ID, SEEKER_IMAGE_PATH, person.getEmail(), helper);
        }

        return message;
    }

    public MimeMessage prepareDeletionMessage(PersonalDataEntity person) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = prepareHelper(person.getEmail(), message);

        helper.setSubject(SUBJECT_OF_DELETION);
        helper.setText(emailContentBuilder.buildDeletionMessage(person), true);

        tryInlineImage(HERRSCHING_HILFT_IMAGE_CONTENT_ID, HERRSCHING_HILFT_IMAGE_PATH, person.getEmail(), helper);

        return message;
    }

    private MimeMessageHelper prepareHelper(String to, MimeMessage message) throws MessagingException, UnsupportedEncodingException {
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setFrom(new InternetAddress(username, "Herrsching hilft"));

        return helper;
    }

    private void tryInlineImage(String imageContentId, String imagePath, String email, MimeMessageHelper helper) throws MessagingException {
        helper.addInline(imageContentId, new ClassPathResource(imagePath));
    }
}

