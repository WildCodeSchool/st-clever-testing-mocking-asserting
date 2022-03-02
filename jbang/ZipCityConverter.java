//DEPS io.quarkus:quarkus-bom:${quarkus.version:1.11.0.Final}@pom
//DEPS io.quarkus:quarkus-resteasy
//DEPS io.quarkus:quarkus-resteasy-jackson
//DEPS io.quarkus:quarkus-smallrye-openapi
//DEPS io.quarkus:quarkus-swagger-ui
//DEPS org.projectlombok:lombok:1.18.22
//Q:CONFIG quarkus.swagger-ui.always-include=true
//FILES ../src/main/resources/germany_zip_codes.json

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Path("/city")
@ApplicationScoped
public class ZipCityConverter {

    private Map<String, ZipCityDto> zipCityMap;

    @PostConstruct
    public void init() throws IOException {
        Map<String, String> zipToCity = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // to not marshall unnecessary "ID" property
        InputStream resourceFromClassLoader = getClass().getClassLoader().getResourceAsStream("germany_zip_codes.json");
        List<ZipCityDto> zipCities = mapper.readValue(resourceFromClassLoader, new TypeReference<>() {});
        zipCityMap = zipCities.stream().collect(Collectors.toMap(ZipCityDto::getZip, Function.identity()));
    }

    @GET
    @Path("/zip")
    @Produces(MediaType.APPLICATION_JSON)
    public ZipCityDto zipToCity(@QueryParam("zip") String zip) {
        return zipCityMap.get(zip);
    }

    @GET
    @Path("/coord")
    @Produces(MediaType.APPLICATION_JSON)
    public ZipCityDto cityToCoord(@QueryParam("city") String city) {
        return zipCityMap.values().stream().filter(it -> it.getCity().equalsIgnoreCase(city)).findFirst().get();
    }

    @Data
    public static class ZipCityDto {

        private String zip;
        private String city;
        private Double lat;
        private Double lon;

    }
}