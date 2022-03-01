package com.example.contur_spring.config;

import com.example.contur_spring.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.example.contur_spring")
public class AppConfig {
    private final org.springframework.boot.ApplicationArguments applicationArguments;

    public AppConfig(ApplicationArguments applicationArguments) {
        this.applicationArguments = applicationArguments;
    }

    @Bean
    public FileRepository FileRepository(){
        FileRepository fileRepository = new FileRepository();
        fileRepository.setPath(applicationArguments.getSourceArgs()[0]);
        return fileRepository;
    }
}
