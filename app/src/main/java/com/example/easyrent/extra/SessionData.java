package com.example.easyrent.extra;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.example.easyrent.activity.RideDetailsActivity;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class SessionData {
    public static SessionData I = new SessionData();
    public static final int PREF_SAVED_DATA = 99;
    public static final int PREF_SAVED_DATA2 = 100;
    public static final int PREF_KEY_VERSION = 21;
    public RideDetailsData rideDetailsData=new RideDetailsData();
    String version = "0";
    public static final String LOCAL_DATA_VERSION = "2";

    Gson gson = new Gson();
    protected SharedPreferences preferences;
    protected String fileName;

    public LocalData localData;
    public String uniqueId = UUID.randomUUID().toString();

    private SessionData() {

    }

    public void goTo(Context context, Class<?> secondClass) {
        Intent intent = new Intent(context, secondClass);
        context.startActivity(intent);
    }

    public void init(Context context) {
        fileName = context.getPackageName() + ".prefFile";
        preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        version = readString(PREF_KEY_VERSION);
        String data = readString(PREF_SAVED_DATA2);
        Log.d("TAG", "SessionData: " + data);


        if (!version.equals(LOCAL_DATA_VERSION) || data.length() <= 1) {
            version = LOCAL_DATA_VERSION;
            localData = new LocalData();
            saveLocalData();
        } else {
            if (data.length() > 1) {
                try {
                    this.localData = (gson.fromJson(data, LocalData.class));
                } catch (Exception e) {
                    localData = new LocalData();
                }
            }
        }
    }

    public void saveLocalData() {
        writeString(PREF_KEY_VERSION, version);
        String data = gson.toJson(localData, LocalData.class);
        Log.d("TAG", "saveData: " + data);
        writeString(PREF_SAVED_DATA2, data);
    }

    public void writeString(Integer key, String value) {
        preferences.edit().putString(key.toString(), value).apply();
    }

    public String readString(Integer key) {
        switch (key) {
            case PREF_SAVED_DATA:
                return preferences.getString(key.toString(), "");
        }
        return preferences.getString(key.toString(), "");
    }

    public String encodeToBase64(Bitmap image) {
        Bitmap image1 = image;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image1.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] b = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public void setLogin(boolean isLogin) {
        preferences.edit().putBoolean("isLogin", isLogin).apply();
    }

    public boolean isLogin() {
        return preferences.getBoolean("isLogin", false);
    }

    public void clearLocalData() {
        localData = new LocalData();
        saveLocalData();
    }
}
