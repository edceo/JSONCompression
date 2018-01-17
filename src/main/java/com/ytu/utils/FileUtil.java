package com.ytu.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class FileUtil {

    public static String readFileLines(File file) throws IOException {
        return StringUtils.trim(FileUtils.readFileToString(file, "UTF-8"));
    }

    public static void writeFile(File file, byte[] bytes) throws IOException {
        FileUtils.writeByteArrayToFile(file, bytes);
    }

    public static void gzipComp(String packed, File destFile) throws IOException {
        try {

            GZIPOutputStream gzos =
                    new GZIPOutputStream(new FileOutputStream(destFile));

            gzos.write(packed.getBytes(), 0, packed.getBytes().length);

            gzos.finish();
            gzos.close();

            System.out.println("Done");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public static String gzipDecomp(File sourceFile) {
        byte[] buffer = new byte[1024];
        StringBuilder builder = new StringBuilder();


        try {
            GZIPInputStream gzis =
                    new GZIPInputStream(new FileInputStream(sourceFile));

            int len;
            while ((len = gzis.read(buffer)) > 0) {
                builder.append(new String(buffer, 0, len));
            }



            gzis.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return builder.toString();
    }
}
