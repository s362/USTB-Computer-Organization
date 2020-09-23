package com.example.ustbdemo.Config;

import com.example.ustbdemo.Util.OSUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class TomcatConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String filePath= OSUtil.isLinux()?"file:/home/ustbDemo/static/":"file:F:\\D_disk\\ustbdemo\\staticFiles\\";
        registry.addResourceHandler("/image/**").addResourceLocations(filePath);
    }
}
