package com.contexagon.treasurehunt.model.siteaggregat.beacons;

import com.contexagon.treasurehunt.model.siteaggregat.areas.Coordinates;

/**
 * Created by ankaufma on 15.02.2016.
 */
public class Beacon {

    private final String alias;
    private final Coordinates coordinates;
    private final String description;
    private final String id;
    private final String major;
    private final String minor;
    private final String siteId;
    private final String type;
    private final String uuid;

    public Beacon(String alias, Coordinates coordinates, String description, String id, String major, String minor, String siteId, String type, String uuid) {
        this.alias = alias;
        this.coordinates = coordinates;
        this.description = description;
        this.id = id;
        this.major = major;
        this.minor = minor;
        this.siteId = siteId;
        this.type = type;
        this.uuid = uuid;
    }

    public String getAlias() {
        return alias;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getMajor() {
        return major;
    }

    public String getMinor() {
        return minor;
    }

    public String getSiteId() {
        return siteId;
    }

    public String getType() {
        return type;
    }

    public String getUuid() {
        return uuid;
    }
}
