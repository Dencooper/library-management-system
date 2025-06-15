package com.library.userservice.config;

import com.library.commonservice.config.CorsConfig;
import com.library.commonservice.exception.GlobalExceptionHandler;
import com.library.commonservice.utils.FormatApiResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        FormatApiResponse.class,
        GlobalExceptionHandler.class,
})
public class CommonServiceImportConfig {
}
