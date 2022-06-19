package com.dizzystem.airlab;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

@Controller
public class WebpageController {
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

	@GetMapping("/index")
	public String index(@RequestParam(value="airport", required=false) String airport, 
            @RequestParam(value="routeType", required=false) String routeType,
            Model model) {
        Airport[] airports = (Airport[]) fetchApiData("airports", Airport[].class);
        int numOfAirports = airports.length;
        String[] airportNames = new String[numOfAirports];

        for (int i=0;i<numOfAirports;i++) {
            airportNames[i] = airports[i].getName();
        }
        
        model.addAttribute("airportNames", airportNames);

        if (airport != null && routeType != null) {
            String query = "";
            if (routeType.equals("sids")) {
                query += "sids/";
                model.addAttribute("sidstar", "SIDs");
            } else {
                query += "stars/"; //no injection
                model.addAttribute("sidstar", "STARs");
            }

            int airportIndex = java.util.Arrays.asList(airportNames).indexOf(airport);
            if (airportIndex > -1) {
                String icao = airports[airportIndex].getIcao();

                query += "airport/" + icao;
                
                SIDSTAR[] sidstars = (SIDSTAR[]) fetchApiData(query, SIDSTAR[].class);

                model.addAttribute("icao", icao);

                Map<String, Integer> waypointTally = new HashMap<String, Integer>();
                String mostFrequent = "N/A";
                int maxFrequency = 0;

                for (SIDSTAR sidstar : sidstars) {
                    for (Waypoint waypoint : sidstar.getWaypoints()) {
                        String waypointUid = waypoint.getUid();
                        int count = 0;

                        if (waypointTally.containsKey(waypointUid)) {
                            count = waypointTally.get(waypointUid);
                        }
                        count ++;
                        
                        if (count > maxFrequency) {
                            mostFrequent = waypointUid;
                            maxFrequency = count;
                        }
                        
                        waypointTally.put(waypointUid, count);
                    }
                }

                model.addAttribute("mostFrequent", mostFrequent);
                model.addAttribute("maxFrequency", maxFrequency);
            }
        }

        return "index";
	}
}