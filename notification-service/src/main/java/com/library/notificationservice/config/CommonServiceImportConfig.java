package com.library.notificationservice.config;

import com.library.commonservice.config.CorsConfig;
import com.library.commonservice.config.KafkaConfig;
import com.library.commonservice.config.MailConfig;
import com.library.commonservice.exception.GlobalExceptionHandler;
import com.library.commonservice.service.EmailService;
import com.library.commonservice.utils.FormatApiResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        FormatApiResponse.class,
        GlobalExceptionHandler.class,
        EmailService.class,
        KafkaConfig.class,
        MailConfig.class,
        CorsConfig.class
})
public class CommonServiceImportConfig {
}