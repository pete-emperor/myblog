package com.pyt.bean;

/**
 * Created by peter on 2020/4/2.
 */
public class ArticleTask {

   private Integer id;
   private String indexUrl;
   private String firstUrlRegex;
   private String secondUrlRegex;
   private String titleRegex;
   private String contentRegex;
   private Integer type;
   private String splitStr;
   private String ignoreStr;
   private String pageCharSet = "UTF-8";
   private String articleCategory = "12";
   private String imgPre = "";
   private String pathPre = "";
   private Integer pageSize = 10;

   private Integer maxRepeat = 500;

    private Integer repeat = 0;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIndexUrl() {
        return indexUrl;
    }

    public void setIndexUrl(String indexUrl) {
        this.indexUrl = indexUrl;
    }

    public String getFirstUrlRegex() {
        return firstUrlRegex;
    }

    public void setFirstUrlRegex(String firstUrlRegex) {
        this.firstUrlRegex = firstUrlRegex;
    }

    public String getSecondUrlRegex() {
        return secondUrlRegex;
    }

    public void setSecondUrlRegex(String secondUrlRegex) {
        this.secondUrlRegex = secondUrlRegex;
    }

    public String getTitleRegex() {
        return titleRegex;
    }

    public void setTitleRegex(String titleRegex) {
        this.titleRegex = titleRegex;
    }

    public String getContentRegex() {
        return contentRegex;
    }

    public void setContentRegex(String contentRegex) {
        this.contentRegex = contentRegex;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSplitStr() {
        return splitStr;
    }

    public void setSplitStr(String splitStr) {
        this.splitStr = splitStr;
    }

    public String getIgnoreStr() {
        return ignoreStr;
    }

    public void setIgnoreStr(String ignoreStr) {
        this.ignoreStr = ignoreStr;
    }

    public String getPageCharSet() {
        return pageCharSet;
    }

    public void setPageCharSet(String pageCharSet) {
        this.pageCharSet = pageCharSet;
    }

    public String getArticleCategory() {
        return articleCategory;
    }

    public void setArticleCategory(String articleCategory) {
        this.articleCategory = articleCategory;
    }

    public String getImgPre() {
        return imgPre;
    }

    public void setImgPre(String imgPre) {
        this.imgPre = imgPre;
    }

    public String getPathPre() {
        return pathPre;
    }

    public void setPathPre(String pathPre) {
        this.pathPre = pathPre;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getMaxRepeat() {
        return maxRepeat;
    }

    public void setMaxRepeat(Integer maxRepeat) {
        this.maxRepeat = maxRepeat;
    }

    public Integer getRepeat() {
        return repeat;
    }

    public void setRepeat(Integer repeat) {
        this.repeat = repeat;
    }
}
