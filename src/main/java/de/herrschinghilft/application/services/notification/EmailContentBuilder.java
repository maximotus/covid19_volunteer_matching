package de.herrschinghilft.application.services.notification;

import de.herrschinghilft.application.entities.AssistantEntity;
import de.herrschinghilft.application.entities.PersonalDataEntity;
import de.herrschinghilft.application.entities.SeekerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class EmailContentBuilder {

    private final TemplateEngine templateEngine;

    @Autowired
    public EmailContentBuilder(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String buildConfirmationMessage(PersonalDataEntity personalDataEntity, String url) {
        Context context = new Context();
        context.setVariable("firstName", personalDataEntity.getFirstName());
        context.setVariable("lastName", personalDataEntity.getLastName());
        context.setVariable("date", getToday());
        context.setVariable("city", personalDataEntity.getCity());
        context.setVariable("emailConfirmationUrl", url);

        return templateEngine.process("confirmationEmailTemplate", context);
    }

    private String getToday() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate today = LocalDate.now();
        return dateTimeFormatter.format(today);
    }

    public String buildMatchingMessage(PersonalDataEntity personalDataEntity) {
        Context context = new Context();

        context.setVariable("firstNameThis", personalDataEntity.getFirstName());
        context.setVariable("lastNameThis", personalDataEntity.getLastName());

        if (personalDataEntity instanceof AssistantEntity) {
            AssistantEntity assistantEntity = (AssistantEntity) personalDataEntity;
            SeekerEntity lastSeekerEntity = assistantEntity.getSeekerEntities().get(assistantEntity.getSeekerEntities().size()-1);

            context.setVariable("firstNameThat", lastSeekerEntity.getFirstName());
            context.setVariable("lastNameThat", lastSeekerEntity.getLastName());
            context.setVariable("cityThat", lastSeekerEntity.getCity());
            context.setVariable("messageThat", lastSeekerEntity.getMessage());
            context.setVariable("phoneNumberThat", lastSeekerEntity.getPhoneNumber());
            context.setVariable("emailThat", lastSeekerEntity.getEmail());
            StringBuilder messengers = new StringBuilder();
            lastSeekerEntity.getMessengers().forEach(m -> messengers.append(m.getDisplayName()).append(", "));
            context.setVariable("messengerThat", messengers.toString());
            context.setVariable("preferredMessengerThat", lastSeekerEntity.getPreferredCommunication().getDisplayName());

            return templateEngine.process("assistantMatchingEmailTemplate", context);
        } else {
            SeekerEntity seekerEntity = (SeekerEntity) personalDataEntity;
            AssistantEntity lastAssistantEntity = seekerEntity.getAssistantEntities().get(seekerEntity.getAssistantEntities().size()-1);

            context.setVariable("firstNameThat", lastAssistantEntity.getFirstName());
            context.setVariable("lastNameThat", lastAssistantEntity.getLastName());
            context.setVariable("cityThat", lastAssistantEntity.getCity());
            context.setVariable("messageThat", lastAssistantEntity.getMessage());
            context.setVariable("phoneNumberThat", lastAssistantEntity.getPhoneNumber());
            context.setVariable("emailThat", lastAssistantEntity.getEmail());
            StringBuilder messengers = new StringBuilder();
            lastAssistantEntity.getMessengers().forEach(m -> messengers.append(m.getDisplayName()).append(", "));
            context.setVariable("messengerThat", messengers.toString());

            return templateEngine.process("seekerMatchingEmailTemplate", context);
        }
    }

    public String buildDeletionMessage(PersonalDataEntity person) {
        Context context = new Context();
        context.setVariable("firstName", person.getFirstName());
        context.setVariable("lastName", person.getLastName());
        return templateEngine.process("deletionEmailTemplate", context);
    }
}
