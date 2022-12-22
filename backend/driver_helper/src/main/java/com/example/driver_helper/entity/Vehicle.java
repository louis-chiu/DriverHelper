package com.example.driver_helper.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="vehicle_info")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {

    @Id
    @Column(name = "Vid")
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="MFD")
    private String mfd;

    @Column(name="mileage")
    private Long mileage;

    @Column(name="type")
    private String type;

    @Column(name="brand")
    private String brand;

    @Column(name="model")
    private String model;

    @JsonIgnore
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<RefuelingRecord> refuelingRecords;

    @JsonIgnore
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<MaintenanceRecord> maintenanceRecords;

}
