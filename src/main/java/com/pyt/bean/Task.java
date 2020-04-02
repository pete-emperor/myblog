package com.pyt.bean;

/**
 * Created by peter on 2020/4/2.
 */
public class Task {
    private Integer id;
    private String url;
    private Integer type;//0开启，1关闭

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
