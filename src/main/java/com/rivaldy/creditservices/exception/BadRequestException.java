package com.rivaldy.creditservices.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class BadRequestException extends RuntimeException{
    private List<String> errors;
    public BadRequestException(String message, List<String> errors){
        super(message);
        this.errors = errors;
    }
}
