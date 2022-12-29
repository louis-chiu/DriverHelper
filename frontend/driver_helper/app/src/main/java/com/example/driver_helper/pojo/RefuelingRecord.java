package com.example.driver_helper.pojo;

import java.io.Serializable;

public class RefuelingRecord extends Record implements Serializable {

    private Double liter;

    public RefuelingRecord(Long rid, String date, String item, Double liter, Long price, String notes, Long vid) {
        super(rid, date, item, price, notes , vid);
        this.liter = liter;
    }


    public Double getLiter() {
        return liter;
    }

    public void setLiter(Double liter) {
        this.liter = liter;
    }
}
