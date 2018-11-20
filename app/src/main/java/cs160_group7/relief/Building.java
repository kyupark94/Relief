package cs160_group7.relief;

import java.util.List;
import java.util.ArrayList;

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
        reviews = new ArrayList<String>();
        ratings = new ArrayList<Double>();
    }

    public void setRating(double r) {
        rating = r;
    }
    
    public void addReview(String rev){
        reviews.add(rev);
    }
    
    public void addRating(Double rat){
        ratings.add(rat);
    }

}
