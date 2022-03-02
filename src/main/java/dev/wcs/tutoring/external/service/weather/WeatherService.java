package dev.wcs.tutoring.external.service.weather;

import dev.wcs.tutoring.external.service.weather.dto.WeatherDTO;

public interface WeatherService {

    WeatherDTO getCurrentWeatherConditionsForCity(String city);

}
