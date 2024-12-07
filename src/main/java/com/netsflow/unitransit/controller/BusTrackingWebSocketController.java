package com.netsflow.unitransit.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.netsflow.unitransit.model.BusLocation;
import com.netsflow.unitransit.service.LocationService;

@Controller
public class BusTrackingWebSocketController {

    private final LocationService locationService;

    public BusTrackingWebSocketController(LocationService locationService) {
        this.locationService = locationService;
    }

    // Driver updates location via WebSocket
    @MessageMapping("/update-location/{busId}")
    @SendTo("/topic/bus-tracking/{busId}")
    public BusLocation updateLocation(
            @DestinationVariable Long busId,
            BusLocation locationUpdate) {
        // Update location in database
        locationService.updateLocationViaWebSocket(busId, locationUpdate.getLatitude(), locationUpdate.getLongitude());

        // Broadcast to all subscribers (already handled in LocationService)
        return locationUpdate;
    }
}
