package de.herrschinghilft.application.development;

import java.util.UUID;

public class UserAccountEntity {

    private String username;
    private String passwordHash;
    private String passwordSalt;
    private boolean isVerified;
    private UUID verificationToken;

    public UserAccountEntity(String username, String passwordHash, String passwordSalt, boolean isVerified, UUID verificationToken) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
        this.isVerified = isVerified;
        this.verificationToken = verificationToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    /**
     * Generates the hash from the password provided and a newly generated salt.
     * Sets {@link #passwordHash} and {@link #passwordSalt} accordingly.
     *
     * @param password The password to be hashed
     */
    public void setPassword(String password) {
        // TODO PBKDF2 with SHA-256 and 32-byte salt
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public UUID getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(UUID verificationToken) {
        this.verificationToken = verificationToken;
    }
}
