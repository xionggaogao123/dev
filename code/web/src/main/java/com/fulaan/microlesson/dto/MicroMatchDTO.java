package com.fulaan.microlesson.dto;

import com.fulaan.utils.StringUtil;
import com.pojo.app.IdValuePair;
import com.pojo.microlesson.MicroMatchEntry;
import com.pojo.microlesson.ScoreTypeEntry;
import com.pojo.user.UserEntry;
import com.pojo.user.UserInfoDTO;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wang_xinxin on 2015/8/20.
 */
public class MicroMatchDTO {

    private String id;

    private String matchname;

    private String content;

    private String path;

    private String begintime;

    private String endtime;

    private String scorebegintime;

    private String scoreendtime;

    private String[] userlist;

    private String[] matchtypelist;

    private String[] scoretypelist;

    private List<IdValuePair> matchtypes;

    private List<ScoreTypeEntry> scoretypes;

    private List<TypeValueDTO> types;

    private List<TypeValueDTO> stypes;

    private List<UserInfoDTO> users;

    private int showup;

    private long edtime;

    
    private String userName=Constant.EMPTY;
    private String schoolName=Constant.EMPTY;
    
    

    public String getMatchname() {
        return matchname;
    }

    public String getContent() {
        return content;
    }

    public String getBegintime() {
        return begintime;
    }

    public String getEndtime() {
        return endtime;
    }

    public String getScorebegintime() {
        return scorebegintime;
    }

    public String getScoreendtime() {
        return scoreendtime;
    }

    public void setMatchname(String matchname) {
        this.matchname = matchname;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setBegintime(String begintime) {
        this.begintime = begintime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public void setScorebegintime(String scorebegintime) {
        this.scorebegintime = scorebegintime;
    }

    public void setScoreendtime(String scoreendtime) {
        this.scoreendtime = scoreendtime;
    }

    public String[] getUserlist() {
        return userlist;
    }

    public List<ScoreTypeEntry> getScoretypes() {
        return scoretypes;
    }

    public void setScoretypes(List<ScoreTypeEntry> scoretypes) {
        this.scoretypes = scoretypes;
    }

    public String[] getMatchtypelist() {
        return matchtypelist;
    }

    public List<TypeValueDTO> getStypes() {
        return stypes;
    }

    public void setStypes(List<TypeValueDTO> stypes) {
        this.stypes = stypes;
    }

    public void setUserlist(String[] userlist) {
        this.userlist = userlist;
    }

    public void setMatchtypelist(String[] matchtypelist) {
        this.matchtypelist = matchtypelist;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<IdValuePair> getMatchtypes() {
        return matchtypes;
    }

    public void setMatchtypes(List<IdValuePair> matchtypes) {
        this.matchtypes = matchtypes;
    }

    public List<UserInfoDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserInfoDTO> users) {
        this.users = users;
    }

    public List<TypeValueDTO> getTypes() {
        return types;
    }

    public void setTypes(List<TypeValueDTO> types) {
        this.types = types;
    }

    public int getShowup() {
        return showup;
    }

    public void setShowup(int showup) {
        this.showup = showup;
    }

    public long getEdtime() {
        return edtime;
    }

    public void setEdtime(long edtime) {
        this.edtime = edtime;
    }
    
//List<IdValuePair> matchtypelist

    public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

    public String[] getScoretypelist() {
        return scoretypelist;
    }

    public void setScoretypelist(String[] scoretypelist) {
        this.scoretypelist = scoretypelist;
    }

    public MicroMatchEntry buildMicroMatchEntry(ObjectId userid,ObjectId bureauid) {
        List<IdValuePair> matchtypes = new ArrayList<IdValuePair>();
        String[] matchtypelist = this.getMatchtypelist();
        if (matchtypelist!=null && matchtypelist.length!=0) {
            for (String type : matchtypelist) {
                if (!StringUtils.isEmpty(type)) {
                    IdValuePair pair = new IdValuePair(new ObjectId(),type);
                    matchtypes.add(pair);
                }
            }
        }
        List<ScoreTypeEntry> scoretypes = new ArrayList<ScoreTypeEntry>();
        String[] scoreTypeList = this.getScoretypelist();
        if (scoreTypeList!=null && scoreTypeList.length!=0) {
            for (String type : scoreTypeList) {
                if (!StringUtils.isEmpty(type)) {
                    ScoreTypeEntry pair = new ScoreTypeEntry(type.split(";")[0],Integer.valueOf(type.split(";")[1]));
                    scoretypes.add(pair);
                }
            }
        }
        List<ObjectId> users = new ArrayList<ObjectId>();
        String[] userlist = this.getUserlist();
        if (userlist!=null && userlist.length!=0) {
            for (String user : userlist) {
                if (!StringUtils.isEmpty(user)) {
                    users.add(new ObjectId(user));
                }
            }
        }

        MicroMatchEntry microMatchEntry = new MicroMatchEntry(userid,
                this.getMatchname(),
                this.getContent(),
                this.getPath(),
                DateTimeUtils.stringToDate(this.getBegintime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A).getTime(),
                DateTimeUtils.stringToDate(this.getEndtime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A).getTime(),
                DateTimeUtils.stringToDate(this.getScorebegintime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A).getTime(),
                DateTimeUtils.stringToDate(this.getScoreendtime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A).getTime(),
                users,
                matchtypes,
                scoretypes,
                bureauid);
        return microMatchEntry;
    }

//    public MicroMatchDTO (MicroMatchEntry match) {
//        this.matchname = match.getMatchname();
//        this.content = match.getConntent();
//        this.path = match.getPath();
//        this.begintime = DateTimeUtils.convert(match.getBegintime(),DateTimeUtils.DATE_YYYY_MM_DD_B);
//        this.endtime = DateTimeUtils.convert(match.getEndtime(),DateTimeUtils.DATE_YYYY_MM_DD_B);
//        this.scorebegintime = DateTimeUtils.convert(match.getScorebegintime(),DateTimeUtils.DATE_YYYY_MM_DD_B);
//        this.scoreendtime = DateTimeUtils.convert(match.getScoreendtime(),DateTimeUtils.DATE_YYYY_MM_DD_B);
//        this.id = match.getID().toString();
//        this.matchtypes = match.getMatchtypelist();
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MicroMatchEntry buildUpdateMicroMatchEntry() {
        MicroMatchEntry microMatchEntry = new MicroMatchEntry(new ObjectId(),
                this.getMatchname(),
                this.getContent(),
                this.getPath(),
                DateTimeUtils.stringToDate(this.getBegintime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A).getTime(),
                DateTimeUtils.stringToDate(this.getEndtime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A).getTime(),
                DateTimeUtils.stringToDate(this.getScorebegintime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A).getTime(),
                DateTimeUtils.stringToDate(this.getScoreendtime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A).getTime(),
                new ArrayList<ObjectId>(),
                new ArrayList<IdValuePair>(),
                new ArrayList<ScoreTypeEntry>(),
                new ObjectId());
        return microMatchEntry;

    }
}


