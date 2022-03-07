package com.example.contur_spring.models;




import java.awt.image.BufferedImage;


public class BMPFile {

    int height;
    int width;
    String fileName;
    String format = "bmp";
    private final int mode = BufferedImage.TYPE_INT_RGB;
    private BufferedImage image;


    public BMPFile(int height, int width){
        setHeight(height);
        setWidth(width);
    }


    public void setHeight(int height) {
        this.height = height;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName+"."+format;
    }
    public String getFormat() {
        return format;
    }

    public void createEmptyImage(){
        image = new BufferedImage(width, height, mode);
    }

    public BufferedImage getImage() {
        return image;
    }


}
