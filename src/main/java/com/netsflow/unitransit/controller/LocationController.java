package com.netsflow.unitransit.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netsflow.unitransit.model.BusLocation;
import com.netsflow.unitransit.service.LocationService;

@RestController
@RequestMapping("/location")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/{busId}")
    public ResponseEntity<BusLocation> getBusLocation(@PathVariable Long busId) {
        BusLocation location = locationService.getBusLocation(busId);
        return ResponseEntity.ok(location);
    }
}
