package com.pyt.bean;

/**
 * Created by PC on 2020/4/7.
 */
public class PageInfo {
    private Integer pageIndex = 1;
    private Integer pageSize = 10;
    private Integer pageCount;

    private Integer start;

    public Integer getStart() {
        return (pageIndex-1)*pageSize;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }
}
