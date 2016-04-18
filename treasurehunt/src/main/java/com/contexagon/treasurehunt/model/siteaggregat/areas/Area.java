package com.contexagon.treasurehunt.model.siteaggregat.areas;

import java.util.List;

/**
 * Created by ankaufma on 15.02.2016.
 */
public class Area {

    private final List<BeaconArea> beaconArea;
    private final List<Coordinates> coordinates;
    private final String description;
    private final String id;
    private final String name;
    private final String siteId;
    private final String type;

    public Area(List<BeaconArea> beaconArea, List<Coordinates> coordinates, String description, String id, String name, String siteId, String type) {
        this.beaconArea = beaconArea;
        this.coordinates = coordinates;
        this.description = description;
        this.id = id;
        this.name = name;
        this.siteId = siteId;
        this.type = type;
    }

    public List<BeaconArea> getBeaconArea() {
        return beaconArea;
    }

    public List<Coordinates> getCoordinates() {
        return coordinates;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSiteId() {
        return siteId;
    }

    public String getType() {
        return type;
    }
}
