package com.fulaan.interestCategory.service;

import com.db.interestCategory.InterestClassTermsDao;
import com.db.school.SchoolDao;
import com.fulaan.examresult.service.ExamResultService;
import com.pojo.app.IdNameValuePair;
import com.pojo.app.IdNameValuePairDTO;
import com.pojo.interestCategory.InterestClassTermsDTO;
import com.pojo.interestCategory.InterestClassTermsEntry;
import com.pojo.school.SchoolEntry;
import com.sys.constants.Constant;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fl on 2016/1/25.
 */
@Service
public class InterestClassTermsService {

    private InterestClassTermsDao interestClassTermsDao = new InterestClassTermsDao();
    @Autowired
    private ExamResultService examResultService;
    private SchoolDao schoolDao = new SchoolDao();

    /**
     *
     * @param schoolId
     * @return
     */
    public InterestClassTermsDTO getInterestClassTermsDTO(ObjectId schoolId){
//        ExamResultService examResultService = new ExamResultService();
//        String currentTerm = examResultService.getCurrentTerm();
//        Boolean hasCurrentTerm = false;
        InterestClassTermsEntry entry = interestClassTermsDao.findInterestClassTermsEntryBySchoolId(schoolId);
        if(null == entry){
            entry = addInterestClassTermsEntry(schoolId);
        }
        InterestClassTermsDTO interestClassTermsDTO = new InterestClassTermsDTO(entry);
//        List<IdNameValuePairDTO> terms = interestClassTermsDTO.getTerms();
//        int max = 0;
//        if(null != terms){
//            for(IdNameValuePairDTO term : terms){
//                if(term.getName().contains(currentTerm)){
//                    hasCurrentTerm = true;
//                }
//                if((Integer)term.getValue() > max){
//                    max = (Integer)term.getValue();
//                }
//            }
//        }
//        if(!hasCurrentTerm){
//            terms.add(new IdNameValuePairDTO("", currentTerm, max + 1));
//        }
        return interestClassTermsDTO;
    }

    private InterestClassTermsEntry addInterestClassTermsEntry(ObjectId schoolId){
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(schoolId,Constant.FIELDS);
        String termName = examResultService.getCurrentTerm();
        IdNameValuePair term = new IdNameValuePair(null, termName, schoolEntry.getTermType());
        List<IdNameValuePair> terms = new ArrayList<IdNameValuePair>();
        terms.add(term);
        InterestClassTermsEntry interestClassTermsEntry = new InterestClassTermsEntry(schoolId, terms);
        interestClassTermsDao.add(interestClassTermsEntry);
        return interestClassTermsEntry;
    }
}
