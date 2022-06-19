package com.dizzystem.airlab;

import com.dizzystem.airlab.Airport;
import com.dizzystem.airlab.Waypoint;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SIDSTAR {
  private String name;
  private Airport airport;
  private Waypoint[] waypoints;

  public SIDSTAR() {
  }

  public String getName() {
    return name;
  }
  public Airport getAirport() {
    return airport;
  }
  public Waypoint[] getWaypoints() {
    return waypoints;
  }

  @Override
  public String toString() {
    String airportString = airport.toString();
    
    String waypointString = "";
    for (Waypoint waypoint : waypoints) {
        waypointString += waypoint.toString();
    }

    return "SIDSTAR{" +
        ", name='" + name + '\'' +
        ", airport='" + airportString + '\'' +
        ", waypoints='" + waypointString + '\'' +
        '}';
  }
}