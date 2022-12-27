package com.example.driver_helper.pojo;

import java.io.Serializable;

public class RefuelingRecord extends Record implements Serializable {

    private Long rid;
    private Double liter;

    public RefuelingRecord(Long rid, String date, String item, Double liter, Long price, String notes, Long vid) {
        super(date, item, price, notes , vid);
        this.rid = rid;
        this.liter = liter;
    }

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public Double getLiter() {
        return liter;
    }

    public void setLiter(Double liter) {
        this.liter = liter;
    }
}
