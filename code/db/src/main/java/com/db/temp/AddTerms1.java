package com.db.temp;

import com.db.interestCategory.InterestClassTermsDao;
import com.db.school.SchoolDao;
import com.pojo.app.IdNameValuePair;
import com.pojo.interestCategory.InterestClassTermsDTO;
import com.pojo.interestCategory.InterestClassTermsEntry;
import com.pojo.school.SchoolEntry;
import com.sys.constants.Constant;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by fl on 2016/1/25.
 */
public class AddTerms1 {
    private SchoolDao schoolDao = new SchoolDao();
    private InterestClassTermsDao interestClassTermsDao = new InterestClassTermsDao();


    private void addTerms(){//为学校创建学期表
        List<SchoolEntry> schoolEntries = schoolDao.getSchoolEntry(0,500);
        if(null!=schoolEntries){
            for(SchoolEntry schoolEntry : schoolEntries){
                addInterestClassTermsEntry(schoolEntry.getID());
            }
        }

    }

    private InterestClassTermsDTO getInterestClassTermsDTO(ObjectId schoolId){
        InterestClassTermsEntry entry = interestClassTermsDao.findInterestClassTermsEntryBySchoolId(schoolId);
        if(null == entry){
            entry = addInterestClassTermsEntry(schoolId);
        }
        return new InterestClassTermsDTO(entry);
    }

    private InterestClassTermsEntry addInterestClassTermsEntry(ObjectId schoolId){
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(schoolId,Constant.FIELDS);
        String termName = getCurrentTerm();
        IdNameValuePair term = new IdNameValuePair(null, termName, schoolEntry.getTermType());
        List<IdNameValuePair> terms = new ArrayList<IdNameValuePair>();
        terms.add(term);
        InterestClassTermsEntry interestClassTermsEntry = new InterestClassTermsEntry(schoolId, terms);
        interestClassTermsDao.add(interestClassTermsEntry);
        return interestClassTermsEntry;
    }
    private String getCurrentTerm() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String schoolYear;
        if (month < 9 && month > 2) {
            schoolYear = (year - 1) + "-" + year + "学年第二学期";
        } else if(month >= 9){
            schoolYear = year + "-" + (year + 1) + "学年第一学期";
        } else {
            schoolYear = (year - 1) + "-" + year + "学年第一学期";
        }
        return schoolYear;
    }

    public static void main(String[] args){
        new AddTerms1().addTerms();
    }
}

