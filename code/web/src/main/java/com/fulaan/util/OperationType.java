package com.fulaan.util;

/**
 * Created by admin on 2016/10/26.
 */
public enum OperationType {

    NORMAL(0, "社区成员"),
    MONITOR(1, "副社长"),
    MANAGEMENT(2, "社长"),;

    OperationType(int role, String decs) {
        this.role = role;
        this.decs = decs;
    }

    private int role;
    private String decs;

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getDecs() {
        return decs;
    }

    public void setDecs(String decs) {
        this.decs = decs;
    }
}
