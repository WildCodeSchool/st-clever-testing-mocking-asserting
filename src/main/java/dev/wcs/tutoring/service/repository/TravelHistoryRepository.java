package dev.wcs.tutoring.service.repository;


import dev.wcs.tutoring.service.domain.TravelInformation;

public interface TravelHistoryRepository {

    void storeTravelInformation(TravelInformation travelInformation);

}
