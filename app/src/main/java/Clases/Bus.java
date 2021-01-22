package Clases;

public class Bus {

    private int Capacidad;
    private String Conductor;
    private double lat;
    private double lon;
    private String placa;

    public Bus() {
    }


    public int getCapacidad() {
        return Capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.Capacidad = capacidad;
    }

    public String getConductor() {
        return Conductor;
    }

    public void setConductor(String conductor) {
        this.Conductor = conductor;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }
}
