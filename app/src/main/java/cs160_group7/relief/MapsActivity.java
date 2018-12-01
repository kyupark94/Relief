package cs160_group7.relief;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.location.FusedLocationProviderClient;


import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    private List<Building> buildingList = new ArrayList<>();
    public static HashMap<String, Building> buildingMap = new HashMap<>();
    static RecyclerView rv;
    private FusedLocationProviderClient client;
    String state = "distance";
    double curLat = 37.8721;
    double curLong = -122.2578;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        requestPermission();


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

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Add a marker in Campanile and move the camera
        // TODO: change hardcoded curLat, curLong
        requestPermission();

//        client = LocationServices.getFusedLocationProviderClient(this);
//        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            return;
//        }
//        client.getLastLocation().addOnSuccessListener(MapsActivity.this, new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                if(location!=null){
//                    curLat = location.getLatitude();
//                    curLong = location.getLongitude();
//                }
//            }
//        });

        mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
        mMap.setOnMyLocationClickListener(onMyLocationClickListener);
        enableMyLocationIfPermitted();
        LocationManager locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);


        LocationListener locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                curLat=location.getLatitude();
                curLong=location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000L,500.0f, locationListener);
        Location location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        curLat = location.getLatitude();
        curLong = location.getLongitude();
        LatLng curLoc = new LatLng(curLat, curLong);
        //mMap.addMarker(new MarkerOptions().position(curLoc).title("Current Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        pinAllBuildings();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curLoc, 18.0f));

    }

    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }

    private void showDefaultLocation() {
        Toast.makeText(this, "Location permission not granted, " +
                        "showing default location",
                Toast.LENGTH_SHORT).show();
        LatLng campanille = new LatLng(37.8721, -122.2578);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(campanille));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocationIfPermitted();
                } else {
                    showDefaultLocation();
                }
                return;
            }

        }
    }

    private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener =
            new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    mMap.setMinZoomPreference(15);
                    return false;
                }
            };

    private GoogleMap.OnMyLocationClickListener onMyLocationClickListener =
            new GoogleMap.OnMyLocationClickListener() {
                @Override
                public void onMyLocationClick(@NonNull Location location) {

                    mMap.setMinZoomPreference(12);
                    curLat = location.getLatitude();
                    curLong = location.getLongitude();
                    CircleOptions circleOptions = new CircleOptions();
                    circleOptions.center(new LatLng(location.getLatitude(),
                            location.getLongitude()));

                    circleOptions.radius(200);
                    circleOptions.fillColor(Color.RED);
                    circleOptions.strokeWidth(6);

                    mMap.addCircle(circleOptions);
                }
            };


    public void pinAllBuildings() {
        for (Building b : buildingList) {
            LatLng loc = new LatLng(b.latitude, b.longitude);
            mMap.addMarker(new MarkerOptions().position(loc).title(b.name));
        }
    }


    void initializeAdapter() {
        buildingMap = BuildHashMap.buildHashMap(getAssets());
        buildingList = new ArrayList<>(buildingMap.values());
        sortBuildingList();
        Log.d("buildingList", buildingList.toString());
        BuildingAdapter adapter = new BuildingAdapter(buildingList, curLat, curLong);
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
                    double d1 = Math.sqrt(Math.pow(curLat - o1.latitude, 2.0) + Math.pow(curLong - o1.longitude, 2.0));
                    double d2 = Math.sqrt(Math.pow(curLat - o2.latitude, 2.0) + Math.pow(curLong - o2.longitude, 2.0));
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
