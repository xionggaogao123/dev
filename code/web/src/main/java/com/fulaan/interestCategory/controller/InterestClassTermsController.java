package com.fulaan.interestCategory.controller;


import com.fulaan.base.controller.BaseController;
import com.fulaan.interestCategory.service.InterestClassTermsService;
import com.pojo.interestCategory.InterestClassTermsDTO;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by fl on 2016/1/25.
 */
@Controller
@RequestMapping("/interestClassTerms")
public class InterestClassTermsController extends BaseController{
    @Autowired
    private InterestClassTermsService interestClassTermsService;

    @RequestMapping("/terms")
    @ResponseBody
    public InterestClassTermsDTO getInterestClassTerms(){
        ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
        return interestClassTermsService.getInterestClassTermsDTO(schoolId);
    }
}
