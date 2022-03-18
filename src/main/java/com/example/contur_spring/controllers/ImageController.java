package com.example.contur_spring.controllers;


import com.example.contur_spring.models.BMPFile;
import com.example.contur_spring.repositories.FileRepository;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
@RequestMapping("/chartas")
public class ImageController {

    private final String FILE_NOT_FOUND_MSG = "File not found";
    private final String INVALID_COORDINATES = "Invalid coordinates";

    private final FileRepository fileRepository;

    public ImageController(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }


    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<?> createImage(@RequestParam("width") int width, @RequestParam("height") int height) throws IOException {
        if (width > 20_000 || width < 1 || height < 1 || height > 50_000) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        String id = fileRepository.generateUniqId();
        BMPFile bmpFile = new BMPFile(height, width);
        bmpFile.setFileName(id);
        bmpFile.createEmptyImage();

        try {
            fileRepository.saveImage(bmpFile);
        } catch (IOException e) {
            System.err.println("error while saving empty image GET request /chartas/?width=" + width + "&height=" + height);
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Произошла ошибка на сервере, попробуйте создать холст позже");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteImage(@PathVariable("id") String imageId) {
        if (fileRepository.fileExists(imageId, ".bmp")) {
            if (fileRepository.fileDelete(imageId, ".bmp")) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Can't get access to delete image");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(FILE_NOT_FOUND_MSG);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> setPartImage(HttpServletRequest requestEntity, @PathVariable("id") String imageId, @RequestParam("width") int width, @RequestParam("height") int height, @RequestParam("x") int x, @RequestParam("y") int y) throws IOException {
        if (width > 20_000 || width == 0 || height == 0 || height > 50_000) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(INVALID_COORDINATES);
        }

        if (!fileRepository.fileExists(imageId, ".bmp")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(FILE_NOT_FOUND_MSG);
        }

        String INVALID_INPUT_IMAGE = "You should put image to request";

        if (requestEntity.getInputStream() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(INVALID_INPUT_IMAGE);
        }

        BufferedImage image = ImageIO.read(requestEntity.getInputStream());

        if (image == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(INVALID_INPUT_IMAGE);
        }

        if (image.getHeight() != height || image.getWidth() != width) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(INVALID_INPUT_IMAGE);
        }

        try {
            BMPFile holst = fileRepository.getBMPFile(imageId + ".bmp");
            if (y + height - 1 < 0 || y > holst.getImage().getHeight() - 1 || width + x - 1 < 0 || x > holst.getImage().getWidth() - 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fragment not in holst");
            }
            holst.setPuzzle(image, x, y, height, width);
            fileRepository.saveImage(holst);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IOException e) {
            System.err.println("Error getting access to file id:" + imageId + " when adding fragment");
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in server, please, try again after 5 min");
        }

    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getPartImage(@PathVariable("id") String imageId, @RequestParam("width") int width, @RequestParam("height") int height, @RequestParam("x") int x, @RequestParam("y") int y) {
        if (width > 5000 || height > 5000) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Too big width or height");
        }
        if (x + width - 1 < 0 || y + height - 1 < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Empty result");
        }

        if (!fileRepository.fileExists(imageId, ".bmp")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(FILE_NOT_FOUND_MSG);
        }

        try {
            BMPFile holst = fileRepository.getBMPFile(imageId + ".bmp");
            if (x > holst.getWidth() - 1 || y > holst.getHeight() - 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(INVALID_COORDINATES);
            }
            return ResponseEntity.ok().contentType(MediaType.valueOf("image/bmp")).body(new InputStreamResource(holst.getImagePart(x, y, width, height).getInputStream()));
        } catch (IOException e) {
            System.err.println("Error getting access to file id:" + imageId + " when adding fragment");
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in server, please, try again after 5 min");
        }
    }
}
