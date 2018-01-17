package com.ytu;


import com.github.luben.zstd.Zstd;
import com.ytu.compression.JSONCompress;
import com.ytu.utils.FileUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

//CJSON, HPACK, JSONH olarak sıkıştırma yapılır.
public class App {

    private static final String DIR_NAME = "/Users/yusufonder/IdeaProjects/JSONCompression/src/main/resources/";
    private static final Path JSON_FILE_NAME = Paths.get(DIR_NAME + "raw.json");
    private static final Path ZSTD_FILE_NAME = Paths.get(DIR_NAME + "comp.zstd");
    private static final Path GZIP_FILE_NAME = Paths.get(DIR_NAME + "comp.gzip");
    private static final Path COMP_FILE_NAME = Paths.get(DIR_NAME + "comp.json");


    public static void main(String[] args) throws IOException {
        String lines = FileUtil.readFileLines(JSON_FILE_NAME.toFile());
        String pack = JSONCompress.pack(StringUtils.trim(lines).replace("\n", ""));
        FileUtil.writeFile(COMP_FILE_NAME.toFile(), pack.getBytes());
        long json = JSON_FILE_NAME.toFile().length() / 1024;
        System.out.println("ORIGINAL FILE LENGTH -> " + json);
        long comp = COMP_FILE_NAME.toFile().length() / 1024;
        System.out.println("COMP FILE LENGTH -> " + comp);
        zstd(pack);
        gzip(pack);
        //System.out.println(JSONCompress.unpack(pack));

        System.out.println("RATIO -> " + comp / (double)json);

    }

    private static void zstd(String pack) throws IOException {

        byte[] compress = Zstd.compress(pack.getBytes());
        FileUtil.writeFile(ZSTD_FILE_NAME.toFile(), compress);
        System.out.println("ZSTD FILE LENGTH -> " + ZSTD_FILE_NAME.toFile().length() / 1024);
        byte[] decompress = Zstd.decompress(compress, (int) Zstd.decompressedSize(compress));
    }

    private static void gzip(String pack) throws IOException {
        FileUtil.gzipComp(pack, GZIP_FILE_NAME.toFile());
        System.out.println("GZIP FILE LENGTH -> " + GZIP_FILE_NAME.toFile().length() / 1024);
        String s = FileUtil.gzipDecomp(GZIP_FILE_NAME.toFile());

    }
}
