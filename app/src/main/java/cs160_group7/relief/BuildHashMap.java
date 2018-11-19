package com.example.mharris.relief;

import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import org.json.simple.parser.ParseException;

/* !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!IMPORTANT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/
/*Must add gradle dependency "compile 'com.googlecode.json-simple:json-simple:1.1'" in order for this file to compile */
/* !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!IMPORTANT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/

public class BuildHashMap {

    public static HashMap<String, Building> buildHashMap(String JSONFilepath){

        HashMap<String, Building> map = new HashMap<>();

        JSONParser parser = new JSONParser();

        try {

            org.json.simple.JSONObject jsonObj = (org.json.simple.JSONObject) parser.parse(new FileReader(JSONFilepath));
            System.out.println(jsonObj);


            Set<String> buildings = jsonObj.keySet();


            for (String name: buildings) {

                org.json.simple.JSONObject tempBuilding = (org.json.simple.JSONObject) jsonObj.get(name);

                double lat = Double.parseDouble( (String)tempBuilding.get("latitude"));
                double lon = Double.parseDouble((String) tempBuilding.get("longitude"));
                String details = (String) tempBuilding.get("details");

                Building b = new Building(lat, lon, details, name);

                map.put(name, b);


            }

        } catch ( ParseException | IOException e){
            System.out.println(e);

        }

        return map;

    }

}
