package com.fulaan.dorm.service;

import com.db.dorm.DormDao;
import com.db.dormitory.DormitoryDao;
import com.db.user.UserDao;
import com.fulaan.docflow.dto.SimpleSchoolDTO;
import com.fulaan.dorm.dto.DormitoryDTO;
import com.fulaan.dorm.dto.MoveLogDTO;
import com.fulaan.dorm.dto.RoomDTO;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.myschool.controller.GradeView;
import com.fulaan.school.service.SchoolService;
import com.mongodb.BasicDBObject;
import com.pojo.app.IdNameValuePair;
import com.pojo.app.IdNameValuePairDTO;
import com.pojo.dorm.DormEntry;
import com.pojo.dormitory.*;
import com.pojo.school.ClassInfoDTO;
import com.pojo.school.Grade;
import com.pojo.school.SchoolDTO;
import com.pojo.school.Subject;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang_xinxin on 2016/9/12.
 */
@Service
public class DormManageService {

    private DormitoryDao dormDao = new DormitoryDao();

    private UserDao userDao = new UserDao();

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private ClassService classService;

    /**
     * 添加一条宿舍信息
     * @author huanxiaolei@ycode.cn
     * @param e
     * @return
     */
    public ObjectId addDormitoryEntry(DormitoryEntry e){
        return dormDao.addDormitoryEntry(e);
    }

    /**
     *
     * @param dormId
     * @param name
     * @param remark
     */
    public void updateDormitoryEntry(String dormId,String name,String remark) {
        dormDao.updateDormitoryEntry(new ObjectId(dormId),name,remark);
    }

    /**
     *
     * @param schoolId
     * @return
     */
    public List<DormitoryDTO> selDormitoryEntryList(ObjectId schoolId) {
        List<DormitoryDTO> dormitoryDTOs = new ArrayList<DormitoryDTO>();
        List<DormitoryEntry> dormitoryEntryList = dormDao.selDormitoryEntryList(schoolId);
        if (dormitoryEntryList!=null && dormitoryEntryList.size()!=0) {
            for (DormitoryEntry dorm : dormitoryEntryList) {
                dormitoryDTOs.add(new DormitoryDTO(dorm));
            }
        }
        return dormitoryDTOs;
    }

    /**
     *
     * @param id
     */
    public void deleteDormitoryEntryById(ObjectId id){
        DormitoryEntry dormitoryEntry = dormDao.selDormitoryEntry(id);
        List<LoopEntry> loopEntryList = dormDao.selLoopEntryList(dormitoryEntry.getID());
        if (loopEntryList!=null && loopEntryList.size()!=0) {
            for(LoopEntry loop : loopEntryList){
                List<RoomEntry> roomEntryList = dormDao.selRoomEntryList(loop.getID(),2);
                if (roomEntryList!=null && roomEntryList.size()!=0) {
                    for(RoomEntry roomEntry :roomEntryList){
                        dormDao.deleteRoomEntry(roomEntry.getID());
                    }
                }
                dormDao.deleteLoopEntry(loop.getID());
            }
        }
        dormDao.deleteDormitoryEntry(id);
    }

    /**
     * 添加一条宿舍楼层信息
     * @author huanxiaolei@ycode.cn
     * @param e
     * @return
     */
    public ObjectId addLoopEntry(LoopEntry e){
        return dormDao.addLoopEntry(e);
    }

    /**
     *宿舍楼层信息
     * @param id
     */
    public void deleteLoopEntryById(ObjectId id){
        LoopEntry loopEntry = dormDao.selLoopEntry(id);
        List<RoomEntry> roomEntryList = dormDao.selRoomEntryList(id,2);
        if (roomEntryList!=null && roomEntryList.size()!=0) {
            for(RoomEntry roomEntry :roomEntryList){
                dormDao.deleteRoomEntry(roomEntry.getID());
            }
        }
        dormDao.deleteLoopEntry(id);
    }

    /**
     *
     * @param dormId
     * @return
     */
    public List<DormitoryDTO> selLoopEntryList(String dormId) {
        List<DormitoryDTO> dormitoryDTOs = new ArrayList<DormitoryDTO>();
        List<LoopEntry> loopEntryList = dormDao.selLoopEntryList(new ObjectId(dormId));
        if (loopEntryList!=null && loopEntryList.size()!=0) {
            for (LoopEntry loop : loopEntryList) {
                dormitoryDTOs.add(new DormitoryDTO(loop));
            }
        }
        return dormitoryDTOs;
    }

    /**
     * 添加一条宿舍房间信息
     * @author huanxiaolei@ycode.cn
     * @param e
     * @return
     */
    public ObjectId addRoomEntry(RoomEntry e){
        return dormDao.addRoomEntry(e);
    }

    /**
     *宿舍房间信息
     * @param id
     */
    public void deleteRoomEntryById(ObjectId id){
        dormDao.deleteRoomEntry(id);
    }

    /**
     *
     * @param loopId
     * @param name
     * @param remark
     */
    public void updateLoopEntry(String loopId, String name, String remark) {
        dormDao.updateLoopEntry(new ObjectId(loopId),name,remark);
    }

    public List<DormitoryDTO> selRoomEntryList(String loopId,String dormId) {
        List<DormitoryDTO> dormitoryDTOs = new ArrayList<DormitoryDTO>();
        List<ObjectId> loopIds = new ArrayList<ObjectId>();
        if (StringUtils.isEmpty(loopId)) {
            List<LoopEntry> loopEntryList = dormDao.selLoopEntryList(new ObjectId(dormId));
            if (loopEntryList!=null && loopEntryList.size()!=0) {
                for (LoopEntry loopEntry : loopEntryList) {
                    loopIds.add(loopEntry.getID());
                }
            }
        } else  {
            loopIds.add(new ObjectId(loopId));
        }
        List<RoomEntry> roomEntryList = dormDao.selRoomEntryListBySex(loopIds, 2);
        if (roomEntryList!=null && roomEntryList.size()!=0) {
            for (RoomEntry room : roomEntryList) {
                dormitoryDTOs.add(new DormitoryDTO(room));
            }
        }
        return dormitoryDTOs;
    }

    /**
     *
     * @param roomId
     * @param name
     * @param remark
     * @param roomType
     * @param bedNum
     */
    public void updateRoomEntry(String roomId, String name, String remark, int roomType, int bedNum) {
        RoomEntry roomEntry = dormDao.selRoomEntry(new ObjectId(roomId));
        List<RoomUserEntry> idNameValuePairs = roomEntry.getSimpleDTOs();
        List<RoomUserEntry> idNameValuePairlist = new ArrayList<RoomUserEntry>();
        List<BasicDBObject> basicDBObjects = new ArrayList<BasicDBObject>();
        if (roomEntry.getBedNum()!=bedNum) {
            if (roomEntry.getBedNum()>bedNum) {
                for (int i=0;i<bedNum;i++) {
                    for(RoomUserEntry roomUserEntry : idNameValuePairs) {
                        if (i+1==roomUserEntry.getBedNum()) {
                            idNameValuePairlist.add(roomUserEntry);
                        }
                    }
                }
            } else {
                idNameValuePairlist.addAll(idNameValuePairs);
                for (int i=0;i<bedNum-roomEntry.getBedNum();i++) {
                    idNameValuePairlist.add(new RoomUserEntry(roomEntry.getBedNum()+i+1,"",null));
                }
            }
        } else {
            idNameValuePairlist.addAll(idNameValuePairs);
        }
        for (RoomUserEntry idNameValuePair : idNameValuePairlist) {
            basicDBObjects.add(idNameValuePair.getBaseEntry());
        }
        dormDao.updateRoomEntry(new ObjectId(roomId),name,bedNum,roomType,remark,basicDBObjects);
    }

    /**
     *
     * @param dormId
     * @param loopId
     * @param sex
     */
    public void selDormManageListInfo(ObjectId schoolId,String dormId, String loopId, int sex,Map map) {
        Map<ObjectId,LoopEntry> loopEntryMap = new HashMap<ObjectId, LoopEntry>();
        List<LoopEntry> loopEntryList = dormDao.selLoopEntryList(new ObjectId(dormId));
        List<ObjectId> loopIds = new ArrayList<ObjectId>();
//        List<ObjectId> dormIds = new ArrayList<ObjectId>();
        /*if (StringUtils.isEmpty(dormId) && StringUtils.isEmpty(loopId)) {
            List<DormitoryEntry> dormitoryEntryList = dormDao.selDormitoryEntryList(schoolId);
            if (dormitoryEntryList!=null && dormitoryEntryList.size()!=0) {
                for (DormitoryEntry dormitoryEntry : dormitoryEntryList) {
                    List<LoopEntry> loopEntryList = dormDao.selLoopInfoListByDormIds(dormIds);
                    if (loopEntryList!=null && loopEntryList.size()!=0) {
                        for (LoopEntry loopEntry : loopEntryList) {
                            loopIds.add(loopEntry.getID());
                        }
                    }
                }
            }
        } else */if (!StringUtils.isEmpty(loopId)) {
            loopEntryMap.put(new ObjectId(loopId),dormDao.selLoopEntry(new ObjectId(loopId)));
            loopIds.add(new ObjectId(loopId));
        }else/* if (!StringUtils.isEmpty(dormId))*/ {
            if (loopEntryList!=null && loopEntryList.size()!=0) {
                for (LoopEntry loopEntry : loopEntryList) {
                    loopIds.add(loopEntry.getID());
                    loopEntryMap.put(loopEntry.getID(),loopEntry);
                }
            }
        }
        map.put("loopCnt",loopEntryList.size());
        List<RoomEntry> roomEntryList = dormDao.selRoomEntryListBySex(loopIds, sex);
        int cin = 0;
        int cbed = 0;
        int croom = 0;
        List<RoomDTO> roomDTOs = new ArrayList<RoomDTO>();
        if(roomEntryList!=null && roomEntryList.size()!=0) {
            map.put("roomCnt",roomEntryList.size());
            for (RoomEntry roomEntry : roomEntryList) {
                cin += roomEntry.getOccupancyNum();
                cbed += roomEntry.getBedNum()-roomEntry.getOccupancyNum();
                if (roomEntry.getOccupancyNum()==0) {
                    croom++;
                }
                roomDTOs.add(new RoomDTO(roomEntry,loopEntryMap.get(roomEntry.getLoopId())));
            }
        }
        map.put("cin",cin);
        map.put("cbed",cbed);
        map.put("croom",croom);
        map.put("roomList",roomDTOs);
    }

    /**
     * 人员入住
     * @param roomId
     * @param userId
     * @param bedNum
     */
    public void addRoomUserInfo(ObjectId roomId, ObjectId userId, int bedNum) {
        UserEntry userEntry = userDao.getUserEntry(userId, new BasicDBObject("nm", 1));
        RoomEntry roomEntry = dormDao.selRoomEntry(roomId);
        dormDao.addRoomUserInfo(roomId,roomEntry.getOccupancyNum()+1,new RoomUserEntry(bedNum,userEntry.getUserName(),userId));
    }

    /**
     * 获取入住弹框信息
     * @param roomId
     * @param bedNum
     */
    public void selBedOptionInfo(ObjectId schoolId,ObjectId roomId, String bedNum,Map map) {
        RoomEntry roomEntry = dormDao.selRoomEntry(roomId);
        SchoolDTO schoolDTO = schoolService.findSchoolById(schoolId.toString());
        List<Grade> gradeList = schoolDTO.getGradeList();
        List<GradeView> gradeViewList = new ArrayList<GradeView>();
        for (Grade grade : gradeList) {
            gradeViewList.add(new GradeView(grade));
        }
        map.put("gradeList", gradeViewList);
        List<ClassInfoDTO> classInfoDTOList = classService.findClassByGradeId(gradeList.get(0).getGradeId().toString());
        map.put("classList", classInfoDTOList);
        List<UserDetailInfoDTO> userDetailInfoDTOList = new ArrayList<UserDetailInfoDTO>();
        List<UserDetailInfoDTO> userDetailInfoDTOs = selRoomUser(schoolId);
        List<ObjectId> userList = new ArrayList<ObjectId>();
        if (userDetailInfoDTOs!=null && userDetailInfoDTOs.size()!=0) {
            for (UserDetailInfoDTO userdto : userDetailInfoDTOs) {
                userList.add(new ObjectId(userdto.getId()));
            }

        }
        if (classInfoDTOList!=null && classInfoDTOList.size()!=0) {
            List<UserEntry> userEntryList = userDao.getUserEntryListBySex(classInfoDTOList.get(0).getStudentIds(), roomEntry.getRoomType(), new BasicDBObject("nm", 1));
            if (userEntryList!=null && userEntryList.size()!=0) {
                for (UserEntry user:userEntryList) {
                    if (userList!=null && userList.size()!=0) {
                        if (!userList.contains(user.getID())) {
                            userDetailInfoDTOList.add(new UserDetailInfoDTO(user.getID(),user.getUserName()));
                        }
                    } else {
                        userDetailInfoDTOList.add(new UserDetailInfoDTO(user.getID(),user.getUserName()));
                    }

                }
            }
        }
        map.put("userList",userDetailInfoDTOList);
        List<Integer> nums = new ArrayList<Integer>();
        if (roomEntry.getSimpleDTOs()!=null && roomEntry.getSimpleDTOs().size()!=0) {
            for (RoomUserEntry pair : roomEntry.getSimpleDTOs()) {
                if (StringUtils.isEmpty(pair.getUserName())) {
                    nums.add(pair.getBedNum());
                }
            }
        }
        map.put("nums",nums);
        map.put("roomName",roomEntry.getRoomName());
        LoopEntry loopEntry = dormDao.selLoopEntry(roomEntry.getLoopId());
        map.put("loopName",loopEntry.getLoopName());
        DormitoryEntry dormitoryEntry = dormDao.selDormitoryEntry(loopEntry.getDormitoryId());
        map.put("dormName",dormitoryEntry.getDormitoryName());
    }

    /**
     * 选择年级班级
     * @param gradeId
     * @param classId
     * @param map
     */
    public void selGradeClassUserInfo(ObjectId schoolId,String gradeId, String classId,ObjectId roomId,int type, Map map) {
        RoomEntry roomEntry = dormDao.selRoomEntry(roomId);
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        if (type==1) {
            List<ClassInfoDTO> classInfoDTOList = classService.findClassByGradeId(gradeId);
            map.put("classList", classInfoDTOList);
            if (classInfoDTOList!=null && classInfoDTOList.size()!=0) {
                userIds = classInfoDTOList.get(0).getStudentIds();
            }
        } else if (type==2) {
            ClassInfoDTO classInfoDTO = classService.findClassInfoByClassId(classId);
            userIds = classInfoDTO.getStudentIds();
        }
        List<UserDetailInfoDTO> userDetailInfoDTOList = new ArrayList<UserDetailInfoDTO>();
        if (userIds!=null && userIds.size()!=0) {
            List<UserEntry> userEntryList = userDao.getUserEntryListBySex(userIds, roomEntry.getRoomType(), new BasicDBObject("nm", 1));
            List<ObjectId> userList = new ArrayList<ObjectId>();
            List<UserDetailInfoDTO> userDetailInfoDTOs = selRoomUser(schoolId);
            if (userDetailInfoDTOs!=null && userDetailInfoDTOs.size()!=0) {
                for (UserDetailInfoDTO userdto : userDetailInfoDTOs) {
                    userList.add(new ObjectId(userdto.getId()));
                }

            }
            if (userEntryList!=null && userEntryList.size()!=0) {
                for (UserEntry user:userEntryList) {
                    if (userList!=null && userList.size()!=0) {
                        if (!userList.contains(user.getID())) {
                            userDetailInfoDTOList.add(new UserDetailInfoDTO(user.getID(),user.getUserName()));
                        }
                    } else {
                        userDetailInfoDTOList.add(new UserDetailInfoDTO(user.getID(),user.getUserName()));
                    }
                }
            }
        }

        map.put("userList",userDetailInfoDTOList);
    }

    /**
     * 已有宿舍的人员
     * @param schoolId
     * @return
     */
    private List<UserDetailInfoDTO> selRoomUser(ObjectId schoolId) {
        List<UserDetailInfoDTO> userDetailInfoDTOList = new ArrayList<UserDetailInfoDTO>();
        List<ObjectId> dormIds = new ArrayList<ObjectId>();
        List<ObjectId> loopIds = new ArrayList<ObjectId>();
        List<DormitoryEntry> dormitoryEntryList = dormDao.selDormitoryEntryList(schoolId);
        if (dormitoryEntryList!=null && dormitoryEntryList.size()!=0) {
            for (DormitoryEntry dormitoryEntry : dormitoryEntryList) {
                dormIds.add(dormitoryEntry.getID());
            }
            List<LoopEntry> loopEntryList = dormDao.selLoopInfoListByDormIds(dormIds);
            if (loopEntryList!=null && loopEntryList.size()!=0) {
                for (LoopEntry loopEntry : loopEntryList) {
                    loopIds.add(loopEntry.getID());
                }
                List<RoomEntry> roomEntryList = dormDao.selRoomEntryListBySex(loopIds,2);
                if (roomEntryList!=null && roomEntryList.size()!=0) {
                    for (RoomEntry roomEntry : roomEntryList) {
                        for(RoomUserEntry roomUserEntry : roomEntry.getSimpleDTOs()) {
                            if (!StringUtils.isEmpty(roomUserEntry.getUserName())) {
                                userDetailInfoDTOList.add(new UserDetailInfoDTO(roomUserEntry.getUserId(),roomUserEntry.getUserName()));
                            }
                        }
                    }
                }
            }
        }
        return userDetailInfoDTOList;
    }

    /**
     * 迁出人员
     * @param roomId
     * @param bedNum
     * @param cause
     */
    public void moveUserInfo(ObjectId roomId, int bedNum, String cause) {
        RoomEntry roomEntry = dormDao.selRoomEntry(roomId);
        RoomUserEntry roomUserEntry = null;
        for(RoomUserEntry pair : roomEntry.getSimpleDTOs()) {
            if (bedNum==pair.getBedNum()) {
                roomUserEntry = pair;
                dormDao.updateRoomUser(roomId,new RoomUserEntry(bedNum,"",null),roomEntry.getOccupancyNum()-1);
            }
        }
        LoopEntry loopEntry = dormDao.selLoopEntry(roomEntry.getLoopId());
        DormitoryEntry dormitoryEntry = dormDao.selDormitoryEntry(loopEntry.getDormitoryId());
        dormDao.addMoveLogEntry(new MoveLogEntry(roomId,roomUserEntry.getUserName(),roomUserEntry.getBedNum(),roomUserEntry.getUserId(),cause,dormitoryEntry.getDormitoryName(),loopEntry.getLoopName(),roomEntry.getRoomName()));
    }

    /**
     * 调整宿舍
     * @param orgRoomId
     * @param orgBedNum
     * @param roomId
     * @param bedNum
     */
    public void updateUserRoom(String orgRoomId, int orgBedNum, String roomId, int bedNum) {
        RoomEntry orgRoomEntry = dormDao.selRoomEntry(new ObjectId(orgRoomId));
        LoopEntry loopEntry = dormDao.selLoopEntry(orgRoomEntry.getLoopId());
        DormitoryEntry dormitoryEntry = dormDao.selDormitoryEntry(loopEntry.getDormitoryId());
        RoomEntry roomEntry = dormDao.selRoomEntry(new ObjectId(roomId));
        for(RoomUserEntry pair : orgRoomEntry.getSimpleDTOs()) {
            if (orgBedNum==pair.getBedNum()) {
                dormDao.addMoveLogEntry(new MoveLogEntry(new ObjectId(orgRoomId),pair.getUserName(),pair.getBedNum(),pair.getUserId(),"调换宿舍",dormitoryEntry.getDormitoryName(),loopEntry.getLoopName(),roomEntry.getRoomName()));
                dormDao.updateRoomUser(new ObjectId(orgRoomId),new RoomUserEntry(orgBedNum,"",null),orgRoomEntry.getOccupancyNum()-1);
                RoomEntry room = dormDao.selRoomEntry(new ObjectId(roomId));
                dormDao.updateRoomUser(new ObjectId(roomId),new RoomUserEntry(bedNum,pair.getUserName(),pair.getUserId()),room.getOccupancyNum()+1);
            }
        }
    }

    /**
     * 获取迁出列表
     * @param dormId
     * @param loopId
     * @param roomId
     * @param userName
     * @param page
     * @param pageSize
     * @return
     */
    public List<MoveLogDTO> selMoveUserList(ObjectId dormId, String loopId, String roomId, String userName, int page, int pageSize) {

        List<MoveLogDTO> moveLogDTOs = new ArrayList<MoveLogDTO>();
        List<MoveLogEntry> moveLogEntryList = dormDao.selMoveUserList(getRoomList(dormId,loopId,roomId),userName,page < 1 ? 0 : ((page - 1) * pageSize),pageSize);
        if (moveLogEntryList!=null && moveLogEntryList.size()!=0) {
            for (MoveLogEntry moveLogEntry : moveLogEntryList) {
                moveLogDTOs.add(new MoveLogDTO(moveLogEntry));
            }
        }
        return moveLogDTOs;
    }

    /**
     *获取迁出数量
     * @param dormId
     * @param loopId
     * @param roomId
     * @param userName
     * @return
     */
    public int selMoveUserCount(ObjectId dormId, String loopId, String roomId, String userName) {
        return dormDao.selMoveUserCount(getRoomList(dormId,loopId,roomId),userName);
    }

    /**
     * 获取roomIds
     * @param dormId
     * @param loopId
     * @param roomId
     * @return
     */
    private List<ObjectId> getRoomList(ObjectId dormId, String loopId, String roomId) {
        List<ObjectId> roomIds = new ArrayList<ObjectId>();
        List<ObjectId> loopIds = new ArrayList<ObjectId>();
        if (!StringUtils.isEmpty(roomId)) {
            roomIds.add(new ObjectId(roomId));
        } else if (!StringUtils.isEmpty(loopId)) {
            List<RoomEntry> roomEntryList = dormDao.selRoomEntryList(new ObjectId(loopId),2);
            if (roomEntryList!=null && roomEntryList.size()!=0) {
                for (RoomEntry roomEntry : roomEntryList) {
                    roomIds.add(roomEntry.getID());
                }
            }
        } else {
            List<LoopEntry> loopEntryList = dormDao.selLoopEntryList(dormId);
            if (loopEntryList!=null && loopEntryList.size()!=0) {
                for (LoopEntry loopEntry : loopEntryList) {
                    loopIds.add(loopEntry.getID());
                }
                List<RoomEntry> roomEntryList = dormDao.selRoomEntryListBySex(loopIds,2);
                if (roomEntryList!=null && roomEntryList.size()!=0) {
                    for (RoomEntry roomEntry : roomEntryList) {
                        roomIds.add(roomEntry.getID());
                    }
                }
            }
        }
        return roomIds;
    }

    /**
     *住宿名单数量
     * @param dormId
     * @param loopId
     * @param roomId
     * @param userName
     * @return
     */
    public int selRoomUserCount(ObjectId dormId, String loopId, String roomId, String userName) {
        List<RoomEntry> roomEntryList = dormDao.selRoomUserCount(getRoomList(dormId,loopId,roomId),userName);
        int count = 0;
        if (roomEntryList!=null && roomEntryList.size()!=0) {
            for (RoomEntry roomEntry : roomEntryList) {
                for (RoomUserEntry pair : roomEntry.getSimpleDTOs()) {
                    if (!StringUtils.isEmpty(pair.getUserName())) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /**
     *住宿名单列表
     * @param dormId
     * @param loopId
     * @param roomId
     * @param userName
     * @param page
     * @param pageSize
     * @return
     */
    public List<MoveLogDTO> selRoomUserList(ObjectId schoolId,ObjectId dormId, String loopId, String roomId, String userName, int page, int pageSize) {
        Map<ObjectId,DormitoryEntry> dormitoryEntryMap = new HashMap<ObjectId, DormitoryEntry>();
        Map<ObjectId,LoopEntry> loopEntryMap = new HashMap<ObjectId, LoopEntry>();
        List<ObjectId> dormIds = new ArrayList<ObjectId>();
        List<DormitoryEntry> dormitoryEntryList = dormDao.selDormitoryEntryList(schoolId);
        if (dormitoryEntryList!=null && dormitoryEntryList.size()!=0) {
            for (DormitoryEntry dormitoryEntry : dormitoryEntryList) {
                dormitoryEntryMap.put(dormitoryEntry.getID(),dormitoryEntry);
                dormIds.add(dormitoryEntry.getID());
            }
        }
        List<LoopEntry> loopEntryList = dormDao.selLoopInfoListByDormIds(dormIds);
        if (loopEntryList!=null && loopEntryList.size()!=0) {
            for (LoopEntry loopEntry : loopEntryList) {
                loopEntryMap.put(loopEntry.getID(),loopEntry);
            }
        }
        List<RoomEntry> roomEntryList = dormDao.selRoomUserCount(getRoomList(dormId,loopId,roomId),userName);
        List<MoveLogDTO> moveLogDTOs = new ArrayList<MoveLogDTO>();
        if (roomEntryList!=null && roomEntryList.size()!=0) {
            for (RoomEntry roomEntry : roomEntryList) {
                for (RoomUserEntry roomUserEntry : roomEntry.getSimpleDTOs()) {
                    if (!StringUtils.isEmpty(roomUserEntry.getUserName())) {
                        MoveLogDTO moveLogDTO = new MoveLogDTO();
                        moveLogDTO.setUserName(roomUserEntry.getUserName());
                        moveLogDTO.setRoomName(roomEntry.getRoomName());
                        moveLogDTO.setTime(DateTimeUtils.getLongToStrTimeTwo(roomUserEntry.getDateTime()));
                        LoopEntry loopEntry = loopEntryMap.get(roomEntry.getLoopId());
                        moveLogDTO.setLoopName(loopEntry == null ? "" : loopEntry.getLoopName());
                        moveLogDTO.setDormName(dormitoryEntryMap.get(loopEntry.getDormitoryId()) == null ? "" : dormitoryEntryMap.get(loopEntry.getDormitoryId()).getDormitoryName());
                        moveLogDTOs.add(moveLogDTO);
                    }
                }
            }
        }
        return listImitatePage(moveLogDTOs,page,pageSize);
    }


    /**
     * 模拟对list分页查询
     * @param list
     * @param page
     * @param pageSize
     * @return
     */
    public List<MoveLogDTO> listImitatePage(List<MoveLogDTO> list,int page,int pageSize) {
        int totalCount =list.size();
        int pageCount=0;
        int m=totalCount%pageSize;
        if(m>0){
            pageCount=totalCount/pageSize+1;
        } else {
            pageCount=totalCount/pageSize;
        }
        List<MoveLogDTO> subList=new ArrayList<MoveLogDTO>();
        if(list!=null&&list.size()>0) {
            if (m == 0) {
                subList = list.subList((page - 1) * pageSize, pageSize * (page));
            } else {
                if (page == pageCount) {
                    subList = list.subList((page - 1) * pageSize, totalCount);
                } else {
                    subList = list.subList((page - 1) * pageSize, pageSize * (page));
                }
            }
        }
        return subList;
    }

    /**
     * 清空数据
     * @param schoolId
     * @param remark
     */
    public void cleanRoomUserInfo(ObjectId schoolId, String remark) {
        Map<ObjectId,DormitoryEntry> dormitoryEntryMap = new HashMap<ObjectId, DormitoryEntry>();
        Map<ObjectId,LoopEntry> loopEntryMap = new HashMap<ObjectId, LoopEntry>();
        List<ObjectId> dormIds = new ArrayList<ObjectId>();
        List<ObjectId> loopIds = new ArrayList<ObjectId>();
        List<DormitoryEntry> dormitoryEntryList = dormDao.selDormitoryEntryList(schoolId);
        if (dormitoryEntryList!=null && dormitoryEntryList.size()!=0) {
            for (DormitoryEntry dormitoryEntry : dormitoryEntryList) {
                dormitoryEntryMap.put(dormitoryEntry.getID(),dormitoryEntry);
                dormIds.add(dormitoryEntry.getID());
            }
            List<LoopEntry> loopEntryList = dormDao.selLoopInfoListByDormIds(dormIds);
            if (loopEntryList!=null && loopEntryList.size()!=0) {
                for (LoopEntry loopEntry : loopEntryList) {
                    loopEntryMap.put(loopEntry.getID(),loopEntry);
                    loopIds.add(loopEntry.getID());
                }
                List<RoomEntry> roomEntryList = dormDao.selRoomEntryListBySex(loopIds,2);
                if (roomEntryList!=null && roomEntryList.size()!=0) {
                    for (RoomEntry roomEntry : roomEntryList) {
                        for(RoomUserEntry roomUserEntry : roomEntry.getSimpleDTOs()) {
                            if (!StringUtils.isEmpty(roomUserEntry.getUserName())) {
                                dormDao.updateRoomUser(roomEntry.getID(),new RoomUserEntry(roomUserEntry.getBedNum(),"",null),0);
                                LoopEntry loopEntry = loopEntryMap.get(roomEntry.getLoopId());
                                dormDao.addMoveLogEntry(new MoveLogEntry(roomEntry.getID(),roomUserEntry.getUserName(),roomUserEntry.getBedNum(),roomUserEntry.getUserId(),remark,dormitoryEntryMap.get(loopEntry.getDormitoryId()).getDormitoryName(),loopEntry==null?"":loopEntry.getLoopName(),roomEntry.getRoomName()));
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     *获取寝室调整弹框信息
     * @param schoolId
     * @param sex
     * @param dormId
     * @param loopId
     * @param roomId
     * @param type
     * @param map
     */
    public void selRoomOptionInfo(ObjectId schoolId, int sex, String dormId, String loopId, String roomId, int type, Map map) {
        List<DormitoryDTO> dormitoryDTOs = new ArrayList<DormitoryDTO>();
        List<DormitoryDTO> loopDTOs = new ArrayList<DormitoryDTO>();
        List<DormitoryDTO> roomDTOs = new ArrayList<DormitoryDTO>();
        List<Integer> bedNums = new ArrayList<Integer>();
        if (type==0) {
            List<DormitoryEntry> dormitoryEntryList = dormDao.selDormitoryEntryList(schoolId);
            if (dormitoryEntryList!=null && dormitoryEntryList.size()!=0) {
                for (DormitoryEntry dormitoryEntry : dormitoryEntryList) {
                    dormitoryDTOs.add(new DormitoryDTO(dormitoryEntry));
                }
            }
            List<LoopEntry> loopEntryList = dormDao.selLoopEntryList(dormitoryEntryList.get(0).getID());
            if (loopEntryList!=null && loopEntryList.size()!=0) {
                for (LoopEntry loopEntry : loopEntryList) {
                    loopDTOs.add(new DormitoryDTO(loopEntry));
                }
            }
            List<RoomEntry> roomEntryList = dormDao.selRoomEntryList(loopEntryList.get(0).getID(),sex);
            if (roomEntryList!=null && roomEntryList.size()!=0) {
                for (RoomEntry roomEntry : roomEntryList) {
                    roomDTOs.add(new DormitoryDTO(roomEntry));
                }
                for (RoomUserEntry roomUserEntry : roomEntryList.get(0).getSimpleDTOs()) {
                    if (StringUtils.isEmpty(roomUserEntry.getUserName())) {
                        bedNums.add(roomUserEntry.getBedNum());
                    }
                }
            }
        } else if (type==1) {
            List<LoopEntry> loopEntryList = dormDao.selLoopEntryList(new ObjectId(dormId));
            if (loopEntryList!=null && loopEntryList.size()!=0) {
                for (LoopEntry loopEntry : loopEntryList) {
                    loopDTOs.add(new DormitoryDTO(loopEntry));
                }
            }
            List<RoomEntry> roomEntryList = dormDao.selRoomEntryList(loopEntryList.get(0).getID(),sex);
            if (roomEntryList!=null && roomEntryList.size()!=0) {
                for (RoomEntry roomEntry : roomEntryList) {
                    roomDTOs.add(new DormitoryDTO(roomEntry));
                }
                for (RoomUserEntry roomUserEntry : roomEntryList.get(0).getSimpleDTOs()) {
                    if (StringUtils.isEmpty(roomUserEntry.getUserName())) {
                        bedNums.add(roomUserEntry.getBedNum());
                    }
                }
            }
        } else if (type==2) {
            List<RoomEntry> roomEntryList = dormDao.selRoomEntryList(new ObjectId(loopId),sex);
            if (roomEntryList!=null && roomEntryList.size()!=0) {
                for (RoomEntry roomEntry : roomEntryList) {
                    roomDTOs.add(new DormitoryDTO(roomEntry));
                }
                for (RoomUserEntry roomUserEntry : roomEntryList.get(0).getSimpleDTOs()) {
                    if (StringUtils.isEmpty(roomUserEntry.getUserName())) {
                        bedNums.add(roomUserEntry.getBedNum());
                    }
                }
            }
        } else if (type==3) {
            RoomEntry roomEntry = dormDao.selRoomEntry(new ObjectId(roomId));
            for (RoomUserEntry roomUserEntry : roomEntry.getSimpleDTOs()) {
                if (StringUtils.isEmpty(roomUserEntry.getUserName())) {
                    bedNums.add(roomUserEntry.getBedNum());
                }
            }
        }
        map.put("dormList",dormitoryDTOs);
        map.put("loopList",loopDTOs);
        map.put("roomList",roomDTOs);
        map.put("bedNums",bedNums);
    }
}
