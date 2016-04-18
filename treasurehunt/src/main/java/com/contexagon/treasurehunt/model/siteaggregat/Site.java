package com.contexagon.treasurehunt.model.siteaggregat;

import com.contexagon.treasurehunt.model.siteaggregat.areas.Area;
import com.contexagon.treasurehunt.model.siteaggregat.beacons.Beacon;
import com.contexagon.treasurehunt.model.siteaggregat.games.Game;

import java.util.List;

/**
 * Created by ankaufma on 15.02.2016.
 */
public class Site {

    private final String _id;
    private final String _rev;
    private final List<Area> areas;
    private final List<Beacon> beacons;
    private final String description;
    private final List<Game> games;
    private final String id;
    private final String name;
    private final String type;

    public Site(String _id, String _rev, List<Area> areas, List<Beacon> beacons, String description, List<Game> games, String id, String name, String type) {
        this._id = _id;
        this._rev = _rev;
        this.areas = areas;
        this.beacons = beacons;
        this.description = description;
        this.games = games;
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public String get_id() {
        return _id;
    }

    public String get_rev() {
        return _rev;
    }

    public List<Area> getAreas() {
        return areas;
    }

    public List<Beacon> getBeacons() {
        return beacons;
    }

    public String getDescription() {
        return description;
    }

    public List<Game> getGames() {
        return games;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Area getAreaById(String areaId) {
        Area searchArea = null;
        for(Area findArea: areas) {
            if(findArea.getId().equals(areaId)) {
                searchArea = findArea;
            }
        }
        return searchArea;
    }
}
