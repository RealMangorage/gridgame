package org.mangorage.gridgame.api;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CacheAPI {
    private static final Map<String, Image> IMAGE_CACHE = new HashMap<>();

    public static byte[] getResourceBytesFromWithin(String resourcePath) {
        try (InputStream inputStream = CacheAPI.class.getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                return null;
            }
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int bytesRead;
            byte[] data = new byte[8192]; // 8 KB buffer
            while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, bytesRead);
            }
            return buffer.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static InputStream getResourceStreamInternal(String resourcePath) {
        return CacheAPI.class.getResourceAsStream(resourcePath);
    }

    public static Image byteArrayToImage(byte[] byteArray) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
            BufferedImage bImage = ImageIO.read(bis);
            bis.close();
            return bImage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Image getInternalImage(String path) {
        if (IMAGE_CACHE.containsKey(path)) return IMAGE_CACHE.get(path);
        var data = getResourceBytesFromWithin(path);
        Image image = byteArrayToImage(data);
        IMAGE_CACHE.put(path, image);
        return image;
    }
}
