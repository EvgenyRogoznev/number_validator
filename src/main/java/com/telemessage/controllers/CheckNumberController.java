package com.telemessage.controllers;


import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import com.telemessage.dto.CheckNumberServiceInputDto;
import com.telemessage.dto.ResponseDto;
import com.telemessage.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Signature;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/checkNumber")

public class CheckNumberController {
    Gson g = new Gson();

    @PostMapping
    public  ResponseEntity check(
            @RequestHeader(name = "x-date", required = false) String xDate,
            @RequestHeader(name = "x-Signature", required = false) String xSignature,
            @RequestBody List<CheckNumberServiceInputDto> numbers) {

        if(xDate==null) {
            String json = g.toJson(new ErrorResponse("Date header not present","ERROR" ));
            return ResponseEntity.status(401).body(json);
        } else if (xSignature==null) {
            String json = g.toJson(new ErrorResponse("Signature header not present","ERROR"));
            return ResponseEntity.status(401).body(json);
        } else if(numbers.size()>10) {
            String json = g.toJson(new ErrorResponse("request exceeds maximum limit of numbers allowed max allowed 10"));
            return ResponseEntity.status(400).body(json);
        }

        List<ResponseDto> response = new ArrayList<>();
        for (CheckNumberServiceInputDto number: numbers) {
         ResponseDto responseDto = new ResponseDto(number);
        response.add(responseDto);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);


    }

}
