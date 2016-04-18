package com.contexagon.treasurehunt.model.playGame;

import com.contexagon.treasurehunt.model.siteaggregat.games.Waypoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ankaufma on 16.02.2016.
 */
public class PlayGame implements Comparable<PlayGame> {

    private String id;
    private String type;
    private String title;
    private String playerName;
    private List<PlayGameWaypoint> playGameWaypoints;
    private String estimatedTime;
    private String endTime;
    private String startTime;
    private int points = 0;

    public PlayGame(String id, String type, String title, String playerName, List<PlayGameWaypoint> playGameWaypoints, String estimatedTime) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.playerName = playerName;
        this.playGameWaypoints = playGameWaypoints;
        this.estimatedTime = estimatedTime;
    }

    public static List<PlayGameWaypoint> toPlayGameWaypoints(List<Waypoint> wps) {
        List<PlayGameWaypoint> pgm = new ArrayList<PlayGameWaypoint>();
        for(Waypoint wp: wps) {
            pgm.add(new PlayGameWaypoint(wp.getAreaId(), wp.getHints(), wp.getId(), wp.getImages(), wp.getItem(), wp.getQuestions()));
        }
        return pgm;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String id) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String name) {
        this.playerName = playerName;
    }

    public List<PlayGameWaypoint> getPlayGameWaypoints() {
        return playGameWaypoints;
    }

    public void setWaypoints(List<PlayGameWaypoint> playGameWaypoints) {
        this.playGameWaypoints = playGameWaypoints;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public PlayGame setEndTime(String endTime) {
        this.endTime = endTime;
        return this;
    }

    public String getStartTime() {
        return startTime;
    }

    public PlayGame setStartTime(String startTime) {
        this.startTime = startTime;
        return this;
    }

    public int getPoints() {
        return points;
    }

    public PlayGame setPoints(int points) {
        this.points = points;
        return this;
    }

    @Override
    public int compareTo(PlayGame another) {
        return another.points - this.points;
    }
}
