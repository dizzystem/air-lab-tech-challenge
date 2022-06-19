package com.example.consumingrest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Airport {

  private String uid, name, iata, icao;
  private Value lat, lng, alt;

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

  public Value getLat() {
    return lat;
  }
  public Value getLng() {
    return lng;
  }
  public Value getAlt() {
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