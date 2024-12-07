package com.netsflow.unitransit.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "buses")
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String busNumber; // Unique identifier for the bus

    @Column(nullable = false, unique = true)
    private String plateNumber; // Unique license plate number

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BusType type; // Girls, Boys, or Combined bus

    @Column(nullable = false)
    private Boolean isOperational = true; // Indicates if the bus is active or inactive
}
