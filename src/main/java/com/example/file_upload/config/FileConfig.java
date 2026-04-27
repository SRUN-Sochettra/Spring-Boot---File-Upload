package com.example.file_upload.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class FileConfig implements WebMvcConfigurer {

//    @Value("${spring.file-upload-path}")
//    private String pathName;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        Path rootPath = Paths.get(pathName);
//        registry.addResourceHandler("/files/**").addResourceLocations("file:" + rootPath + "/");
        registry.addResourceHandler("/files/**").addResourceLocations("classpath:/files/");
    }
}