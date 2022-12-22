package com.example.driver_helper.service;

import com.example.driver_helper.entity.RefuelingRecord;

import java.util.List;

public interface RefuelingRecordService {
    RefuelingRecord saveRecord(RefuelingRecord refuelingRecord, Long vId);

    RefuelingRecord getRecord(Long rId);

    RefuelingRecord updateRecord(RefuelingRecord refuelingRecord, Long rId);

    void deleteRecord(Long rId);

    List<RefuelingRecord> getRecordsByVehicle(Long Vid);

    List<RefuelingRecord> getAllRecords();
}
