package com.arminzheng.wallpaper;

import com.arminzheng.wallpaper.domain.Image;
import com.arminzheng.wallpaper.util.FileUtils;
import com.arminzheng.wallpaper.util.HttpUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Daily Wallpaper
 *
 * @author az
 * @version 2022/2/27
 */
public class Wallpaper {

    private static String BING_API;
    private static String URL_PREFIX;

    static {

        Properties env = new Properties();
        try (InputStream inputStream = Wallpaper.class.getResourceAsStream(
                "/wallpaper" + ".properties")) {
            env.load(inputStream);
            BING_API   = env.getProperty("bingUrl");
            URL_PREFIX = env.getProperty("urlPrefix");
        } catch (IOException ignore) {
        }
    }

    public static void main(String[] args) throws IOException {

        Image image = currentDailyWallpaper();
        writeFiles(image);
        writeHtml(image);
    }

    private static void writeHtml(Image image) throws IOException {

        String index = new String(Files.readAllBytes(Paths.get("wallpaper.html")),
                                  StandardCharsets.UTF_8).replace("${url}", image.getUrl())
                                                         .replace("${title}",
                                                                  String.format("%s %s",
                                                                                image.getTitle(),
                                                                                image.getDate()))
                                                         .replace("${desc}", image.getDesc());
        Path indexPath = Paths.get("index.html");
        Files.deleteIfExists(indexPath);
        Files.createFile(indexPath);
        Files.write(indexPath, index.getBytes(StandardCharsets.UTF_8));
    }

    private static void writeFiles(Image image) throws IOException {

        List<Image> imagesList = FileUtils.readWallpaper();
        imagesList.set(0, image);
        imagesList = imagesList.stream().distinct().collect(Collectors.toList());
        FileUtils.writeWallpaper(imagesList);
        FileUtils.writeReadme(imagesList);
    }

    public static Image currentDailyWallpaper() throws IOException {

        String content = HttpUtils.getHttpContent(BING_API);

        JsonObject target = JsonParser.parseString(content).getAsJsonObject();
        JsonArray  images = target.getAsJsonArray("images");
        target = images.get(0).getAsJsonObject();

        String url = URL_PREFIX + target.get("url").getAsString();
        url = url.substring(0, url.indexOf("&"));

        // noinspection SpellCheckingInspection
        String startdate = target.get("startdate").getAsString();
        startdate = LocalDate.parse(startdate, DateTimeFormatter.BASIC_ISO_DATE)
                             .format(DateTimeFormatter.ISO_LOCAL_DATE);

        String copyright = target.get("copyright").getAsString();

        String desc = target.get("desc").getAsString();

        return new Image(copyright, desc, startdate, url);
    }

}
