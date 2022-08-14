package com.telemessage.conrtollers;

import com.google.gson.Gson;
import com.telemessage.controllers.CheckNumberController;
import com.telemessage.dto.CheckNumberServiceInputDto;
import com.telemessage.exception.ErrorResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.*;
import java.util.stream.Stream;

import static com.telemessage.utils.Headers.getValidXSignature;
import static com.telemessage.utils.Headers.getXDate;
import static com.telemessage.utils.NumberGenerator.getRandomPhone;


public class CheckNumberControllerTest {

    private final CheckNumberController checkNumberController = new CheckNumberController();
    private static final Gson g = new Gson();

    @ParameterizedTest
    @ValueSource(ints = { 0, 2, 10 })
    public void checkCorrectSizesRequest(int inputSize){

        var response = checkNumberController.check(
                getXDate(),
                getValidXSignature (),
                createRandomCheckNumberServiceInputBySize(inputSize) );
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @ParameterizedTest()
    @MethodSource("headersTestData")
    public void checkHeadersIsNull(String xDate, String xSignature, ResponseEntity expectedResponse){

        List <CheckNumberServiceInputDto> input = new ArrayList<>();
        CheckNumberServiceInputDto number = new CheckNumberServiceInputDto("10123456789");
        input.add(number);
        var response = checkNumberController.check(
                xDate,
                xSignature,
                createRandomCheckNumberServiceInputBySize(1) );
      Assertions.assertEquals(response, expectedResponse);
    }

    @Test
    public void checkInvalidSize(){
        List <CheckNumberServiceInputDto> input = new ArrayList<>();
        for (int i=0; i<11; i++){
            input.add(new CheckNumberServiceInputDto(getRandomPhone(11)));
        }

        var response = checkNumberController.check(
                getXDate(),
                getValidXSignature (),
                input );

        String toMachSizeBody = g.toJson(new ErrorResponse("request exceeds maximum limit of numbers allowed max allowed 10"));
        ResponseEntity expectedResponse = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(toMachSizeBody);

        Assertions.assertEquals(response, expectedResponse);
    }
    private List<CheckNumberServiceInputDto> createRandomCheckNumberServiceInputBySize(int inputSize) {
        List <CheckNumberServiceInputDto> input = new ArrayList<>();
        for (int i=0; i<inputSize; i++){
            input.add(new CheckNumberServiceInputDto(getRandomPhone(11)));
        }
        return input;
    }
    private static Stream<Arguments> headersTestData() {

        String jsonNoDateBody = g.toJson(new ErrorResponse("Date header not present","ERROR" ));
        ResponseEntity responseNoDate = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(jsonNoDateBody);
        String jsonNoSignatureBody = g.toJson(new ErrorResponse("Signature header not present","ERROR" ));
        ResponseEntity responseNoSignatureBody = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(jsonNoSignatureBody);

        return Stream.of(
                Arguments.of(null, getValidXSignature (),  responseNoDate),
                Arguments.of(getXDate(), null,  responseNoSignatureBody),
                Arguments.of(null, null, responseNoDate)
        );
    }
}
