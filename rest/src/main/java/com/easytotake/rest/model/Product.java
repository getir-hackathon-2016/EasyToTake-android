package com.easytotake.rest.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Product implements Serializable {
    private String oid;
    private String name;
    private String picture;
    private int stock;
    private String parentOid;
    private BigDecimal price;

    public String getParentOid() {
        return parentOid;
    }

    public void setParentOid(String parentOid) {
        this.parentOid = parentOid;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
