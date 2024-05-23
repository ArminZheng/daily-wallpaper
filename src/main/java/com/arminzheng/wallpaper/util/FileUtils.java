package com.arminzheng.wallpaper.util;

import com.arminzheng.wallpaper.domain.Image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * FileUtils
 *
 * @author az
 * @version 2022/2/27
 */
public class FileUtils {

    private static final Path README_PATH = Paths.get("README.md");
    private static final Path BING_PATH   = Paths.get("Wallpaper.md");

    /**
     * Read with BING_PATH
     *
     * @return images info in BING_PATH
     * @throws IOException file exception
     */
    public static List<Image> readWallpaper() throws IOException {

        if (!Files.exists(BING_PATH)) {
            Files.createFile(BING_PATH);
        }
        List<String> allLines = Files.readAllLines(BING_PATH);
        allLines = allLines.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());
        List<Image> imgList = new ArrayList<>();
        imgList.add(null);
        for (int i = 1; i < allLines.size(); i++) {
            String s        = allLines.get(i).trim();
            int    descEnd  = s.indexOf("]");
            int    urlStart = s.lastIndexOf("(") + 1;

            String date = s.substring(0, 10);
            String desc = s.substring(14, descEnd);
            String url  = s.substring(urlStart, s.length() - 1);
            imgList.add(new Image(desc, date, url));
        }
        return imgList;
    }

    /**
     * Write to BING_PATH
     *
     * @param imgList images info in README
     * @throws IOException file exception
     */
    public static void writeWallpaper(List<Image> imgList) throws IOException {

        if (!Files.exists(BING_PATH)) {
            Files.createFile(BING_PATH);
        }
        Files.write(BING_PATH, "## Wallpaper List".getBytes());
        Files.write(BING_PATH, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
        Files.write(BING_PATH, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
        for (Image images : imgList) {
            Files.write(BING_PATH, images.formatMarkdown().getBytes(), StandardOpenOption.APPEND);
            Files.write(BING_PATH, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            Files.write(BING_PATH, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
        }
    }

    /**
     * Read with README_PATH
     *
     * @return images info in README
     * @throws IOException file exception
     */
    public static List<Image> readReadme() throws IOException {

        if (!Files.exists(README_PATH)) {
            Files.createFile(README_PATH);
        }
        List<String> allLines = Files.readAllLines(README_PATH);
        List<Image>  imgList  = new ArrayList<>();
        for (int i = 3; i < allLines.size(); i++) {
            String content = allLines.get(i);
            Arrays.stream(content.split("\\|")).filter(s -> !s.isEmpty()).map(s -> {
                int dateStartIndex = s.indexOf("[", 3) + 1;
                int urlStartIndex  = s.indexOf("(", 4) + 1;
                String date = s.substring(dateStartIndex, dateStartIndex + 10);
                String url = s.substring(urlStartIndex, s.length() - 1);
                return new Image(null, date, url);
            }).forEach(imgList::add);
        }
        return imgList;
    }

    /**
     * Write to README_PATH
     *
     * @param imgList images info in README
     * @throws IOException file exception
     */
    public static void writeReadme(List<Image> imgList) throws IOException {

        if (!Files.exists(README_PATH)) {
            Files.createFile(README_PATH);
        }
        Files.write(README_PATH, "## Daily Wallpaper Form bing.com".getBytes());
        Files.write(README_PATH, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
        Files.write(README_PATH, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
        Files.write(README_PATH, imgList.get(0).toLarge().getBytes(), StandardOpenOption.APPEND);
        Files.write(README_PATH, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
        Files.write(README_PATH, "|      |      |      |".getBytes(), StandardOpenOption.APPEND);
        Files.write(README_PATH, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
        Files.write(README_PATH,
                    "| :----: | :----: | :----: |".getBytes(),
                    StandardOpenOption.APPEND);
        Files.write(README_PATH, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
        int i = 1;
        for (Image image : imgList) {
            Files.write(README_PATH,
                        ("|" + image.toString()).getBytes(),
                        StandardOpenOption.APPEND);
            if (i % 3 == 0) {
                Files.write(README_PATH, "|".getBytes(), StandardOpenOption.APPEND);
                Files.write(README_PATH,
                            System.lineSeparator().getBytes(),
                            StandardOpenOption.APPEND);
            }
            i++;
        }
        if (i % 3 != 1) {
            Files.write(README_PATH, "|".getBytes(), StandardOpenOption.APPEND);
        }
    }

}
