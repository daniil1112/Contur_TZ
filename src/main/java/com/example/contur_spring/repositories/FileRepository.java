package com.example.contur_spring.repositories;

import com.example.contur_spring.models.BMPFile;
import org.apache.commons.lang.RandomStringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FileRepository {
    String path;

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isFileExist(String fileName) {
        File file = new File(path, fileName);
        return file.exists();
    }

    public boolean createFile(String fileId, String extension) throws IOException {
        File file = new File(path, fileId + extension);
        if (file.exists()) {
            return false;
        }
        return file.createNewFile();
    }

    public boolean fileExists(String fileId, String extension) {
        File file = new File(path, fileId + extension);
        return file.exists();
    }

    public boolean fileDelete(String fileId, String extension) {
        File file = new File(path, fileId + extension);
        return file.delete();
    }

    public boolean createFile(String filename) throws IOException {
        File file = new File(path, filename);
        if (file.exists()) {
            return false;
        }
        return file.createNewFile();
    }

    public String generateUniqId() {
        String id = RandomStringUtils.randomAlphanumeric(16);
        File file = new File(path, id + ".bmp");
        while (file.exists()) {
            id = RandomStringUtils.randomAlphanumeric(16);
            file = new File(path, id + ".bmp");
        }
        return id;
    }


    public void saveImage(BMPFile image) throws IOException {
        File output = new File(path, image.getFileName());
        ImageIO.write(image.getImage(), image.getFormat(), output);
    }

    public BMPFile getBMPFile(String filename) throws IOException {
        BufferedImage img = ImageIO.read(new File(path, filename));
        BMPFile result = new BMPFile(img.getHeight(), img.getWidth());
        result.setFileName(filename.split("\\.")[0]);
        result.setImage(img);
        return result;
    }

}
