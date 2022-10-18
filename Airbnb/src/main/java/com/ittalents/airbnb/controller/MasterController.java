package com.ittalents.airbnb.controller;

import com.ittalents.airbnb.model.exceptions.BadRequestException;
import com.ittalents.airbnb.model.dto.ErrorDto;
import com.ittalents.airbnb.model.exceptions.NotFoundException;
import com.ittalents.airbnb.model.exceptions.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;

public abstract class MasterController {
    @ResponseBody
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BadRequestException.class)
    private ErrorDto handleBadRequestException(Exception e){
        return buildErrorInfo(e, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = NotFoundException.class)
    private ErrorDto handleNotFoundException(Exception e){
        return buildErrorInfo(e, HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = UnauthorizedException.class)
    private ErrorDto handleUnauthorizedException(Exception e){
        return buildErrorInfo(e, HttpStatus.UNAUTHORIZED);
    }

    private ErrorDto buildErrorInfo(Exception e, HttpStatus status){
        ErrorDto err = new ErrorDto();
        err.setStatus(status.value());
        err.setMsg(e.getMessage());
        err.setTime(LocalDateTime.now());
        return err;
    }
}