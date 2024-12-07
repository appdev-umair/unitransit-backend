package com.netsflow.unitransit.service;

import java.util.List;

import com.netsflow.unitransit.model.Bus;
import com.netsflow.unitransit.model.BusRouteAssignment;
import com.netsflow.unitransit.model.Driver;
import com.netsflow.unitransit.model.Route;
import com.netsflow.unitransit.model.Student;

public interface AdminService {

    String approveDriver(Long driverId);

    List<Student> getAllStudents();

    List<Driver> getAllDrivers();

    Bus addBus(Bus bus);

    public Bus updateBus(Long busId, Bus updatedBus);

    Route addRoute(Route route);

    String assignBusToDriver(Long busId, Long routeId, Long driverId);

    List<BusRouteAssignment> getAssignmentsByRoute(Long routeId);
}
