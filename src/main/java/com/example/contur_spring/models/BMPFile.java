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


    public void setPuzzle(BufferedImage image_puzzle, int x, int y){
        System.out.println("Holst: width: "+this.width+" height: "+this.height);
        System.out.println("Puzzle: width: "+image_puzzle.getWidth()+" height: "+image_puzzle.getHeight());

        for (int i = Math.max(y, 0); i<this.height && i<image_puzzle.getHeight()+y; i++) {
            for (int j = Math.max(x, 0); j < this.width && j < image_puzzle.getWidth()+x; j++) {
                System.out.println("holst x: "+(x + j)+" y: "+(y + i)+" puzzle: x: "+(j-x)+" y: "+(i-y));
                image.setRGB(j, i, image_puzzle.getRGB(j-x, i-y));
            }
        }

    }

}
