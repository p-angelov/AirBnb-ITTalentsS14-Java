package com.ittalents.airbnb.exceptions;

public class NotFoundException extends  RuntimeException{
    public NotFoundException(String msg){
        super(msg);
    }
}
