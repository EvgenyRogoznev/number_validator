package com.telemessage.dto;

import lombok.Value;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import static com.telemessage.validation.NumberValidator.checkNumber;

@Value
public class ResponseDto {
    String mdn;
    String transactionId;
    String type;
    String requestReceivedDate;
    Integer currentStatus;
    String statusInfo;

    public static final String fullStatusInfo = "Success Mobile is a full US number";
    public static final String  notFullStatusInfo = "Mobile is not a full number";
    public static final String notNumberStatusInfo = "It is not a phone number";
    public static final String  notUSStatusInfo = "Mobile is not US number";
    public static final String  noCountryCodeStatusInfo = "Mobile does not have a country code ";
    public ResponseDto(CheckNumberServiceInputDto number){
        mdn = number.getNumber();
        transactionId = number.getNumber()+ new Date().getTime();
        type = checkNumber(number).toString();
        requestReceivedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss*SSSZZZZ").format(Date.from(Instant.now()));
        currentStatus = getCurrentStatusByType(checkNumber(number));
        statusInfo = getStatusInfoByType(checkNumber(number));
    }

    private Integer getCurrentStatusByType(Type type) {
        switch (type) {
            case Full: return 2;
            case NotFull:
            case NotUS:
            case NoCountryCode:
                return 4;
            case NotNumber: return -1;
        }
        return -1;
    }
    private String getStatusInfoByType(Type type){
        switch (type) {
            case Full: return fullStatusInfo;
            case NotFull: return notFullStatusInfo;
            case NotNumber: return notNumberStatusInfo;
            case NotUS: return notUSStatusInfo;
            case NoCountryCode:return noCountryCodeStatusInfo;
        }
        return null;
    }
}
