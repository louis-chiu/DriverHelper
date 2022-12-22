package com.example.driver_helper.repository;

import com.example.driver_helper.entity.MaintenanceRecord;
import com.example.driver_helper.entity.RefuelingRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceRecordRepository extends CrudRepository<MaintenanceRecord, Long> {
    public List<MaintenanceRecord> findByVehicleId(Long vId);
}
