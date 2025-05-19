package com.library.commonservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Meta {
    private int page;
    private int pageSize;
    private int pages;
    private long total;
}
