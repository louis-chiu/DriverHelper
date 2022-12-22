package com.example.driver_helper.service;

import com.example.driver_helper.entity.MaintenanceRecord;
import com.example.driver_helper.entity.Vehicle;

import java.util.List;

public interface VehicleService {

    Vehicle saveVehicle(Vehicle vehicle);

    Vehicle getVehicle(Long vId);

    Vehicle updateVehicle(Vehicle vehicle, Long vId);

    void deleteVehicle(Long vId);

    List<Vehicle> getAllVehicles();
}
