package ru.codeforensics.photomark.services;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;



class DataMatrixServiceTest {

    @Test
    void decode() {
        DataMatrixService dataMatrixService = new DataMatrixService();
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream("qr-code.png")) {
            Assert.assertEquals("Сервис работает отлично!", dataMatrixService.decode(ImageIO.read(inputStream)));
        } catch (IOException | NotFoundException e) {
            e.printStackTrace();
        }
    }
}