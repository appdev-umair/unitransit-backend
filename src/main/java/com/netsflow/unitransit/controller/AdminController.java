package com.netsflow.unitransit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.netsflow.unitransit.model.Bus;
import com.netsflow.unitransit.model.BusRouteAssignment;
import com.netsflow.unitransit.model.Driver;
import com.netsflow.unitransit.model.Route;
import com.netsflow.unitransit.model.Student;
import com.netsflow.unitransit.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * Approves a driver account (Only accessible by admin)
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/approve-driver")
    public ResponseEntity<String> approveDriver(@RequestParam Long driverId) {
        String response = adminService.approveDriver(driverId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all students
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/students")
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(adminService.getAllStudents());
    }

    /**
     * Get all drivers
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/drivers")
    public ResponseEntity<List<Driver>> getAllDrivers() {
        return ResponseEntity.ok(adminService.getAllDrivers());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add-bus")
    public ResponseEntity<Bus> addBus(@RequestBody Bus bus) {
        return ResponseEntity.ok(adminService.addBus(bus));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add-route")
    public ResponseEntity<Route> addRoute(@RequestBody Route route) {
        return ResponseEntity.ok(adminService.addRoute(route));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/assign-bus")
    public ResponseEntity<String> assignBusToDriver(@RequestParam Long busId, @RequestParam Long routeId, @RequestParam Long driverId) {
        return ResponseEntity.ok(adminService.assignBusToDriver(busId, routeId, driverId));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/assignments/{routeId}")
    public ResponseEntity<List<BusRouteAssignment>> getAssignmentsByRoute(@PathVariable Long routeId) {
        return ResponseEntity.ok(adminService.getAssignmentsByRoute(routeId));
    }
}
