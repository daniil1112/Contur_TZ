package com.example.contur_spring.repositories;

import com.example.contur_spring.models.BMPFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class FileRepository {
    String path;
    public void setPath(String path){
        this.path = path;
    }
    public boolean isFileExist(String fileName){
        File file = new File(path, fileName);
        return file.exists();
    }

    public boolean createFile(String fileId, String extension) throws IOException {
        File file = new File(path, fileId+extension);
        if (file.exists()){
            return false;
        }
        return file.createNewFile();
    }

    public boolean createFile(String filename) throws IOException {
        File file = new File(path, filename);
        if (file.exists()){
            return false;
        }
        return file.createNewFile();
    }

    public String generateUniqId(){
        String id = generateFileId();
        File file = new File(path, id+".bmp");
        while (file.exists()){
            id = generateFileId();
            file = new File(path, id+".bmp");
        }
        return id;
    }

    private String generateFileId(){
        Random rng = new Random(3324524);
        String characters = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < 20; i++)
        {
            res.append(characters.charAt(rng.nextInt(characters.length())));
        }
        return res.toString();
    }

    public void saveImage(BMPFile image) throws IOException {
        File output = new File(image.getFileName());
        ImageIO.write(image.getImage(), image.getFormat(), output);
    }

}
