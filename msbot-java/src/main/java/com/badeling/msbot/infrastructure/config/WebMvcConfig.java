package com.badeling.msbot.infrastructure.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{

    @Autowired
    private ConstRepository constRepository;

	public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //static/upload/**是对应resource下工程目录
        registry.addResourceHandler("/image/**").addResourceLocations("file:"+ constRepository.getImageUrl());
     }
}
