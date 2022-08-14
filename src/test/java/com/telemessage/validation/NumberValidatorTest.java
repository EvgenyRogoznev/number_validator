package com.telemessage.validation;

import com.telemessage.dto.CheckNumberServiceInputDto;
import com.telemessage.dto.Type;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

import static com.telemessage.validation.NumberValidator.checkNumber;


class NumberValidatorTest {

    @ParameterizedTest
    @MethodSource("numbersAndResults")
    void checkNumberTest(String number, Type expectedType) {

        Assertions.assertEquals(expectedType ,checkNumber(new CheckNumberServiceInputDto(number)));
    }

    private static Stream<Arguments> numbersAndResults() {
        return Stream.of(
                Arguments.of("0123456789", Type.NoCountryCode),
                Arguments.of("10123456789", Type.Full),
                Arguments.of("1234567890", Type.NotFull),
                Arguments.of("123456789", Type.NotUS),
                Arguments.of("123456789", Type.NotUS),
                Arguments.of("01234567890", Type.NotUS),
                Arguments.of("123456789012",Type.NotUS),
                Arguments.of("1234e6789012",Type.NotNumber)
        );
    }
}