package dev.wcs.tutoring.external.service.route.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Waypoint {

    private double lon;
    private double lat;
    private String identifier;

}
