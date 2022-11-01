package com.ittalents.airbnb.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorDto {

   private String msg;
   private int status;
   private LocalDateTime time;
}
