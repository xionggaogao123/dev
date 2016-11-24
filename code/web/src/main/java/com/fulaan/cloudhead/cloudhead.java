package com.fulaan.cloudhead;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Tony on 2016/1/14.
 */
@Controller
@RequestMapping("/micro1")
public class cloudhead{
    @RequestMapping("/cloudhead")
    public String page1()
    {
        return "common_new/head-cloud";
    }
}