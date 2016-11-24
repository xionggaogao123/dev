package com.pojo.elect;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qinbo on 15/2/28.
 * 投票选举信息表
 * <pre>
 * collectionName:elects
 * </pre>
 * <pre>
 * {
 *  nm:标题
 *  desc:说明
 *  cls[]:班级id,空表示全校
 *  sid:学校id
 *  stt:开始时间,long
 *  edt:结束时间,long
 *  pbt:发布时间,long
 *  mdt:修改时间,long
 *  pb:发布人id
 *  pare:家长参选资格0,1
 *  stue:学生参选资格0,1
 *  teae:教师参选资格0,1
 *  leae:校领导投票资格0,1-------新加
 *  parv:家长投票资格0,1
 *  stuv:学生投票资格0,1
 *  teav:教师投票资格0,1
 *  leav:校领导投票资格0,1-------新加
 *  bcnt:每人票数
 *  cands[]:候选人列表,空表示暂无
 *  pub:是否发布选举结果 0发布（默认），1不发布----新加;用于前端时变为2位数，十位数为原数字，个位数0表示用户可见1表示不可见
 *
 * }
 * </pre>
 *
 * @author qinbo
 */
public class ElectEntry extends BaseDBObject{

    private static final long serialVersionUID = 7175486522656363547L;


    public ElectEntry(DBObject dbo) {
        this((BasicDBObject)dbo);
    }


    public ElectEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    /**
     * 构造器
     *
     */
    public ElectEntry(String name,String description,List<ObjectId> classIds,ObjectId schoolId,
                      long startTime,long endTime,long publishTime,long modifyTime,
                      ObjectId publisherId,int parentEligible,int studentEligible,
                      int teacherEligible, int leaderEligible,int parentVotable, int studentVotable,
                      int teacherVotable, int leaderVotable,int ballotCount,List<Candidate> candidates,int pub,int manageType)
    {
        super();

        BasicDBList cls = null;
        if(classIds !=null && classIds.size()>0)
        {
            cls = new BasicDBList();
            for(ObjectId cid : classIds)
            {
                cls.add(cid);
            }
        }


        BasicDBList cands =null ;
        if(candidates!=null&& candidates.size()>0)
        {
            cands = new BasicDBList();
            for(Candidate ca : candidates)
            {
                cands.add(ca.getBaseEntry());
            }
        }

        BasicDBObject dbo =new BasicDBObject()
                .append("nm", name)
                .append("desc", description)
                .append("cls", cls)
                .append("sid", schoolId)
                .append("stt", startTime)
                .append("edt", endTime)
                .append("pbt", publishTime)
                .append("mdt", modifyTime)
                .append("pb", publisherId)
                .append("pare", parentEligible)
                .append("stue", studentEligible)
                .append("teae", teacherEligible)
                .append("leae", leaderEligible)
                .append("parv", parentVotable)
                .append("stuv", studentVotable)
                .append("teav", teacherVotable)
                .append("leav", leaderVotable)
                .append("bcnt", ballotCount)
                .append("cands", cands)
                .append("pub",pub)
                .append("mtp",manageType);
        setBaseEntry(dbo);
    }
//    public int getManageType(){return getSimpleIntegerValue("mtp");}
    public void setManageType(int manageType){ setSimpleValue("mtp",manageType);}
    public String getName() {
        return getSimpleStringValue("nm");
    }
    public void setName(String name) {
        setSimpleValue("nm", name);
    }

    public String getDescription() {return getSimpleStringValue("desc");}
    public void setDescription(String description) { setSimpleValue("desc", description);}

    public List<String> getClassIds() {
        List<String> classes = null;
        BasicDBList list =(BasicDBList)getSimpleObjectValue("cls");
        if(null!=list && !list.isEmpty())
        {
            classes =new ArrayList<String>();
            for(Object o:list)
            {
                classes.add(o.toString());
            }
        }
        return classes;
    }
    public void setClassIds(List<String> classIds) {
        BasicDBList classes = null;
        if(classIds!=null&& classIds.size()>0)
        {
            classes = new BasicDBList();
            for(String cid : classIds)
            {
                classes.add(new ObjectId(cid));
            }
        }

        setSimpleValue("cls", classes);
    }

    public ObjectId getSchoolId() {return getSimpleObjecIDValue("sid");}
    public void setSchoolId(ObjectId schoolId) {setSimpleValue("sid", schoolId);}

    public long getStartTime() {return getSimpleLongValue("stt");}
    public void setStartTime(long startTime) {setSimpleValue("stt",startTime);}

    public long getEndTime() {return getSimpleLongValue("edt");}
    public void setEndTime(long endTime) {setSimpleValue("edt",endTime);}

    public long getPublishTime() {return getSimpleLongValue("pbt");}
    public void setPublishTime(long publishTime) {setSimpleValue("pbt",publishTime);}

    public long getModifyTime() {return getSimpleLongValue("mdt");}
    public void setModifyTime(long time) {setSimpleValue("mdt",time);}


    public ObjectId getPublisherId() {return getSimpleObjecIDValue("pb");}
    public void setPublisherId(ObjectId publisherId) {setSimpleValue("pb", publisherId);}

    public int getStudentEligible() {return getSimpleIntegerValue("stue");}
    public void setStudentEligible(int eligible) {setSimpleValue("stue", eligible);}


    public int getTeacherEligible() {return getSimpleIntegerValue("teae");}
    public void setTeacherEligible(int eligible) {setSimpleValue("teae", eligible);}

    public int getParentEligible() {return getSimpleIntegerValue("pare");}
    public void setParentEligible(int parentEligible) {setSimpleValue("pare", parentEligible);}

    public int getLeaderEligible() {
        if (this.getBaseEntry().containsField("leav")) {
            return getSimpleIntegerValue("leae");
        }
        return Constant.ZERO;
    }
    public void setLeaderEligible(int leaderEligible) {setSimpleValue("leae", leaderEligible);}

    public int getParentVotable() {return getSimpleIntegerValue("parv");}
    public void setParentVotable(int votable) {setSimpleValue("parv", votable);}


    public int getStudentVotable() {return getSimpleIntegerValue("stuv");}
    public void setStudentVotable(int votable) {setSimpleValue("stuv", votable);}


    public int getTeacherVotable() {return getSimpleIntegerValue("teav");}
    public void setTeacherVotable(int votable) {setSimpleValue("teav", votable);}

    public int getLeaderVotable() {
        if (this.getBaseEntry().containsField("leav")) {
            return getSimpleIntegerValue("leav");
        }
        return Constant.ZERO;
    }
    public void setLeaderVotable(int votable) {setSimpleValue("leav", votable);}

    public int getBallotCount(){return getSimpleIntegerValue("bcnt");}
    public void setBallotCount(int ballotCount){ setSimpleValue("bcnt",ballotCount);}

    public List<Candidate> getCandidates(){
        List<Candidate> candidateList =null;
        BasicDBList list =(BasicDBList)getSimpleObjectValue("cands");
        if(null!=list && !list.isEmpty())
        {
            candidateList =new ArrayList<Candidate>();
            for(Object o:list)
            {
                candidateList.add(new Candidate((BasicDBObject)o));
            }
        }
        return candidateList;
    }

    public void setCandidateList(List<Candidate> candidateList) {

        BasicDBList list = null;
        if(candidateList!=null && candidateList.size()>0)
        {
            list = new BasicDBList();
            for(Candidate cand : candidateList)
            {
                list.add(cand.getBaseEntry());
            }
        }

        setSimpleValue("cands", list);
    }
    public int getPublish()
    {
        if(this.getBaseEntry().containsField("pub"))
        {
            return getSimpleIntegerValue("pub");
        }
        return 0;
    }
    public void setPublish(int publish)
    {
        setSimpleValue("pub",publish);
    }


}
