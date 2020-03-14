package com.ronaldbarrera.nine11.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "center")
public class CenterEntry {

    @PrimaryKey
    private int id;
    private String name;
    private String title;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String phone;
    private double lat;
    private double lng;

    public CenterEntry(int id, String name, String title, String address, String city, String state, String zip, String phone, double lat, double lng) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phone = phone;
        this.lat = lat;
        this.lng = lng;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }

    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }

    public void setState(String state) { this.state = state; }

    public String getZip() { return zip; }

    public void setZip(String zip) { this.zip = zip; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public double getLat() { return lat; }

    public void setLat(double lat) { this.lat = lat; }

    public double getLng() { return lng; }

    public void setLng(double lng) { this.lng = lng; }
}


