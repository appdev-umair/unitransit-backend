package com.netsflow.unitransit.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean isVerified = false;

    // Add this to your User class
    @Column(nullable = false)
    private String role = "USER"; // Default role is USER

    @Column
    private String verificationCode; // Store the OTP or unique code

    @Column
    private LocalDateTime otpExpirationTime; // Store OTP expiration time
}
