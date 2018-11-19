package com.example.mharris.relief;


public class Building {
    double rating;
    double cleanliness;
    double latitude;
    double longitude;
    String details;
    String name;

    public Building(double lat, double lon, String det, String name_){

        latitude = lat;
        longitude = lon;
        details = det;
        name = name_;
    }

}
