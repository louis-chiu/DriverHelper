package com.example.driver_helper.pojo;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MaintenanceRecord extends Record implements Serializable {

    private Long mid;

    public MaintenanceRecord(Long mid, String date, String item, Long price, String notes, Long vid) {
        super(date, item, price, notes, vid);
        this.mid = mid;
    }

    public MaintenanceRecord(String date, String item, Long price, String notes, Long vid, Long mid) {
        super(date, item, price, notes, vid);
        this.mid = mid;
    }
}
