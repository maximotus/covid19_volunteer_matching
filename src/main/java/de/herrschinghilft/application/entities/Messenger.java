package de.herrschinghilft.application.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class represents the different kinds of messengers / communication possibilities.
 * Important: We currently use the String value mapping of Spring Boot JPA.
 * That means, if we change the name of an enum value (e.g. EMAIL to E-MAIL), we need to update the whole database manually.
 * Otherwise we will have some E-MAIL and EMAIL in the database and JPA will not work with EMAIL anymore.
 * <p>
 * Here are some more options to map an enum with Spring Boot JPA:
 * https://www.baeldung.com/jpa-persisting-enums-in-jpa
 * <p>
 * Thinking in long terms it would be much better to write a converter (@ConverterAnnotation)!!!
 * But for the startup-straight-forward-thinking this implementation works fine.
 */
public enum Messenger {
    @JsonProperty("E-Mail")
    EMAIL("E-Mail"),

    @JsonProperty("Telefon")
    PHONE("Telefon"),

    @JsonProperty("SMS")
    SMS("SMS"),

    @JsonProperty("WhatsApp")
    WHATSAPP("WhatsApp"),

    @JsonProperty("Facebook")
    FACEBOOK("Facebook"),

    @JsonProperty("Telegram")
    TELEGRAM("Telegram"),

    @JsonProperty("Threema")
    THREEMA("Threema"),

    @JsonProperty("Signal")
    SIGNAL("Signal");

    private final String displayName;

    Messenger(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return this.name() + " (\"" + this.displayName + "\")";
    }

    public String getDisplayName() {
        return displayName;
    }
}
