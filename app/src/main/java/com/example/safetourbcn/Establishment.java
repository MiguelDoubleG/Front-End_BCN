package com.example.safetourbcn;

public class Establishment {
    private int id;
    private String name;
    private String idCompany;
    private String description;
    private Double lat;
    private Double lng;
    private Integer maxCapacity;
    private String schedule;
    private String category;
    private Integer price;
    private Float  rating;
    private Boolean discount;
    private String address;

    public Establishment(){ }

    public Establishment(int id, String name, String idCompany, String description,
                         Double lat, Double lng, Integer maxCapacity, String schedule,
                         String category, Integer price, Float  rating, Boolean discount, String address){
        this.id = id;
        this.idCompany = idCompany;
        this.name = name;
        this.description = description;
        this. lat = lat;
        this.lng = lng;
        this.maxCapacity = maxCapacity;
        this.schedule = schedule;
        this.category = category;
        this.price = price;
        this.rating = rating;
        this.discount = discount;
        this.address = address;
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

    public Double getLat() { return lat; }

    public Double getLng() { return lng; }

    public String getIdCompany() {
        return idCompany;
    }

    public String getCategory() {
        return category;
    }

    public Integer getPrice() {
        return price;
    }

    public Float getRating() {
        return rating;
    }

    public Boolean getDiscount() {
        return discount;
    }

    public String getAddress() { return address; }



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