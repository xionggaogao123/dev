package com.fulaan.teachermanage.dto;

import com.pojo.teachermanage.CertificateEntry;

/**
 * Created by wang_xinxin on 2016/3/8.
 */
public class CertificateDTO {
    private String time;
    private String name;
    private String record;
    private String open;

    public CertificateDTO(){

    }

    public CertificateDTO(CertificateEntry entry) {
        this.time = entry.getTime();
        this.name = entry.getName();
        this.record = entry.getRecord();
        this.open = String.valueOf(entry.getOpen());
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }
}
