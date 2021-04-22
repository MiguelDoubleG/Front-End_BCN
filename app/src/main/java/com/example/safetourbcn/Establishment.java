package com.example.safetourbcn;

public class Establishment {
    private int id;
    private String name;
    private String idCompany;
    private String description;
    private double lat;
    private double lng;
    private int maxCapacity;
    private String schedule;

    public Establishment(){ }

    public Establishment(int id, String name, String idCompany, String description, double lat, double lng, int maxCapacity, String schedule){
        this.id = id;
        this.idCompany = idCompany;
        this.name = name;
        this.description = description;
        this. lat = lat;
        this.lng = lng;
        this.maxCapacity = maxCapacity;
        this.schedule = schedule;
    }

    public String getName() { return name; }

    public String getDescription() {
        return description;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getId() {
        return id;
    }

    public String getSchedule() {
        return schedule;
    }

    public double getLat() { return lat; }

    public double getLng() { return lng; }

    public String getIdCompany() {
        return idCompany;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) { this.id = id; }

    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }

    public void setSchedule(String schedule) {  this.schedule = schedule; }

    public void setLat(double lat) { this.lat = lat; }

    public void setLng(double lng) { this.lng = lng; }

    public void setIdCompany(String idCompany) { this.idCompany = idCompany; }
}