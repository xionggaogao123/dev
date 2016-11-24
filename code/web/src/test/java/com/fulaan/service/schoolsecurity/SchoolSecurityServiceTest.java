package com.fulaan.service.schoolsecurity;

import com.fulaan.schoolsecurity.service.SchoolSecurityService;
import com.pojo.app.Platform;
import com.pojo.schoolsecurity.SchoolSecurityEntry;
import com.pojo.schoolsecurity.SchoolSecurityImage;
import com.sys.exceptions.ResultTooManyException;
import org.bson.types.ObjectId;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2015/6/19.
 */
public class SchoolSecurityServiceTest {

    private SchoolSecurityService schoolSecurityService=new SchoolSecurityService();

    @Test
    public void addSchoolSecurity()
    {
        List<SchoolSecurityImage> imageList=new ArrayList<SchoolSecurityImage>();
        SchoolSecurityImage image1=new SchoolSecurityImage("C:\\Users\\Public\\Pictures\\Sample Pictures");
        imageList.add(image1);
        SchoolSecurityEntry e=new SchoolSecurityEntry(
                new ObjectId("55521e198cf404c144dd0ea3"),
                "水管漏水2",
                Platform.PC,
                new ObjectId("557994077f727a970e6aff09"),
                imageList);
        schoolSecurityService.addSchoolSecurity(e);
        System.out.println(e.getID().toString());
    }

    @Test
    public void deleteSchoolSecurity(){
        schoolSecurityService.deleteSchoolSecurityInfo("55837cf1158f8bef122cd8d1",new ObjectId("55521e198cf404c144dd0ea3"));
    }

    @Test
    public void getSchoolSecurityCount(){
        int count=schoolSecurityService.selSchoolSecurityCount(0, "557994077f727a970e6aff09");
        System.out.println(count);
    }


    @Test
    public void getSchoolSecurityEntryList() throws ResultTooManyException {
        List<SchoolSecurityEntry> list=schoolSecurityService.selSchoolSecurityInfo(0, new ObjectId("55521e198cf404c144dd0ea3"),"557994077f727a970e6aff09", 0, 10);
        for(SchoolSecurityEntry item:list){
            System.out.println(item.toString());
        }
    }


    @Test
    public void getOneSchoolSecurityInfo(){
        SchoolSecurityEntry item=schoolSecurityService.selOneSchoolSecurityInfo("55837cf1158f8bef122cd8d1");
        System.out.println(item.toString());
    }

    @Test
    public void handleSchoolSecurity(){
        schoolSecurityService.handleSchoolSecurity("55837cf1158f8bef122cd8d1",new ObjectId("55521e198cf404c144dd0ea3"));
    }
}
