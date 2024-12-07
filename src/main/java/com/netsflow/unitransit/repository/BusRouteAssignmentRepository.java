package com.netsflow.unitransit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.netsflow.unitransit.model.BusRouteAssignment;
import com.netsflow.unitransit.model.Driver;

public interface BusRouteAssignmentRepository extends JpaRepository<BusRouteAssignment, Long> {

    Optional<BusRouteAssignment> findByDriver(Driver driver);

    List<BusRouteAssignment> findByRouteId(Long routeId);
}
