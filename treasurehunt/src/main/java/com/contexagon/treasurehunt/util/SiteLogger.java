package com.contexagon.treasurehunt.util;

import android.util.Log;
import com.contexagon.treasurehunt.model.siteaggregat.areas.Area;
import com.contexagon.treasurehunt.model.siteaggregat.areas.BeaconArea;
import com.contexagon.treasurehunt.model.siteaggregat.areas.Coordinates;
import com.contexagon.treasurehunt.model.siteaggregat.beacons.Beacon;
import com.contexagon.treasurehunt.model.siteaggregat.Site;
import com.contexagon.treasurehunt.model.siteaggregat.games.*;

/**
 * Created by ankaufma on 15.02.2016.
 */
public class SiteLogger {

    private static final String SITE = "SITE";
    private static final String AREA = "AREA";
    private static final String BEACONAREA = "BEACONAREA";
    private static final String COORDINATES = "COORDINATES";
    private static final String BEACON = "BEACON";
    private static final String GAME = "GAME";
    private static final String WAYPOINT = "WAYPOINT";
    private static final String HINT = "HINT";
    private static final String QUESTION = "QUESTION";
    private static final String ANSWER = "ANSWER";

    public static void logSite(Site site) {
        if(checkIfNull(site.get_id())) Log.d(SITE, site.get_id());
        if(checkIfNull(site.get_rev())) Log.d(SITE, site.get_rev());
        if(checkIfNull(site.getDescription())) Log.d(SITE, site.getDescription());
        if(checkIfNull(site.getId())) Log.d(SITE, site.getId());
        if(checkIfNull(site.getName())) Log.d(SITE, site.getName());
        if(checkIfNull(site.getType())) Log.d(SITE, site.getType());
        for(Area area: site.getAreas()) {
            if(checkIfNull(area.getDescription())) Log.d(AREA, area.getDescription());
            if(checkIfNull(area.getId())) Log.d(AREA, area.getId());
            if(checkIfNull(area.getName())) Log.d(AREA, area.getName());
            if(checkIfNull(area.getSiteId())) Log.d(AREA, area.getSiteId());
            if(checkIfNull(area.getType())) Log.d(AREA, area.getType());
            if(area.getBeaconArea() != null) {
                for (BeaconArea ba : area.getBeaconArea()) {
                    if (ba.getBeaconId() != null) {
                        if(checkIfNull(ba.getBeaconId())) Log.d(BEACONAREA, ba.getBeaconId());
                        if(checkIfNull(ba.getRadius()))  Log.d(BEACONAREA, ba.getRadius());
                    }
                }
            }
            for(Coordinates coordinate: area.getCoordinates()){
                if(checkIfNull(coordinate.getLat())) Log.d(COORDINATES, coordinate.getLat());
                if(checkIfNull(coordinate.getLon())) Log.d(COORDINATES, coordinate.getLon());
            }
        }
        for(Beacon beacon: site.getBeacons()) {
            if(checkIfNull(beacon.getAlias())) Log.d(BEACON, beacon.getAlias());
            if(checkIfNull(beacon.getDescription())) Log.d(BEACON, beacon.getDescription());
            if(checkIfNull(beacon.getId())) Log.d(BEACON, beacon.getId());
            if(checkIfNull(beacon.getMajor())) Log.d(BEACON, beacon.getMajor());
            if(checkIfNull(beacon.getMinor())) Log.d(BEACON, beacon.getMinor());
            if(checkIfNull(beacon.getSiteId())) Log.d(BEACON, beacon.getSiteId());
            if(checkIfNull(beacon.getType())) Log.d(BEACON, beacon.getType());
            if(checkIfNull(beacon.getUuid())) Log.d(BEACON, beacon.getUuid());
            if(checkIfNull(beacon.getCoordinates().getLat())) Log.d(BEACON, beacon.getCoordinates().getLat() + " " + beacon.getCoordinates().getLon());
        }
        for(Game game: site.getGames()) {
            if(checkIfNull(game.getDescription()))  Log.d(GAME, game.getDescription());
            if(checkIfNull(game.getEstimatedTime()))  Log.d(GAME, game.getEstimatedTime());
            if(checkIfNull(game.getId()))  Log.d(GAME, game.getId());
            if(checkIfNull(game.getIsFreeGame()))   Log.d(GAME, game.getIsFreeGame());
            if(checkIfNull(game.getiTemsToCount()))  Log.d(GAME, game.getiTemsToCount());
            if(checkIfNull(game.getSiteId()))  Log.d(GAME, game.getSiteId());
            if(checkIfNull(game.getTitle()))  Log.d(GAME, game.getTitle());
            if(checkIfNull(game.getType()))  Log.d(GAME, game.getType());
            for(Waypoint wp: game.getWaypoints()) {
                if(checkIfNull(wp.getAreaId()))  Log.d(WAYPOINT, wp.getAreaId());
                if(checkIfNull(wp.getId()))  Log.d(WAYPOINT, wp.getId());
                if(wp.getImages()!=null && wp.getImages().size() == 2) {
                    if (checkIfNull(wp.getImages().get(0))) Log.d(WAYPOINT, wp.getImages().get(0));
                    if (checkIfNull(wp.getImages().get(1))) Log.d(WAYPOINT, wp.getImages().get(1));
                }
                if(checkIfNull(wp.getItem()))  Log.d(WAYPOINT, wp.getItem());
                for(Hint hint: wp.getHints()) {
                    if(checkIfNull(hint.getDescription()))  Log.d(HINT, hint.getDescription());
                    if(checkIfNull(hint.getId())) Log.d(HINT, hint.getId());
                }
                if(wp.getQuestions()!=null) {
                    for (Question q : wp.getQuestions()) {
                        if (checkIfNull(q.getId())) Log.d(QUESTION, q.getId());
                        if (checkIfNull(q.getQuestion())) Log.d(QUESTION, q.getQuestion());
                        for (Answer aw : q.getAnswers()) {
                            if (checkIfNull(aw.getId())) Log.d(ANSWER, aw.getId());
                            if (checkIfNull(aw.getAnswer())) Log.d(ANSWER, aw.getAnswer());
                            if (checkIfNull(aw.getIsTrue())) Log.d(ANSWER, aw.getIsTrue());
                        }
                    }
                }
            }
        }
    }

    private static boolean checkIfNull(String s) {
        boolean a = true;
        if(s==null) {
            a=false;
        }
        return a;
    }
}
