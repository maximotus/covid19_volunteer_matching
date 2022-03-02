package de.herrschinghilft.application.controllers;

import de.herrschinghilft.application.entities.PersonCount;
import de.herrschinghilft.application.entities.SeekerEntity;
import de.herrschinghilft.application.repositories.SeekerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/seeker")
public class SeekerController {

    private static final Logger log = LoggerFactory.getLogger(SeekerController.class);

    @Autowired
    private SeekerRepository seekerRepository;

    @Autowired
    private ControllerUtility controllerUtility;

    @PostMapping(consumes = "application/json")
    public @ResponseBody
    ResponseEntity<String> insertSeeker(@RequestBody SeekerEntity seeker) {
        log.info("Received request to persist a seeker: " + seeker.toString());

        seekerRepository.save(seeker);
        log.info("Persisted seeker " + seeker);

        controllerUtility.sendConfirmationEmail(seeker);

        return ResponseEntity.ok().body("");
    }

    @GetMapping(path = "/count", produces = "application/json")
    public @ResponseBody
    ResponseEntity<PersonCount> getSeekerCount() {
        log.info("Received request to count the persisted seekers. There are " + seekerRepository.count());
        int count = Math.toIntExact(seekerRepository.count());
        return ResponseEntity.ok().body(new PersonCount(count));
    }

    @DeleteMapping(path = "/{uuid}")
    public @ResponseBody
    ResponseEntity<String> deleteAssistant(@PathVariable UUID uuid) {
        log.info("Received request to delete seeker with UUID " + uuid.toString());
        SeekerEntity seekerToDelete = seekerRepository.findByUuid(uuid);

        if (seekerToDelete == null) {
            log.error("Seeker with uuid " + uuid + " was not found.");
            return ResponseEntity.notFound().build();
        }

        seekerRepository.delete(seekerToDelete);
        log.info("Deleted seeker " + seekerToDelete);

        controllerUtility.sendDeletionEmail(seekerToDelete);
        return ResponseEntity.ok().body("");
    }
}
