package com.netsflow.unitransit.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netsflow.unitransit.model.Bus;
import com.netsflow.unitransit.model.BusRouteAssignment;
import com.netsflow.unitransit.model.Route;
import com.netsflow.unitransit.repository.BusRouteAssignmentRepository;
import com.netsflow.unitransit.repository.RouteRepository;

@RestController
@RequestMapping("/public")
public class PublicController {

    private final RouteRepository routeRepository;
    private final BusRouteAssignmentRepository busRouteAssignmentRepository;

    public PublicController(RouteRepository routeRepository, BusRouteAssignmentRepository busRouteAssignmentRepository) {
        this.routeRepository = routeRepository;
        this.busRouteAssignmentRepository = busRouteAssignmentRepository;
    }

    @GetMapping("/routes")
    public ResponseEntity<List<Route>> getAllRoutes() {
        List<Route> routes = routeRepository.findAll();
        return ResponseEntity.ok(routes);
    }

    @GetMapping("/buses/{routeId}")
    public ResponseEntity<List<Bus>> getBusesByRoute(@PathVariable Long routeId) {
        List<Bus> buses = busRouteAssignmentRepository.findByRouteId(routeId)
                .stream()
                .map(BusRouteAssignment::getBus)
                .toList();
        return ResponseEntity.ok(buses);
    }
}
