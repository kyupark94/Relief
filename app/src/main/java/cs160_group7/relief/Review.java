package cs160_group7.relief;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.HashMap;
import android.widget.RatingBar;
import android.widget.TextView;


public class Review extends AppCompatActivity {
    protected HashMap<String, Building> hashmap;
    protected String buildingName;
    protected Building curr;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review);
        Intent intent = getIntent();
        hashmap = (HashMap<String, Building>)intent.getSerializableExtra("map");
        buildingName = intent.getStringExtra("buildingName");
//        Log.v("HashMapTest", hashMap.get("key"));
        curr = hashmap.get(buildingName);

    }

    protected void submit(){
        TextView reviewText = findViewById(R.id.reviewText);
        String review = reviewText.getText().toString();
        RatingBar ratingBarItem = findViewById(R.id.ratingBar);
        Float ratingT = ratingBarItem.getRating();
        Double rating = ratingT.doubleValue();

        curr.addReview(review);
        curr.addRating(rating);
        hashmap.put(buildingName, curr);

        System.out.println("switching activities");
        Intent intent = new Intent(this, DetailedView.class);
        intent.putExtra("buildingName", buildingName);
        intent.putExtra("map", hashmap);
        startActivity(intent);

    }
}
