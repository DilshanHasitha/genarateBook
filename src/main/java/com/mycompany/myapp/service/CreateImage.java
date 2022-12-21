package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.ImageCreatorDTO;
import com.mycompany.myapp.service.dto.ImageParameterDTO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateImage {

    void imageCreator(ImageCreatorDTO imageCreatorDTO) {
        // page size
        int w = imageCreatorDTO.getPageWidth();
        int h = imageCreatorDTO.getPageHeight();
        BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics g = combined.getGraphics();
        // paint both images, preserving the alpha channels
        for (ImageParameterDTO images : imageCreatorDTO.getImage()) {
            BufferedImage image = loadImage(images.getImageUrl());
            g.drawImage(image, images.getX(), images.getY(), images.getWidth(), images.getHeight(), null);
        }

        g.dispose();
        try {
            ImageIO.write(combined, "PNG", new File("combined.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    BufferedImage loadImage(String name) {
        String imgFileName = name;
        URL url = null;
        try {
            url = new URL(imgFileName);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        BufferedImage img = null;
        try {
            img = ImageIO.read(url);
        } catch (Exception e) {}
        return img;
    }
}
