package de.herrschinghilft.application.repositories;

import de.herrschinghilft.application.entities.AssistantEntity;

import javax.transaction.Transactional;

@Transactional
public interface AssistantRepository extends PersonalDataBaseRepository<AssistantEntity> {
    public AssistantEntity findByAssistantId(int assistantId);
}
