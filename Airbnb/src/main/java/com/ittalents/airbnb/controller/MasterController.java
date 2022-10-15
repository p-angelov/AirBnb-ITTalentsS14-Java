package com.ittalents.airbnb.controller;

import com.ittalents.airbnb.exceptions.BadRequestException;
import com.ittalents.airbnb.exceptions.ErrorJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public abstract class MasterController {
    @ResponseBody
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    private ErrorJson handleBadRequestException(Exception e){
      ErrorJson err = new ErrorJson();
      err.setMsg(e.getMessage());
        return err;
    }
}
