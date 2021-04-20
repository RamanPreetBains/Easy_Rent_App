package com.example.easyrent.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.easyrent.R;
import com.example.easyrent.activity.RideDetailsActivity;
import com.example.easyrent.extra.GeoCodeParser;
import com.example.easyrent.extra.LocationTrack;
import com.example.easyrent.extra.RideDetailsData;
import com.example.easyrent.extra.SessionData;
import com.example.easyrent.extra.geocodeAdressHelper.GeoCodeResponseData;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class HomeFragment extends Fragment {

    private final String apiKey = "AIzaSyAZiDnD1WmSlA3LfjFrKlcEDx73Ievm1u4";
    private EditText placeName;
    private PlacesClient placesClient;
    private LocationTrack locationTrack;
    private Gson gson;
    private Button btnSearchRide;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
//      Initialize the SDK
        Places.initialize(getActivity(), apiKey);
        // Create a new PlacesClient instance
        placesClient = Places.createClient(getActivity());
        btnSearchRide = root.findViewById(R.id.btn_search_ride);
        placeName = root.findViewById(R.id.et_postal_code);
        placeName.setEnabled(false);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        //Get current location
        locationTrack = new LocationTrack(getActivity());

//      Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS_COMPONENTS));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onPlaceSelected(@NotNull Place place) {
                // TODO: Get info about the selected place.
                String address = place.getName() + "\n" + place.getAddress();
                LatLng currentLatLng = new LatLng(locationTrack.getLatitude(), locationTrack.getLongitude());
                getGeoLocationAddress(currentLatLng, place);
                placeName.setText(address);
            }


            @Override
            public void onError(@NotNull Status status) {
                // TODO: Handle the error.
                Log.i("TAG", "An error occurred: " + status);
            }
        });

        btnSearchRide.setOnClickListener(v -> {

            if(placeName.getText().toString().equals("")){
                Toast.makeText(getActivity(), "Select any place before proceeding.", Toast.LENGTH_SHORT).show();
                return;
            }
            SessionData.I.goTo(getActivity(), RideDetailsActivity.class);
        });

        return root;
    }

    private void getGeoLocationAddress(LatLng currentLatLng, Place place) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + currentLatLng.latitude + ",%20" + currentLatLng.longitude + "&key=" + getResources().getString(R.string.google_maps_key);
        new GeoCodeParser(getActivity(), data -> {
            SessionData.I.localData.geoCodeResponseData = new Gson().fromJson(data, GeoCodeResponseData.class);
            SessionData.I.saveLocalData();
            RideDetailsData rideDetailsData = new RideDetailsData();
            rideDetailsData.setDestination(place.getLatLng());
            rideDetailsData.setOrigin(currentLatLng);
            rideDetailsData.setDestinationAddress(place.getAddress());
            try {
                rideDetailsData.setOriginAddress(SessionData.I.localData.geoCodeResponseData.getResults().get(0).getFormattedAddress());
            } catch (IndexOutOfBoundsException e) {
                Log.e("EXCEPTION", e.getMessage());
            }
            SessionData.I.rideDetailsData = rideDetailsData;
        }).execute(url);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}