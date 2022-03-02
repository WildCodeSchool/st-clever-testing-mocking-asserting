package dev.wcs.tutoring.external.service.zip2city;

import dev.wcs.tutoring.external.service.zip2city.dto.ZipCityDto;

public interface ZipToCityService {

    ZipCityDto zipToCity(String city);

}
