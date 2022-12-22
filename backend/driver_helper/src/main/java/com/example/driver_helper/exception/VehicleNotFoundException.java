package com.example.driver_helper.exception;

public class VehicleNotFoundException extends RuntimeException {

    public VehicleNotFoundException(Long id) {
        super("The vehicle id '" + id + "' does not exist in our records");
    }

}
