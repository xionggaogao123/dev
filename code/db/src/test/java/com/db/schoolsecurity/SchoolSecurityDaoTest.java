package com.db.schoolsecurity;

import com.pojo.app.Platform;
import com.pojo.schoolsecurity.SchoolSecurityEntry;
import com.pojo.schoolsecurity.SchoolSecurityImage;
import com.pojo.utils.DeleteState;
import com.sys.exceptions.ResultTooManyException;
import org.bson.types.ObjectId;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by guojing on 2015/5/14.
 */
public class SchoolSecurityDaoTest
{
    private SchoolSecurityDao dao =new SchoolSecurityDao();

    @Test
    public void addSchoolSecurity()
    {
        List<SchoolSecurityImage> imageList=new ArrayList<SchoolSecurityImage>();
        SchoolSecurityImage image1=new SchoolSecurityImage("C:\\Users\\Public\\Pictures\\Sample Pictures");
        imageList.add(image1);
        SchoolSecurityEntry e=new SchoolSecurityEntry(
                new ObjectId("55521e198cf404c144dd0ea3"),
                "水管漏水",
                Platform.PC,
                new ObjectId("557994077f727a970e6aff09"),
                imageList);
        dao.addSchoolSecurity(e);
        System.out.println(e.getID().toString());
    }

    @Test
    public void deleteSchoolSecurity(){
        dao.deleteSchoolSecurity(new ObjectId("55837193158f1f953e46e0ad"),new ObjectId("55837193158f1f953e46e0ad"),new Date().getTime());
    }

    @Test
    public void getSchoolSecurityCount(){
        int count=dao.getSchoolSecurityCount(1,"557994077f727a970e6aff09");
        System.out.println(count);
    }


    @Test
    public void getSchoolSecurityEntryList() throws ResultTooManyException {
        List<SchoolSecurityEntry> list=dao.getSchoolSecurityEntryList(1,new ObjectId("55837193158f1f953e46e0ad"), DeleteState.NORMAL,"557994077f727a970e6aff09",0,10);
        for(SchoolSecurityEntry item:list){
            System.out.println(item.toString());
        }
    }


    @Test
    public void getOneSchoolSecurityInfo(){
        SchoolSecurityEntry item=dao.getOneSchoolSecurityInfo(new ObjectId("55837193158f1f953e46e0ad"),null);
        System.out.println(item.toString());
    }

    @Test
    public void handleSchoolSecurity(){
        dao.handleSchoolSecurity(new ObjectId("55837193158f1f953e46e0ad"),new ObjectId("55837193158f1f953e46e0ad"),new Date().getTime());
    }
}
