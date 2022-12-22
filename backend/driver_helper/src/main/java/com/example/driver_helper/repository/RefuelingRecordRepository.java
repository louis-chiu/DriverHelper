package com.example.driver_helper.repository;

import com.example.driver_helper.entity.RefuelingRecord;
import com.example.driver_helper.entity.Vehicle;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefuelingRecordRepository extends CrudRepository<RefuelingRecord, Long> {
    public List<RefuelingRecord> findByVehicleId(Long vId);
}
