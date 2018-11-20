package cs160_group7.relief;

import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public HashMap<String, Building> buildingMap = new HashMap<>();
    private List<Building> buildingList = new ArrayList<>();
    static RecyclerView rv;
    String state = "distance";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Recycle View && Person Cards Handling
        rv = (RecyclerView) findViewById(R.id.cardList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(false);

        configureFilterButtons();
        initializeAdapter();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Campanile and move the camera
        LatLng Campanile = new LatLng(37.8, -122.3);
        mMap.addMarker(new MarkerOptions().position(Campanile).title("Marker in Campanile"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Campanile));
    }

    void initializeAdapter() {
        buildingMap = BuildHashMap.buildHashMap(getAssets());
        buildingList = new ArrayList<>(buildingMap.values());
        sortBuildingList();
        Log.d("buildingList", buildingList.toString());
        BuildingAdapter adapter = new BuildingAdapter(buildingList);
        rv.setAdapter(adapter);
    }

    public void configureFilterButtons() {
        Button distance = (Button) findViewById(R.id.distance);
        distance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = "distance";
                sortBuildingList();
            }
        });

        Button openNow = (Button) findViewById(R.id.openNow);
        openNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = "openHours";
//                sortBuildingList(); // TODO
            }
        });

        Button rating = (Button) findViewById(R.id.rating);
        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = "rating";
                sortBuildingList();
            }
        });

        Button cleanliness = (Button) findViewById(R.id.cleanliness);
        cleanliness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = "cleanliness";
                sortBuildingList();
            }
        });
    }


    // TODO: remove hardcoded location, add Open Now comparator
    public void sortBuildingList() {
        if (state.equals("distance")) {
            buildingList.sort(new Comparator<Building>() {
                @Override
                public int compare(Building o1, Building o2) {
                    double d1 = Math.sqrt(Math.pow(37.8 - o1.latitude, 2.0) + Math.pow(-122.3 - o1.longitude, 2.0));
                    double d2 = Math.sqrt(Math.pow(37.8 - o2.latitude, 2.0) + Math.pow(-122.3 - o2.longitude, 2.0));
                    if (d1 < d2) return -1;
                    if (d1 > d2) return 1;
                    return 0;
                }
            });
        } else if (state.equals("rating")) {
            buildingList.sort(new Comparator<Building>() {
                @Override
                public int compare(Building o1, Building o2) {
                    if (o2.rating < o1.rating) return -1;
                    if (o2.rating > o1.rating) return 1;
                    return 0;
                }
            });
        } else if (state.equals("cleanliness")) {
            buildingList.sort(new Comparator<Building>() {
                @Override
                public int compare(Building o1, Building o2) {
                    if (o2.cleanliness < o1.cleanliness) return -1;
                    if (o2.cleanliness > o1.cleanliness) return 1;
                    return 0;
                }
            });
        }

        if (rv.getAdapter() != null) {
            rv.getAdapter().notifyDataSetChanged();
        }
    }
}
