package dev.wcs.tutoring.mocking;

import com.github.javafaker.Faker;
import com.google.common.collect.Lists;
import dev.wcs.tutoring.external.service.covid.COVIDService;
import dev.wcs.tutoring.external.service.route.RouteService;
import dev.wcs.tutoring.external.service.route.dto.RouteDTO;
import dev.wcs.tutoring.external.service.route.dto.Waypoint;
import dev.wcs.tutoring.external.service.weather.WeatherService;
import dev.wcs.tutoring.service.TravelService;
import dev.wcs.tutoring.service.domain.TravelInformation;
import dev.wcs.tutoring.service.repository.TravelHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@Slf4j
public class TestServiceMocking {

    private static NumberFormat nformat = NumberFormat.getInstance(Locale.getDefault());

    @Test
    public void createStubForServices1() throws ParseException {
        // Arrange
        RouteDTO routeDto = createRouteDto();

        RouteService routeServiceMock = Mockito.mock(RouteService.class);
        when(routeServiceMock.calculateRouteFromTo(anyString(), anyString())).thenReturn(routeDto);

        // Act
        RouteDTO res = routeServiceMock.calculateRouteFromTo("anystring", "anystring");

        // Assert
        assertThat(res).isEqualTo(routeDto);
        log.info(res.toString());
    }

    @Test
    public void createStubForServices2() throws ParseException {
        // Arrange
        RouteDTO routeDto = createRouteDto();

        RouteService routeServiceMock = Mockito.mock(RouteService.class);
        when(routeServiceMock.calculateRouteFromTo(eq("hello"), anyString())).thenReturn(routeDto);
        // don't do this, always use eq(...)
        // when(routeServiceMock.calculateRouteFromTo("hello", anyString())).thenReturn(routeDto);

        // Act
        RouteDTO res = routeServiceMock.calculateRouteFromTo("anystring", "anystring");

        // Assert
        assertThat(res).isNull();

        // Act
        res = routeServiceMock.calculateRouteFromTo("hello", "anystring");

        // Assert
        assertThat(res).isEqualTo(routeDto);
        log.info(res.toString());
    }

    @Test
    public void createMockForServices() throws ParseException {
        // Arrange
        TravelInformation.TravelInformationBuilder travelInformationBuilder = TravelInformation.builder();

        RouteService routeServiceMock = Mockito.mock(RouteService.class);
        WeatherService weatherServiceMock = Mockito.mock(WeatherService.class);
        COVIDService covidServiceMock = Mockito.mock(COVIDService.class);
        TravelHistoryRepository travelHistoryRepository = Mockito.mock(TravelHistoryRepository.class);
        TravelService sutService = new TravelService(covidServiceMock, routeServiceMock, weatherServiceMock, travelHistoryRepository);

        // Act
        sutService.collectTravelInformation("Darmstadt");

        // Assert
        verify(travelHistoryRepository, times(1)).storeTravelInformation(travelInformationBuilder.cityName("Darmstadt").build());
    }

    private RouteDTO createRouteDto() throws ParseException {
        Faker faker = new Faker(Locale.US);
        RouteDTO.RouteDTOBuilder routeDTOBuilder =  RouteDTO.builder();
        Waypoint.WaypointBuilder waypointBuilder = Waypoint.builder();
        waypointBuilder.lat(nformat.parse(faker.address().latitude()).doubleValue());
        waypointBuilder.lon(nformat.parse(faker.address().longitude()).doubleValue());
        waypointBuilder.identifier(faker.address().cityName());
        List<Waypoint> waypoints = Lists.newArrayList(waypointBuilder.build());
        routeDTOBuilder.waypoints(waypoints);
        RouteDTO routeDto = routeDTOBuilder.build();
        return routeDto;
    }
}
