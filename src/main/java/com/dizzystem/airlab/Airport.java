package com.dizzystem.airlab;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Airport {

  private String uid, name, iata, icao;
  private float lat, lng;
  private int alt;

  public Airport() {
  }

  public String getUid() {
    return uid;
  }
  public String getName() {
    return name;
  }
  public String getIata() {
    return iata;
  }
  public String getIcao() {
    return icao;
  }

  public float getLat() {
    return lat;
  }
  public float getLng() {
    return lng;
  }
  public int getAlt() {
    return alt;
  }

  @Override
  public String toString() {
    return "Airport{" +
        "uid='" + uid + '\'' +
        ", name='" + name + '\'' +
        ", iata='" + iata + '\'' +
        ", icao='" + icao + '\'' +
        ", lat=" + lat +
        ", lng=" + lng +
        ", alt=" + alt +
        '}';
  }
}