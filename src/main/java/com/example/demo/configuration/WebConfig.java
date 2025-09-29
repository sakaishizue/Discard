package com.example.demo.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // "/image/**" というURLでリクエストが来た場合
        registry.addResourceHandler("/image/**")
                // "file:C:/uploads/" ディレクトリからファイルを探す
                .addResourceLocations("file:" + System.getenv("IMAGE_UPLOAD_DIR") + "/");
    }
}
