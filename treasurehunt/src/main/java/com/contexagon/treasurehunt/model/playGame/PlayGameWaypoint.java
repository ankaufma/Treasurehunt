package com.contexagon.treasurehunt.model.playGame;

import com.contexagon.treasurehunt.model.siteaggregat.Site;
import com.contexagon.treasurehunt.model.siteaggregat.areas.Area;
import com.contexagon.treasurehunt.model.siteaggregat.areas.BeaconArea;
import com.contexagon.treasurehunt.model.siteaggregat.games.Hint;
import com.contexagon.treasurehunt.model.siteaggregat.games.Question;
import com.contexagon.treasurehunt.model.siteaggregat.games.Waypoint;

import java.util.List;

/**
 * Created by ankaufma on 17.02.2016.
 */
public class PlayGameWaypoint extends Waypoint {
    private boolean reached = false;

    public PlayGameWaypoint(String areaId, List<Hint> hints, String id, List<String> images, String item, List<Question> questions) {
        super(areaId, hints, id, images, item, questions);
    }

    public boolean isReached() {
        return reached;
    }

    public PlayGameWaypoint setReached(boolean reached) {
        this.reached = reached;
        return this;
    }

    public List<BeaconArea> getBeaconArea(Site site) {
        return site.getAreaById(getAreaId()).getBeaconArea();
    }
}
