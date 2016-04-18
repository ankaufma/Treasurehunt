package com.contexagon.treasurehunt.database;

import android.content.Context;
import android.util.Log;
import com.contexagon.treasurehunt.R;
import com.contexagon.treasurehunt.controller.Controller;
import com.contexagon.treasurehunt.model.siteaggregat.areas.Area;
import com.contexagon.treasurehunt.model.siteaggregat.areas.BeaconArea;
import com.contexagon.treasurehunt.model.siteaggregat.areas.Coordinates;
import com.contexagon.treasurehunt.model.siteaggregat.beacons.Beacon;
import com.contexagon.treasurehunt.model.playGame.PlayGame;
import com.contexagon.treasurehunt.model.playGame.PlayGameWaypoint;
import com.contexagon.treasurehunt.model.siteaggregat.Site;
import com.contexagon.treasurehunt.model.siteaggregat.games.*;
import com.contexagon.treasurehunt.util.SiteLogger;
import com.couchbase.lite.*;
import com.couchbase.lite.android.AndroidContext;
import com.couchbase.lite.replicator.Replication;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created by ankaufma on 23.02.2016.
 */
public class CouchbaseLite {

    private Context context;
    private Manager manager = null;
    private Database database = null;
    public static Document document = null;
    public static final String DB_NAME = "treasurehunt";
    public static final String TAG = "Couchbase";

    public CouchbaseLite(Context context) {
        this.context = context;
        try {
            manager = getManagerInstance();
            database = getDatabaseInstance();
        } catch(IOException e) {
            Log.d(TAG, e.getMessage());
        } catch(CouchbaseLiteException e) {
            Log.d(TAG, e.getMessage());
        }
        startReplications();
        initSiteModel();
    }

    public Database getDatabaseInstance() throws CouchbaseLiteException {
        if ((this.database == null) & (this.manager != null)) {
            this.database = manager.getDatabase(DB_NAME);
        }
        return database;
    }

    public Manager getManagerInstance() throws IOException {
        if (manager == null) {
            manager = new Manager(new AndroidContext(context), Manager.DEFAULT_OPTIONS);
        }
        return manager;
    }

    public Document getDocumentInstance() {
        if (document == null) {
            document = database.getDocument(context.getResources().getString(com.contexagon.treasurehunt.R.string.thisSite));
        }
        return document;
    }

    private URL createSyncURL(boolean isEncrypted) {
        URL syncURL = null;
        String host = "http://192.168.2.27";
        String port = "4984";
        String dbName = "guide";
        try {
            syncURL = new URL(host + ":" + port + "/" + dbName);
        } catch (MalformedURLException me) {
            me.printStackTrace();
        }
        Log.d(TAG, syncURL.toString());
        return syncURL;
    }

    private void startReplications() {
            Replication pull = database.createPullReplication(this.createSyncURL(false));
            Replication push = database.createPushReplication(this.createSyncURL(false));
            pull.setContinuous(true);
            push.setContinuous(true);
            pull.start();
            push.start();
    }

    public void initSiteModel() {
        Site site = null;
        String docId = context.getResources().getString(R.string.thisSite);
        this.document = getDocumentInstance();
        String siteDesc = database.getDocument(docId).getProperty("description").toString();
        String siteId = database.getDocument(docId).getProperty("id").toString();
        String siteName = database.getDocument(docId).getProperty("name").toString();
        String siteType = database.getDocument(docId).getProperty("type").toString();
        ArrayList<Area> areaList = new ArrayList<Area>();
        ArrayList<Object> areas = (ArrayList<Object>) database.getDocument(docId).getProperty("areas");
        for (Object areamap : areas) {
            HashMap<String, Object> area = (HashMap<String, Object>) areamap;
            String adescription = area.get("description").toString();
            String aid = area.get("id").toString();
            String name = area.get("name").toString();
            String aSiteId = area.get("siteId").toString();
            String type = area.get("type").toString();
            ArrayList<BeaconArea> beaconAreaList = new ArrayList<BeaconArea>();
            List<Object> beaconAreas = (ArrayList<Object>) area.get("beaconArea");
            for (Object beaconAreaMap : beaconAreas) {
                HashMap<String, Object> beaconArea = (HashMap<String, Object>) beaconAreaMap;
                String beaconId = beaconArea.get("beaconID").toString();
                int radius = Integer.valueOf(beaconArea.get("radius").toString());
                beaconAreaList.add(new BeaconArea(beaconId, String.valueOf(radius)));
            }
            ArrayList<Coordinates> coordinateList = new ArrayList<Coordinates>();
            List<Object> coordinates = (ArrayList<Object>) area.get("coordinates");
            for (Object coordinateMap : coordinates) {
                HashMap<String, Object> coordinate = (HashMap<String, Object>) coordinateMap;
                String latitude = coordinate.get("lat").toString();
                String longitude = coordinate.get("lon").toString();
                coordinateList.add(new Coordinates(latitude, longitude));
            }
            areaList.add(new Area(beaconAreaList, coordinateList, adescription, aid, name, aSiteId, type));
        }
        ArrayList<Beacon> beaconList = new ArrayList<Beacon>();
        ArrayList<Object> beacons = (ArrayList<Object>) database.getDocument(docId).getProperty("beacons");
        for (Object beaconmap : beacons) {
            HashMap<String, Object> beacon = (HashMap<String, Object>) beaconmap;
            String alias = beacon.get("alias").toString();
            String bdescription = beacon.get("description").toString();
            String bid = beacon.get("id").toString();
            String major = beacon.get("major").toString();
            String minor = beacon.get("minor").toString();
            String siteIdBeacon = beacon.get("siteId").toString();
            String btype = beacon.get("type").toString();
            String uuid = beacon.get("uuid").toString();
            HashMap<String, Object> coordinateB = (HashMap<String, Object>) beacon.get("coordinates");
            String latitudeB = "";
            String longitudeB = "";
            if(coordinateB != null) {
                latitudeB = coordinateB.get("lat").toString();
                longitudeB = coordinateB.get("lon").toString();
            }
            beaconList.add(new Beacon(alias, new Coordinates(latitudeB, longitudeB), bdescription, bid, major, minor, siteIdBeacon, btype, uuid));
        }
        ArrayList<Game> gamesList = new ArrayList<Game>();
        ArrayList<Object> games = (ArrayList<Object>) database.getDocument(docId).getProperty("games");
        for (Object gamemap : games) {
            HashMap<String, Object> game = (HashMap<String, Object>) gamemap;
            String gdescription = game.get("description").toString();
            String estimatedTime = game.get("estimatedTime").toString();
            String gid = game.get("id").toString();
            Boolean isFreeGame = Boolean.valueOf(game.get("isFreeGame").toString());
            int itemsToCount = Integer.valueOf(game.get("itemsToCount").toString());
            String siteIdGame = game.get("siteId").toString();
            String title = game.get("title").toString();
            String type = game.get("type").toString();
            ArrayList<Waypoint> waypointList = new ArrayList<Waypoint>();
            List<Object> waypoints = (ArrayList<Object>) game.get("waypoints");
            for (Object wpmap : waypoints) {
                HashMap<String, Object> wp = (HashMap<String, Object>) wpmap;
                String areaId = wp.get("areaId").toString();
                String wpId = wp.get("id").toString();
                String item = wp.get("item").toString();
                List<String> images = (ArrayList<String>) wp.get("images");
                ArrayList<Hint> hintList = new ArrayList<Hint>();
                List<Object> hints = (ArrayList<Object>) wp.get("hints");
                for (Object hintmap : hints) {
                    HashMap<String, Object> hint = (HashMap<String, Object>) hintmap;
                    String hintdescription = hint.get("description").toString();
                    String hid = hint.get("id").toString();
                    hintList.add(new Hint(hintdescription, hid));
                }
                ArrayList<Question> questionList = new ArrayList<Question>();
                List<Object> questions = (ArrayList<Object>) wp.get("question");
                for (Object qmap : questions) {
                    HashMap<String, Object> question = (HashMap<String, Object>) qmap;
                    String quest = question.get("question").toString();
                    String qid = question.get("id").toString();
                    ArrayList<Answer> answerList = new ArrayList<Answer>();
                    List<Object> answers = (ArrayList<Object>) question.get("answers");
                    for (Object awmap : answers) {
                        HashMap<String, Object> aw = (HashMap<String, Object>) awmap;
                        String answer = aw.get("answer").toString();
                        String aid = aw.get("id").toString();
                        Boolean isTrue = Boolean.valueOf(aw.get("isTrue").toString());
                        answerList.add(new Answer(answer, aid, String.valueOf(isTrue)));
                    }
                    questionList.add(new Question(answerList, qid, quest));
                }
                waypointList.add(new Waypoint(areaId, hintList, wpId, images, item, questionList));
            }
            gamesList.add(new Game(gdescription, gid, String.valueOf(isFreeGame), String.valueOf(itemsToCount), siteIdGame, title, type, waypointList, estimatedTime));
        }
        site = new Site("0", "0", areaList, beaconList, siteDesc, gamesList, siteId, siteName, siteType);
        SiteLogger.logSite(site);
        for(PlayGame pgm: getPlayedGamesSortedByPoints()) {
            Log.d("PlayGame:", pgm.getType() + " " + pgm.getPlayerName() +  " " + pgm.getPoints());
        }
        Controller.getInstance(context).setSiteModel(site);
    }

    public List<PlayGame> getPlayedGamesSortedByPoints() {
        List<PlayGame> pgms = new ArrayList<PlayGame>();
        Query query = database.createAllDocumentsQuery();
        query.setAllDocsMode(Query.AllDocsMode.ALL_DOCS);
        QueryEnumerator result = null;
        try {
            result = query.run();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        for (Iterator<QueryRow> it = result; it.hasNext(); ) {
            QueryRow row = it.next();
            if(row.getDocumentId().matches("PlayGame.*")) {
                Document doc = database.getDocument(row.getDocumentId());
                String id = (String) doc.getProperty("id");
                String type = (String) doc.getProperty("type");
                String title = (String) doc.getProperty("title");
                String name = (String) doc.getProperty("name");
                List<PlayGameWaypoint> waypoints = new ArrayList<PlayGameWaypoint>();//(String) doc.getProperty("waypoints");
                String endTime = (String) doc.getProperty("endTime");
                String estimatedTime = (String) doc.getProperty("estimatedTime");
                String date = (String) doc.getProperty("date");
                int points = Integer.valueOf(String.valueOf(doc.getProperty("points")));
                pgms.add(
                        new PlayGame(id, type, title, name, waypoints, estimatedTime).setEndTime(endTime).setPoints(points).setStartTime(date)
                );
            }
        }
        Collections.sort(pgms);
        return pgms;
    }

    public List<PlayGame> queryPlayGames(String gameTitle) {
        List<PlayGame> pgms = new ArrayList<PlayGame>();
        createPlayGameView(gameTitle);
        Query playGameQuery = database.getView("PlayGameView").createQuery();
        QueryEnumerator result = null;
        try {
            result = playGameQuery.run();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        for (Iterator<QueryRow> it = result; it.hasNext(); ) {
            QueryRow row = it.next();
            Document doc = database.getDocument(row.getDocumentId());
            String id = (String) doc.getProperty("id");
            String type = (String) doc.getProperty("type");
            String title = (String) doc.getProperty("title");
            String name = (String) doc.getProperty("name");
            List<PlayGameWaypoint> waypoints = new ArrayList<PlayGameWaypoint>();//(String) doc.getProperty("waypoints");
            String endTime = (String) doc.getProperty("endTime");
            String estimatedTime = (String) doc.getProperty("estimatedTime");
            String date = (String) doc.getProperty("date");
            int points = Integer.valueOf(String.valueOf(doc.getProperty("points")));
            pgms.add(
                    new PlayGame(id, type, title, name, waypoints, estimatedTime).setEndTime(endTime).setPoints(points).setStartTime(date)
            );
        }
        Collections.sort(pgms);
        Log.d("PLAYGAME SIZE", String.valueOf(pgms.size()));
        return pgms;
    }

    public View createPlayGameView(final String gameTitle) {
        database.deleteViewNamed("PlayGameView");
        View streamView = database.getView("PlayGameView");
        streamView.setMap(new Mapper() {
            @Override
            public void map(Map<String, Object> document, Emitter emitter) {
                if(document.get("type").equals("PlayGame")
                        && document.get("title").equals(gameTitle))
                    emitter.emit(document.get("date"), null);
            }
        },"1");
        return streamView;
    }

}
