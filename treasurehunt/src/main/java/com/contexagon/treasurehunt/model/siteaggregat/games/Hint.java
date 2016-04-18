package com.contexagon.treasurehunt.model.siteaggregat.games;

/**
 * Created by ankaufma on 03.02.2016.
 */
public class Hint {
    private final String description;
    private final  String id;

    public Hint(String description, String id) {
        this.description = description;
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

}
