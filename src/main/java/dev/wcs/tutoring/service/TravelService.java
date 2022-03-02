package dev.wcs.tutoring.service;

import dev.wcs.tutoring.external.service.covid.COVIDService;
import dev.wcs.tutoring.external.service.route.RouteService;
import dev.wcs.tutoring.external.service.weather.WeatherService;
import dev.wcs.tutoring.service.domain.TravelInformation;
import dev.wcs.tutoring.service.repository.TravelHistoryRepository;

public class TravelService {

    private final COVIDService covidService;
    private final RouteService routeService;
    private final WeatherService weatherService;
    private final TravelHistoryRepository travelHistoryRepository;

    public TravelService(COVIDService covidService, RouteService routeService, WeatherService weatherService, TravelHistoryRepository travelHistoryRepository) {
        this.covidService = covidService;
        this.routeService = routeService;
        this.weatherService = weatherService;
        this.travelHistoryRepository = travelHistoryRepository;
    }

    public TravelInformation collectTravelInformation(String city) {
        TravelInformation travelInformation = TravelInformation.builder().cityName(city).build();
        travelHistoryRepository.storeTravelInformation(travelInformation);
        return travelInformation;
    }

}
