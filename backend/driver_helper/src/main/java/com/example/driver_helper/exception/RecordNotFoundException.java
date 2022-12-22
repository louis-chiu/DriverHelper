package com.example.driver_helper.exception;

public class RecordNotFoundException extends RuntimeException {

    public RecordNotFoundException(Long id) {
        super("The record id '" + id + "' does not exist in our records");
    }

}
