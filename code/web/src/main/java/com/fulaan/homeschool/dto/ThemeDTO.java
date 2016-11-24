package com.fulaan.homeschool.dto;

/**
 * Created by wang_xinxin on 2015/12/14.
 */
public class ThemeDTO {

    private String id;

    private String themedsc;

    private int count;

    private int delflg;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThemedsc() {
        return themedsc;
    }

    public void setThemedsc(String themedsc) {
        this.themedsc = themedsc;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getDelflg() {
        return delflg;
    }

    public void setDelflg(int delflg) {
        this.delflg = delflg;
    }
}
