package vn.aptech.pixelpioneercourse.config;


import java.util.Properties;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
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

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("namkhanhvuduy0867@gmail.com");
        mailSender.setPassword("bhzt wkui jqmv fokf");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
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