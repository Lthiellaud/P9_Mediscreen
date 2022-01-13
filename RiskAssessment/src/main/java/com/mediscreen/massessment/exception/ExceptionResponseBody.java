package com.mediscreen.massessment.exception;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class ExceptionResponseBody {

    private Timestamp timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
    private String trace;

}
