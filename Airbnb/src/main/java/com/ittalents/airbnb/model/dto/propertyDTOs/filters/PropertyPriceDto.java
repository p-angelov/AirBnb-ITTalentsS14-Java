package com.ittalents.airbnb.model.dto.propertyDTOs.filters;

import lombok.Data;

@Data
public class PropertyPriceDto {
    private int minPrice;
    private int maxPrice;
}
