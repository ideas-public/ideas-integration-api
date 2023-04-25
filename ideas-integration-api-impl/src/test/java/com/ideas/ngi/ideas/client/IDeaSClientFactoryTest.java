package com.ideas.ngi.ideas.client;

import com.ideas.ngi.ideas.client.auth.OAuth2Config;
import com.ideas.ngi.ideas.client.auth.OAuth2Provider;
import com.ideas.ngi.ideas.client.auth.OAuth2RequestInterceptorConfiguration;
import com.ideas.ngi.ideas.client.reservation.IDeaSReservationV1ApiClient;
import com.ideas.ngi.ideas.client.reservation.model.IDeaSRate;
import com.ideas.ngi.ideas.client.reservation.model.IDeaSReservation;
import com.ideas.ngi.ideas.client.reservation.model.IDeaSRoomStay;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@EnableWebSecurity
@ImportAutoConfiguration(OAuth2ClientAutoConfiguration.class)
@ContextConfiguration(classes = {
        OAuth2Config.class,
        OAuth2Provider.class,
        OAuth2RequestInterceptorConfiguration.class,
        IDeaSClientFactory.class,
        TestConfiguration.class
})
class IDeaSClientFactoryTest {

    @Autowired
    private IDeaSClientFactory iDeaSClientFactory;

    @Test
    void build() {
        var reservationClient = iDeaSClientFactory.<IDeaSReservationV1ApiClient>build(IDeaSDataType.RESERVATION);
        ResponseEntity<Void> response = reservationClient.saveReservations(Collections.singletonList(getNucleusReservation()));
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    private IDeaSReservation getNucleusReservation() {
        // All required fields should be fulfilled, otherwise 400 BAD_REQUEST will be returned
        var iDeaSReservation = new IDeaSReservation();
        iDeaSReservation.setClientCode("TEST"); // <----------- needs to be equal to what is stored in IDeaS
        iDeaSReservation.setPropertyCode("TESTPUB_1"); // <------ same here, property should be READWRITE
        iDeaSReservation.setCorrelationId("correlationId");
        iDeaSReservation.setBookingDate(OffsetDateTime.now());
        iDeaSReservation.setReservationId("reservationId");
        iDeaSReservation.setStatus(IDeaSReservation.StatusEnum.IN_HOUSE);
        iDeaSReservation.setChannel("channel");
        iDeaSReservation.setConfirmationNumber("confirmationNumber");
        iDeaSReservation.setCustomerValue("customerValue");
        iDeaSReservation.setNumberOfRooms(1);

        var ideasRoomStay = new IDeaSRoomStay();
        ideasRoomStay.setArrivalDate(LocalDate.now());
        ideasRoomStay.setDepartureDate(LocalDate.now().plusDays(1));
        ideasRoomStay.setBookedRoomTypeCode("roomTypeCode");
        ideasRoomStay.setMarketCode("marketCode");
        ideasRoomStay.setRoomTypeCode("DLX");
        ideasRoomStay.setRateCode("rateCode");
        ideasRoomStay.setSourceBookingCode("sourceBookingCode");
        ideasRoomStay.setNumberOfChildren(1);
        ideasRoomStay.setNumberOfAdults(2);
        ideasRoomStay.setBookingType("bookingType");

        var ideasRate = new IDeaSRate();
        ideasRate.setRateValue(BigDecimal.ONE);
        ideasRate.setStartDate(LocalDate.now());
        ideasRate.setEndDate(LocalDate.now().plusDays(1));

        ideasRoomStay.setRates(Collections.singletonList(ideasRate));
        iDeaSReservation.setRoomStays(Collections.singletonList(ideasRoomStay));

        return iDeaSReservation;
    }
}