package com.example.driver_helper.controller;

import com.example.driver_helper.entity.MaintenanceRecord;
import com.example.driver_helper.entity.RefuelingRecord;
import com.example.driver_helper.service.MaintenanceRecordService;
import com.example.driver_helper.service.RefuelingRecordService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/maintenance")
public class MaintenanceRecordController {
    MaintenanceRecordService maintenanceRecordService;

    @GetMapping("/{mId}")
    public ResponseEntity<MaintenanceRecord> getRecord(@PathVariable Long mId){
        return new ResponseEntity<>(maintenanceRecordService.getRecord(mId), HttpStatus.OK);
    }

    @PostMapping("/vehicle/{vId}")
    public ResponseEntity<MaintenanceRecord> saveRecord(@RequestBody MaintenanceRecord maintenanceRecord,@PathVariable Long vId) {
        return new ResponseEntity<>(maintenanceRecordService.saveRecord(maintenanceRecord, vId), HttpStatus.CREATED);
    }


    @PutMapping("/{mId}")
    public ResponseEntity<MaintenanceRecord> updateRecord(@RequestBody MaintenanceRecord maintenanceRecord, @PathVariable Long mId) {
        return new ResponseEntity<>(maintenanceRecordService.updateRecord(maintenanceRecord,mId), HttpStatus.OK);
    }

    @DeleteMapping("/{mId}")
    public ResponseEntity<HttpStatus> deleteRecord(@PathVariable Long mId) {
        maintenanceRecordService.deleteRecord(mId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/vehicle/{vId}")
    public ResponseEntity<List<MaintenanceRecord>> getRecordsByVehicle(@PathVariable Long vId) {
        return new ResponseEntity<>(maintenanceRecordService.getRecordsByVehicle(vId), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<MaintenanceRecord>> getRecords() {
        return new ResponseEntity<>(maintenanceRecordService.getAllRecords(), HttpStatus.OK);
    }
}
