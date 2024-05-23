package com.arminzheng.wallpaper.domain;

import java.util.Objects;

/**
 * Images
 *
 * @author az
 * @version 2022/2/27
 */
public class Image {

    private String title;
    private String desc;
    private String date;
    private String url;

    public Image(String title, String desc, String date, String url) {

        this.title = title;
        this.desc  = desc;
        this.date  = date;
        this.url   = url;
    }

    public Image(String title, String date, String url) {

        this.title = title;
        this.date  = date;
        this.url   = url;
    }

    /**
     * for README cell content.
     *
     * @return ![](thumbnail url)date [View Original](hyperlink)
     */
    @Override
    public String toString() {

        String smallUrl = url + "&pid=hp&w=384&h=216&rs=1&c=4";
        return String.format("![](%s)%s [View Original](%s)", smallUrl, date, url);
    }

    public String formatMarkdown() {

        return String.format("%s | [%s](%s) ", date, title, url);
    }

    public String toLarge() {

        String smallUrl = url + "&w=1000";
        return String.format("![](%s)Today: [%s](%s)", smallUrl, title, url);
    }

    @Override
    public int hashCode() {

        // Call the hashCode () method of the property in turn and assemble it.
        // serial number * 31 + hashCode
        return Objects.hash(title, desc, date, url);
    }

    @Override
    public boolean equals(Object o) {

        // Determine whether the properties of different objects are equal: 3 steps
        // 1. promotion speed, quick back.
        if (this == o) return true;
        // 2. Class judgement
        if (o == null || getClass() != o.getClass()) return false;
        Image images = (Image)o;
        // 3. Perform property judgment
        // call the equals method of the property and pass in the compared property
        return Objects.equals(title, images.title) && Objects.equals(desc, images.desc)
               && Objects.equals(date, images.date) && Objects.equals(url, images.url);
    }

    public String getTitle() {

        return this.title;
    }

    public String getDate() {

        return this.date;
    }

    public String getUrl() {

        return this.url;
    }

    public String getDesc() {

        return desc;
    }

}
