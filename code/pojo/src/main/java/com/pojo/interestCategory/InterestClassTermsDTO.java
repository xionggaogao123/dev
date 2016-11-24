package com.pojo.interestCategory;

import com.pojo.app.IdNameValuePair;
import com.pojo.app.IdNameValuePairDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fl on 2016/1/25.
 */
public class InterestClassTermsDTO {

    private String schoolId;
    private List<IdNameValuePairDTO> terms = new ArrayList<IdNameValuePairDTO>();
    
    public InterestClassTermsDTO(){}

    public InterestClassTermsDTO(InterestClassTermsEntry entry){
        this.schoolId = entry.getSchoolId().toString();
        List<IdNameValuePair> termList = entry.getTerms();
        if(null!=termList && termList.size()>0){
            for(IdNameValuePair term : termList){
                terms.add(new IdNameValuePairDTO(term));
            }
        }
        formateTerms(terms);
    }

    private void formateTerms(List<IdNameValuePairDTO> terms){
        Map<String, Integer> map = new HashMap<String, Integer>();
        if(null != terms){
            for(IdNameValuePairDTO term : terms){
                String name = term.getName();
                if(map.containsKey(name)){
                    map.put(name, map.get(name) + 1);
                } else {
                    map.put(name, 1);
                }
            }
            int num = 1;
            String previousName = "";
            for(IdNameValuePairDTO term : terms){
                String name = term.getName();
                if(!name.equals(previousName)){
                    num = 1;
                }
                previousName = name;
                if(map.get(name) != 1){
                    term.setName(name + "第" + num +"次选课");
                    num++;
                }
            }
        }
    }


    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public List<IdNameValuePairDTO> getTerms() {
        return terms;
    }

    public void setTerms(List<IdNameValuePairDTO> terms) {
        this.terms = terms;
    }
}
