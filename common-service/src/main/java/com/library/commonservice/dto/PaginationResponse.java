package com.library.commonservice.dto;

import lombok.Data;

@Data
public class PaginationResponse {
    private Meta meta;
    private Object result;
}
