package com.example.driver_helper.service;

import com.example.driver_helper.entity.MaintenanceRecord;
import com.example.driver_helper.entity.RefuelingRecord;
import com.example.driver_helper.entity.Vehicle;
import com.example.driver_helper.exception.RecordNotFoundException;
import com.example.driver_helper.repository.MaintenanceRecordRepository;
import com.example.driver_helper.repository.VehicleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class MaintenanceRecordServiceImpl implements MaintenanceRecordService{

    MaintenanceRecordRepository maintenanceRecordRepository;
    VehicleRepository vehicleRepository;
    @Override
    public MaintenanceRecord saveRecord(MaintenanceRecord maintenanceRecord, Long vId) {
        Vehicle vehicle = VehicleServiceImpl.unwrapVehicle(vehicleRepository.findById(vId),vId);
        maintenanceRecord.setVehicle(vehicle);
        return maintenanceRecordRepository.save(maintenanceRecord);
    }

    @Override
    public MaintenanceRecord getRecord(Long mId) {
        Optional<MaintenanceRecord> maintenanceRecord = maintenanceRecordRepository.findById(mId);
        return unwrapMaintenanceRecord(maintenanceRecord, mId);
    }

    @Override
    public MaintenanceRecord updateRecord(MaintenanceRecord maintenanceRecord, Long mId) {
        Optional<MaintenanceRecord> wrappedRecord = maintenanceRecordRepository.findById(mId);
        MaintenanceRecord unwrapRecord = unwrapMaintenanceRecord(wrappedRecord, mId);
        unwrapRecord.setMid(maintenanceRecord.getMid());
        unwrapRecord.setMdate(maintenanceRecord.getMdate());
        unwrapRecord.setItems(maintenanceRecord.getItems());
        unwrapRecord.setNotes(maintenanceRecord.getNotes());
        unwrapRecord.setPrice(maintenanceRecord.getPrice());
        return maintenanceRecordRepository.save(unwrapRecord);
    }


    @Override
    public void deleteRecord(Long mId) {
        maintenanceRecordRepository.deleteById(mId);
    }

    @Override
    public List<MaintenanceRecord> getRecordsByVehicle(Long vId) {
        return maintenanceRecordRepository.findByVehicleId(vId);
    }// 注意一下這個是否成功

    @Override
    public List<MaintenanceRecord> getAllRecords() {
        return (List<MaintenanceRecord>) maintenanceRecordRepository.findAll();
    }

    static MaintenanceRecord unwrapMaintenanceRecord(Optional<MaintenanceRecord> entity, Long id) {
        if (entity.isPresent()) return entity.get();
        else throw new RecordNotFoundException(id);
    }
}
