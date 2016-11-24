package com.fulaan.webspider;

import org.junit.Test;

import static org.junit.Assert.*;

public class WebSpiderTest {

    private WebSpider webSpider=new WebSpider();
    @Test
    public void testMain() throws Exception {
        webSpider.main();
    }
    @Test
    public void add()
    {
        webSpider.addBozhou();
    }
    @Test
    public void update()
    {
        webSpider.updateChaohu();
    }
    @Test
    public void addRegion()
    {
        webSpider.addRegion();
    }
    @Test
    public void getMapInfo()
    {
        webSpider.getMapInfo();
    }

    @Test
    public void setSchoolId()
    {
        webSpider.setSchoolId();
    }

    @Test
    public void updateSchoolType()
    {
        webSpider.updateSchoolType();
    }

    @Test
    public void setTestSchool()
    {
        webSpider.setShidianxiao();
    }
    @Test
    public void addSchool()
    {
        webSpider.addSchool();
    }
    @Test
    public void setAllTest()
    {
        /*webSpider.setAllTest();*/
    }
}