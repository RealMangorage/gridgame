package org.mangorage.gridgame.api;

import org.apache.commons.compress.compressors.lzma.LZMACompressorInputStream;
import org.apache.commons.compress.compressors.lzma.LZMACompressorOutputStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class Util {
    public static void compressFile(File sourceFilePath, String compressedFilePath) throws IOException {
        try (InputStream is = new FileInputStream(sourceFilePath);
             OutputStream os = new FileOutputStream(compressedFilePath);
             BufferedOutputStream bos = new BufferedOutputStream(os);
             LZMACompressorOutputStream lzmaos = new LZMACompressorOutputStream(bos)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                lzmaos.write(buffer, 0, len);
            }
        }
    }

    public static File decompressFile(File compressedFilePath, String decompressedFilePath) throws IOException {
        try (InputStream is = new FileInputStream(compressedFilePath);
             BufferedInputStream bis = new BufferedInputStream(is);
             LZMACompressorInputStream lzmais = new LZMACompressorInputStream(bis);
             OutputStream os = new FileOutputStream(decompressedFilePath)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = lzmais.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
        }
        return new File(decompressedFilePath);
    }

    public static void serializeIntArray2D(byte[][][] grid, File filename) {
        try {
            FileOutputStream fileOut = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(grid);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public static byte[][][] deserializeIntArray2D(File filename) {
        byte[][][] gridData = null;
        try {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            gridData = (byte[][][]) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("2D Array deserialized successfully.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return gridData;
    }
}
