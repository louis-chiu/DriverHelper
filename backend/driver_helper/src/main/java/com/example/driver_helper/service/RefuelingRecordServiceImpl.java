package com.example.driver_helper.service;

import com.example.driver_helper.entity.MaintenanceRecord;
import com.example.driver_helper.entity.RefuelingRecord;
import com.example.driver_helper.entity.Vehicle;
import com.example.driver_helper.exception.RecordNotFoundException;
import com.example.driver_helper.exception.VehicleNotFoundException;
import com.example.driver_helper.repository.RefuelingRecordRepository;
import com.example.driver_helper.repository.VehicleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class RefuelingRecordServiceImpl implements RefuelingRecordService{

    RefuelingRecordRepository refuelingRecordRepository;
    VehicleRepository vehicleRepository;

    @Override
    public RefuelingRecord saveRecord(RefuelingRecord refuelingRecord, Long vId) {
        Vehicle vehicle = VehicleServiceImpl.unwrapVehicle(vehicleRepository.findById(vId),vId);
        refuelingRecord.setVehicle(vehicle);
        return refuelingRecordRepository.save(refuelingRecord);
    }

    @Override
    public RefuelingRecord getRecord(Long rId) {
        Optional<RefuelingRecord> refuelingRecord = refuelingRecordRepository.findById(rId);
        return unwrapRefuelingRecord(refuelingRecord, rId);
    }

    @Override
    public RefuelingRecord updateRecord(RefuelingRecord refuelingRecord, Long rId) {
        Optional<RefuelingRecord> wrappedRecord = refuelingRecordRepository.findById(rId);
        RefuelingRecord unwrapRecord = unwrapRefuelingRecord(wrappedRecord, rId);
        unwrapRecord.setRid(refuelingRecord.getRid());
        unwrapRecord.setNotes(refuelingRecord.getNotes());
        unwrapRecord.setLiter(refuelingRecord.getLiter());
        unwrapRecord.setRdate(refuelingRecord.getRdate());
        unwrapRecord.setPrice(refuelingRecord.getPrice());
        unwrapRecord.setGastype(refuelingRecord.getGastype());
        return refuelingRecordRepository.save(unwrapRecord);
    }

    @Override
    public void deleteRecord(Long rId) {
        refuelingRecordRepository.deleteById(rId);
    }

    @Override
    public List<RefuelingRecord> getRecordsByVehicle(Long Vid) {
        return refuelingRecordRepository.findByVehicleId(Vid);
    }// 注意一下這個是否成功

    @Override
    public List<RefuelingRecord> getAllRecords() {
        return (List<RefuelingRecord>) refuelingRecordRepository.findAll();
    }

    static RefuelingRecord unwrapRefuelingRecord(Optional<RefuelingRecord> entity, Long id) {
        if (entity.isPresent()) return entity.get();
        else throw new RecordNotFoundException(id);
    }
}
