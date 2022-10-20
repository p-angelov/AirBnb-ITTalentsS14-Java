package com.ittalents.airbnb.model.exceptions;

public class UnauthorizedException extends RuntimeException{

    public UnauthorizedException(String msg){
        super(msg);
    }
    public UnauthorizedException(String msg,Throwable cause){
        super(msg,cause);
    }
}
