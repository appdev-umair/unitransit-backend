package com.netsflow.unitransit.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.netsflow.unitransit.model.BusLocation;

@Component
public class BusTrackingBroadcaster {

    private final SimpMessagingTemplate messagingTemplate;

    public BusTrackingBroadcaster(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Method to broadcast location updates
    public void broadcastBusLocation(BusLocation location) {
        System.out.println("Umair");
        messagingTemplate.convertAndSend(
                "/topic/bus-tracking/" + location.getId(),
                location
        );
    }
}
