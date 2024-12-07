package com.netsflow.unitransit.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Admin extends User {

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0") // Default to false
    private boolean isActive = false;

}
