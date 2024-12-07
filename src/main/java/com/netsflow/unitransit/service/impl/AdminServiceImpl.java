package com.netsflow.unitransit.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netsflow.unitransit.domain.Role;
import com.netsflow.unitransit.model.Bus;
import com.netsflow.unitransit.model.BusRouteAssignment;
import com.netsflow.unitransit.model.Driver;
import com.netsflow.unitransit.model.Route;
import com.netsflow.unitransit.model.Student;
import com.netsflow.unitransit.repository.BusRepository;
import com.netsflow.unitransit.repository.BusRouteAssignmentRepository;
import com.netsflow.unitransit.repository.DriverRepository;
import com.netsflow.unitransit.repository.RouteRepository;
import com.netsflow.unitransit.repository.UserRepository;
import com.netsflow.unitransit.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private BusRouteAssignmentRepository assignmentRepository;

    @Override
    public Bus addBus(Bus bus) {
        return busRepository.save(bus);
    }

    @Override

    public Bus updateBus(Long busId, Bus updatedBus) {
        Bus existingBus = busRepository.findById(busId)
                .orElseThrow();
        existingBus.setBusNumber(updatedBus.getBusNumber());
        existingBus.setPlateNumber(updatedBus.getPlateNumber());
        existingBus.setType(updatedBus.getType());
        existingBus.setIsOperational(updatedBus.getIsOperational());
        return busRepository.save(existingBus);
    }

    @Override
    public Route addRoute(Route route) {
        return routeRepository.save(route);
    }

    @Override
    public String assignBusToDriver(Long busId, Long routeId, Long driverId) {
        Bus bus = busRepository.findById(busId).orElseThrow(() -> new RuntimeException("Bus not found"));
        Route route = routeRepository.findById(routeId).orElseThrow(() -> new RuntimeException("Route not found"));
        Driver driver = driverRepository.findById(driverId).orElseThrow(() -> new RuntimeException("Driver not found"));

        BusRouteAssignment assignment = new BusRouteAssignment();
        assignment.setBus(bus);
        assignment.setRoute(route);
        assignment.setDriver(driver);

        assignmentRepository.save(assignment);
        return "Bus and Route assigned to Driver successfully";
    }

    @Override
    public List<BusRouteAssignment> getAssignmentsByRoute(Long routeId) {
        return assignmentRepository.findByRouteId(routeId);
    }

    @Override
    public String approveDriver(Long driverId) {
        Optional<Driver> optionalDriver = driverRepository.findById(driverId);

        if (optionalDriver.isEmpty()) {
            return "Driver not found.";
        }

        Driver driver = optionalDriver.get();
        if (driver.getIsApproved()) {
            return "Driver is already approved.";
        }

        driver.setIsApproved(true);
        driverRepository.save(driver);
        return "Driver account approved successfully.";
    }

    @Override
    public List<Student> getAllStudents() {
        // Cast Users with Role.STUDENT to Student type
        return userRepository.findByRole(Role.ROLE_STUDENT)
                .stream()
                .map(user -> (Student) user)
                .toList();
    }

    @Override
    public List<Driver> getAllDrivers() {
        // Cast Users with Role.DRIVER to Driver type
        return userRepository.findByRole(Role.ROLE_DRIVER)
                .stream()
                .map(user -> (Driver) user)
                .toList();
    }
}
