package dev.wcs.tutoring.mocking;

import com.github.javafaker.Faker;
import com.google.common.collect.Lists;
import dev.wcs.tutoring.external.service.covid.COVIDService;
import dev.wcs.tutoring.external.service.covid.dto.CovidStatsDTO;
import dev.wcs.tutoring.external.service.route.RouteService;
import dev.wcs.tutoring.external.service.route.dto.RouteDTO;
import dev.wcs.tutoring.external.service.route.dto.Waypoint;
import dev.wcs.tutoring.external.service.weather.WeatherService;
import dev.wcs.tutoring.external.service.weather.dto.WeatherDTO;
import dev.wcs.tutoring.service.TravelService;
import dev.wcs.tutoring.service.domain.TravelInformation;
import dev.wcs.tutoring.service.repository.TravelHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@Slf4j
public class TestServiceMocking {

    private static NumberFormat nformat = NumberFormat.getInstance(Locale.getDefault());

    private RouteService routeServiceMock;
    private WeatherService weatherServiceMock;
    private COVIDService covidServiceMock;
    private TravelHistoryRepository travelHistoryRepository;

    @Test
    public void createStubForServices1() throws ParseException {
        // Arrange
        RouteDTO routeDto = createRouteDto();

        RouteService routeServiceMock = Mockito.mock(RouteService.class);
        when(routeServiceMock.calculateRouteFromTo(anyString(), anyString())).thenReturn(routeDto);

        // Act
        RouteDTO res = routeServiceMock.calculateRouteFromTo("donald", "duck");

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
        RouteDTO res = routeServiceMock.calculateRouteFromTo("donald", "duck");

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
        arrangeMocks();
        TravelService sutService = new TravelService(covidServiceMock, routeServiceMock, weatherServiceMock, travelHistoryRepository);

        // Act
        sutService.collectTravelInformation("Darmstadt");

        // Assert - does not work!
        // verify(travelHistoryRepository, times(1)).storeTravelInformation(TravelInformation.builder().cityName("Darmstadt").build());

        // Assert with Capture
        ArgumentCaptor<TravelInformation> argumentCaptor = ArgumentCaptor.forClass(TravelInformation.class);
        verify(travelHistoryRepository).storeTravelInformation(argumentCaptor.capture());
        TravelInformation capturedArgument = argumentCaptor.getValue();
        assertThat(capturedArgument.getCityName()).isEqualTo("Darmstadt");
    }


    @Test
    public void createMockForServicesWithCapture() throws ParseException {
        // Arrange
        arrangeMocks();
        TravelService sutService = new TravelService(covidServiceMock, routeServiceMock, weatherServiceMock, travelHistoryRepository);

        // Act
        sutService.collectTravelInformation("Darmstadt");

        // Assert only invocation times: 1
        verify(travelHistoryRepository, times(1)).storeTravelInformation(any(TravelInformation.class));
    }

    @Test
    public void createMockForServicesThrowException() throws ParseException {
        // Arrange
        arrangeMocks();
        TravelService sutService = new TravelService(covidServiceMock, routeServiceMock, weatherServiceMock, travelHistoryRepository);

        doThrow(NullPointerException.class).when(travelHistoryRepository).storeTravelInformation(eq(TravelInformation.builder().build()));

        // Act & Assert
        try {
            sutService.collectTravelInformation(null);
            fail("NPE expected.");
        } catch (NullPointerException npe) {
            // continue, was expected
        }

        // Act
        sutService.collectTravelInformation("Darmstadt");

        // Assert only invocation times: 1
        verify(travelHistoryRepository, times(1)).storeTravelInformation(any(TravelInformation.class));
    }

    @Test
    public void createMockForServicesInvokeInOrder() throws ParseException {
        // Arrange
        arrangeMocks();
        TravelService sutService = new TravelService(covidServiceMock, routeServiceMock, weatherServiceMock, travelHistoryRepository);

        // Act
        sutService.collectTravelInformation("Darmstadt");

        // Assert invocation inOrder
        InOrder inOrder = Mockito.inOrder(routeServiceMock, weatherServiceMock, covidServiceMock, travelHistoryRepository);
        inOrder.verify(covidServiceMock).readCovidStatsFor(anyString());
        inOrder.verify(routeServiceMock).calculateRouteFromTo(anyString(), anyString());
        inOrder.verify(weatherServiceMock).getCurrentWeatherConditionsForCity(anyString());
        // inOrder.verify(travelHistoryRepository, times(1)).storeTravelInformation(travelInformationBuilder.cityName("Darmstadt").build());
    }

    private void arrangeMocks() throws ParseException {
        routeServiceMock = Mockito.mock(RouteService.class);
        RouteDTO routeDTO = createRouteDto();
        when(routeServiceMock.calculateRouteFromTo(anyString(), anyString())).thenReturn(routeDTO);

        weatherServiceMock = Mockito.mock(WeatherService.class);
        WeatherDTO weatherDTO = createWeatherDTO();
        when(weatherServiceMock.getCurrentWeatherConditionsForCity(anyString())).thenReturn(weatherDTO);

        covidServiceMock = Mockito.mock(COVIDService.class);
        CovidStatsDTO covidStatsDTO = createCovidStatsDTO();
        when(covidServiceMock.readCovidStatsFor(anyString())).thenReturn(covidStatsDTO);

        travelHistoryRepository = Mockito.mock(TravelHistoryRepository.class);
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

    private WeatherDTO createWeatherDTO() {
        Faker faker = new Faker(Locale.US);
        WeatherDTO.WeatherDTOBuilder weatherDTOBuilder = WeatherDTO.builder();
        weatherDTOBuilder.condition(faker.weather().description());
        weatherDTOBuilder.temperature(new BigDecimal(faker.number().randomDigitNotZero()));
        return weatherDTOBuilder.build();
    }

    private CovidStatsDTO createCovidStatsDTO() {
        Faker faker = new Faker(Locale.US);
        CovidStatsDTO.CovidStatsDTOBuilder covidStatsDTOBuilder = CovidStatsDTO.builder();
        covidStatsDTOBuilder.cases7day100k(faker.number().randomDigitNotZero());
        covidStatsDTOBuilder.cityName(faker.address().cityName());
        return covidStatsDTOBuilder.build();
    }

}
