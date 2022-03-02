package de.herrschinghilft.application;

import de.herrschinghilft.application.controllers.ControllerUtility;
import de.herrschinghilft.application.entities.AssistantEntity;
import de.herrschinghilft.application.entities.PersonalDataEntity;
import de.herrschinghilft.application.entities.SeekerEntity;
import de.herrschinghilft.application.repositories.AssistantRepository;
import de.herrschinghilft.application.repositories.SeekerRepository;
import de.herrschinghilft.application.services.matching.MatchingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@EntityScan("de.herrschinghilft.application")
@EnableJpaRepositories("de.herrschinghilft.application.repositories")
@EnableScheduling
public class Application implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Autowired
    private AssistantRepository assistantRepository;

    @Autowired
    private SeekerRepository seekerRepository;

    @Autowired
    private MatchingService matchingService;

    @Autowired
    private ControllerUtility controllerUtility;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args); // launches the application
    }

    @Override
    public void run(String... args) throws Exception {
    }

    @Scheduled(cron = "0 4 17 * * *") // every day at 4 am
    private void doMatching() {
        log.info("Starting Matching...");
        List<AssistantEntity> assistants = new ArrayList<>();
        List<SeekerEntity> seekers = new ArrayList<>();
        assistantRepository.findAll().iterator().forEachRemaining(assistants::add);
        seekerRepository.findAll().iterator().forEachRemaining(seekers::add);

        List<AssistantEntity> assistantsToMatch = assistants.stream().filter(PersonalDataEntity::isEmailConfirmed).collect(Collectors.toList());
        List<SeekerEntity> seekersToMatch = seekers.stream().filter(PersonalDataEntity::isEmailConfirmed).filter(s -> s.getAssistantEntities().isEmpty()).collect(Collectors.toList());

        List<SeekerEntity> matchedSeekers = matchingService.tryMatch(assistantsToMatch, seekersToMatch);

        log.info("Matching ended. Matched " + matchedSeekers.size() + " seekers. " + (matchedSeekers.isEmpty() ? "" : "Sending emails now..."));

        for (SeekerEntity seeker : matchedSeekers) {
            controllerUtility.sendMatchingEmail(seeker);

            for (AssistantEntity assistant : seeker.getAssistantEntities()) {
                controllerUtility.sendMatchingEmail(assistant);
            }
        }
    }
}
