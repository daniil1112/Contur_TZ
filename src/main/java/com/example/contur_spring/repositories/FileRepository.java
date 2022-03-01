package com.example.contur_spring.repositories;

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
    public String createFile(String fileName) throws IOException {
        String id = generateFileId(20);
        File file = new File(path, id+".bmp");
        while (file.exists()){
            id = generateFileId(20);
            file = new File(path, id+".bmp");
        }
        file.createNewFile();
        return id;
    }


    private String generateFileId(int length){
        Random rng = new Random(3324524);
        String characters = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < length; i++)
        {
            res.append(characters.charAt(rng.nextInt(characters.length())));
        }
        return res.toString();
    }

}
