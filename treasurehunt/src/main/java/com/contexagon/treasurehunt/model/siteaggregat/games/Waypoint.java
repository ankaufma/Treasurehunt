package com.contexagon.treasurehunt.model.siteaggregat.games;

import java.util.List;

/**
 * Created by ankaufma on 03.02.2016.
 */
public class Waypoint {

    private final String areaId;
    private final List<Hint> hints;
    private final String id;
    private final List<String> images;
    private final String item;
    private final List<Question> questions;

    public Waypoint(String areaId, List<Hint> hints, String id, List<String> images, String item, List<Question> questions) {
        this.areaId = areaId;
        this.hints = hints;
        this.id = id;
        this.images = images;
        this.item = item;
        this.questions = questions;
    }

    public String getAreaId() {
        return areaId;
    }

    public List<Hint> getHints() {
        return hints;
    }

    public String getId() {
        return id;
    }

    public List<String> getImages() {
        return images;
    }

    public String getItem() {
        return item;
    }

    public List<Question> getQuestions() {
        return questions;
    }
}
