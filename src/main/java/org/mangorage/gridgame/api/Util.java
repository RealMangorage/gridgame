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
import java.util.ArrayList;
import java.util.List;

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

    public static void serializeIntArray2D(List<byte[][]> data, File filename) {
        try {
            FileOutputStream fileOut = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeInt(data.size());
            data.forEach(arr -> {
                try {
                    out.writeObject(arr);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public static List<byte[][]> deserializeIntArray2D(File filename) {
        List<byte[][]> grids = new ArrayList<>();
        try {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            int size = in.readInt();

            for (int i = 0; i < size; i++) {
                grids.add((byte[][]) in.readObject());
            }

            in.close();
            fileIn.close();
            System.out.println("2D Array deserialized successfully.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return grids;
    }
}
