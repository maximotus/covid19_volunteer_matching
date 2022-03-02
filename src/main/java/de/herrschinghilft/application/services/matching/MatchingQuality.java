package de.herrschinghilft.application.services.matching;

/**
 * Describes the quality of a matching.
 * The higher the {@link #quality}, the better the matching.
 * All quality levels include the levels below them.
 */
public enum MatchingQuality {
    NONE(0),
    CITY(1),
    MESSENGER(2),
    PREFERRED_MESSENGER(3);

    private final int quality;

    MatchingQuality(int quality) {
        this.quality = quality;
    }

    public int getQuality() {
        return quality;
    }

    public boolean isBetterThan(MatchingQuality other) {
        return this.quality > other.quality;
    }

    public boolean isBetterOrEqualThan(MatchingQuality other) {
        return this.quality >= other.quality;
    }

    public boolean isWorseThan(MatchingQuality other) {
        return this.quality < other.quality;
    }

    public boolean isWorseOrEqualThan(MatchingQuality other) {
        return this.quality <= other.quality;
    }
}
