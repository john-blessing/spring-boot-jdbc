package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Administrator on 2017/5/29.
 */
@Configuration
@EnableWebMvc
public class MyConfig extends WebMvcConfigurerAdapter{
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("PUT", "DELETE", "POST", "GET")
                .allowedHeaders("X-Requested-With")
//                .exposedHeaders("*")
                .allowCredentials(false).maxAge(3600);
    }
}
