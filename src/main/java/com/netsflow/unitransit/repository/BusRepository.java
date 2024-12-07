package com.netsflow.unitransit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.netsflow.unitransit.model.Bus;

public interface BusRepository extends JpaRepository<Bus, Long> {
}
