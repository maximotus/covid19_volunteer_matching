package de.herrschinghilft.application.entities;

import de.herrschinghilft.application.controllers.SeekerController;
import org.hibernate.LazyInitializationException;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@MappedSuperclass
// MappedSuperclass will create for every Entity class that extends that superclass an own table, but not for the superclass itself
public abstract class PersonalDataEntity {

    private static final Logger log = LoggerFactory.getLogger(PersonalDataEntity.class);

    protected String firstName;
    protected String lastName;
    protected String streetWithNumber;
    protected int postalCode;
    protected String city;
    protected String country;
    protected String phoneNumber;
    protected String email;
    protected boolean emailConfirmed;

    @Size(max = 2047)
    protected String message;

    @Type(type = "uuid-char")
    protected UUID uuid;

    @Type(type = "uuid-char")
    protected UUID emailConfirmationToken;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection(targetClass = Messenger.class)
    @Enumerated(EnumType.STRING)
    protected List<Messenger> messengers; // this is not a @ManyToMany relation because the Messenger enum is not an entity!

    /**
     * Public constructor for Spring Boot JPA.
     */
    public PersonalDataEntity() {
        this.uuid = UUID.randomUUID();
        this.emailConfirmationToken = UUID.randomUUID();
        this.emailConfirmed = false;
    }

    public PersonalDataEntity(String firstName, String lastName, String streetWithNumber, int postalCode, String city, String country, String phoneNumber, String email, List<Messenger> messengers, String message) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.streetWithNumber = streetWithNumber;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.messengers = messengers;
        this.message = message;
    }

    @Override
    public String toString() {

        String messengers = "";

        // can throw LazyInitializationException when the person has already been deleted
        try {
            messengers = Arrays.toString(this.messengers.toArray(new Messenger[]{}));
        } catch (LazyInitializationException e) {
            log.error("The messengers of person " + uuid + " do not exist (maybe because they have already been deleted): " + e.getMessage());
        }

        return "PersonalData: {"
                + "firstName: " + firstName + "; "
                + "lastName: " + lastName + "; "
                + "streetWithNumber: " + streetWithNumber + "; "
                + "postalCode: " + postalCode + "; "
                + "city: " + city + "; "
                + "country: " + country + "; "
                + "phoneNumber: " + phoneNumber + "; "
                + "email: " + email + "; "
                + "emailConfirmed: " + emailConfirmed + "; "
                + "message: " + message.substring(0, Math.min(message.length(), 30)) + "; "
                + "uuid: " + uuid + "; "
                + "emailConfirmationToken: " + emailConfirmationToken + "; "
                + "messengers: " + messengers
                + "}";
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStreetWithNumber() {
        return streetWithNumber;
    }

    public void setStreetWithNumber(String streetAndNumber) {
        this.streetWithNumber = streetAndNumber;
    }

    public int getPostalCode() {
        return postalCode;
    }

    /**
     * Returns the postal code as 5-digit number string, with leading zeros (e.g. for postal codes from Saxony (Sachsen))
     * Use this for displaying in user interface or other user output.
     *
     * @return postalCode as 5-digit number string
     */
    public String getPostalCodeWithLeadingZeros() {
        return String.format("%05d", postalCode);
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Messenger> getMessengers() {
        return messengers;
    }

    public void setMessengers(List<Messenger> messengers) {
        this.messengers = messengers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public boolean isEmailConfirmed() {
        return emailConfirmed;
    }

    public void setEmailConfirmed(boolean confirmedEmail) {
        this.emailConfirmed = confirmedEmail;
    }

    public UUID getEmailConfirmationToken() {
        return emailConfirmationToken;
    }

    public void setEmailConfirmationToken(UUID token) {
        this.emailConfirmationToken = token;
    }
}
