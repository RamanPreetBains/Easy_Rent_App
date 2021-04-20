package com.example.easyrent.extra;


import com.example.easyrent.extra.geocodeAdressHelper.GeoCodeResponseData;

import java.util.ArrayList;

public class LocalData {
    public ArrayList<UserData> userList = new ArrayList<>();
    public UserData currentUserData = new UserData();
    public GeoCodeResponseData geoCodeResponseData=new GeoCodeResponseData();
    public CardDetailsData cardDetailsData;
}
