package com.netsflow.unitransit.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.netsflow.unitransit.model.Bus;
import com.netsflow.unitransit.model.BusRouteAssignment;
import com.netsflow.unitransit.model.Driver;
import com.netsflow.unitransit.model.Route;
import com.netsflow.unitransit.repository.BusRouteAssignmentRepository;
import com.netsflow.unitransit.repository.DriverRepository;

@Service
public class DriverService {

    private final DriverRepository driverRepository;
    private final BusRouteAssignmentRepository assignmentRepository;

    public DriverService(DriverRepository driverRepository, BusRouteAssignmentRepository assignmentRepository) {
        this.driverRepository = driverRepository;
        this.assignmentRepository = assignmentRepository;
    }

    public Map<String, Object> getAssignedDetails(String email) {
        // Fetch the driver by email
        Driver driver = driverRepository.findByEmail(email)
                .orElseThrow();

        // Fetch the assignment for the driver
        BusRouteAssignment assignment = assignmentRepository.findByDriver(driver)
                .orElseThrow();

        Bus bus = assignment.getBus();
        Route route = assignment.getRoute();

        // Prepare response map
        Map<String, Object> response = new HashMap<>();
        response.put("driverName", driver.getName());
        response.put("busDetails", Map.of(
                "busNumber", bus.getBusNumber(),
                "plateNumber", bus.getPlateNumber(),
                "type", bus.getType(),
                "isOperational", bus.getIsOperational()
        ));
        response.put("routeDetails", Map.of(
                "routeName", route.getRouteName(),
                "startLocation", route.getStartLocation(),
                "endLocation", route.getEndLocation(),
                "timings", route.getTimings()
        ));

        return response;
    }
}
