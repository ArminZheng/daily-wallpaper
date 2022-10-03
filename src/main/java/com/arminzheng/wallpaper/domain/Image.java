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
  private String date;
  private String url;

  public Image(String title, String date, String url) {
    this.title = title;
    this.date = date;
    this.url = url;
  }

  public Image() {
  }

  @Override
  public String toString() {
    String smallUrl = url + "&pid=hp&w=384&h=216&rs=1&c=4";
    // ![](smallUrl)date [View Original](url)
    // 缩略图 日期 超链接 <= README 单元格内容
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
    // 依次调用属性的 hashCode() 方法，并进行组装（序号*31 + hashCode）
    return Objects.hash(title, date, url);
  }

  @Override
  public boolean equals(Object o) {
    // 判断不同对象的属性是否相等: 3 步
    // 1. promotion speed, quick back.
    if (this == o) return true;
    // 2. 属性判断预处理
    if (o == null || getClass() != o.getClass()) return false;
    Image images = (Image) o;
    // 3. 进行属性判断（调用属性的 equals 方法，传入对比的属性）
    return Objects.equals(title, images.title)
        && Objects.equals(date, images.date)
        && Objects.equals(url, images.url);
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

  public void setTitle(String title) {
    this.title = title;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
