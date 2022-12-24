package com.example.driver_helper.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="refueling_record")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefuelingRecord {

    @Id
    @NonNull
    @Column(name = "Rid")
    private Long rid;

    @Column(name = "Rdate")
    private String rdate;

    @Column(name = "gas_type")
    private String gastype;

    @Column(name = "liter")
    private Float liter;

    @Column(name = "price")
    private Long price;

    @Column(name = "notes")
    private String notes;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Vid" ,referencedColumnName = "Vid")
    private Vehicle vehicle;


}