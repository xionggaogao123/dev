package com.fulaan.count.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * <简述>作业图表dto
 * <详细描述>
 * @author   Brant
 * @version  $Id$
 * @since
 * @see
 */
public class ZytbDto {

    private List<String> subjectName = new ArrayList<String>();
    
    private List<String> subjectId = new ArrayList<String>();
    
    private List<Integer> num = new ArrayList<Integer>();

    public List<String> getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(List<String> subjectName) {
        this.subjectName = subjectName;
    }

    public List<String> getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(List<String> subjectId) {
        this.subjectId = subjectId;
    }

    public List<Integer> getNum() {
        return num;
    }

    public void setNum(List<Integer> num) {
        this.num = num;
    }

    
    
    
}
