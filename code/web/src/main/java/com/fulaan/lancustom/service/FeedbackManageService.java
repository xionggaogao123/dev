package com.fulaan.lancustom.service;

import com.db.backstage.TeacherApproveDao;
import com.db.operation.AppOperationDao;
import com.db.user.NewVersionUserRoleDao;
import com.db.user.UserDao;
import com.fulaan.lancustom.dto.FeedbackManageDto;
import com.fulaan.operation.dto.AppOperationDTO;
import com.pojo.backstage.TeacherApproveEntry;
import com.pojo.operation.AppOperationEntry;
import com.pojo.user.NewVersionUserRoleEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: taotao.chan
 * @Date: 2018/11/21 11:04
 * @Description:
 */
@Service
public class FeedbackManageService {

    private  AppOperationDao appOperationDao = new AppOperationDao();
    private UserDao userDao = new UserDao();
    private TeacherApproveDao teacherApproveDao = new TeacherApproveDao();
    private NewVersionUserRoleDao newVersionUserRoleDao = new NewVersionUserRoleDao();
    /**
     * 获取待阅留言反馈
     * @param inputParam
     * @param page
     * @param pageSize
     * @return
     */
    public Map<String,Object> getFeedbackMsgByReadyRead(String inputParam, int page, int pageSize) {
        Map<String,Object> result = new HashMap<String, Object>();
        ObjectId userId = null;
        if (!"".equals(inputParam)){
            //用户名，家校美ID 转用户id
            UserEntry userEntry = userDao.findByUserNameOrMobileOrJiaId(inputParam);
            userId = userEntry.getID();
        }
        result = appOperationDao.getFeedbackMsgByReadyRead(userId, page, pageSize);
        List<AppOperationEntry> appOperationEntries = (ArrayList) result.get("entries");

        //获取用户基础信息
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        for (AppOperationEntry entry : appOperationEntries){
            userIds.add(entry.getUserId());
        }
        Map<ObjectId,UserEntry> userEntryMap =  userDao.getUserEntryMap(userIds,Constant.FIELDS);

        //获取用户角色信息
        //老师
        Map<ObjectId,TeacherApproveEntry> teacherApproveEntryMap = teacherApproveDao.getTeacherEntryMap(userIds);
        //学生或家长
        Map<ObjectId,NewVersionUserRoleEntry> userRoleEntryMap = newVersionUserRoleDao.getEntryMap(userIds);

        List<FeedbackManageDto> feedbackManageDtos = new ArrayList<FeedbackManageDto>();
        for (AppOperationEntry entry : appOperationEntries){
            FeedbackManageDto feedbackManageDto = new FeedbackManageDto(entry);
            UserEntry userEntry = userEntryMap.get(entry.getUserId());
            feedbackManageDto.setUserName(userEntry.getUserName());
            feedbackManageDto.setJiaId(userEntry.getGenerateUserCode());
            //用户角色先判断是否是老师，再判断学生和家长
            if (null != teacherApproveEntryMap.get(entry.getUserId())){
                feedbackManageDto.setUserRoleName("老师");
            }else {
                NewVersionUserRoleEntry userRoleEntry = userRoleEntryMap.get(entry.getUserId());
                if (null != userRoleEntry){
                    if (0 == userRoleEntry.getNewRole()){
                        feedbackManageDto.setUserRoleName("家长");
                    }else {
                        feedbackManageDto.setUserRoleName("学生");
                    }
                }else {
                    feedbackManageDto.setUserRoleName("未知");
                }
            }
            feedbackManageDtos .add(feedbackManageDto);
        }
        result.put("feedbackManageDtos",feedbackManageDtos);
        result.remove("entries");
        return result;
    }

    /**
     * 获取已办留言反馈
     * @param inputParam
     * @param page
     * @param pageSize
     * @return
     */
    public Map<String,Object> getFeedbackMsgByAlreadyRead(String inputParam, int page, int pageSize) {
        Map<String,Object> result = new HashMap<String, Object>();
        ObjectId userId = null;
        if (!"".equals(inputParam)){
            //用户名，家校美ID 转用户id
            UserEntry userEntry = userDao.findByUserNameOrMobileOrJiaId(inputParam);
            userId = userEntry.getID();
        }
        result = appOperationDao.getFeedbackMsgByAlreadyRead(userId, page, pageSize);
        List<AppOperationEntry> appOperationEntries = (ArrayList) result.get("entries");

        //获取用户基础信息
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        for (AppOperationEntry entry : appOperationEntries){
            userIds.add(entry.getUserId());
        }
        Map<ObjectId,UserEntry> userEntryMap =  userDao.getUserEntryMap(userIds,Constant.FIELDS);

        //获取用户角色信息
        //老师
        Map<ObjectId,TeacherApproveEntry> teacherApproveEntryMap = teacherApproveDao.getTeacherEntryMap(userIds);
        //学生或家长
        Map<ObjectId,NewVersionUserRoleEntry> userRoleEntryMap = newVersionUserRoleDao.getEntryMap(userIds);

        List<FeedbackManageDto> feedbackManageDtos = new ArrayList<FeedbackManageDto>();
        for (AppOperationEntry entry : appOperationEntries){
            FeedbackManageDto feedbackManageDto = new FeedbackManageDto(entry);
            UserEntry userEntry = userEntryMap.get(entry.getUserId());
            feedbackManageDto.setUserName(userEntry.getUserName());
            feedbackManageDto.setJiaId(userEntry.getGenerateUserCode());
            //用户角色先判断是否是老师，再判断学生和家长
            if (null != teacherApproveEntryMap.get(entry.getUserId())){
                feedbackManageDto.setUserRoleName("老师");
            }else {
                NewVersionUserRoleEntry userRoleEntry = userRoleEntryMap.get(entry.getUserId());
                if (null != userRoleEntry){
                    if (0 == userRoleEntry.getNewRole()){
                        feedbackManageDto.setUserRoleName("家长");
                    }else {
                        feedbackManageDto.setUserRoleName("学生");
                    }
                }else {
                    feedbackManageDto.setUserRoleName("未知");
                }
            }
            feedbackManageDtos .add(feedbackManageDto);
        }
        result.put("feedbackManageDtos",feedbackManageDtos);
        result.remove("entries");
        return result;
    }

    /**
     * 回复留言
     * @param dto
     * @return
     */
    public String replyFeedbackMsg(FeedbackManageDto dto) {
        AppOperationEntry en = dto.buildAddEntry();
        //获得当前时间
        long current=System.currentTimeMillis();
        en.setDateTime(current);
        String id = appOperationDao.addEntry(en);
        //提的问题 置为已读
        appOperationDao.updateAppOperationEntryToRead(new ObjectId(dto.getParentId()));
        return id;
    }

    /**
     * 查看回复
     * @param parentId
     * @return
     */
    public FeedbackManageDto getFeedbackMsgAnswerByParentId(String parentId) {
        AppOperationEntry appOperationEntry = appOperationDao.getAnswerEntryByParentId(parentId);
        FeedbackManageDto feedbackManageDto = new FeedbackManageDto(appOperationEntry);
        UserEntry userEntry = userDao.findByUserId(appOperationEntry.getUserId());
        feedbackManageDto.setUserName(userEntry.getUserName());
        return feedbackManageDto;
    }
}
