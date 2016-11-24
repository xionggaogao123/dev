package com.fulaan.meeting.dto;

import com.pojo.app.IdValuePair;
import com.pojo.app.SimpleDTO;
import com.pojo.lesson.LessonWare;
import com.pojo.meeting.MeetingEntry;
import com.pojo.user.UserDetailInfoDTO;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2016/8/1.
 */
public class MeetingDTO {

    private String id;

    private String name;

    private String modelName;

    private int type;

    private String startTime;

    private String endTime;

    private String cause;

    private String process;

    private String[] users;

    private String order;

    private String issue;

    private String approvalUserId;

    private int modelType;

    private String[] filenameAry;

    private String[] pathAry;

    private String time;

    private int status;

    private String meetDate;

    private List<UserDetailInfoDTO> userlist;

    private List<UserDetailInfoDTO> checkUserList;

    private List<UserDetailInfoDTO> noCheckUserList;

    private List<SimpleDTO> coursewareList;

    private List<VoteDTO> voteDTOList;

    private int approvalType;

    private String userName;

    private List<UserDetailInfoDTO> sheHeUserList;

    private String meetId;

    private String chatId;

    private List<UserDetailInfoDTO> chatUsers;

    private String userId;

    private List<SimpleDTO> issueList;

    public MeetingDTO() {

    }

    public MeetingDTO(MeetingEntry meetingEntry) {
        this.id = meetingEntry.getID().toString();
        this.name = meetingEntry.getName();
        this.modelName = meetingEntry.getModelName();
        this.approvalUserId = meetingEntry.getApprovalUserId();
        this.issue = meetingEntry.getIssue();
        this.cause = meetingEntry.getCause();
        this.process = meetingEntry.getProcess();
        this.order = meetingEntry.getOrder();
        this.chatId = meetingEntry.getRoomId();
        this.type = meetingEntry.getType();
        this.startTime = DateTimeUtils.getLongToStrTimeSIX(meetingEntry.getOpenTime());
        this.endTime = DateTimeUtils.getLongToStrTimeSIX(meetingEntry.getEndTime());
        this.meetDate = DateTimeUtils.getLongToStrTimeSEVEN(meetingEntry.getEndTime());
        this.approvalType = meetingEntry.getApprovalType();
        this.userId = meetingEntry.getUserId().toString();
        this.time = DateTimeUtils.getLongToStrTimeFive(meetingEntry.getOpenTime())+"-"+DateTimeUtils.getLongToStrTimeSIX(meetingEntry.getEndTime());
        int stus = 0;
        if (meetingEntry.getStatus()==1) {
            stus = 1;
        } else {
            if (meetingEntry.getStatus()==4||DateTimeUtils.compare_date(meetingEntry.getEndTime(), System.currentTimeMillis())==-1) {
                stus = 4;//一结束
            } else if (DateTimeUtils.compare_date(meetingEntry.getOpenTime(), System.currentTimeMillis())==1) {
                stus = 2;//未开始
            } else {
                stus = 3;
            }
        }
        this.status = stus;
        List<SimpleDTO> issueList = new ArrayList<SimpleDTO>();

        if (meetingEntry.getIssues()!=null && meetingEntry.getIssues().size()!=0) {
            for (int i=meetingEntry.getIssues().size();i>0;i--) {
                SimpleDTO simpleDTO = new SimpleDTO();
                simpleDTO.setId(meetingEntry.getIssues().get(i-1).getId().toString());
                simpleDTO.setValue(meetingEntry.getIssues().get(i-1).getValue().toString());
                simpleDTO.setType(1);
                issueList.add(simpleDTO);
            }
        }
        SimpleDTO simpleDTO = new SimpleDTO();
        simpleDTO.setId("");
        simpleDTO.setValue(meetingEntry.getIssue());
        simpleDTO.setType(0);
        issueList.add(simpleDTO);
        this.issueList = issueList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String[] getUsers() {
        return users;
    }

    public void setUsers(String[] users) {
        this.users = users;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getApprovalUserId() {
        return approvalUserId;
    }

    public void setApprovalUserId(String approvalUserId) {
        this.approvalUserId = approvalUserId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public int getModelType() {
        return modelType;
    }

    public void setModelType(int modelType) {
        this.modelType = modelType;
    }

    public String[] getFilenameAry() {
        return filenameAry;
    }

    public void setFilenameAry(String[] filenameAry) {
        this.filenameAry = filenameAry;
    }

    public String[] getPathAry() {
        return pathAry;
    }

    public void setPathAry(String[] pathAry) {
        this.pathAry = pathAry;
    }

    //ObjectId userId, ObjectId schoolId,String name,int type,long openTime,long endTime,String cause,String process,List<ObjectId> userIds,String order,String issue,
    //List<LessonWare> lessonWareList,String approvalUserId,List<ObjectId> approvalUserIds
    public MeetingEntry buildMeetingEntry(ObjectId userId,ObjectId schoolId,List<String> chatIds,String roomId) {
        List<LessonWare> lessonWares = new ArrayList<LessonWare>();
        if (this.getFilenameAry()!=null) {
            for (int i=0;i<this.getFilenameAry().length;i++) {
                if (!StringUtils.isEmpty(this.getFilenameAry()[i])) {
                    lessonWares.add(new LessonWare(this.getFilenameAry()[i].substring(this.getFilenameAry()[i].lastIndexOf(".")+1),this.getFilenameAry()[i],this.getPathAry()[i]));
                }
            }
        }
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        if (this.getUsers()!=null) {
            for(int i=0;i<this.getUsers().length;i++) {
                if (!StringUtils.isEmpty(this.getUsers()[i])) {
                    userIds.add(new ObjectId(this.getUsers()[i]));
                }
            }
        }

        int status = 1;
        if (StringUtils.isEmpty(this.approvalUserId)) {
            status = 2;
        }
        List<IdValuePair> users = new ArrayList<IdValuePair>();
        if (!StringUtils.isEmpty(this.approvalUserId)) {
            users.add(new IdValuePair(new ObjectId(this.approvalUserId),""));
        }
        return new MeetingEntry(userId,schoolId,this.name,this.type,
                DateTimeUtils.getStrToLongTime(this.startTime,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM),
                DateTimeUtils.getStrToLongTime(this.endTime,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM),this.cause,this.process,userIds,chatIds,roomId,
                this.order,this.issue,
                lessonWares,this.approvalUserId,null,this.modelType,this.modelName,status
        );
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMeetDate() {
        return meetDate;
    }

    public void setMeetDate(String meetDate) {
        this.meetDate = meetDate;
    }

    public List<UserDetailInfoDTO> getUserlist() {
        return userlist;
    }

    public void setUserlist(List<UserDetailInfoDTO> userlist) {
        this.userlist = userlist;
    }

    public List<SimpleDTO> getCoursewareList() {
        return coursewareList;
    }

    public void setCoursewareList(List<SimpleDTO> coursewareList) {
        this.coursewareList = coursewareList;
    }

    public List<UserDetailInfoDTO> getCheckUserList() {
        return checkUserList;
    }

    public void setCheckUserList(List<UserDetailInfoDTO> checkUserList) {
        this.checkUserList = checkUserList;
    }

    public List<UserDetailInfoDTO> getNoCheckUserList() {
        return noCheckUserList;
    }

    public void setNoCheckUserList(List<UserDetailInfoDTO> noCheckUserList) {
        this.noCheckUserList = noCheckUserList;
    }

    public List<VoteDTO> getVoteDTOList() {
        return voteDTOList;
    }

    public void setVoteDTOList(List<VoteDTO> voteDTOList) {
        this.voteDTOList = voteDTOList;
    }

    public int getApprovalType() {
        return approvalType;
    }

    public void setApprovalType(int approvalType) {
        this.approvalType = approvalType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<UserDetailInfoDTO> getSheHeUserList() {
        return sheHeUserList;
    }

    public void setSheHeUserList(List<UserDetailInfoDTO> sheHeUserList) {
        this.sheHeUserList = sheHeUserList;
    }

    public String getMeetId() {
        return meetId;
    }

    public void setMeetId(String meetId) {
        this.meetId = meetId;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public List<UserDetailInfoDTO> getChatUsers() {
        return chatUsers;
    }

    public void setChatUsers(List<UserDetailInfoDTO> chatUsers) {
        this.chatUsers = chatUsers;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<SimpleDTO> getIssueList() {
        return issueList;
    }

    public void setIssueList(List<SimpleDTO> issueList) {
        this.issueList = issueList;
    }
}
