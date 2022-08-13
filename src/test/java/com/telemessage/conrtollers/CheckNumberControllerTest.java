package com.telemessage.conrtollers;

import com.google.gson.Gson;
import com.telemessage.controllers.CheckNumberController;
import com.telemessage.dto.CheckNumberServiceInputDto;
import com.telemessage.exception.ErrorResponse;
import org.apache.tomcat.util.json.JSONParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import java.util.*;
import java.util.stream.Stream;

import static com.telemessage.utils.Headers.getValidXSignature;
import static com.telemessage.utils.Headers.getXDate;


public class CheckNumberControllerTest {

    private final CheckNumberController checkNumberController = new CheckNumberController();
    Gson g = new Gson();


    @ParameterizedTest()
    @MethodSource("headersTestData")
    public void checkXDateIsNull(String xDate, String xSignature, HttpStatus expectedHttpStatus, String expectedStatus,
                                 String expectedStatusInfo ){

        List <CheckNumberServiceInputDto> input = new ArrayList<>();
        CheckNumberServiceInputDto number = new CheckNumberServiceInputDto("10123456789");
        input.add(number);
        var response = checkNumberController.check(
                xDate,
                xSignature,
                input );
        var body = response.getBody();
        var gd = (ErrorResponse)body;



        Assertions.assertAll(
                () -> Assertions.assertEquals(response.getStatusCode(), expectedHttpStatus),
                () -> Assertions.assertEquals(body.getStatus(), expectedStatus),
                () -> Assertions.assertEquals(body.getStatusInfo(), expectedStatusInfo)
        );
    }


    private static Stream<Arguments> headersTestData() {
        return Stream.of(
                Arguments.of(getXDate(), getValidXSignature (), HttpStatus.OK, null, "Success Mobile is a full US number" ),
                Arguments.of(null, getValidXSignature (),  HttpStatus.UNAUTHORIZED, "ERROR", "Date header not present" ),
                Arguments.of(getXDate(), null,  HttpStatus.UNAUTHORIZED, "ERROR", "Signature header not present" ),
                Arguments.of(null, null, HttpStatus.UNAUTHORIZED, "ERROR", "Date header not present" )
        );
    }
}
