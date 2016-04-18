package com.contexagon.treasurehunt.model.siteaggregat.games;

import java.util.List;

/**
 * Created by ankaufma on 03.02.2016.
 */
public class Game {
    private final String description;
    private final String id;
    private final String isFreeGame;
    private final String itemsToCount;
    private final String siteId;
    private final String title;
    private final String type;
    private final List<Waypoint> waypoints;
    private final String estimatedTime;

    public Game(String description, String id, String isFreeGame, String itemsToCount, String siteId, String title, String type, List<Waypoint> waypoints, String estimatedTime) {
        this.description = description;
        this.id = id;
        this.isFreeGame = isFreeGame;
        this.itemsToCount = itemsToCount;
        this.siteId = siteId;
        this.title = title;
        this.type = type;
        this.waypoints = waypoints;
        this.estimatedTime = estimatedTime;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getIsFreeGame() {
        return isFreeGame;
    }

    public String getiTemsToCount() {
        return itemsToCount;
    }

    public String getSiteId() {
        return siteId;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }
}
