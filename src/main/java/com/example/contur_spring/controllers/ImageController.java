package com.example.contur_spring.controllers;


import com.example.contur_spring.models.BMPFile;
import com.example.contur_spring.repositories.FileRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/chartas")
public class ImageController {


    private final ApplicationContext context;

    public ImageController(ApplicationContext context) {
        this.context = context;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<String> createImage(@RequestParam("width") int width, @RequestParam("height") int height) throws IOException {
        FileRepository fileRepository = context.getBean(FileRepository.class);
        String id = fileRepository.generateUniqId();
        BMPFile bmpFile = new BMPFile(height, width);
        bmpFile.setFileName(id);
        bmpFile.createEmptyImage();
        fileRepository.saveImage(bmpFile);
//        bmpFile.createImage("dsfgsgsdf");
        return ResponseEntity.ok("okey " + id);
//        if (fileRepository.createFile(id, ".bmp")) {
//
//        }
//        return (ResponseEntity<String>) ResponseEntity.status(500);


    }
}
