package com.dizzystem.airlab;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Waypoint {
  private String uid, name;
  private float lat, lng;

  public Waypoint() {
  }

  public String getUid() {
    return uid;
  }
  public String getName() {
    return name;
  }
  public float getLat() {
    return lat;
  }
  public float getLng() {
    return lng;
  }

  @Override
  public String toString() {
    return "Waypoint{" +
        "uid='" + uid + '\'' +
        ", name='" + name + '\'' +
        ", lat=" + lat +
        ", lng=" + lng +
        '}';
  }
}