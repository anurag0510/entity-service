package org.logistics.entityService.service;

import org.logistics.entityService.shared.PlaceDto;
import org.logistics.entityService.shared.RouteDto;

import java.util.List;

public interface RouteService {

    RouteDto createRoute(RouteDto routeDto, String requesterId);

    List<RouteDto> getAllRoutes(boolean all);

    List<RouteDto> getAllRouteBasedOnRid(String rid, boolean all);

    RouteDto updateRoute(String rid, RouteDto routeDto, String requesterId);

    RouteDto updateRouteStatus(String rid, RouteDto routeDto, String requesterId);

    RouteDto deleteRoute(RouteDto routeDto, String requesterId);
}