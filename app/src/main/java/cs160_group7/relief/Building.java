package cs160_group7.relief;

import java.util.List;

public class Building {
    double rating;
    double cleanliness;
    double latitude;
    double longitude;
    String details;
    String name;
    String openHours;
    List<String> reviews;
    List<Double> ratings;
    List<Double> cleanlinessRatings;

    public Building(double lat, double lon, String det, String name_){

        latitude = lat;
        longitude = lon;
        details = det;
        name = name_;
    }

}
