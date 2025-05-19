package com.library.commonservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;

@Configuration
public class MailConfig {

    @Primary
    @Bean
    public FreeMarkerConfigurationFactory factoryBean(){
        FreeMarkerConfigurationFactory bean = new FreeMarkerConfigurationFactory();
        bean.setTemplateLoaderPath("classpath:/templates");
        return bean;
    }
}