package com.telemessage.Dto;

import com.telemessage.dto.CheckNumberServiceInputDto;
import com.telemessage.dto.ResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class ResponseDtoTest {

    @Test
    public void createDtoByCorrectNumber() throws ParseException {
        Date startDate = Date.from(Instant.now().minusSeconds(2));
        ResponseDto responseDto = new ResponseDto(new CheckNumberServiceInputDto("11234567890"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss*SSSZZZZ");

        Assertions.assertAll(
                () -> Assertions.assertEquals("11234567890", responseDto.getMdn()),
                () -> Assertions.assertTrue(responseDto.getTransactionId().contains("11234567890")),
                () -> Assertions.assertEquals("Full", responseDto.getType()),
                () -> {
                    Assertions.assertTrue(
                            formatter.parse( responseDto.getRequestReceivedDate()).before(Date.from(Instant.now().plusSeconds(2)))
                            && formatter.parse( responseDto.getRequestReceivedDate()).after(startDate)
                    );
                },
                () -> Assertions.assertEquals(2, responseDto.getCurrentStatus() ),
                () -> Assertions.assertEquals("Success Mobile is a full US number", responseDto.getStatusInfo())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = { "21234567890", "123456789012","023456789"})
    public void createDtoByNotUsNumber(String number){
        ResponseDto responseDto = new ResponseDto(new CheckNumberServiceInputDto(number));
        Assertions.assertAll(
                () -> Assertions.assertEquals(number, responseDto.getMdn()),
                () -> Assertions.assertEquals("NotUS", responseDto.getType()),
                () -> Assertions.assertEquals(4, responseDto.getCurrentStatus() ),
                () -> Assertions.assertEquals("Mobile is not US number", responseDto.getStatusInfo())
        );
    }

    @Test
    public void createDtoByNotFullNumber(){
        ResponseDto responseDto = new ResponseDto(new CheckNumberServiceInputDto("1234567890"));
        Assertions.assertAll(
                () -> Assertions.assertEquals("1234567890", responseDto.getMdn()),
                () -> Assertions.assertEquals("NotFull", responseDto.getType()),
                () -> Assertions.assertEquals(4, responseDto.getCurrentStatus() ),
                () -> Assertions.assertEquals("Mobile is not a full number", responseDto.getStatusInfo())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = { "", " 0123456789","123q4567890","1234<567890"})
    public void createDtoByNotNumber(String number){
        ResponseDto responseDto = new ResponseDto(new CheckNumberServiceInputDto(number));
        Assertions.assertAll(
                () -> Assertions.assertEquals(number, responseDto.getMdn()),
                () -> Assertions.assertEquals("NotNumber", responseDto.getType()),
                () -> Assertions.assertEquals(-1, responseDto.getCurrentStatus() ),
                () -> Assertions.assertEquals("It is not a phone number", responseDto.getStatusInfo())
        );
    }

    @Test
    public void createDtoByNumberWithoutCode(){
        ResponseDto responseDto = new ResponseDto(new CheckNumberServiceInputDto("0123456789"));
        Assertions.assertAll(
                () -> Assertions.assertEquals("0123456789", responseDto.getMdn()),
                () -> Assertions.assertEquals("NoCountryCode", responseDto.getType()),
                () -> Assertions.assertEquals(4, responseDto.getCurrentStatus() ),
                () -> Assertions.assertEquals("Mobile does not have a country code", responseDto.getStatusInfo())
        );
    }

}
