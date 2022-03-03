package dev.wcs.tutoring.mocking;

import com.google.common.collect.Maps;
import dev.wcs.tutoring.mocking.assertions.TravelInformationAssert;
import dev.wcs.tutoring.service.domain.TravelInformation;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Condition;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class PlainAssertions {

    @Test
    public void testAssertionsOnPlanets() {
        List<String> allPlanets = Lists.newArrayList("Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune");
        assertThat(allPlanets).contains("Mercury", atIndex(0)).containsOnlyOnce("Earth", "Mars", "Jupiter");
        assertThat(allPlanets).isSortedAccordingTo(String::compareToIgnoreCase);
    }

    @Test
    public void testAssertionsOnMaps() {
        Map<String, BigDecimal> planetToMass = Maps.newHashMap();
        planetToMass.put("Mercury", new BigDecimal("3.30e23"));
        planetToMass.put("Venus", new BigDecimal("4.87e24"));
        planetToMass.put("Earth", new BigDecimal("5.97e24"));
        planetToMass.put("Mars", new BigDecimal("6.42e23"));
        planetToMass.put("Jupiter", new BigDecimal("1.90e27"));
        planetToMass.put("Saturn", new BigDecimal("5.68e26"));
        planetToMass.put("Uranus", new BigDecimal("8.68e25"));
        planetToMass.put("Neptune", new BigDecimal("1.02e26"));

        // check for multiple keys at once
        assertThat(planetToMass).containsKeys("Venus", "Earth", "Mars");
        // check if the value of a certain key satisfies a condition
        assertThat(planetToMass).hasEntrySatisfying("Venus", value -> assertThat(value).isGreaterThan(BigDecimal.ZERO));
        // check if all entries of an other map are contained in a map
        Map<String, BigDecimal> planetToMass2 = Maps.newHashMap();
        planetToMass2.put("Mercury", new BigDecimal("3.30e23"));
        planetToMass2.put("Venus", new BigDecimal("4.87e24"));
        assertThat(planetToMass).containsAllEntriesOf(planetToMass2);
    }

    public void testDates() {
        LocalDate todayMinus2Days = LocalDate.now().minusDays(2);
        assertThat(todayMinus2Days).isBefore("03-04-2020").isCloseTo("03-04-2020", within(1, ChronoUnit.DAYS));
        assertThat(todayMinus2Days).isBetween("2017-01-31", "2017-12-31");
    }

    @Test
    public void testFilesAndStreams() {
        File fileWithContent = new File("src/test/resources/countries.csv");
        assertThat(fileWithContent).hasContent("Algiers");

        // file assertions:
        assertThat(fileWithContent).exists();
        assertThat(contentOf(fileWithContent)).startsWith("Country");

        assertThat(fileWithContent).isNotEmpty();
    }

    @Test
    public void testCustomMessages() {
        TravelInformation travelInformation = TravelInformation.builder().cityName("Berlin").cases7day100k(1000).build();
        // is null?
        assertThat(travelInformation).isNotNull().withFailMessage("TravelInformation is null");
        // with description
        assertThat(travelInformation.getCases7day100k()).as("Covid information for city %s is empty", travelInformation.getCityName()).isNotNull();
    }

    @Test
    public void testCustomAssertions() {
        TravelInformation travelInformationWOWeather = TravelInformation.builder().cityName("Berlin").cases7day100k(1000).build();
        // has no weather information
        TravelInformationAssert.assertThat(travelInformationWOWeather).hasNoWeatherInformation();
        TravelInformation travelInformationWithWeather = TravelInformation.builder()
                .cityName("Berlin").cases7day100k(1000).weatherCondition("sunny").weatherTemperature(BigDecimal.TEN).build();
        // has weather information
        Condition<TravelInformation> weatherInformation = new Condition<>(
            ti -> (ti.getWeatherCondition() != null) && (ti.getWeatherTemperature() != null),
            "weather information");
        TravelInformationAssert.assertThat(travelInformationWithWeather).has(weatherInformation);
        TravelInformationAssert.assertThat(travelInformationWOWeather).doesNotHave(weatherInformation);
    }


}

