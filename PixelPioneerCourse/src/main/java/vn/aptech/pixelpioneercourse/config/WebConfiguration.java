package vn.aptech.pixelpioneercourse.config;


import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import vn.aptech.pixelpioneercourse.jwt.JWT;
import vn.aptech.pixelpioneercourse.jwt.JWTImpl;

@Configuration
public class WebConfiguration implements WebMvcConfigurer{
    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setAmbiguityIgnored(false);
        return modelMapper;
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String staticFolder = "file:///" + System.getProperty("user.dir") + "/static/";
        registry.addResourceHandler("/static/**").addResourceLocations(staticFolder);
        WebMvcConfigurer.super.addResourceHandlers(registry);
    } //lay css tu folder static trong resource

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("*");        
    }
    
    @Bean
    JWT jwt(){
        return new JWTImpl();
    } 
}