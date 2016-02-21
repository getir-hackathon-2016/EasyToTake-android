package com.easytotake.rest.model;

/**
 * Created by hasanmumin on 20/02/16.
 */
public class Category {

    private String oid;
    private String name;
    private String picture;

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
