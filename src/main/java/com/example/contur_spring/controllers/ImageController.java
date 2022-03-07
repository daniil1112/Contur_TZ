package com.example.contur_spring.controllers;


import com.example.contur_spring.models.BMPFile;
import com.example.contur_spring.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/chartas")
public class ImageController {

    private final FileRepository fileRepository;

    public ImageController(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }


    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity createImage(@RequestParam("width") int width, @RequestParam("height") int height) throws IOException {
        if (width > 20_000 || width < 1 || height < 1 || height > 50_000){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        String id = fileRepository.generateUniqId();
        BMPFile bmpFile = new BMPFile(height, width);
        bmpFile.setFileName(id);
        bmpFile.createEmptyImage();
        fileRepository.saveImage(bmpFile);

        return new ResponseEntity(id, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteImage(@PathVariable("id") String imageId){
        if (fileRepository.fileExists(imageId, ".bmp")){
            if (fileRepository.fileDelete(imageId, ".bmp")){
                return new ResponseEntity(HttpStatus.OK);
            }
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public ResponseEntity setPartImage(HttpServletRequest requestEntity, @PathVariable("id") String imageId, @RequestParam("width") int width, @RequestParam("height") int height, @RequestParam("x") int x, @RequestParam("y") int y) throws IOException {
        if (width > 20_000 || width < 1 || height < 1 || height > 50_000 || !fileRepository.fileExists(imageId, ".bmp")){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        if (requestEntity.getInputStream() == null){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        BufferedImage image = ImageIO.read(requestEntity.getInputStream());
        if (image == null){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if (image.getHeight() != height || image.getWidth() != width){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }



        BMPFile holst = fileRepository.getBMPFile(imageId+".bmp");

        holst.setPuzzle(image, x, y, height, width);

        fileRepository.saveImage(holst);

        return new ResponseEntity(HttpStatus.CREATED);
    }
}
