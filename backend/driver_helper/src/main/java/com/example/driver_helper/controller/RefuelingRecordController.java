package com.example.driver_helper.controller;

import com.example.driver_helper.entity.RefuelingRecord;
import com.example.driver_helper.entity.Vehicle;
import com.example.driver_helper.service.RefuelingRecordService;
import com.example.driver_helper.service.VehicleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("/refueling")
public class RefuelingRecordController {

    RefuelingRecordService refuelingRecordService;

    @GetMapping("/{rId}")
    public ResponseEntity<RefuelingRecord> getRecord(@PathVariable Long rId){
        return new ResponseEntity<>(refuelingRecordService.getRecord(rId), HttpStatus.OK);
    }

    @PostMapping("/vehicle/{vId}")
    public ResponseEntity<RefuelingRecord> saveRecord(@RequestBody RefuelingRecord refuelingRecord, @PathVariable Long vId) {
        return new ResponseEntity<>(refuelingRecordService.saveRecord(refuelingRecord, vId), HttpStatus.CREATED);
    }

    @PutMapping("/{rId}")
    public ResponseEntity<RefuelingRecord> updateRecord(@RequestBody RefuelingRecord refuelingRecord, @PathVariable Long rId) {
        return new ResponseEntity<>(refuelingRecordService.updateRecord(refuelingRecord,rId), HttpStatus.OK);
    }

    @DeleteMapping("/{rId}")
    public ResponseEntity<HttpStatus> deleteRecord(@PathVariable Long rId) {
        refuelingRecordService.deleteRecord(rId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/vehicle/{vId}")
    public ResponseEntity<List<RefuelingRecord>> getRecordsByVehicle(@PathVariable Long vId) {
        return new ResponseEntity<>(refuelingRecordService.getRecordsByVehicle(vId), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<RefuelingRecord>> getRecords() {
        return new ResponseEntity<>(refuelingRecordService.getAllRecords(), HttpStatus.OK);
    }
}
