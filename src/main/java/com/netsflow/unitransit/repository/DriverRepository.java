package com.netsflow.unitransit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.netsflow.unitransit.model.Driver;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    Optional<Driver> findByEmail(String email);
}
