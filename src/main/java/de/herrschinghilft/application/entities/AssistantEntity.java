package de.herrschinghilft.application.entities;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity(name = "Assistant")
public class AssistantEntity extends PersonalDataEntity {

    @Id
    @Column(name = "assistantId", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int assistantId;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "AssistantSeekerMapping", joinColumns = @JoinColumn(name = "assistantId", referencedColumnName = "assistantId"), inverseJoinColumns = @JoinColumn(name = "seekerId", referencedColumnName = "seekerId"))
    private List<SeekerEntity> seekerEntities;

    /**
     * Empty public constructor for Spring Boot JPA.
     */
    public AssistantEntity() {
    }

    public AssistantEntity(String firstName, String lastName, String streetAndNumber, Integer postalCode, String city, String country, String phoneNumber, String email, List<Messenger> messengers, String message) {
        super(firstName, lastName, streetAndNumber, postalCode, city, country, phoneNumber, email, messengers, message);
    }

    public List<SeekerEntity> getSeekerEntities() {
        return seekerEntities;
    }

    public void setSeekerEntities(List<SeekerEntity> seekerEntities) {
        this.seekerEntities = seekerEntities;
    }

    public String toString() {
        return super.toString() + ", additional properties of AssistantEntity: {"
                + "assistantId: " + assistantId
                + "}";
        // TODO do we want to output the matches?
    }
}
