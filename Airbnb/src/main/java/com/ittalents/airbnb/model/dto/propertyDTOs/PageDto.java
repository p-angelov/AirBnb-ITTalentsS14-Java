package com.ittalents.airbnb.model.dto.propertyDTOs;

import lombok.Data;

import java.util.List;

@Data
public class PageDto {
    private int totalItems;
    private List<PagePropertyDto> properties;
    private int totalPages;
    private int currentPage;
}
