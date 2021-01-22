package com.example.recorrido_buses;

import org.joda.time.DateTime;

import java.util.Date;

public class MapsCoor {

    private String fecha;
    private Double lat;
    private Double lon;

    public MapsCoor() {
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
