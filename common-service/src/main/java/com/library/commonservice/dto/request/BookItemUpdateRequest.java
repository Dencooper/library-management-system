package com.library.commonservice.dto.request;

import com.library.commonservice.utils.constant.BookItemCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookItemUpdateRequest {
    private String code;
    private BookItemCondition bookItemCondition;
    private Boolean isAvailable;
}