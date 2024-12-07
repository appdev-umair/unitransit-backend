package com.netsflow.unitransit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.netsflow.unitransit.model.Bus;
import com.netsflow.unitransit.model.BusLocation;

@Repository
public interface BusLocationRepository extends JpaRepository<BusLocation, Long> {

    Optional<BusLocation> findByBus(Bus bus);
}
