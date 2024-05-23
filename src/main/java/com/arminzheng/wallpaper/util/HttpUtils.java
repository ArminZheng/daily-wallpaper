package com.arminzheng.wallpaper.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * HttpUtils
 *
 * @author az
 * @version 2022/2/27
 */
public class HttpUtils {

    /**
     * Get HTTP connection
     *
     * @param url The URL to request
     * @return The HTTP connection object
     * @throws IOException If an I/O error occurs
     */
    public static HttpURLConnection getHttpUrlConnection(String url) throws IOException {

        URL               httpUrl        = new URL(url);
        HttpURLConnection httpConnection = (HttpURLConnection)httpUrl.openConnection();
        httpConnection.setRequestProperty("User-Agent",
                                          "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                                          + "AppleWebKit/537.36 (KHTML, like Gecko)"
                                          + " Chrome/83.0.4103" + ".116 Safari/537.36");
        return httpConnection;
    }

    /**
     * Request content from the specified URL
     *
     * @param url The URL to request
     * @return The content returned from the request
     * @throws IOException If an I/O error occurs
     */
    public static String getHttpContent(String url) throws IOException {

        HttpURLConnection httpUrlConnection = getHttpUrlConnection(url);
        StringBuilder     stringBuilder     = new StringBuilder();

        try (InputStream input = httpUrlConnection.getInputStream();
             BufferedInputStream bis = new BufferedInputStream(input)) {
            byte[] buffer = new byte[1024];
            int    len;
            // Return -1 when reaching the end of the file.
            while ((len = bis.read(buffer)) != -1) {
                stringBuilder.append(new String(buffer, 0, len));
            }
        } catch (Exception ignore) {
        } finally {
            httpUrlConnection.disconnect();
        }
        return stringBuilder.toString();
    }

}
