package de.herrschinghilft.application.services.matching;

import de.herrschinghilft.application.entities.AssistantEntity;
import de.herrschinghilft.application.entities.Messenger;
import de.herrschinghilft.application.entities.SeekerEntity;
import de.herrschinghilft.application.repositories.AssistantRepository;
import de.herrschinghilft.application.repositories.SeekerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BiFunction;

@Service
public class MatchingService {
    private static final Logger log = LoggerFactory.getLogger(MatchingService.class);

    LinkedHashMap<BiFunction<AssistantEntity, SeekerEntity, Boolean>, MatchingQuality> matchingFunctions;

    @Autowired
    private AssistantRepository assistantRepository;

    @Autowired
    private SeekerRepository seekerRepository;

    public MatchingService() {
        matchingFunctions = new LinkedHashMap<>();

        matchingFunctions.put(this::citiesMatch, MatchingQuality.CITY);
        matchingFunctions.put(this::haveCommonMessenger, MatchingQuality.MESSENGER);
        matchingFunctions.put(this::preferredMessengerAvailable, MatchingQuality.PREFERRED_MESSENGER);
    }

    public List<SeekerEntity> tryMatch(List<AssistantEntity> assistantsToMatch, List<SeekerEntity> seekersToMatch) {
        List<SeekerEntity> matchedSeekers = new ArrayList<>();
        Map<SeekerEntity, AssistantEntity> improvableMatches = new LinkedHashMap<>();

        assistantsToMatch.sort(Comparator.comparingInt(a -> a.getSeekerEntities().size()));

        for (SeekerEntity seeker : seekersToMatch) {
            for (AssistantEntity assistant : assistantsToMatch) {
                MatchingQuality quality = match(assistant, seeker);

                if (quality == MatchingQuality.MESSENGER) { // first acceptable match, but still not perfect
                    improvableMatches.put(seeker, assistant);
                    log.info("Added seeker " + seeker.getFirstName() + " " + seeker.getLastName() + " with assistant " + assistant.getFirstName() + " " + assistant.getLastName() + " to improvable");
                    continue;
                }
                if (quality == MatchingQuality.PREFERRED_MESSENGER) { // perfect match
                    improvableMatches.remove(seeker);
                    saveMapping(assistant, seeker);
                    matchedSeekers.add(seeker);
                    log.info("Matched seeker " + seeker.getFirstName() + " " + seeker.getLastName() + " with assistant " + assistant.getFirstName() + " " + assistant.getLastName());
                    assistantsToMatch.sort(Comparator.comparingInt(a -> a.getSeekerEntities().size())); // re-sort the assistants to ensure equal distribution
                    break;
                }
            }
        }

        // save all non-perfect matches which are still acceptable
        for (SeekerEntity seeker : improvableMatches.keySet()) {
            saveMapping(improvableMatches.get(seeker), seeker);
            matchedSeekers.add(seeker);
        }

        return matchedSeekers;
    }

    private void saveMapping(AssistantEntity assistant, SeekerEntity seeker) {
        seeker.getAssistantEntities().add(assistant);
        assistant.getSeekerEntities().add(seeker);
        assistantRepository.save(assistant);
        seekerRepository.save(seeker);
    }

    private MatchingQuality match(AssistantEntity assistant, SeekerEntity seeker) {
        MatchingQuality currentQuality = MatchingQuality.NONE;
        for (BiFunction<AssistantEntity, SeekerEntity, Boolean> function : matchingFunctions.keySet()) {
            if (!function.apply(assistant, seeker)) {
                return currentQuality;
            }
            currentQuality = matchingFunctions.get(function);
        }
        return currentQuality;
    }

    private boolean citiesMatch(AssistantEntity assistant, SeekerEntity seeker) {
        return assistant.getCity().equals(seeker.getCity());
    }

    private boolean haveCommonMessenger(AssistantEntity assistant, SeekerEntity seeker) {
        for (Messenger assistantMessenger : assistant.getMessengers()) {
            for (Messenger seekerMessenger : seeker.getMessengers()) {
                if (assistantMessenger.equals(seekerMessenger)) {
                    return true; // TODO refactor to be nicer than two nested loops
                }
            }
        }
        return false;
    }

    private boolean preferredMessengerAvailable(AssistantEntity assistant, SeekerEntity seeker) {
        return assistant.getMessengers().contains(seeker.getPreferredCommunication());
    }
}
