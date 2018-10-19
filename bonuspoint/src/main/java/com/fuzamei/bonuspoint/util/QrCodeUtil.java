package com.fuzamei.bonuspoint.util;

import com.fuzamei.common.bean.FastDFSClient;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 18519 on 2018/5/10.
 * lkm
 */
@Component
public class QrCodeUtil {

    private final FastDFSClient fastDFSClient;

    @Autowired
    public QrCodeUtil(FastDFSClient fastDFSClient) {
        this.fastDFSClient = fastDFSClient;
    }

    public String createQrCode(String url, String fileName) {
        try {
            Map<EncodeHintType, String> hints = new HashMap<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, 400, 400, hints);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BufferedImage image = toBufferedImage(bitMatrix);
            ImageIO.write(image,"jpg",byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            return fastDFSClient.uploadFile(bytes,fileName);
        } catch (Exception e) {
            throw new RuntimeException();
        }

    }

    static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, file)) {
            throw new IOException("Could not write an image of format " + format + " to " + file);
        }
    }

    static void writeToStream(BitMatrix matrix, String format, OutputStream stream) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of format " + format);
        }
    }

    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }
}
