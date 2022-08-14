package com.telemessage.validation;

import com.telemessage.dto.CheckNumberServiceInputDto;
import com.telemessage.dto.Type;

public class NumberValidator {

    public static Type checkNumber(CheckNumberServiceInputDto checkNumberServiceInputDto){

        String number = checkNumberServiceInputDto.getNumber();
        if(!number.matches("\\d+")){
           return  Type.NotNumber;
        }
        if (number.length() == 10 && number.charAt(0) == '1'){
            return Type.NotFull;
        }
        else if (number.length()<10||number.length()>11||(number.length()==11&&number.charAt(0)!='1')){
            return Type.NotUS;
        }
        else if (number.length()==10){
            return Type.NoCountryCode;
        }
        else return Type.Full;
    }
}
