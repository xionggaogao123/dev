package com.fulaan.pojo;


import java.util.List;

/**
 * Created by jerry on 2016/9/6.
 * 分页数据
 */
public class PageModel<T> {

    private int page;
    private int pageSize;
    private int totalCount;
    private long totalPages;
    private int totalUnReadCount;
    private List<T> result;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public int getTotalUnReadCount() {
        return totalUnReadCount;
    }

    public void setTotalUnReadCount(int totalUnReadCount) {
        this.totalUnReadCount = totalUnReadCount;
    }
}
