package com.ittalents.airbnb.model.dto.userDTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserHostStatusDto {
    @JsonProperty("hostStatus")
    boolean hostStatus;
}
