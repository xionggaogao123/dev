package com.fulaan.count.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * <简述>通知图表dto
 * <详细描述>
 * @author   Brant
 * @version  $Id$
 * @since
 * @see
 */
public class TztbDto {

    private List<String> dateList = new ArrayList<String>();
    
    private List<Integer> num = new ArrayList<Integer>();

    public List<String> getDateList() {
        return dateList;
    }

    public void setDateList(List<String> dateList) {
        this.dateList = dateList;
    }

    public List<Integer> getNum() {
        return num;
    }

    public void setNum(List<Integer> num) {
        this.num = num;
    } 
    
    
}
