package de.herrschinghilft.application.controllers;

import de.herrschinghilft.application.entities.AssistantEntity;
import de.herrschinghilft.application.entities.PersonCount;
import de.herrschinghilft.application.repositories.AssistantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/assistant")
public class AssistantController {

    private static final Logger log = LoggerFactory.getLogger(AssistantController.class);

    @Autowired
    private AssistantRepository assistantRepository;

    @Autowired
    private ControllerUtility controllerUtility;

    @PostMapping(consumes = "application/json")
    public @ResponseBody
    ResponseEntity<String> insertAssistant(@RequestBody AssistantEntity assistant) {
        log.info("Received request to persist an assistant: " + assistant.toString());

        assistantRepository.save(assistant);
        log.info("Persisted assistant " + assistant);

        controllerUtility.sendConfirmationEmail(assistant);

        return ResponseEntity.ok().body("");
    }

    @GetMapping(path = "/count", produces = "application/json")
    public @ResponseBody
    ResponseEntity<PersonCount> getSeekerCount() {
        log.info("Received request to count the persisted assistants. There are " + assistantRepository.count());
        int count = Math.toIntExact(assistantRepository.count());
        return ResponseEntity.ok().body(new PersonCount(count));
    }

    @DeleteMapping(path = "/{uuid}")
    public @ResponseBody
    ResponseEntity<String> deleteAssistant(@PathVariable UUID uuid) {
        log.info("Received request to delete assistant with UUID " + uuid.toString());
        AssistantEntity assistantToDelete = assistantRepository.findByUuid(uuid);

        if (assistantToDelete == null) {
            log.error("Assistant with uuid " + uuid + " was not found.");
            return ResponseEntity.notFound().build();
        }

        assistantRepository.delete(assistantToDelete);
        log.info("Deleted assistant " + assistantToDelete);

        controllerUtility.sendDeletionEmail(assistantToDelete);
        return ResponseEntity.ok().body("");
    }
}
