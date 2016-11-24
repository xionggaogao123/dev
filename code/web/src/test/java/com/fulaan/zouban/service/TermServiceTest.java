package com.fulaan.zouban.service;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class TermServiceTest {

    private TermService termService;

    @Before
    public void setUp() throws Exception {
        termService=new TermService();
    }

    @Test
    public void testFindDTO()
    {
        termService.findTermDTO("2015-2016学年",new ObjectId("55934c15f6f28b7261c19c86"));
    }

    @Test
    public void testGetCurrentTerm()
    {
        Map<String,Object> map= termService.getCurrentTerm("55934c14f6f28b7261c19c62");
        System.out.println(map);
    }
}