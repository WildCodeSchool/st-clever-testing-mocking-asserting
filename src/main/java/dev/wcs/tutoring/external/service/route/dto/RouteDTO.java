package dev.wcs.tutoring.external.service.route.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
public class RouteDTO {

    private List<Waypoint> waypoints;

}
