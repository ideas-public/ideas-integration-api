package com.ideas.ngi.ideas.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.ideas.ngi.ideas.client.auth.OAuth2Config;
import com.ideas.ngi.ideas.client.auth.OAuth2Provider;
import com.ideas.ngi.ideas.client.auth.OAuth2RequestInterceptorConfiguration;
import com.ideas.ngi.ideas.client.props.IDeaSProperties;
import com.ideas.ngi.ideas.client.props.IDeaSRetryProperties;
import com.ideas.ngi.ideas.client.reservation.IDeaSReservationV1ApiClient;
import com.ideas.ngi.ideas.client.reservation.model.IDeaSReservation;
import com.ideas.ngi.ideas.client.reservation.model.RoomStay;
import feign.RequestInterceptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static com.fasterxml.jackson.databind.DeserializationFeature.*;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static feign.Logger.Level.BASIC;
import static java.time.Duration.ofSeconds;

@SpringBootTest
@EnableWebSecurity
@ImportAutoConfiguration(OAuth2ClientAutoConfiguration.class)
@ContextConfiguration(classes = {OAuth2Config.class, OAuth2Provider.class, OAuth2RequestInterceptorConfiguration.class})
class IDeaSClientFactoryTest {

    @Autowired
    RequestInterceptor pmsInboundAuthInterceptor;

    @Test
    void build() {
        var iDeaSClientFactory = new IDeaSClientFactory(
                getObjectMapper(),
                BASIC,
                new IDeaSRetryProperties(ofSeconds(5), ofSeconds(30), 6),
                new IDeaSProperties("https://pmsinbound-internal.stage.ideasrms.com"),
                pmsInboundAuthInterceptor);

        var reservationClient = iDeaSClientFactory.<IDeaSReservationV1ApiClient>build(IDeaSDataType.RESERVATION);

        reservationClient.saveReservations(Arrays.asList(getNucleusReservation()));
    }

    private ObjectMapper getObjectMapper() {
        var javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));

        return new ObjectMapper()
                .registerModules(new Jdk8Module(), javaTimeModule)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .enable(ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                .enable(ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .disable(FAIL_ON_MISSING_CREATOR_PROPERTIES)
                .disable(FAIL_ON_NULL_CREATOR_PROPERTIES)
                .disable(FAIL_ON_IGNORED_PROPERTIES)
                .disable(WRITE_DATES_AS_TIMESTAMPS);
    }

    private IDeaSReservation getNucleusReservation() {
        var iDeaSReservation = new IDeaSReservation();
        iDeaSReservation.setClientCode("WHG"); // <----------- needs to be equal to what is stored in FDS UIS service
        iDeaSReservation.setPropertyCode("TESTPUB_1"); // <------ same here, property should be READWRITE
        iDeaSReservation.setCorrelationId("correlationId");
        iDeaSReservation.setBookingDate(OffsetDateTime.now());
        iDeaSReservation.setReservationId("reservationId");
        iDeaSReservation.setStatus(IDeaSReservation.StatusEnum.IN_HOUSE);
        iDeaSReservation.setChannel("channel");
        iDeaSReservation.setConfirmationNumber("confirmationNumber");
        iDeaSReservation.setCustomerValue("customerValue");
        iDeaSReservation.setNumberOfRooms(1);
        iDeaSReservation.setLastModifiedDate(OffsetDateTime.now());


        return iDeaSReservation;
    }

}