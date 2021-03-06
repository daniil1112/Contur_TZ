package com.example.contur_spring.config;

import com.example.contur_spring.repositories.FileRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
@ComponentScan("com.example.contur_spring")
public class AppConfig {
    private final org.springframework.boot.ApplicationArguments applicationArguments;

    public AppConfig(ApplicationArguments applicationArguments) {
        this.applicationArguments = applicationArguments;
    }

    @Bean
    public FileRepository FileRepository() {
        FileRepository fileRepository = new FileRepository();
        String[] args = applicationArguments.getSourceArgs();
        if (args.length > 0) {
            fileRepository.setPath(args[0]);
        } else {
            String path = new File(new File(".").getAbsolutePath()).getAbsolutePath();
            fileRepository.setPath(path);
            System.err.println("Please, launch app with files path, by default set path " + path);
        }
        return fileRepository;
    }
}
