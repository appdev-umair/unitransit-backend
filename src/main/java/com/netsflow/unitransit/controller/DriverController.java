package com.netsflow.unitransit.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netsflow.unitransit.service.DriverService;

@RestController
@RequestMapping("/driver")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    /**
     * Fetch assigned bus and route details for the driver.
     */
    @GetMapping("/assigned-details")
    @PreAuthorize("hasRole('ROLE_DRIVER')")
    public ResponseEntity<Map<String, Object>> getAssignedDetails(Authentication authentication) {
        // Extract driver email from token
        String email = authentication.getName();
        Map<String, Object> assignedDetails = driverService.getAssignedDetails(email);

        return ResponseEntity.ok(assignedDetails);
    }
}
