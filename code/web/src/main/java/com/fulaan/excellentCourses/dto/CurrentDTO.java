package com.fulaan.excellentCourses.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-05-04.
 */
public class CurrentDTO {
    private String isBuy;
    private String isEnd;
    private String isXian;
    private List<HourClassDTO> list = new ArrayList<HourClassDTO>();
    private HourClassDTO now;
}
