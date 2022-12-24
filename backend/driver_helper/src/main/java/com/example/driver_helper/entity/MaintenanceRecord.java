package com.example.driver_helper.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="maintenance_record") //拼字錯誤！
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MaintenanceRecord {

    @Id
    @NonNull
    @Column(name = "Mid")
    private Long mid;

    @Column(name = "Mdate")
    private String mdate;

    @Column(name = "items")
    private String items;

    @Column(name = "price")
    private Long price;

    @Column(name = "notes")
    private String notes;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Vid" ,referencedColumnName = "Vid")
    private Vehicle vehicle;
}