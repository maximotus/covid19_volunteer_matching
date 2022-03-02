package de.herrschinghilft.application.repositories;

import de.herrschinghilft.application.entities.Messenger;
import de.herrschinghilft.application.entities.SeekerEntity;

import javax.transaction.Transactional;

@Transactional
public interface SeekerRepository extends PersonalDataBaseRepository<SeekerEntity> {
    public SeekerEntity findBySeekerId(Integer seekerId);

    public SeekerEntity findByPreferredCommunication(Messenger messenger);
}
