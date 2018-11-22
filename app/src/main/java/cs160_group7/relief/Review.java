package cs160_group7.relief;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.HashMap;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;


public class Review extends AppCompatActivity {
    protected HashMap<String, Building> mapping_set;
    protected String buildingName;
    protected Building curr;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review);
        Intent intent = getIntent();
       // hashmap = (HashMap<String, Building>)intent.getSerializableExtra("map");
        buildingName = intent.getStringExtra("hall_name");
//        curr = hashmap.get(buildingName);
        mapping_set = MapsActivity.buildingMap;
        System.out.println("reivew map: "+ mapping_set);

    }

    protected void submit(View v){
        TextView reviewText = findViewById(R.id.reviewText);
        String review = reviewText.getText().toString();
        RatingBar ratingBarItem = findViewById(R.id.ratingBar);
        Float ratingT = ratingBarItem.getRating();
        Double rating = ratingT.doubleValue();

        curr.addReview(review);
        curr.addRating(rating);
        curr.averageRatings();
        mapping_set.put(buildingName, curr);
        MapsActivity.buildingMap = mapping_set;
        System.out.println("new map!!: " +MapsActivity.buildingMap );
        //change this later
        Intent intent = new Intent(v.getContext(), DetailedView.class);
        intent.putExtra("name", buildingName);

        v.getContext().startActivity(intent);

//        Intent intent = new Intent(v.getContext(), DetailedView.class);
//        intent.putExtra("name", bi.name);
//        v.getContext().startActivity(intent);

    }
}
