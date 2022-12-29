package com.example.driver_helper.pojo;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MaintenanceRecord extends Record implements Serializable {


    public MaintenanceRecord(Long mid, String date, String item, Long price, String notes, Long vid) {
        super(mid, date, item, price, notes, vid);
    }



}
