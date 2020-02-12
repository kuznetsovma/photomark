package ru.codeforensics.photomark.services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.datamatrix.DataMatrixWriter;
import java.awt.image.BufferedImage;
import org.springframework.stereotype.Service;

@Service
public class DataMatrixService {

  public String decode(BufferedImage bufferedImage) throws NotFoundException {
    LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

    Result result = new MultiFormatReader().decode(bitmap);
    return result.getText();
  }

  public BufferedImage encode(String code, int width, int height) {
    BitMatrix bitMatrix = new DataMatrixWriter()
        .encode(code, BarcodeFormat.DATA_MATRIX, width, height);
    return MatrixToImageWriter.toBufferedImage(bitMatrix);
  }
}
