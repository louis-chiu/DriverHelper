package com.example.driver_helper.pojo;

import java.io.Serializable;

public class Record implements Serializable {
    private Long id;
    private String date;
    private Long price;
    private String notes;
    private String item;
    private Long vid;

    public Record(Long id, String date, String item, Long price, String notes, Long vid) {
        this.id = id;
        this.date = date;
        this.price = price;
        this.notes = notes;
        this.item = item;
        this.vid = vid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Long getVid() {
        return vid;
    }

    public void setVid(Long vid) {
        this.vid = vid;
    }
}
