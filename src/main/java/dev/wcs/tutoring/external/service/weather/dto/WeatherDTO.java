package dev.wcs.tutoring.external.service.weather.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class WeatherDTO {

    private String condition;
    private BigDecimal temperature;

}
