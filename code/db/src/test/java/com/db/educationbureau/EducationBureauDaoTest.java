package com.db.educationbureau;

import com.db.app.RegionDao;
import com.db.school.SchoolDao;
import com.pojo.educationbureau.EducationBureauEntry;
import com.pojo.school.SchoolDTO;
import com.pojo.school.SchoolEntry;
import com.sys.exceptions.ResultTooManyException;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by guojing on 2015/5/14.
 */
public class EducationBureauDaoTest
{
    private EducationBureauDao dao =new EducationBureauDao();

    private RegionDao regionDao=new RegionDao();

    private SchoolDao schoolDao = new SchoolDao();

    @Test
    public void addEducation()
    {
        Date currTime=new Date();
        Long createTime= currTime.getTime();

        EducationBureauEntry e=new EducationBureauEntry("安徽省教科院","55934c13f6f28b7261c19c3d","","",createTime,createTime);
        ObjectId id= dao.addEducation(e);
        dao.addEduUser(id,new ObjectId("55934c39f6f28b7261c1e8ff"),createTime);
       // dao.addRelationSchool(id, "55934c15f6f28b7261c19c6f", createTime);

        System.out.println(e.getID().toString());
    }

    
  
    @Test
    public void addEduUser()
    {
        Date currTime=new Date();
        Long updateTime= currTime.getTime();
        ObjectId id=new ObjectId("55eea848158f817046ba9cef");
        ObjectId userId=new ObjectId("55934c26f6f28b7261c1baae");
        dao.addEduUser(id,userId,updateTime);
    }

    @Test
    public void delEduUser()
    {
        Date currTime=new Date();
        Long updateTime= currTime.getTime();
        ObjectId id=new ObjectId("55eea848158f817046ba9cef");
        ObjectId userId=new ObjectId("552e05fdf6f27442d6b52f87");
        dao.delEduUser(id, userId, updateTime);
    }

    @Test
    public void addRelationSchool()
    {
        Date currTime=new Date();
        Long updateTime= currTime.getTime();
        ObjectId id=new ObjectId("55eea848158f817046ba9cef");
        ObjectId schoolId=new ObjectId("55934c14f6f28b7261c19c60");
        DateTimeUtils time=new DateTimeUtils();
        String schoolCreateDate=time.getLongToStrTime(schoolId.getTime());
        dao.addRelationSchool(id,schoolCreateDate, schoolId, updateTime);
    }

/*    @Test
    public void addRelationSchools()
            throws ResultTooManyException {
        Date currTime=new Date();
        Long updateTime= currTime.getTime();
        ObjectId id=new ObjectId("55e6ad45158f56cb4e323ec9");
        ObjectId provinceId=new ObjectId("55934c13f6f28b7261c19c3d");
        List<RegionEntry> regions=regionDao.getRegionEntryList(3, provinceId);
        List<ObjectId> regionIds=new ArrayList<ObjectId>();
        for(RegionEntry item:regions){
            regionIds.add(item.getID());
        }
        List<SchoolEntry> schools=schoolDao.getSchoolEntryByRegionIdList(regionIds);
        List<ObjectId> schoolIds=new ArrayList<ObjectId>();
        for(SchoolEntry item:schools){
            schoolIds.add(item.getID());
        }
        dao.addRelationSchools(id, schoolIds, updateTime);
    }*/
    @Test
    public void addRelationSchools()
            throws ResultTooManyException {
        Date currTime=new Date();
        Long updateTime= currTime.getTime();
        ObjectId id=new ObjectId("55eea848158f817046ba9cef");
        List<ObjectId> schoolIds=new ArrayList<ObjectId>();
        schoolIds.add(new ObjectId("55934c14f6f28b7261c19c63"));
        schoolIds.add(new ObjectId("55934c14f6f28b7261c19c65"));
        schoolIds.add(new ObjectId("55934c14f6f28b7261c19c66"));
        schoolIds.add(new ObjectId("55934c15f6f28b7261c19c7d"));
        schoolIds.add(new ObjectId("55934c15f6f28b7261c19d3a"));
        Collections.sort(schoolIds);
        DateTimeUtils time=new DateTimeUtils();
        String schoolCreateDate=time.getLongToStrTime(schoolIds.get(0).getTime());
        dao.addRelationSchools(id, schoolCreateDate, schoolIds, updateTime);
    }

    @Test
    public void delRelationSchool()
    {
        Date currTime=new Date();
        Long updateTime= currTime.getTime();
        ObjectId id=new ObjectId("55eea848158f817046ba9cef");
        ObjectId schoolId=new ObjectId("552e05f8f6f27442d6b5143c");
        dao.delRelationSchool(id, schoolId, updateTime);
    }

    @Test
    public void selEducationByUserId()
    {
        ObjectId userId=new ObjectId("552e05f9f6f27442d6b51514");
        //ObjectId userId=new ObjectId("552e05f9f6f27442d6b5151a");
        EducationBureauEntry educationBureauEntry =dao.selEducationByUserId(userId);

        System.out.println(educationBureauEntry.toString());
    }

    @Test
    public void selEducationById()
    {
        ObjectId id=new ObjectId("55eea848158f817046ba9cef");
        EducationBureauEntry educationBureauEntry =dao.selEducationById(id);

        System.out.println(educationBureauEntry.toString());
    }

    @Test
    public void updateEducationInfo()
    {
        ObjectId id=new ObjectId("55eea848158f817046ba9cef");
        Date currTime=new Date();
        Long updateTime= currTime.getTime();
        dao.updateEducationInfo(id, "安徽省教育局", "552e05f7f6f27442d6b51417", "", "", updateTime);
    }

    @Test
    public void findSchoolInfoBySchoolIds()
    {
        ObjectId userId=new ObjectId("552e05f9f6f27442d6b51514");
        EducationBureauEntry educationBureauEntry =dao.selEducationByUserId(userId);
        SchoolDao schoolDao=new SchoolDao();
        List<SchoolDTO> list=new ArrayList<SchoolDTO>();
        List<SchoolEntry> SEList=schoolDao.getSchoolEntryList(educationBureauEntry.getSchoolIds());
        for(SchoolEntry schoolEntry:SEList){
            SchoolDTO schoolDTO=new SchoolDTO(schoolEntry);
            list.add(schoolDTO);
        }
    }

    @Test
    public void selAllEducation()
    {
         List<EducationBureauEntry> list=dao.selAllEducation();
        for(EducationBureauEntry item:list){
            System.out.println(item.toString());
        }
    }

    @Test
    public void selEducationByUserIds()
    {
        List<ObjectId> uids=new ArrayList<ObjectId>();
        uids.add(new ObjectId("552e05f9f6f27442d6b51512"));
        uids.add(new ObjectId("552e05f9f6f27442d6b51510"));
        List<EducationBureauEntry> list=dao.selEducationByUserIds(uids);
        for(EducationBureauEntry item:list){
            System.out.println(item.toString());
        }
    }
}
