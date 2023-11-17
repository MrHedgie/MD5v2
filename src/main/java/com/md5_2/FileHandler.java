package com.md5_2;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;

public interface FileHandler {

    static WritableImage openImage(String path) {
        try{
            BufferedImage bf = ImageIO.read(new File(path));
            return SwingFXUtils.toFXImage(bf, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    static void saveImage(WritableImage image, String name) {
        try {
            File newImage = new File("src/main/java/images/" + name + ".png");
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", newImage);
        } catch (IOException e) {
            System.out.println("File not created");
        }
    }
}
