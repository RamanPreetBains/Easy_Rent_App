package com.example.easyrent.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.icu.text.RelativeDateTimeFormatter;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.easyrent.R;
import com.example.easyrent.extra.SessionData;

public class RideDetailsActivity extends BaseActivity {
    private EditText estimatedPrice, distance, destination;
    private Button btnConfirm, btnMap;
    private RadioButton rbSedan, rbLuxurySedan, rbSuv;
    private RadioGroup radioGroup;

    @Override

    protected int getLayoutId() {
        return R.layout.activity_ride_detail;
    }

    @Override
    protected void initViews() {
        estimatedPrice = findViewById(R.id.et_estimated_price);
        distance = findViewById(R.id.et_distance);
        destination = findViewById(R.id.et_destination);
        btnConfirm = findViewById(R.id.btn_confirm);
        btnMap = findViewById(R.id.btn_map);
        rbSedan = findViewById(R.id.rb_sedan);
        rbLuxurySedan = findViewById(R.id.rb_luxury_sedan);
        rbSuv = findViewById(R.id.rb_suv);
        radioGroup = findViewById(R.id.rg_car_category);
    }

    @Override
    protected void initViewsWithData() {


        destination.setText(SessionData.I.rideDetailsData.getDestinationAddress());

        Location startPoint = new Location("Start");
        startPoint.setLatitude(SessionData.I.rideDetailsData.getOrigin().latitude);
        startPoint.setLongitude(SessionData.I.rideDetailsData.getOrigin().longitude);

        Location endPoint = new Location("End");
        endPoint.setLatitude(SessionData.I.rideDetailsData.getDestination().latitude);
        endPoint.setLongitude(SessionData.I.rideDetailsData.getDestination().longitude);

        double distanceInKM = startPoint.distanceTo(endPoint);
        int dist = 0;
        String distanceValue = null;
        if (distanceInKM >= 1000.0) {
            dist = (int) (distanceInKM / 1000);
            distanceValue = dist + "";
            distance.setText(distanceValue + "KM");
        } else {
            dist = (int) distanceInKM;
            distanceValue = dist + "";
            distance.setText(distanceValue + "m");
        }

        int finalDist = dist;

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_sedan:
                    estimatedPrice.setText("$" + (finalDist + 50));
                    break;
                case R.id.rb_luxury_sedan:
                    estimatedPrice.setText("$" + (finalDist + 150));
                    break;
                case R.id.rb_suv:
                    estimatedPrice.setText("$" + (finalDist +100));
                    break;
            }

        });

        btnMap.setOnClickListener(v -> SessionData.I.goTo(this, MapsActivity.class));
        btnConfirm.setOnClickListener(v -> {
            if (rbSedan.isChecked() || rbLuxurySedan.isChecked() || rbSuv.isChecked()) {
                open();
            } else {
                Toast.makeText(RideDetailsActivity.this, "Please select the car category", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void open() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure, You wanted to confirm ride.");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        SessionData.I.goTo(RideDetailsActivity.this, MapsActivity.class);
                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
