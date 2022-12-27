package com.example.driver_helper.service;

import com.example.driver_helper.entity.Vehicle;
import com.example.driver_helper.exception.VehicleNotFoundException;
import com.example.driver_helper.repository.VehicleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class VehicleServiceImpl implements VehicleService{
    VehicleRepository vehicleRepository;

    @Override
    public Vehicle saveVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    @Override
    public Vehicle getVehicle(Long vId) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(vId);
        return unwrapVehicle(vehicle, vId);
    }

    @Override   // 待補： Update 都需要判斷若無傳入值則保持原樣，若原本無值則保持空值。
    public Vehicle updateVehicle(Vehicle vehicle, Long vId) {
        Optional<Vehicle> wrappedVehicle = vehicleRepository.findById(vId);
        Vehicle unwrapVehicle = unwrapVehicle(wrappedVehicle, vId);
        unwrapVehicle.setId(vehicle.getId());
        unwrapVehicle.setMfd(vehicle.getMfd());
        unwrapVehicle.setBrand(vehicle.getBrand());
        unwrapVehicle.setMileage(vehicle.getMileage());
        unwrapVehicle.setType(vehicle.getType());
        unwrapVehicle.setModel(vehicle.getModel());
        unwrapVehicle.setName(vehicle.getName());

        return vehicleRepository.save(unwrapVehicle);
    }

    @Override
    public void deleteVehicle(Long vId) {
        vehicleRepository.deleteById(vId);
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        return (List<Vehicle>) vehicleRepository.findAll();
    }

    static Vehicle unwrapVehicle(Optional<Vehicle> entity, Long id) {
        if (entity.isPresent()) return entity.get();
        else throw new VehicleNotFoundException(id);
    }
}
