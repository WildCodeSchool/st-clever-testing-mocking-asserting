package dev.wcs.tutoring.external.service.covid.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CovidStatsDTO {

    private Integer cases7day100k;
    private String cityName;

}
