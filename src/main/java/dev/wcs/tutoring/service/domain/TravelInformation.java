package dev.wcs.tutoring.service.domain;

import dev.wcs.tutoring.external.service.route.dto.Waypoint;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class TravelInformation {

    private String cityName;
    private String weatherCondition;
    private BigDecimal weatherTemperature;
    private List<Waypoint> waypoints;
    private Integer cases7day100k;

}
