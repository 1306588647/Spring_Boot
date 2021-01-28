package com.hqyj.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//文件上传注册，注册虚拟路径
@Configuration
public class UploadConfig implements WebMvcConfigurer {
    @Value("${file.path}")
    private String path;

    @Value("${file.address}")
    private String address;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(path).addResourceLocations("file:" + address);
    }
}
