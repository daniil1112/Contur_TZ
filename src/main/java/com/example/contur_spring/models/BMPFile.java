package com.example.contur_spring.models;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class BMPFile {

    private int height;
    private int width;
    private String fileName;
    private final String format = "bmp";
    private final int mode = BufferedImage.TYPE_INT_RGB;
    private BufferedImage image;


    public BMPFile(int height, int width) {
        setHeight(height);
        setWidth(width);
    }

    public void setImage(BufferedImage image) {
        this.image = image;
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
        return fileName + "." + format;
    }

    public String getFormat() {
        return format;
    }

    public void createEmptyImage() {
        image = new BufferedImage(width, height, mode);
    }

    public BufferedImage getImage() {
        return image;
    }


    public void setPuzzle(BufferedImage image_puzzle, int x, int y, int height, int width) {
        for (int i = Math.max(y, 0); i < this.height && i < height + y; i++) {
            for (int j = Math.max(x, 0); j < this.width && j < width + x; j++) {
                image.setRGB(j, i, image_puzzle.getRGB(j - x, i - y));
            }
        }
    }

    public BMPFile getImagePart(int x, int y, int width, int height) {
        BMPFile result = new BMPFile(height, width);

        BufferedImage imgRes = new BufferedImage(width, height, mode);
        for (int i = y; i < y+height; i++) {
            for (int j = x; j < x+width; j++) {
                if (i >= 0 && j >= 0 && j < image.getHeight() && x < image.getWidth()){
                    imgRes.setRGB(j-x, i-y, image.getRGB(j, i));
                }
            }
        }

        result.setImage(imgRes);
        return result;
    }

    public InputStream getInputStream() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, format, os);
        return new ByteArrayInputStream(os.toByteArray());
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
