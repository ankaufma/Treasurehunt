package com.contexagon.treasurehunt.model.siteaggregat.areas;

import com.contexagon.treasurehunt.model.siteaggregat.Site;
import com.contexagon.treasurehunt.model.siteaggregat.beacons.Beacon;

/**
 * Created by ankaufma on 15.02.2016.
 */
public class BeaconArea {
    private final String beaconID;
    private final String radius;

    public BeaconArea(String beaconID, String radius) {
        this.beaconID = beaconID;
        this.radius = radius;
    }

    public String getBeaconId() {
        return beaconID;
    }

    public String getRadius() {
        return radius;
    }

    public Beacon getBeaconById(Site site) {
        for(Beacon searchBeacon: site.getBeacons())
            if (searchBeacon.getId() == beaconID)
                return searchBeacon;
        return null;
    }
}
