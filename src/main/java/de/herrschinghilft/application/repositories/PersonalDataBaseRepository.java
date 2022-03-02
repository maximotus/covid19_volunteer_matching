package de.herrschinghilft.application.repositories;

import de.herrschinghilft.application.entities.PersonalDataEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

@NoRepositoryBean
public interface PersonalDataBaseRepository<T extends PersonalDataEntity> extends CrudRepository<T, Integer> {
    public T findByFirstName(String firstName);

    public T findByLastName(String lastName);

    public T findByStreetWithNumber(String streetWithNumber);

    public T findByPostalCode(int postalCode);

    public T findByCity(String city);

    public T findByCountry(String country);

    public T findByPhoneNumber(String phoneNumber);

    public T findByEmail(String email);

    public T findByMessage(String message);

    public T findByUuid(UUID uuid);

    public T findByEmailConfirmationToken(UUID token);
}
