package com.netsflow.unitransit.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "drivers")
public class Driver extends User {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false, unique = true)
    private String license; // Driving license number

    @Column(nullable = false)
    private Boolean isApproved = false; // Admin approval status
}
