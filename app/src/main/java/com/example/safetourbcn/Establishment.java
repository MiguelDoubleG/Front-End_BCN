package com.example.safetourbcn;

public class Establishment {
    private int id;
    private String name;
    private String idCompany;
    private String description;
    private Double lat;
    private Double lng;
    private Integer maxCapacity;
    private Integer houropen;
    private Integer hourclose;
    private String category;
    private Integer price;
    private Boolean discount;
    private String address;
    private String website;
    private String instagram;

    public Establishment(){ }

    public Establishment(int id, String name, String idCompany, String description,
                         Double lat, Double lng, Integer maxCapacity, Integer houropen, Integer hourclose,
                         String category, Integer price, Boolean discount, String address, String website, String instagram){
        this.id = id;
        this.idCompany = idCompany;
        this.name = name;
        this.description = description;
        this. lat = lat;
        this.lng = lng;
        this.maxCapacity = maxCapacity;
        this.houropen = houropen;
        this.hourclose = hourclose;
        this.category = category;
        this.price = price;
        this.discount = discount;
        this.address = address;
        this.website = website;
        this.instagram = instagram;
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

    public Boolean getDiscount() {
        return discount;
    }

    public String getAddress() { return address; }

    public Integer getHouropen() { return houropen;}

    public Integer getHourclose() { return hourclose; }

    public String getInstagram() {
        return instagram;
    }

    public String getWebsite() {
        return website;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) { this.id = id; }

    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }

    public void setLat(double lat) { this.lat = lat; }

    public void setLng(double lng) { this.lng = lng; }

    public void setIdCompany(String idCompany) { this.idCompany = idCompany; }
}