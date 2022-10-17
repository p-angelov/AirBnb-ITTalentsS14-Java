package com.ittalents.airbnb.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserHostStatusDto {
    @JsonProperty("hostStatus")
    boolean hostStatus;
}
