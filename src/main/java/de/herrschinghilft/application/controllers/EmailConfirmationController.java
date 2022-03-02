package de.herrschinghilft.application.controllers;

import de.herrschinghilft.application.entities.AssistantEntity;
import de.herrschinghilft.application.entities.SeekerEntity;
import de.herrschinghilft.application.repositories.AssistantRepository;
import de.herrschinghilft.application.repositories.SeekerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@Controller
@RequestMapping("/emailConfirmation")
public class EmailConfirmationController {

    private static final Logger log = LoggerFactory.getLogger(EmailConfirmationController.class);

    @Autowired
    SeekerRepository seekerRepository;

    @Autowired
    AssistantRepository assistantRepository;

    @GetMapping()
    public @ResponseBody
    ResponseEntity<String> getEmailConfirmation(@RequestParam("token") String tokenInc) {

        if (tokenInc == null) {
            log.error("No token given as a parameter of the incoming request.");
            return ResponseEntity.badRequest().body("Missing parameter token.");
        }

        UUID token = UUID.fromString(tokenInc);
        AssistantEntity assistantEntity = assistantRepository.findByEmailConfirmationToken(token);
        SeekerEntity seekerEntity = seekerRepository.findByEmailConfirmationToken(token);

        if (assistantEntity != null) {
            assistantEntity.setEmailConfirmed(true);
            assistantRepository.save(assistantEntity);
            log.info("Successfully confirmed assistant with email " + assistantEntity.getEmail() + " and uuid " + assistantEntity.getUuid());
            return ResponseEntity.ok().body("");
        }
        if (seekerEntity != null) {
            seekerEntity.setEmailConfirmed(true);
            seekerRepository.save(seekerEntity);
            log.info("Successfully confirmed seeker with email " + seekerEntity.getEmail() + " and uuid " + seekerEntity.getUuid());
            return ResponseEntity.ok().body("");
        }

        log.error("No entry found for the given token " + token);
        return ResponseEntity.badRequest().body("No entry found for token " + token);
    }
}
