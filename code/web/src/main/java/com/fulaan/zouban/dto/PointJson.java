package com.fulaan.zouban.dto;


/**
 * Created by qiangm on 2015/10/20.
 */
public class PointJson {
    private int x;
    private int y;

    public PointJson() {
    }

    public PointJson(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }



    @Override
    public int hashCode() {
        return 31 * x - 15 * y + 1024;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof PointJson) {
            PointJson p = (PointJson) o;
            if (p.getX() == x && p.getY() == y) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "x:" + x + ",y:" + y;
    }
}
