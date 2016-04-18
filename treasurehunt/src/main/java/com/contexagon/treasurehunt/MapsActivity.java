package com.contexagon.treasurehunt;

/**
 * Created by ankaufma on 02.03.2016.
 */
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.contexagon.treasurehunt.controller.Controller;
import com.contexagon.treasurehunt.model.siteaggregat.areas.Area;
import com.contexagon.treasurehunt.model.siteaggregat.areas.BeaconArea;
import com.contexagon.treasurehunt.model.siteaggregat.beacons.Beacon;
import com.contexagon.treasurehunt.model.siteaggregat.Site;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        Site site = Controller.getInstance(this).getCurrentSite();
        List<Beacon> beacons = site.getBeacons();

        for(Beacon beacon: beacons) {
            mMap.addMarker(new MarkerOptions()
                    .position(
                            new LatLng(
                                    Double.valueOf(beacon.getCoordinates().getLat()),
                                    Double.valueOf(beacon.getCoordinates().getLon())))
                    .title(beacon.getAlias()));
            for (Area area : site.getAreas()) {
                for (BeaconArea ba : area.getBeaconArea()) {
                    if (ba.getBeaconId().equals(beacon.getId())) {
                        Circle ci = mMap.addCircle(new CircleOptions()
                                .radius(Integer.valueOf(ba.getRadius()))
                                .center(new LatLng(
                                        Double.valueOf(beacon.getCoordinates().getLat()),
                                        Double.valueOf(beacon.getCoordinates().getLon())))
                                .strokeColor(Color.BLUE)
                        );
                    }
                }
            }
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.valueOf(beacons.get(0).getCoordinates().getLat()),Double.valueOf(beacons.get(0).getCoordinates().getLon()))));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(20));
    }
}

