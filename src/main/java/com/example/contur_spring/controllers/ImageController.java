package com.example.contur_spring.controllers;


import com.example.contur_spring.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

@RestController
@RequestMapping("/chartas")
public class ImageController {


    @Autowired
    private ApplicationContext context;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<String> createImage(@RequestParam("width") int width, @RequestParam("height") int height) throws IOException {
        FileRepository fileRepository = context.getBean(FileRepository.class);
        String id = fileRepository.createFile("325153");
//        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path+"/pathscore.txt"));
//        bufferedWriter.write("test");
//        bufferedWriter.close();
        return ResponseEntity.ok("okey "+id);
    }
}
