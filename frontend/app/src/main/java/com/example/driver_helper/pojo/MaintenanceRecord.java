package com.example.driver_helper.pojo;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MaintenanceRecord extends Record implements Serializable {

    private Long mid;
    private String date;
    private String item;
    private Long price;
    private String notes;
    private Long vid;

    public MaintenanceRecord(Long mid, String date, String item, Long price, String notes, Long vid) {
        this.mid = mid;
        this.date = date;
        this.item = item;
        this.price = price;
        this.notes = notes;
        this.vid = vid;
    }


    public Long getMid() {
        return mid;
    }

    public void setMid(Long mid) {
        this.mid = mid;
    }

    @Override
    public String getDate() {
        return date;
    }

    @Override
    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String getItem() {
        return item;
    }

    @Override
    public void setItem(String item) {
        this.item = item;
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
