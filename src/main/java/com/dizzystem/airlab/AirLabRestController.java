package com.dizzystem.airlab;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.http.HttpEntity;
import com.dizzystem.airlab.Airport;
import com.dizzystem.airlab.SIDSTAR;

@RestController
public class AirLabRestController {
    public static String[] airportList;
    public static long lastCache = 0;

    public Object[] fetchApiData(String query, Class cl){
        RestTemplate restTemplate = new RestTemplate();
        URI uri;
        try {
            uri = new URI("https://open-atms.airlab.aero/api/v1/airac/" + query);
        } catch (URISyntaxException x) {
            throw new IllegalArgumentException(x.getMessage(), x);
        }
        HttpHeaders headers = new HttpHeaders() {{
            set("api-key", "G9Tw58HE6HDzyq94HFmnd2yOymAuU32k2mEgL3oTVbhLl6E1opu5Hqxb5BASwCWv");
        }};
        HttpEntity<Airport[]> requestEntity = new HttpEntity<>(null, headers);
        
        ResponseEntity<Airport[]> response = restTemplate.exchange(
            uri, HttpMethod.GET, requestEntity, cl);

        return response.getBody();
    }

    public Airport[] fetchAirportDataAndCache(){
        Airport[] airports = (Airport[]) fetchApiData("airports", Airport[].class);

        //Refresh cached airport list incidentally whenever this is called.
        int numOfAirports = airports.length;
        airportList = new String[numOfAirports];
        for (int i=0;i<numOfAirports;i++) {
            airportList[i] = airports[i].getName();
        }

        return airports;
    }

    public String[] getAirportList(){
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCache > 60 * 60 * 1000){
            lastCache = currentTime;
            
            //Update cache.
            fetchAirportDataAndCache();
        }
        return airportList;
    }
    
	@GetMapping("/airportlist")
	public String[] airportList(Model model){
        return getAirportList();
    }

	@GetMapping("/mostfrequented")
	public ResponseEntity<Object> mostFrequented(@RequestParam(value="airport", required=false) String airport, 
            @RequestParam(value="routeType", required=false) String routeType,
            Model model) {
        Airport[] airports = fetchAirportDataAndCache();
        String[] airportNames = getAirportList();

        if (airport != null && routeType != null) {
            String query = "";
            if (routeType.equals("sids")) {
                query += "sids/";
            } else {
                query += "stars/"; //prevent injection
            }

            int airportIndex = java.util.Arrays.asList(airportNames).indexOf(airport);
            if (airportIndex > -1) {
                String icao = airports[airportIndex].getIcao();

                query += "airport/" + icao;
                
                SIDSTAR[] sidstars = (SIDSTAR[]) fetchApiData(query, SIDSTAR[].class);

                Map<String, Integer> waypointTally = new HashMap<String, Integer>();
                String mostFrequent = "N/A", secondMostFrequent = "N/A";
                int highestFrequency = 0, secondHighestFrequency = 0;

                for (SIDSTAR sidstar : sidstars) {
                    for (Waypoint waypoint : sidstar.getWaypoints()) {
                        String waypointUid = waypoint.getUid();
                        int count = 0;

                        if (waypointTally.containsKey(waypointUid)) {
                            count = waypointTally.get(waypointUid);
                        }
                        count ++;
                        
                        if (count > highestFrequency) {
                            mostFrequent = waypointUid;
                            highestFrequency = count;
                        } else if (count > secondHighestFrequency) {
                            secondMostFrequent = waypointUid;
                            secondHighestFrequency = count;
                        }
                        
                        waypointTally.put(waypointUid, count);
                    }
                }

                Object[] returnArray = new Object[4];
                returnArray[0] = mostFrequent;
                returnArray[1] = highestFrequency;
                returnArray[2] = secondMostFrequent;
                returnArray[3] = secondHighestFrequency;

                return ResponseEntity.ok(returnArray);
            }
        }

        return ResponseEntity.badRequest().build();
	}
}