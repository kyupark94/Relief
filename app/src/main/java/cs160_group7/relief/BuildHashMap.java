package cs160_group7.relief;

import android.content.res.AssetManager;

import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;
import org.json.simple.parser.ParseException;

/* !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!IMPORTANT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/
/*Must add gradle dependency "compile 'com.googlecode.json-simple:json-simple:1.1'" in order for this file to compile */
/* !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!IMPORTANT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/

public class BuildHashMap {

    public static HashMap<String, Building> buildHashMap(AssetManager am){

        HashMap<String, Building> map = new HashMap<>();

        JSONParser parser = new JSONParser();

        try {

            org.json.simple.JSONObject jsonObj = (org.json.simple.JSONObject) parser.parse(new InputStreamReader(am.open("relief_tmp.json")));
            System.out.println(jsonObj);


            Set<String> buildings = jsonObj.keySet();


            for (String name: buildings) {

                org.json.simple.JSONObject tempBuilding = (org.json.simple.JSONObject) jsonObj.get(name);

                double lat = Double.parseDouble( (String)tempBuilding.get("latitude"));
                double lon = Double.parseDouble((String) tempBuilding.get("longitude"));
                double rating = Double.parseDouble((String) tempBuilding.get("rating"));
                String details = (String) tempBuilding.get("details");

                Building b = new Building(lat, lon, details, name);
                b.setRating(rating);

                map.put(name, b);


            }

        } catch ( ParseException | IOException e){
            System.out.println(e);

        }

        return map;

    }

}
