package com.telemessage.exception;

import lombok.Data;

@Data
public class ErrorResponse
{
    private String statusInfo;
    private String status;

    public ErrorResponse(String statusInfo) {
        this.statusInfo = statusInfo;
    }

    public ErrorResponse(String statusInfo, String status) {
        this.statusInfo = statusInfo;
        this.status = status;
    }
}
