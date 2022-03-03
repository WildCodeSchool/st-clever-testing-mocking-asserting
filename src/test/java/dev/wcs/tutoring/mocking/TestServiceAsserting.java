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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@Slf4j
public class TestServiceAsserting {

    private static NumberFormat nformat = NumberFormat.getInstance(Locale.getDefault());

    @Test
    public void createMockForServicesWithJUnitAssertion() throws ParseException {
        // Arrange
        RouteDTO routeDto = createRouteDto();

        RouteService routeServiceMock = Mockito.mock(RouteService.class);
        when(routeServiceMock.calculateRouteFromTo(anyString(), anyString())).thenReturn(routeDto);

        // Act
        RouteDTO res = routeServiceMock.calculateRouteFromTo("anystring", "anystring");

        // Assert JUnit
        assertEquals(res, routeDto);
        assertEquals(routeDto, res);
        assertEquals(res, routeDto, "Objects are not equal!");

        log.info(res.toString());
    }

    @Test
    public void createMockForServicesWithAssertJAssertion() throws ParseException {
        // Arrange
        RouteDTO routeDto = createRouteDto();

        RouteService routeServiceMock = Mockito.mock(RouteService.class);
        when(routeServiceMock.calculateRouteFromTo(anyString(), anyString())).thenReturn(routeDto);

        // Act
        RouteDTO res = routeServiceMock.calculateRouteFromTo("anystring", "anystring");

        // routeDTO = null;

        // Assert AssertJ
        assertThat(routeDto).isNotNull().isEqualTo(res);

        log.info(res.toString());
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
