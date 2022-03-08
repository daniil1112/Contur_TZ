package com.example.contur_spring.config;

import com.example.contur_spring.models.BMPFile;
import com.example.contur_spring.repositories.FileRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

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
            fileRepository.setPath("/");
        }
        return fileRepository;
    }

    @Bean
    @Scope("prototype")
    public BMPFile bmpFile() {
        return new BMPFile(0, 0);
    }
}
