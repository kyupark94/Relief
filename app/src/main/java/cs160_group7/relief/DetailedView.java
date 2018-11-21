package cs160_group7.relief;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class DetailedView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        Bundle bundle = intent.getBundleExtra("building_map");
        HashMap<String, Building> mapping_set = MapsActivity.buildingMap;
//        HashMap<String, Building> mapping_set = (HashMap<String, Building>)bundle.getSerializable("building_map");
        loadHeader(name, mapping_set);
        new restroom_description_scraper(name, mapping_set).execute();

    }

    public void loadHeader(String hall_name, HashMap<String, Building> mapping_set) {
        TextView headerInfo = (TextView) findViewById(R.id.hall_header);
        headerInfo.setText(hall_name + "\n");
        Building hall = mapping_set.get(hall_name);
        double rating = hall.rating;
        headerInfo.append("Rating: " + rating + "/5" + "\n");
//        String open_hours = hall.openHours;
//        headerInfo.append("Hours: " + open_hours);
    }

    public void loadPicture(String hall_name) {
        String formatted_name = hall_name.toLowerCase().replace("hall", "").replace(" ", "");
        String url = "https://www.berkeley.edu/map/images/" + formatted_name + ".jpg";
        ImageView hallImage = new ImageView(this);
        LinearLayout layout = (LinearLayout) findViewById(R.id.lin_layout);
        Picasso.get().load(url).into(hallImage);
        layout.addView(hallImage);
        hallImage.getLayoutParams().height = 800;
        hallImage.setAdjustViewBounds(true);
        hallImage.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    public void loadReviews(String hall_name, HashMap<String, Building> mapping_set) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.lin_layout);
        TextView review_header = new TextView(DetailedView.this);
        review_header.setText("Reviews");
        review_header.setGravity(Gravity.LEFT);
        review_header.setTextSize(25f);
        review_header.setTextColor(Color.parseColor("#000000"));
        layout.addView(review_header);
        createButton(hall_name);


        if (mapping_set.keySet().contains(hall_name)) {
            List<String> hall_reviews = mapping_set.get(hall_name).reviews;
            for (String rev : hall_reviews) {
                TextView temp = new TextView(this);
                temp.setText(rev);
                layout.addView(temp);
            }
        } else {
            TextView new_text_view = new TextView(this);
            new_text_view.setText("There are no reviews for this building.");
            layout.addView(new_text_view);
        }


    }



    private class restroom_description_scraper extends AsyncTask<Void, Void, String> {
        private String hall_name;
        private HashMap<String, Building> mapping_set;

        public restroom_description_scraper(String hall_name, HashMap<String, Building> mapping_set) {
            super();
            this.hall_name = hall_name;
            this.mapping_set = mapping_set;
        }

        @Override
        protected String doInBackground(Void... params) {
            String res_string ="";
            try {
                String url = "https://access.berkeley.edu/soda-hall";
                Document doc = Jsoup.connect(url).get();
                Element content = doc.selectFirst(".content");
                String restroom_content = content.text();
                int indicator = restroom_content.indexOf("Restrooms:");
                if (indicator == -1) {
                    res_string = "There are no restrooms in this building";
                } else {
                    int cut_off = restroom_content.indexOf("Designated waiting area:");
                    String parsed_info = restroom_content.substring(indicator + 10, cut_off);
                    parsed_info = parsed_info.replace(".", ".\n");
                    res_string = parsed_info;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return res_string;
        }

        @Override
        protected void onPostExecute(String result) {
            TextView restroom_description = new TextView(DetailedView.this);
            restroom_description.setText(result);
            restroom_description.setGravity(Gravity.LEFT | Gravity.CENTER_HORIZONTAL);
            restroom_description.setTextSize(15f);
            restroom_description.setTextColor(Color.parseColor("#000000"));
            LinearLayout layout = (LinearLayout) findViewById(R.id.lin_layout);
            layout.addView(restroom_description);


            loadPicture(hall_name);
//            loadInformation(hall_name);
            loadReviews(hall_name, mapping_set);
        }
    }



    public void createButton(String hall_name) {
        final String intent_hall_name = hall_name;
        Button add_review = new Button(this);
        LinearLayout layout = (LinearLayout) findViewById(R.id.lin_layout);
        layout.addView(add_review);

        add_review.setGravity(Gravity.RIGHT);
        add_review.setText("Add a review!");
        add_review.getLayoutParams().width=450;
        add_review.getLayoutParams().height=150;

        LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams)add_review.getLayoutParams();
        ll.gravity = Gravity.RIGHT;
        add_review.setLayoutParams(ll);

        add_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Change the name of MainActivity.class to whatever the activity file is called
//                Intent temp_intent = new Intent(this, MainActivity.class);
//                temp_intent.putExtra("hall_name", intent_hall_name);
//                startActivity(temp_intent);
            }
        });
    }


    public void loadInformation(String hall_name) {
//        Building hall = mapping_set.get(hall_name);
        TextView rating_header = new TextView(this);
        TextView hall_ratings = new TextView(this);
        LinearLayout layout = findViewById(R.id.lin_layout);


        rating_header.setText("Ratings");
        rating_header.setGravity(Gravity.LEFT);
        rating_header.setTextSize(25f);
        rating_header.setTextColor(Color.parseColor("#000000"));
        layout.addView(rating_header);

//        double cleanliness = hall.cleanliness;
        hall_ratings.setText("Cleanliness: " + "5/5" + "\n");
        hall_ratings.append("Accessibility: " + "4/5" + "\n");
        hall_ratings.append("Privacy: " + "5/5" + "\n");
        hall_ratings.setTextSize(20f);
        hall_ratings.setTextColor(Color.parseColor("#000000"));
        layout.addView(hall_ratings);
//        layout.addView(hall_ratings);



    }
}
