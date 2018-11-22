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
        mapping_set = MapsActivity.buildingMap;
        buildingName = intent.getStringExtra("name");

        curr = mapping_set.get(buildingName);



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
        //change this later
        Intent intent = new Intent(this, DetailedView.class);
        intent.putExtra("name", buildingName);

        startActivity(intent);

//        Intent intent = new Intent(v.getContext(), DetailedView.class);
//        intent.putExtra("name", bi.name);
//        v.getContext().startActivity(intent);

    }
}
