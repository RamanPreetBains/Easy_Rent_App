package com.example.easyrent.activity;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.easyrent.R;
import com.example.easyrent.directionhelpers.FetchURL;
import com.example.easyrent.directionhelpers.TaskLoadedCallback;
import com.example.easyrent.extra.SessionData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback {

    private GoogleMap mMap;
    private MarkerOptions place1, place2;

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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        place1 = new MarkerOptions().position(new LatLng(SessionData.I.rideDetailsData.getOrigin().latitude, SessionData.I.rideDetailsData.getOrigin().longitude)).title("Home");
        place2 = new MarkerOptions().position(new LatLng(SessionData.I.rideDetailsData.getDestination().latitude, SessionData.I.rideDetailsData.getDestination().longitude)).title(SessionData.I.rideDetailsData.getDestinationAddress());
        mMap.addMarker(place1);
        mMap.addMarker(place2);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(SessionData.I.rideDetailsData.getOrigin().latitude, SessionData.I.rideDetailsData.getOrigin().longitude), 50));
        new FetchURL(MapsActivity.this).execute(getUrl(SessionData.I.rideDetailsData.getOriginAddress(), SessionData.I.rideDetailsData.getDestinationAddress(), "driving"), "driving");
    }

    private String getUrl(String origin, String destination, String directionMode) {
        String str_origin = "origin=" + origin;
        String str_dest = "destination=" + destination;
        String mode = "mode=" + directionMode;
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        String output = "json";
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
    }

    @Override
    public void onTaskDone(Object... values) {
        mMap.addPolyline((PolylineOptions) values[0]);
    }
}