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
        fileRepository.saveImage(bmpFile);

        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteImage(@PathVariable("id") String imageId) {
        if (fileRepository.fileExists(imageId, ".bmp")) {
            if (fileRepository.fileDelete(imageId, ".bmp")) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("can't get access to delete image");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(FILE_NOT_FOUND_MSG);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> setPartImage(HttpServletRequest requestEntity, @PathVariable("id") String imageId, @RequestParam("width") int width, @RequestParam("height") int height, @RequestParam("x") int x, @RequestParam("y") int y) throws IOException {
        if (width > 20_000 || width < 1 || height < 1 || height > 50_000) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(INVALID_COORDINATES);
        }
        if (!fileRepository.fileExists(imageId, ".bmp")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(FILE_NOT_FOUND_MSG);
        }
        String INVALID_INPUT_IMAGE = "You should put image to requst";
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

        BMPFile holst = fileRepository.getBMPFile(imageId + ".bmp");

        if (height + y < 0 || y > holst.getImage().getHeight() || width + x < 0 || x > holst.getImage().getWidth()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        holst.setPuzzle(image, x, y, height, width);

        fileRepository.saveImage(holst);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getPartImage(@PathVariable("id") String imageId, @RequestParam("width") int width, @RequestParam("height") int height, @RequestParam("x") int x, @RequestParam("y") int y) throws IOException {
        if (x < 0 || y < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(INVALID_COORDINATES);
        }
        if (!fileRepository.fileExists(imageId, ".bmp")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(FILE_NOT_FOUND_MSG);
        }

        BMPFile holst = fileRepository.getBMPFile(imageId + ".bmp");

        if (!holst.checkXExist(x + width - 1) || !holst.checkYExist(y + height - 1)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(INVALID_COORDINATES);
        }

        return ResponseEntity.ok().contentType(MediaType.valueOf("image/bmp")).body(new InputStreamResource(holst.getImagePart(x, y, width, height).getInputStream()));
    }
}
