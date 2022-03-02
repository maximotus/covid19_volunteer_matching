package de.herrschinghilft.application.entities;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity(name = "Seeker")
public class SeekerEntity extends PersonalDataEntity {

    @Id
    @Column(name = "seekerId", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int seekerId;

    @Enumerated(EnumType.STRING)
    private Messenger preferredCommunication;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(mappedBy = "seekerEntities")
    private List<AssistantEntity> assistantEntities;

    /**
     * Empty public constructor for Spring Boot JPA.
     */
    public SeekerEntity() {
    }

    public SeekerEntity(String firstName, String lastName, String streetAndNumber, int postalCode, String city, String country, String phoneNumber, String email, List<Messenger> messengers, Messenger preferredCommunication, String message) {
        super(firstName, lastName, streetAndNumber, postalCode, city, country, phoneNumber, email, messengers, message);
        this.preferredCommunication = preferredCommunication;
    }

    public String toString() {
        return super.toString() + ", additional properties of SeekerEntity: {"
                + "seekerId: " + seekerId + "; "
                + "preferredCommunication: " + preferredCommunication
                + "}";
        // TODO do we want to output the matches?
    }

    public Messenger getPreferredCommunication() {
        return preferredCommunication;
    }

    public void setPreferredCommunication(Messenger preferredCommunication) {
        this.preferredCommunication = preferredCommunication;
    }


    public List<AssistantEntity> getAssistantEntities() {
        return assistantEntities;
    }

    public void setAssistantEntities(List<AssistantEntity> assistantEntities) {
        this.assistantEntities = assistantEntities;
    }
}
