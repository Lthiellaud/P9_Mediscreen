package com.mediscreen.massessment.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.IOException;

public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String invoker, Response response) {
        ExceptionResponseBody responseBody = null ;
        String body = response.body().toString();

        try {
            responseBody = objectMapper.readValue(body, ExceptionResponseBody.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if ( response.status() == 404 && null != responseBody ) {
            return new NotFoundException(responseBody.getMessage());
        }

        return defaultErrorDecoder.decode(invoker, response);
    }

}

