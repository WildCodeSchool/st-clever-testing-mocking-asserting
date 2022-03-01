package dev.wcs.tutoring;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Path("/zip2city")
public class ZipCityConverter {

    private Map<String, ZipCityDto> zipCityMap;

    @PostConstruct
    public void init() throws IOException {
        Map<String, String> zipToCity = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<ZipCityDto> zipCities = mapper.readValue(new File("src/main/resources/germany_zip_codes.json"), new TypeReference<>() {});
        zipCityMap = zipCities.stream().collect(Collectors.toMap(ZipCityDto::getZip, Function.identity()));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ZipCityDto zipToCity(@QueryParam("zip") String zip) {
        return zipCityMap.get(zip);
    }

    public static class ZipCityDto {

        private String zip;
        private String city;

        public String getZip() {
            return zip;
        }

        public void setZip(String zip) {
            this.zip = zip;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

    }
}