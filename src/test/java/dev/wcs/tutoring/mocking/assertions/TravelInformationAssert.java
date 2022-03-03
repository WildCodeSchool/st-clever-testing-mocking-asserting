package dev.wcs.tutoring.mocking.assertions;

import dev.wcs.tutoring.service.domain.TravelInformation;
import org.assertj.core.api.AbstractAssert;

public class TravelInformationAssert extends AbstractAssert<TravelInformationAssert, TravelInformation> {

    public TravelInformationAssert(TravelInformation actual) {
        super(actual, TravelInformationAssert.class);
    }

    public static TravelInformationAssert assertThat(TravelInformation actual) {
        return new TravelInformationAssert(actual);
    }

    public TravelInformationAssert hasNoWeatherInformation() {
        // check that actual User we want to make assertions on is not null.
        isNotNull();

        // overrides the default error message with a more explicit one
        String assertjErrorMessage = "\nExpecting TravelInformation to have no weather information \n but was not null\n";

        if ((actual.getWeatherCondition() != null) || (actual.getWeatherTemperature() != null)) {
            failWithMessage(assertjErrorMessage, actual);
        }

        return this;
    }

}