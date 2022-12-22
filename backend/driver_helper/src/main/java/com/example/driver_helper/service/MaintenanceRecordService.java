package com.example.driver_helper.service;

import com.example.driver_helper.entity.MaintenanceRecord;
import com.example.driver_helper.entity.RefuelingRecord;

import java.util.List;

public interface MaintenanceRecordService {
    MaintenanceRecord saveRecord(MaintenanceRecord maintenanceRecord, Long vId);

    MaintenanceRecord getRecord(Long mId);

    MaintenanceRecord updateRecord(MaintenanceRecord maintenanceRecord, Long mId);

    void deleteRecord(Long mId);

    List<MaintenanceRecord> getRecordsByVehicle(Long vId);

    List<MaintenanceRecord> getAllRecords();


}
