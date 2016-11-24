package com.pojo.questionnaire;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;

import java.util.*;

/**
 * Created by qinbo on 15/6/11.
 */
public class OldQuestionnaireEntry  extends BaseDBObject {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7646249549127744556L;


	public OldQuestionnaireEntry(DBObject dbo) {
        this((BasicDBObject)dbo);
    }


    public OldQuestionnaireEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }



    public List<Integer> getAnswerSheet() {
        List<Integer> answers = null;
        BasicDBList list =(BasicDBList)getSimpleObjectValue("answerSheet");
        if(null!=list && !list.isEmpty())
        {
            answers =new ArrayList<Integer>();
            for(Object o:list)
            {
                answers.add(Integer.parseInt(o.toString()));
            }
        }
        return answers;
    }


    public Object getRespondents() {
        return getSimpleObjectValue("respondents");
    }


    public String getName() {
        return getSimpleStringValue("name");
    }


    public Integer getPublisher() {

        return getSimpleIntegerValue("publisher");
    }


    public Object getPublishDate() {
        return getSimpleObjectValue("publishDate");

    }

    public Object getEndDate() {
        return getSimpleObjectValue("endDate");
    }


    public Integer getSchoolId() {
        return getSimpleIntegerValue("schoolId");
    }


    public List<Integer> getClassIds() {
        List<Integer> classes = null;
        BasicDBList list =(BasicDBList)getSimpleObjectValue("classIds");
        if(null!=list && !list.isEmpty())
        {
            classes =new ArrayList<Integer>();
            for(Object o:list)
            {
                classes.add(Integer.parseInt(o.toString()));
            }
        }
        return classes;
    }


    public Object getParentRespondent() {

        return getSimpleObjectValue("parentRespondent");
    }


    public Object getStudentRespondent() {

        return getSimpleObjectValue("studentRespondent");
    }


    public Object getTeacherRespondent() {
        return getSimpleObjectValue("teacherRespondent");
    }


    public String getDocUrl() {
        return getSimpleStringValue("docUrl");
    }

}
