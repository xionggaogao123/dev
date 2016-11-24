package com.fulaan.cloudhead;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Tony on 2016/1/14.
 */
@Controller
@RequestMapping("/micro1")
public class cloudsubhead{
    @RequestMapping("/cloudsubhead")
    public String page1()
    {
        return "common_new/subhead-cloud";
    }
}