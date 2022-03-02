package dev.wcs.tutoring;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ZipCityConverterServiceTest {

    @Test
    @Disabled
    public void testTravelEndpoint() {
        given()
          .when().get("/zip2city?zip=60435")
          .then()
             .statusCode(200)
             .body(containsString("Frankfurt"));
    }

}