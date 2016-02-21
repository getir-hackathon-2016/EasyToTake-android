package com.easytotake.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.io.Serializable;

public class Messenger implements ClusterItem, Serializable {

    private double latitude;
    private double longitude;
    private String oid;
    private String name;
    private String picture;
    private String distance;

    public Messenger() {

    }

    public Messenger(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    @JsonIgnore
    @Override
    public LatLng getPosition() {
        return new LatLng(latitude, longitude);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
