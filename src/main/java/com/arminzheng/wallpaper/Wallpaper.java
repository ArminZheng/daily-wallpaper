package com.arminzheng.wallpaper;

import com.arminzheng.wallpaper.domain.Image;
import com.arminzheng.wallpaper.util.FileUtils;
import com.arminzheng.wallpaper.util.HttpUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Daily Wallpaper
 *
 * @author az
 * @version 2022/2/27
 */
public class Wallpaper {

  private static final String BING_API =
      "https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=10&nc=1612409408851&pid=hp&FORM=BEHPTB&uhd=1&uhdwidth=3840&uhdheight=2160";

  private static final String BING_URL = "https://cn.bing.com";

  public static void main(String[] args) throws IOException {
    Image image = currentDailyWallpaper();
    writeFiles(image);
    writeHtml(image);
  }

  private static void writeHtml(Image image) throws IOException {
    String index =
        new String(Files.readAllBytes(Paths.get("wallpaper.html")), StandardCharsets.UTF_8)
            .replace("${url}", image.getUrl())
            .replace("${title}", image.getTitle() + " " + image.getDate());
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
    JsonArray images = target.getAsJsonArray("images");
    target = images.get(0).getAsJsonObject();

    String url = BING_URL + target.get("url").getAsString();
    url = url.substring(0, url.indexOf("&"));

    String startdate = target.get("startdate").getAsString();
    LocalDate localDate = LocalDate.parse(startdate, DateTimeFormatter.BASIC_ISO_DATE);

    startdate = localDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
    String copyright = target.get("copyright").getAsString();
    return new Image(copyright, startdate, url);
  }
}
