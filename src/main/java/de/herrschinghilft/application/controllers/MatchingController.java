package de.herrschinghilft.application.controllers;

import de.herrschinghilft.application.entities.AssistantEntity;
import de.herrschinghilft.application.entities.PersonalDataEntity;
import de.herrschinghilft.application.entities.SeekerEntity;
import de.herrschinghilft.application.repositories.AssistantRepository;
import de.herrschinghilft.application.repositories.SeekerRepository;
import de.herrschinghilft.application.services.matching.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/matching")
public class MatchingController {

    @Autowired
    private AssistantRepository assistantRepository;

    @Autowired
    private SeekerRepository seekerRepository;

    @Autowired
    private MatchingService matchingService;

    @Autowired
    private ControllerUtility controllerUtility;

    @GetMapping
    private void doMatching() {
        List<AssistantEntity> assistants = new ArrayList<>();
        List<SeekerEntity> seekers = new ArrayList<>();
        assistantRepository.findAll().iterator().forEachRemaining(assistants::add);
        seekerRepository.findAll().iterator().forEachRemaining(seekers::add);

        List<AssistantEntity> assistantsToMatch = assistants.stream().filter(PersonalDataEntity::isEmailConfirmed).collect(Collectors.toList());
        List<SeekerEntity> seekersToMatch = seekers.stream().filter(PersonalDataEntity::isEmailConfirmed).filter(s -> s.getAssistantEntities().size() == 0).collect(Collectors.toList());

        List<SeekerEntity> matchedSeekers = matchingService.tryMatch(assistantsToMatch, seekersToMatch);

        for (SeekerEntity seeker : matchedSeekers) {
            controllerUtility.sendMatchingEmail(seeker);

            for (AssistantEntity assistant : seeker.getAssistantEntities()) {
                controllerUtility.sendMatchingEmail(assistant);
            }
        }
    }
}
