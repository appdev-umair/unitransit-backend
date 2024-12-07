package com.netsflow.unitransit.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@DiscriminatorValue("STUDENT")

public class Student extends User {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String gender;
}
