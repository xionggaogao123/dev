package com.pojo.elect;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;

/**
 * Created by qinbo on 15/6/11.
 */
public class OldElectEntry extends BaseDBObject {
    private static final long serialVersionUID = 7175486522656363547L;


    public OldElectEntry(DBObject dbo) {
        this((BasicDBObject)dbo);
    }


    public OldElectEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }


    public String getName() {
        return getSimpleStringValue("name");
    }


    public String getDescription() {
        return getSimpleStringValue("description");
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


    public Integer getSchoolId() {
        return getSimpleIntegerValue("schoolId");
    }


    public Object getStartDate() {
        return getSimpleObjectValue("startDate");
    }

    public Object getEndDate() {
        return getSimpleObjectValue("endDate");
    }


    public Integer getPublisher() {
        return getSimpleIntegerValue("publisher");
    }


    public Object getPublishDate() {
        return getSimpleObjectValue("publishDate");
    }



    public Object getParentEligible() {
        return getSimpleObjectValue("parentEligible");
    }


    public Object getStudentEligible() {
        return getSimpleObjectValue("studentEligible");
    }


    public Object getTeacherEligible() {
        return getSimpleObjectValue("teacherEligible");
    }


    public Object getParentVotable() {
        return getSimpleObjectValue("parentVotable");
    }


    public Object getStudentVotable() {
        return getSimpleObjectValue("studentVotable");
    }


    public Object getTeacherVotable() {
        return getSimpleObjectValue("teacherVotable");
    }



    public Integer getBallotCount() {

        try {
            return getSimpleIntegerValue("ballotCount");
        }
        catch (Exception e){
            return 0;
        }
    }


    public Object getModifyTime() {
        return getSimpleObjectValue("modifyTime");
    }


    public List<OldCandidate> getCandidates() {
        List<OldCandidate> candidates = null;

        try {
            BasicDBList list =(BasicDBList)getSimpleObjectValue("candidates");
            if(null!=list && !list.isEmpty())
            {
                candidates =new ArrayList<OldCandidate>();
                for(Object o:list)
                {
                    candidates.add(new OldCandidate((BasicDBObject)o));
                }
            }
        }
        catch (Exception e){
            return null;
        }

        return candidates;
    }

}
