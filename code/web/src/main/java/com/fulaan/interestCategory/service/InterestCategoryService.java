package com.fulaan.interestCategory.service;

import com.db.interestCategory.InterestCategoryDao;
import com.db.school.InterestClassDao;
import com.db.school.SchoolDao;
import com.pojo.interestCategory.InterestCategoryEntry;
import com.pojo.school.InterestClassEntry;
import com.pojo.school.SchoolEntry;
import com.sys.constants.Constant;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fl on 2015/11/18.
 */
@Service
public class InterestCategoryService {
    private InterestCategoryDao interestCategoryDao= new InterestCategoryDao();
    private InterestClassDao interestClassDao =new InterestClassDao();
    private SchoolDao schoolDao=new SchoolDao();

    public Map<String, Object> getInterestCategoryList(ObjectId schoolId){
        Map<String, Object> returnMap = new HashMap<String, Object>();
        List<InterestCategoryEntry> interestCategoryEntryList = interestCategoryDao.findInterestCategory(schoolId);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if(interestCategoryEntryList != null && interestCategoryEntryList.size()>0){
            for(InterestCategoryEntry entry : interestCategoryEntryList){
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("name", entry.getName());
                map.put("id", entry.getID().toString());
                list.add(map);
            }
        }
        returnMap.put("list", list);
        return returnMap;
    }

    public void addInterestCategory(ObjectId schoolId, String name){
        InterestCategoryEntry interestCategoryEntry = new InterestCategoryEntry(schoolId, name);
        interestCategoryDao.addInterestCategory(interestCategoryEntry);
    }

    public String deleteInterestCategory(ObjectId interestCategoryId, String schoolID){
        SchoolEntry schoolEntry=schoolDao.getSchoolEntry(new ObjectId(schoolID),Constant.FIELDS);
        List<InterestClassEntry> classEntryList= interestClassDao.findClassBySchoolId(new ObjectId(schoolID), schoolEntry.getTermType(),  interestCategoryId.toString(), null);
        if(classEntryList!=null && classEntryList.size()>0){
            return "300";
        }
        interestCategoryDao.removeInterestCategory(interestCategoryId);
        return "200";
    }

    public void editInterestCategory(ObjectId interestCategoryId, String name){
        interestCategoryDao.updateInterestCategory(interestCategoryId, name);
    }

    public void updateTypeId(ObjectId classId, ObjectId typeId){
        interestClassDao.updateTypeId(classId, typeId);
        InterestClassEntry interestClassEntry = interestClassDao.findEntryByClassId(classId);
        if(interestClassEntry.getRelationId() != null){
            interestClassDao.updateTypeId(interestClassEntry.getRelationId(), typeId);
        }
    }
}
