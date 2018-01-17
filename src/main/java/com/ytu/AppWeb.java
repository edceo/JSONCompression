package com.ytu;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AppWeb {
    public static void main(String[] args) throws Exception {
        HttpURLConnection httpcon = (HttpURLConnection) ((new URL("a url").openConnection()));
        httpcon.setDoOutput(true);
        httpcon.setRequestProperty("Content-Type", "text/plain");
        httpcon.setRequestProperty("Accept", "text/plain");
        httpcon.setRequestMethod("POST");
        httpcon.connect();

        byte[] outputBytes = "{'value': 7.5}".getBytes("UTF-8");
        OutputStream os = httpcon.getOutputStream();
        os.write(outputBytes);

        os.close();
    }
}
