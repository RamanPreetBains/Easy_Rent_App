package com.example.easyrent.activity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.easyrent.R;
import com.example.easyrent.extra.SessionData;
import com.example.easyrent.extra.UserData;
import com.example.easyrent.fragment.ProfileFragment;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends BaseActivity implements ProfileFragment.OnProfileUpdateListener {
    private AppBarConfiguration mAppBarConfiguration;
    private TextView tvUserName;
    private ImageView ivProfilePic;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_payment)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        View header = navigationView.getHeaderView(0);
        tvUserName = header.findViewById(R.id.tv_user_name);
        ivProfilePic = header.findViewById(R.id.iv_profile_pic);
    }

    @Override
    protected void initViewsWithData() {
        tvUserName.setText(SessionData.I.localData.currentUserData.getFirstName() + " " + SessionData.I.localData.currentUserData.getLastName());
        ivProfilePic.setImageBitmap(SessionData.I.decodeBase64(SessionData.I.localData.currentUserData.getProfilePic()));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_activity, menu);
        return true;
    }

    private void changelog(){
        new AlertDialog.Builder(this)
                .setTitle("Changelog")
                .setMessage("v1.1\n" +
                        "- Updated the user interface.\n" +
                        "- Updated app logo Icon.\n" +
                        "- Fixed credit card edit option not working.\n" +
                        "- Fixed crash when we don't select any pic when signing up.\n" +
                        "- Fixed crash when we want to edit the profile and select image and there is no camera permission granted on signing up.\n" +
                        "- Added sign out the profile option.\n" +
                        "- Added changelog and about option on home menu.\n"+
                        "- Fixed the crash when we click select ride when we no places selected.\n")

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.yes, null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    private void about(){
        new AlertDialog.Builder(this)
                .setTitle("About")
                .setMessage("Easy Rent v1.1\n" +
                        "Easy Rent app is very intuitive app which helps to find the ride at affordable price.\n\n" +
                        "This app is created by\nPalvi Rani C0764943\nParmjeet Kaur C0764946\nRamanpreet Kaur C0764799\n\n" +
                        "2021 © All Rights Reserved")

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.yes, null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.sign_out:
                SessionData.I.setLogin(false);
                SessionData.I.goTo(this, LoginActivity.class);
                finish();
                break;

            case R.id.changelog:
                changelog();
                break;

            case R.id.about:
                about();
                break;
        }
        return false;

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onProfileUpdate(UserData userData) {
        tvUserName.setText(userData.getFirstName() + " " + userData.getLastName());
        ivProfilePic.setImageBitmap(SessionData.I.decodeBase64(userData.getProfilePic()));
    }
}
