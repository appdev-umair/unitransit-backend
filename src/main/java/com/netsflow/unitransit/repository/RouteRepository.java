package com.netsflow.unitransit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.netsflow.unitransit.model.Route;

public interface RouteRepository extends JpaRepository<Route, Long> {
}
