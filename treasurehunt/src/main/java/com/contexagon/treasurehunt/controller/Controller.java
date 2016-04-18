package com.contexagon.treasurehunt.controller;

import android.content.Context;
import com.contexagon.treasurehunt.asyncTasks.RESTRequestAsyncTask;
import com.contexagon.treasurehunt.model.siteaggregat.beacons.Beacon;
import com.contexagon.treasurehunt.model.siteaggregat.Site;
import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;

/**
 * Created by ankaufma on 15.02.2016.
 */
public class Controller {

    private static Controller instance = null;
    private static Context mcontext;
    private static Site site = null;
    public static Context contextHolder = null;

    protected Controller(Context context) {
        mcontext = context;
    }

    public static Controller getInstance(Context mcontext) {
        if(instance == null) {
            instance = new Controller(mcontext);
        }
        return instance;
    }

    public Site setSiteModel(Site site) {
        this.site = site;
        return this.site;
    }

    public Site getSiteModelAsync(String siteId) {
        try {
            String jsonSite = new RESTRequestAsyncTask(mcontext).execute(siteId).get();
            Gson gson = new Gson();
            site = gson.fromJson(jsonSite, Site.class);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return site;
    }

    public Site getCurrentSite() { return site; }

    public static Beacon getBeaconByBeaconId(String beaconId) {
        Beacon searchBeacon = null;
        for(Beacon findBeacon: site.getBeacons()) {
            if(findBeacon.getId().equals(beaconId)) {
                searchBeacon = findBeacon;
            }
        }
        return searchBeacon;
    }
}
