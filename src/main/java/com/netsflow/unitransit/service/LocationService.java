package com.netsflow.unitransit.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.netsflow.unitransit.model.Bus;
import com.netsflow.unitransit.model.BusLocation;
import com.netsflow.unitransit.repository.BusLocationRepository;
import com.netsflow.unitransit.repository.BusRepository;

@Service
public class LocationService {

    private final BusRepository busRepository;
    private final BusLocationRepository locationRepository;
    private final BusTrackingBroadcaster broadcaster; // Use broadcaster instead of controller

    public LocationService(
            BusRepository busRepository,
            BusLocationRepository locationRepository,
            BusTrackingBroadcaster broadcaster) {
        this.busRepository = busRepository;
        this.locationRepository = locationRepository;
        this.broadcaster = broadcaster;
    }

    @Transactional
    public void updateLocationViaWebSocket(Long busId, Double latitude, Double longitude) {
        Bus bus = busRepository.findById(busId)
                .orElseThrow();

        // Check if location already exists for the bus
        BusLocation location = locationRepository.findByBus(bus)
                .orElse(new BusLocation());

        // Update location details
        // Update location details
        location.setBus(bus);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setLastUpdated(LocalDateTime.now());
        // Save to database
        locationRepository.save(location);

        // Broadcast updated location using broadcaster
        broadcaster.broadcastBusLocation(location);
    }

    public void updateLocation(Long busId, Double latitude, Double longitude) {
        Bus bus = busRepository.findById(busId)
                .orElseThrow();

        // Check if location already exists for the bus
        BusLocation location = locationRepository.findByBus(bus)
                .orElse(new BusLocation());

        // Update location details
        location.setBus(bus);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setLastUpdated(LocalDateTime.now());

        // Save the updated location
        locationRepository.save(location);
    }

    // Existing method for REST API
    public BusLocation getBusLocation(Long busId) {
        Bus bus = busRepository.findById(busId)
                .orElseThrow();

        return locationRepository.findByBus(bus)
                .orElseThrow();
    }
}
