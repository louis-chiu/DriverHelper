package com.example.driver_helper.controller;

import com.example.driver_helper.entity.Vehicle;
import com.example.driver_helper.service.VehicleService;
import com.example.driver_helper.service.VehicleServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    VehicleService vehicleService;

    @GetMapping("/{vId}")
    public ResponseEntity<Vehicle> getVehicle(@PathVariable Long vId){
        return new ResponseEntity<>(vehicleService.getVehicle(vId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Vehicle> saveVehicle(@RequestBody Vehicle vehicle) {
        return new ResponseEntity<>(vehicleService.saveVehicle(vehicle), HttpStatus.CREATED);
    }


    @PutMapping("/{vId}")
    public ResponseEntity<Vehicle> updateVehicle(@RequestBody Vehicle vehicle, @PathVariable Long vId) {
        return new ResponseEntity<>(vehicleService.updateVehicle(vehicle,vId), HttpStatus.OK);
    }

    @DeleteMapping("/{vId}")
    public ResponseEntity<HttpStatus> deleteVehicle(@PathVariable Long vId) {
        vehicleService.deleteVehicle(vId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Vehicle>> getVehicles() {
        return new ResponseEntity<>(vehicleService.getAllVehicles(), HttpStatus.OK);
    }



}
