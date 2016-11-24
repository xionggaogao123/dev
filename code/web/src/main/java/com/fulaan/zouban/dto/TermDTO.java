package com.fulaan.zouban.dto;

import com.pojo.zouban.TermEntry;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by qiangm on 2015/11/9.
 */
public class TermDTO {
    private String id;
    private String schoolId;
    private String year;
    private String fts;
    private String fte;
    private String sts;
    private String ste;
    private int fweek;
    private int sweek;

    public TermDTO() {}

    public TermDTO(TermEntry termEntry) {
        this.id = termEntry.getID().toString();
        this.schoolId = termEntry.getSchoolId().toString();
        this.year = termEntry.getYear();
        this.fts = convert(termEntry.getFirstTermStart());
        this.fte = convert(termEntry.getFirstTermEnd());
        this.sts = convert(termEntry.getSecondTermStart());
        this.ste = convert(termEntry.getSecondTermEnd());
    }

    public TermEntry exportEntry() {
        TermEntry entry = new TermEntry();
        if (StringUtils.isNotBlank(id)) {
            entry.setID(new ObjectId(id));
        }
        entry.setSchoolId(new ObjectId(schoolId));
        entry.setYear(year);
        entry.setFirstTermStart(convert(fts));
        entry.setFirstTermEnd(convert(fte));
        entry.setSecondTermStart(convert(sts));
        entry.setSecondTermEnd(convert(ste));

        return entry;
    }

    private long convert(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date.getTime();
    }

    private String convert(long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date(date));
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getFts() {
        return fts;
    }

    public void setFts(String fts) {
        this.fts = fts;
    }

    public String getFte() {
        return fte;
    }

    public void setFte(String fte) {
        this.fte = fte;
    }

    public String getSts() {
        return sts;
    }

    public void setSts(String sts) {
        this.sts = sts;
    }

    public String getSte() {
        return ste;
    }

    public void setSte(String ste) {
        this.ste = ste;
    }

    public int getFweek() {
        return fweek;
    }

    public void setFweek(int fweek) {
        this.fweek = fweek;
    }

    public int getSweek() {
        return sweek;
    }

    public void setSweek(int sweek) {
        this.sweek = sweek;
    }
}
