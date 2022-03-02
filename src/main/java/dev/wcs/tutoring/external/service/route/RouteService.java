package dev.wcs.tutoring.external.service.route;

import dev.wcs.tutoring.external.service.route.dto.RouteDTO;

public interface RouteService {

    RouteDTO calculateRouteFromTo(String fromCity, String toCity);

}
