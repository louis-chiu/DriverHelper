package com.example.driver_helper.pojo;

import java.io.Serializable;

public class RefuelingRecord extends Record implements Serializable {

    private Long rid;
    private String date;
    private String item;
    private Double liter;
    private Long price;
    private String notes;
    private Long vid;

    public RefuelingRecord(Long rid, String date, String item, Double liter, Long price, String notes, Long vid) {
        this.rid = rid;
        this.date = date;
        this.item = item;
        this.liter = liter;
        this.price = price;
        this.notes = notes;
        this.vid = vid;
    }

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    @Override
    public String getDate() {
        return date;
    }

    @Override
    public void setDate(String date) {
        this.date = date;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Double getLiter() {
        return liter;
    }

    public void setLiter(Double liter) {
        this.liter = liter;
    }

    @Override
    public Long getPrice() {
        return price;
    }

    @Override
    public void setPrice(Long price) {
        this.price = price;
    }

    @Override
    public String getNotes() {
        return notes;
    }

    @Override
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getVid() {
        return vid;
    }

    public void setVid(Long vid) {
        this.vid = vid;
    }
}
