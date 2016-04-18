package com.contexagon.treasurehunt.model.siteaggregat.areas;

/**
 * Created by ankaufma on 15.02.2016.
 */
public class Coordinates {

    private final String lat;
    private final String lon;

    public Coordinates(String lat, String lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }}
