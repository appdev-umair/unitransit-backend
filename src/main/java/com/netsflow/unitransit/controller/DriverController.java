package com.netsflow.unitransit.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.netsflow.unitransit.service.DriverService;
import com.netsflow.unitransit.service.LocationService;

@RestController
@RequestMapping("/driver")
public class DriverController {

    private final DriverService driverService;
    private final LocationService locationService;

    public DriverController(DriverService driverService, LocationService locationService) {
        this.driverService = driverService;
        this.locationService = locationService;

    }

    @PostMapping("/update-location")
    @PreAuthorize("hasRole('ROLE_DRIVER')")

    public ResponseEntity<String> updateLocation(
            @RequestParam Long busId,
            @RequestParam Double latitude,
            @RequestParam Double longitude) {

        locationService.updateLocation(busId, latitude, longitude);
        return ResponseEntity.ok("Location updated successfully");
    }

    /**
     * Fetch assigned bus and route details for the driver.
     */
    // @PostMapping("/update-location")
    // @PreAuthorize("hasRole('ROLE_DRIVER')")
    // public ResponseEntity<String> updateLocation(
    //         @RequestParam Long busId,
    //         @RequestParam Double latitude,
    //         @RequestParam Double longitude) {
    //     locationService.updateLocation(busId, latitude, longitude);
    //     return ResponseEntity.ok("Location updated successfully");
    // }
    @GetMapping("/assigned-details")
    @PreAuthorize("hasRole('ROLE_DRIVER')")
    public ResponseEntity<Map<String, Object>> getAssignedDetails(Authentication authentication) {
        // Extract driver email from token
        String email = authentication.getName();
        Map<String, Object> assignedDetails = driverService.getAssignedDetails(email);

        return ResponseEntity.ok(assignedDetails);
    }
}
