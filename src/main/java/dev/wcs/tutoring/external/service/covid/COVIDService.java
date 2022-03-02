package dev.wcs.tutoring.external.service.covid;

import dev.wcs.tutoring.external.service.covid.dto.CovidStatsDTO;

public interface COVIDService {

    CovidStatsDTO readCovidStatsFor(String city);

}
