package com.fulaan.duty.service;

import com.db.duty.DutyDao;
import com.db.user.UserDao;
import com.fulaan.calendar.service.EventService;
import com.fulaan.duty.dto.*;
import com.fulaan.groupdiscussion.service.EaseMobService;
import com.fulaan.letter.service.LetterService;
import com.fulaan.overtime.dto.OverTimeDTO;
import com.fulaan.overtime.service.OverTimeService;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.LastDayOfMonth;
import com.fulaan.utils.LocalIp;
import com.fulaan.utils.WeekUtil;
import com.fulaan.utils.WeekUtils;
import com.mongodb.BasicDBObject;
import com.pojo.calendar.Event;
import com.pojo.duty.*;
import com.pojo.lesson.LessonWare;
import com.pojo.letter.LetterEntry;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xwpf.usermodel.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wang_xinxin on 2016/6/28.
 */
@Service
public class DutyService {

    private DutyDao dutyDao = new DutyDao();

    private UserDao userDao = new UserDao();

    @Autowired
    private OverTimeService overTimeService;

    @Autowired
    private UserService userService;

    @Autowired
    private EaseMobService easeMobService;

    private EventService eventService = new EventService();

    private LetterService letterService = new LetterService();

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd E");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd");

    /**
     * 获取值班设定
     *
     * @param schoolId
     * @return
     */
    public void selDutySetInfo(ObjectId schoolId, ObjectId userId, Map<String, Object> map, int type, int year, int week) {
        DutySetEntry dutySetEntry = dutyDao.selDutySetInfo(schoolId, year, week);
        ObjectId dutyId = null;
        if (dutySetEntry == null) {
            dutySetEntry = new DutySetEntry(schoolId, userId, new ArrayList<ObjectId>(), 1, 0, "", year, week, "");
            dutyId = dutyDao.addDutySetInfo(dutySetEntry);
        } else {
            dutyId = dutySetEntry.getID();
        }
        if (type == 1) {
            List<UserDetailInfoDTO> userDetailInfoDTOList = new ArrayList<UserDetailInfoDTO>();
            List<ObjectId> userIds = dutySetEntry.getUserIds();
            if (userIds != null && userIds.size() > 0) {
                UserDetailInfoDTO userDetailInfoDTO = new UserDetailInfoDTO();
                List<UserEntry> userEntryList = userDao.getUserEntryList(userIds, new BasicDBObject("nm", 1));
                if (userEntryList != null && userEntryList.size() != 0) {
                    for (UserEntry userEntry : userEntryList) {
                        userDetailInfoDTO = new UserDetailInfoDTO();
                        userDetailInfoDTO.setId(userEntry.getID().toString());
                        userDetailInfoDTO.setUserName(userEntry.getUserName());
                        userDetailInfoDTOList.add(userDetailInfoDTO);
                    }
                }
            }
            map.put("userlist", userDetailInfoDTOList);
        } else if (type == 2) {
            List<DutyTimeDTO> dutyTimeDTOList = new ArrayList<DutyTimeDTO>();
            List<DutyTimeEntry> dutyTimeEntries = dutyDao.selDutyTimeInfo(dutySetEntry.getID());
            if (dutyTimeEntries != null && dutyTimeEntries.size() != 0) {
                for (DutyTimeEntry dutyTimeEntry : dutyTimeEntries) {
                    dutyTimeDTOList.add(new DutyTimeDTO(dutyTimeEntry));
                }
            }
            map.put("dutyTimes", dutyTimeDTOList);
        } else if (type == 3) {
            List<DutyProjectEntry> dutyProject1 = new ArrayList<DutyProjectEntry>();
            List<DutyProjectDTO> dutyProject2 = new ArrayList<DutyProjectDTO>();
            List<DutyProjectEntry> dutyProjectEntryList = dutyDao.selDutyProjectInfo(dutySetEntry.getID(), 2);
            if (dutyProjectEntryList != null && dutyProjectEntryList.size() != 0) {
                for (DutyProjectEntry dutyProjectEntry : dutyProjectEntryList) {
                    if (dutyProjectEntry.getIndex() == 0) {
                        dutyProject1.add(dutyProjectEntry);
                    }
                }
            }
            List<DutyProjectDTO> dutyProjectDTOs = new ArrayList<DutyProjectDTO>();
            if (dutyProject1 != null && dutyProject1.size() != 0) {
                for (DutyProjectEntry dutyProjectEntry : dutyProject1) {
                    DutyProjectDTO dutyProjectDTO = new DutyProjectDTO(dutyProjectEntry);
                    List<DutyProjectEntry> dutyProjectEntries = dutyDao.selDutyProjectCount(dutyProjectEntry.getID().toString(), 1);
                    dutyProjectDTO.setCount(dutyProjectEntries.size());
                    dutyProject2 = new ArrayList<DutyProjectDTO>();
                    if (dutyProjectEntries != null && dutyProjectEntries.size() != 0) {
                        boolean flg = false;
                        for (DutyProjectEntry dutyProject : dutyProjectEntries) {
                            DutyProjectDTO dutyProjectdto = new DutyProjectDTO(dutyProject);
                            dutyProjectdto.setOrgId(dutyProjectEntry.getID().toString());
                            dutyProjectdto.setOrgContent(dutyProjectEntry.getContent());
                            dutyProjectdto.setCount(dutyProjectEntries.size());
                            dutyProjectdto.setType(0);
                            if (!flg) {
                                dutyProjectdto.setType(1);
                            }
                            flg = true;
                            dutyProject2.add(dutyProjectdto);
                        }
                        dutyProjectDTO.setDutyProject(dutyProject2);
                    }

                    dutyProjectDTOs.add(dutyProjectDTO);
                }
            }
            map.put("dutyProjectDTOs", dutyProjectDTOs);
        } else if (type == 4) {
            map.put("type", dutySetEntry.getType());
            map.put("num", dutySetEntry.getNum());
        } else if (type == 5) {
            map.put("ip", dutySetEntry.getIp());
        } else if (type == 6) {
            map.put("explain", dutySetEntry.getExplain());
        }
        map.put("dutyId", dutyId.toString());
    }

    /**
     * @param schoolId
     * @param type
     * @param num
     */
    public void updateDutySetTime(ObjectId schoolId, int type, int num, int year, int week) {
        dutyDao.updateDutySetTime(schoolId, type, num, year, week);
    }

    /**
     * @param schoolId
     * @param userId
     */
    public void addDutyUser(ObjectId schoolId, String userId, int year, int week) {
        dutyDao.addDutyUser(schoolId, new ObjectId(userId), year, week);
    }

    /**
     * @param schoolId
     * @param userId
     */
    public void missDutyUser(ObjectId schoolId, String userId, int year, int week) {
        dutyDao.missDutyUser(schoolId, new ObjectId(userId), year, week);
        List<DutyEntry> dutyEntryList = dutyDao.selDutyInfo(schoolId, week, year);
        if (dutyEntryList != null && dutyEntryList.size() != 0) {
            for (DutyEntry dutyEntry : dutyEntryList) {
                dutyDao.delDutyUserEntryByUserId(dutyEntry.getID(), userId);
            }
        }
    }


    /**
     * @param dutyTimeId
     * @param dutyId
     * @param timeDesc
     * @param startTime
     * @param endTime
     */
    public void addOrUdpDutyTimeInfo(int index, int year, ObjectId schoolId, String dutyTimeId, String dutyId, String timeDesc, String startTime, String endTime) {
        if (StringUtils.isEmpty(dutyTimeId)) {
            ObjectId dtid = dutyDao.addDutyTimeInfo(new DutyTimeEntry(new ObjectId(dutyId), timeDesc, startTime, endTime));
            int count = dutyDao.selDutyCount(schoolId, year, index);
            Calendar cal = Calendar.getInstance();
            List<Date> dateList = WeekUtil.dateToWeek(new Date());
            if (count != 0) {
                List<DutyEntry> dutyEntryList = dutyDao.selDutyInfo(schoolId, index, year);
                if (dutyEntryList != null && dutyEntryList.size() != 0) {
                    DutyEntry dutyEntry = dutyEntryList.get(dutyEntryList.size() - 1);
                    for (int i = 0; i < dateList.size(); i++) {
                        String num = String.valueOf(i + 1) + String.valueOf(Integer.valueOf(dutyEntry.getNum().substring(1)) + 1);
                        dutyDao.addDutyInfo(new DutyEntry(schoolId, dateList.get(i).getTime(), index, num, null, dtid, cal.get(Calendar.YEAR)));
                    }
                }
            }
        } else {
            dutyDao.updateDutyTimeInfo(dutyTimeId, timeDesc, startTime, endTime);
        }
    }

    /**
     * @param dutyTimeId
     */
    public void delDutyTimeInfo(String dutyTimeId, int year, int week, ObjectId schoolId) {
        List<DutyEntry> dutyEntryList = dutyDao.selDutyInfo(schoolId, week, year);
        List<Integer> indexs = new ArrayList<Integer>();
        if (dutyEntryList != null && dutyEntryList.size() != 0) {
            for (DutyEntry dutyEntry : dutyEntryList) {
                indexs.add(Integer.valueOf(dutyEntry.getNum().substring(1)));
            }
            indexs = removeDuplicateInt(indexs);
            List<DutyEntry> dutyEntries = dutyDao.selDutyInfoByTimeId(new ObjectId(dutyTimeId));
            if (dutyEntries != null && dutyEntries.size() != 0) {
                int num = Integer.valueOf(dutyEntries.get(0).getNum().substring(1));
                for (Integer index : indexs) {
                    if (num < index) {
                        for (int i = 1; i < 8; i++) {
                            dutyDao.updateDutyInfoByNum(schoolId, week, year, i + String.valueOf(index), i + String.valueOf(index - 1));
                        }
                    }
                }
            }
        }
        dutyDao.delDutyTimeInfo(dutyTimeId);
        dutyDao.delDutyInfoByDutyTimeId(new ObjectId(dutyTimeId), week, year);
    }

    /**
     * @param list
     * @return
     */
    private List<Integer> removeDuplicateInt(List<Integer> list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
//        Collections.reverse(list);
        return list;
    }

    /**
     * @param dutyProjectId
     * @param content
     */
    public void addOrUdpDutyProject(String dutyId, String orgDutyProjectId, String dutyProjectId, String content, Map map) {
        int index = 0;
        if (!StringUtils.isEmpty(orgDutyProjectId)) {
            index = 1;
        }
        int count = dutyDao.checkDutyProjectName(orgDutyProjectId, index, content, dutyProjectId, dutyId);
        if (count == 0) {
            if (StringUtils.isEmpty(dutyProjectId)) {
                dutyDao.addDutyProject(new DutyProjectEntry(new ObjectId(dutyId), index, orgDutyProjectId, content));
            } else {
                dutyDao.updateDutyProject(dutyProjectId, orgDutyProjectId, content);
            }
        }
        map.put("count", count);
    }

    /**
     * 删除项目  对应删除值班
     *
     * @param dutyProjectId
     */
    public void delDutyProject(String dutyProjectId, String dutyOrgProjectId, ObjectId schoolId) {
        String id = dutyOrgProjectId;
        if (!StringUtils.isEmpty(dutyProjectId)) {
            id = dutyProjectId;
        }
        dutyDao.delDutyProject(id);
        List<DutyEntry> dutyEntryList = dutyDao.selDutyByProjectId(0, 0, id, schoolId);
        if (dutyEntryList != null && dutyEntryList.size() != 0) {
            for (DutyEntry dutyEntry : dutyEntryList) {
                dutyDao.delDutyUserEntry(dutyEntry.getID());
            }
        }
        dutyDao.delDutyInfoByProjectId(id);
    }

    /**
     * 创建设定
     *
     * @param schoolId
     * @param year
     * @param week
     * @return
     */
    public DutySetEntry getDutySetEntry(ObjectId schoolId, int year, int week) {
        DutySetEntry dutySetEntry = dutyDao.selDutySetInfo(schoolId, year, week);
        int year2 = year;
        int week2 = week;
        if (dutySetEntry == null) {
            if (week - 1 == 0) {
                year2 = year - 1;
                week2 = WeekUtils.getMaxWeekNumOfYear(year2) + 1;
            } else {
                week2 = week2 - 1;
            }
            dutySetEntry = dutyDao.selDutySetInfo(schoolId, year2, week2);
            if (dutySetEntry != null) {
                ObjectId dutySetId = dutyDao.addDutySetInfo(new DutySetEntry(dutySetEntry.getSchoolId(), dutySetEntry.getUserId(), dutySetEntry.getUserIds(), dutySetEntry.getType(), dutySetEntry.getNum(), dutySetEntry.getIp(), year, week, dutySetEntry.getExplain()));
                List<DutyTimeEntry> dutyTimeEntryList = dutyDao.selDutyTimeInfo(dutySetEntry.getID());
                if (dutyTimeEntryList != null && dutyTimeEntryList.size() != 0) {
                    for (DutyTimeEntry dutyTime : dutyTimeEntryList) {
                        dutyDao.addDutyTimeInfo(new DutyTimeEntry(dutySetId, dutyTime.getTimeDesc(), dutyTime.getStartTime(), dutyTime.getEndTime()));
                    }
                }
                List<DutyProjectEntry> dutyProjectEntryList = dutyDao.selDutyProjectInfo(dutySetEntry.getID(), 2);
                if (dutyProjectEntryList != null && dutyProjectEntryList.size() != 0) {
                    for (DutyProjectEntry dutyProjectEntry : dutyProjectEntryList) {
                        if (dutyProjectEntry.getIndex() == 0) {
                            ObjectId dpId = dutyDao.addDutyProject(new DutyProjectEntry(dutySetId, dutyProjectEntry.getIndex(), "", dutyProjectEntry.getContent()));
                            List<DutyProjectEntry> dutyProjectEntries = dutyDao.selDutyProjectCount(dutyProjectEntry.getID().toString(), 1);
                            if (dutyProjectEntries != null && dutyProjectEntries.size() != 0) {
                                for (DutyProjectEntry entry : dutyProjectEntries) {
                                    dutyDao.addDutyProject(new DutyProjectEntry(dutySetId, entry.getIndex(), dpId.toString(), entry.getContent()));
                                }
                            }
                        }
                    }
                }
                return getDutySetEntry(schoolId, year, week);
            } else {
                return null;
            }
        } else {
            return dutySetEntry;
        }
    }

    /**
     * @param schoolId
     * @param map
     */
    public void selDutyList(ObjectId schoolId, Map<String, Object> map, int year, int week) {
        List<DutyTimeEntry> dutyTimeEntries = new ArrayList<DutyTimeEntry>();
        Calendar cal = Calendar.getInstance();
        DutySetEntry dutySetEntry = getDutySetEntry(schoolId, year, week);
        if (dutySetEntry == null) {
            if (year <= cal.get(Calendar.YEAR) && getIndex() != week) {
                map.put("flag", 1);
                return;
            }
        } else {
            dutyTimeEntries = dutyDao.selDutyTimeInfo(dutySetEntry.getID());
            if (dutyTimeEntries == null || dutyTimeEntries.size() == 0) {
                if (getIndex() != week) {
                    map.put("flag", 1);
                    return;
                }
            }
        }
        int count = dutyDao.selDutyCount(schoolId, year, week);
        if (week == 0) {
            week = getIndex();
        }
        int index = week;
        Date date = WeekUtil.getFirstDayOfWeek(year, index);
        List<Date> dateList = WeekUtil.dateToWeek(date);
        List<DutyEntry> dutyEntryList = dutyDao.selDutyInfo(schoolId, index, year);
        if (count == 0 || (count != 0 && (dutyEntryList == null || dutyEntryList.size() == 0))) {
            for (int i = 0; i < dateList.size(); i++) {
                for (int j = 0; j < dutyTimeEntries.size(); j++) {
                    String num = String.valueOf(i + 1) + String.valueOf(j + 1);
                    dutyDao.addDutyInfo(new DutyEntry(schoolId, dateList.get(i).getTime(), index, num, null, dutyTimeEntries.get(j).getID(), year));
                }
            }
        }
        dutyEntryList = dutyDao.selDutyInfo(schoolId, index, year);
        List<DutyDTO> dutyDTOList = new ArrayList<DutyDTO>();
        for (DutyEntry dutyEntry : dutyEntryList) {
            DutyDTO dutyDTO = new DutyDTO();
            if (dutyEntry.getProjectId() != null) {
                DutyProjectEntry dutyProjectEntry = dutyDao.selDutyProjectById(dutyEntry.getProjectId());
                if (!StringUtils.isEmpty(dutyProjectEntry.getOrgId())) {
                    DutyProjectEntry dutyProjectEntry2 = dutyDao.selDutyProjectById(new ObjectId(dutyProjectEntry.getOrgId()));
                    dutyDTO.setDutyProject(dutyProjectEntry2.getContent() + "-" + dutyProjectEntry.getContent());
                } else {
                    dutyDTO.setDutyProject(dutyProjectEntry.getContent());
                }
            }
            dutyDTO.setType(0);
            if (dutyEntry.getDate() < System.currentTimeMillis() || DateTimeUtils.getLongToStrTime(dutyEntry.getDate()).equals(DateTimeUtils.getLongToStrTime(System.currentTimeMillis()))) {
                dutyDTO.setType(1);
            }
            dutyDTO.setId(dutyEntry.getID().toString());
            dutyDTO.setIndex(Integer.valueOf(dutyEntry.getNum()));
            dutyDTO.setxIndex(Integer.valueOf(dutyEntry.getNum().substring(0, 1)));
            dutyDTO.setyIndex(Integer.valueOf(dutyEntry.getNum().substring(1, 2)));
            List<DutyUserEntry> dutyUserEntryList = dutyDao.selDutyUserList(dutyEntry.getID());
            String users = "";
            if (dutyUserEntryList != null && dutyUserEntryList.size() != 0) {
                for (DutyUserEntry dutyUserEntry : dutyUserEntryList) {
                    UserEntry user = userDao.getUserEntry(dutyUserEntry.getUserId(), new BasicDBObject("nm", 1));
                    users += user.getUserName() + " ";
                }
            }
            dutyDTO.setUsernames(users);
            dutyDTOList.add(dutyDTO);
        }
        map.put("rows", dutyDTOList);
        List<TimeDTO> timelist = new ArrayList<TimeDTO>();
        List<Integer> dutyTimeCountList = new ArrayList<Integer>();
        TimeDTO timeDTO = new TimeDTO();
        for (int i = 0; i < dateList.size(); i++) {
            timeDTO = new TimeDTO();
            timeDTO.setTime(sdf.format(dateList.get(i)));
            timeDTO.setNum(i + 1);
            timelist.add(timeDTO);
        }
        map.put("times", timelist);
        map.put("curtime", sdf.format(new Date()));
        List<String> dutyTimes = new ArrayList<String>();
        for (int j = 0; j < dutyTimeEntries.size(); j++) {
            dutyTimes.add(dutyTimeEntries.get(j).getTimeDesc());
            dutyTimeCountList.add(j + 1);
        }
        map.put("dutyTimes", dutyTimes);
        map.put("dutyTimeCount", dutyTimeCountList);
        map.put("cnt", 100 / (dutyTimes.size() + 1));
        map.put("flag", 0);
    }

    /**
     * @param li
     * @return
     */
    public List<Long> getNewList(List<Long> li) {
        List<Long> list = new ArrayList<Long>();
        for (int i = 0; i < li.size(); i++) {
            Long str = li.get(i); //获取传入集合对象的每一个元素
            if (!list.contains(str)) { //查看新集合中是否有指定的元素，如果没有则加入
                list.add(str);
            }
        }
        return list; //返回集合
    }

    /**
     * @param schoolId
     * @param modelName
     */
    public void addModel(ObjectId schoolId, String modelName, int year, int week) {
//        int index = getIndex();
        DutySetEntry dutySetEntry = dutyDao.selDutySetInfo(schoolId, year, week);
        ObjectId dutyModelId = dutyDao.addModel(new DutyModelEntry(schoolId, modelName, dutySetEntry.getID()));
        List<DutyEntry> dutyEntryList = dutyDao.selDutyInfo(schoolId, week, year);
        if (dutyEntryList != null && dutyEntryList.size() != 0) {
            for (DutyEntry dutyEntry : dutyEntryList) {
                List<DutyUserEntry> dutyUserEntryList = dutyDao.selDutyUserList(dutyEntry.getID());
                List<ObjectId> userIds = new ArrayList<ObjectId>();
                if (dutyUserEntryList != null && dutyUserEntryList.size() != 0) {
                    for (DutyUserEntry dutyUserEntry : dutyUserEntryList) {
                        userIds.add(dutyUserEntry.getUserId());
                    }
                }
                dutyDao.addDutyModelDetail(new ModelEntry(dutyModelId, dutyEntry.getDate(), dutyEntry.getIndex(), dutyEntry.getNum(), dutyEntry.getProjectId(), dutyEntry.getDutyTimeId(), userIds));
            }
        }
    }

    /**
     * 使用模板
     *
     * @param modelId
     */
    public void useModel(ObjectId schoolId, String modelId, int year, int week) {
        DutySetEntry dutySetEntry = dutyDao.selDutySetInfo(schoolId, year, week);
        DutyModelEntry dutyModelEntry = dutyDao.selDutyModel(new ObjectId(modelId));
        DutySetEntry dutySetEntry2 = dutyDao.selDutySetById(dutyModelEntry.getDutySetId());
        dutyDao.updateDutySetInfo(dutySetEntry.getID(), dutySetEntry2);
        dutyDao.delDutyProjectBySetId(dutySetEntry.getID());
        dutyDao.delDutyTimeBySetId(dutySetEntry.getID());
        Map<ObjectId, ObjectId> dutyTimeMap = new HashMap<ObjectId, ObjectId>();
        Map<ObjectId, ObjectId> dutyProjectMap = new HashMap<ObjectId, ObjectId>();
        List<DutyTimeEntry> dutyTimeEntryList = dutyDao.selDutyTimeInfo(dutySetEntry2.getID());
        if (dutyTimeEntryList != null && dutyTimeEntryList.size() != 0) {
            for (DutyTimeEntry dutyTime : dutyTimeEntryList) {
                ObjectId timeId = dutyDao.addDutyTimeInfo(new DutyTimeEntry(dutySetEntry.getID(), dutyTime.getTimeDesc(), dutyTime.getStartTime(), dutyTime.getEndTime()));
                dutyTimeMap.put(dutyTime.getID(), timeId);
            }
        }
        List<DutyProjectEntry> dutyProjectEntryList = dutyDao.selDutyProjectInfo(dutySetEntry2.getID(), 2);
        if (dutyProjectEntryList != null && dutyProjectEntryList.size() != 0) {
            for (DutyProjectEntry dutyProjectEntry : dutyProjectEntryList) {
                if (dutyProjectEntry.getIndex() == 0) {
                    ObjectId dpId = dutyDao.addDutyProject(new DutyProjectEntry(dutySetEntry.getID(), dutyProjectEntry.getIndex(), "", dutyProjectEntry.getContent()));
                    List<DutyProjectEntry> dutyProjectEntries = dutyDao.selDutyProjectCount(dutyProjectEntry.getID().toString(), 1);
                    if (dutyProjectEntries != null && dutyProjectEntries.size() != 0) {
                        for (DutyProjectEntry entry : dutyProjectEntries) {
                            ObjectId projectId = dutyDao.addDutyProject(new DutyProjectEntry(dutySetEntry.getID(), entry.getIndex(), dpId.toString(), entry.getContent()));
                            dutyProjectMap.put(entry.getID(), projectId);
                        }
                    }
                }
            }
        }
        List<ModelEntry> modelEntryList = dutyDao.selDutyModelDetail(new ObjectId(modelId));
        Calendar cal = Calendar.getInstance();
        List<DutyEntry> dutyEntryList2 = dutyDao.selDutyInfo(schoolId, week, year);
        for (ModelEntry duty1 : modelEntryList) {
            for (DutyEntry duty2 : dutyEntryList2) {
                if (duty1.getNum().equals(duty2.getNum())) {
                    dutyDao.updateDutyInfo(duty2.getID(), dutyTimeMap.get(duty1.getDutyTimeId()), dutyProjectMap.get(duty1.getProjectId()));
                    List<ObjectId> userIds = duty1.getUserIds();
                    dutyDao.delDutyUserEntry(duty2.getID());
                    if (userIds != null && userIds.size() != 0) {
                        for (ObjectId userId : userIds) {
                            dutyDao.addDutyUserEntry(new DutyUserEntry(duty2.getID(), userId, 0d, 0, 0l, 0l, "", "", new ArrayList<LessonWare>()));
                        }
                    }
                }
            }
        }
    }

    /**
     * @param modelId
     * @param modelName
     */
    public void updateDutyModel(String modelId, String modelName) {
        dutyDao.updateDutyModel(new ObjectId(modelId), modelName);
    }

    /**
     * @param modelId
     */
    public void delDutyModel(String modelId) {
        dutyDao.delDutyModel(new ObjectId(modelId));
    }

    public List<DutyModelDTO> selDutyModelList(ObjectId schoolId) {
        List<DutyModelDTO> dutyModelDTOList = new ArrayList<DutyModelDTO>();
        List<DutyModelEntry> dutyModelEntryList = dutyDao.selDutyModelInfo(schoolId);
        if (dutyModelEntryList != null && dutyModelEntryList.size() != 0) {
            for (DutyModelEntry dutyModel : dutyModelEntryList) {
                dutyModelDTOList.add(new DutyModelDTO(dutyModel));
            }
        }
        return dutyModelDTOList;
    }

    /**
     * @param schoolId
     * @return
     */
    public List<DutyProjectDTO> selProjectList(ObjectId schoolId, int type, int year, int week) {
        List<DutyProjectDTO> dutyProjectDTOs = new ArrayList<DutyProjectDTO>();
        DutySetEntry dutySetEntry = dutyDao.selDutySetInfo(schoolId, year, week);
        if (dutySetEntry != null) {
            List<DutyProjectEntry> dutyProjectEntryList = dutyDao.selDutyProjectInfo(dutySetEntry.getID(), type);
            if (dutyProjectEntryList != null && dutyProjectEntryList.size() != 0) {
                for (DutyProjectEntry dutyProjectEntry : dutyProjectEntryList) {
                    dutyProjectDTOs.add(new DutyProjectDTO(dutyProjectEntry));
                }
            }
        }
        return dutyProjectDTOs;

    }

    /**
     * @param schoolId
     * @return
     */
    public List<UserDetailInfoDTO> selDutyUser(ObjectId schoolId) {
        List<UserEntry> userEntryList = userDao.getDutyUser(schoolId, new BasicDBObject("nm", 1));
        List<UserDetailInfoDTO> userDetailInfoDTOList = new ArrayList<UserDetailInfoDTO>();
        UserDetailInfoDTO userDetailInfoDTO = new UserDetailInfoDTO();
        if (userEntryList != null && userEntryList.size() != 0) {
            for (UserEntry userEntry : userEntryList) {
                userDetailInfoDTO = new UserDetailInfoDTO();
                userDetailInfoDTO.setId(userEntry.getID().toString());
                userDetailInfoDTO.setUserName(userEntry.getUserName());
                userDetailInfoDTOList.add(userDetailInfoDTO);
            }
        }
        return userDetailInfoDTOList;
    }

    /**
     * @param map
     * @param num
     */
    public void selSingleDuty(int type, ObjectId schoolId, Map<String, Object> map, String num, int year, int week) {
        DutyEntry dutyEntry = dutyDao.selSingleDuty(schoolId, week, num, year);
        DutyTimeEntry dutyTimeEntry = dutyDao.selDutyTimeById(dutyEntry.getDutyTimeId());
        map.put("time", sdf.format(dutyEntry.getDate()));
        if (dutyTimeEntry != null) {
            map.put("desc", dutyTimeEntry.getTimeDesc());
        }
        if (type == 2) {
            if (dutyEntry.getProjectId() != null) {
                DutyProjectEntry dutyProjectEntry = dutyDao.selDutyProjectById(dutyEntry.getProjectId());
                map.put("project1", dutyProjectEntry.getID().toString());
                if (!StringUtils.isEmpty(dutyProjectEntry.getOrgId())) {
                    DutyProjectEntry dutyProjectEntry2 = dutyDao.selDutyProjectById(new ObjectId(dutyProjectEntry.getOrgId()));
                    map.put("project2", dutyProjectEntry2.getID().toString());
                }
            }
            List<DutyUserEntry> dutyUserEntryList = dutyDao.selDutyUserList(dutyEntry.getID());
            List<UserDetailInfoDTO> userDetailInfoDTOList = new ArrayList<UserDetailInfoDTO>();
            UserDetailInfoDTO userDetailInfoDTO = new UserDetailInfoDTO();
            if (dutyUserEntryList != null && dutyUserEntryList.size() != 0) {
                for (DutyUserEntry dutyUserEntry : dutyUserEntryList) {
                    UserEntry user = userDao.getUserEntry(dutyUserEntry.getUserId(), new BasicDBObject("nm", 1));
                    userDetailInfoDTO = new UserDetailInfoDTO();
                    userDetailInfoDTO.setId(user.getID().toString());
                    userDetailInfoDTO.setUserName(user.getUserName());
                    userDetailInfoDTOList.add(userDetailInfoDTO);
                }
            }
            map.put("users", userDetailInfoDTOList);
        }
    }

    /**
     * @return
     */
    public int getIndex() {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.SUNDAY);
        return c.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * @param id
     * @return
     */
    public List<DutyProjectDTO> selProjectByOrgId(String id) {
        List<DutyProjectEntry> dutyProjectEntryList = dutyDao.selProjectByOrgId(id);
        List<DutyProjectDTO> dutyProjectDTOs = new ArrayList<DutyProjectDTO>();
        if (dutyProjectEntryList != null && dutyProjectEntryList.size() != 0) {
            for (DutyProjectEntry dutyProjectEntry : dutyProjectEntryList) {
                dutyProjectDTOs.add(new DutyProjectDTO(dutyProjectEntry));
            }
        }
        return dutyProjectDTOs;
    }

    /**
     * @param dutyDTO
     */
    public void addOrUpdDutyUserInfo(DutyDTO dutyDTO, ObjectId schoolId, ObjectId userId, int year, int week) {
        DutyEntry dutyEntry = dutyDao.selSingleDuty(schoolId, week, String.valueOf(dutyDTO.getIndex()), year);
        DutyTimeEntry dutyTimeEntry = dutyDao.selDutyTimeById(dutyEntry.getDutyTimeId());
        dutyDao.updateDutyProject(dutyEntry.getID(), dutyDTO.getDutyProject());
        dutyDao.delDutyUserEntry(dutyEntry.getID());
        for (int i = 0; i < dutyDTO.getUsers().length; i++) {
            if (!StringUtils.isEmpty(dutyDTO.getUsers()[i])) {
                String message = DateTimeUtils.getLongToStrTime(dutyEntry.getDate()) + " " + dutyTimeEntry.getStartTime() + "-" + dutyTimeEntry.getEndTime() + "值班";
                dutyDao.addDutyUserEntry(new DutyUserEntry(dutyEntry.getID(), new ObjectId(dutyDTO.getUsers()[i]), 0d, 0, 0l, 0l, "", "", new ArrayList<LessonWare>()));
                Event e = new Event(new ObjectId(dutyDTO.getUsers()[i]), 2, "值班", message,
                        DateTimeUtils.getStrToLongTime(DateTimeUtils.getLongToStrTime(dutyEntry.getDate()) + " " + dutyTimeEntry.getStartTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A),
                        DateTimeUtils.getStrToLongTime(DateTimeUtils.getLongToStrTime(dutyEntry.getDate()) + " " + dutyTimeEntry.getEndTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A));
                eventService.addEvent(e);
                List<ObjectId> receiverIds = new ArrayList<ObjectId>();
                receiverIds.add(new ObjectId(dutyDTO.getUsers()[i]));
                LetterEntry letterEntry = new LetterEntry(userId,
                        message, receiverIds);
                letterService.sendLetter(letterEntry);
            }
        }

    }

    /**
     * @param schoolId
     * @param map
     */
    public void selMyDutyInfo(ObjectId schoolId, Map<String, Object> map, ObjectId userId) {
        Map<ObjectId, DutyEntry> dts = new HashMap<ObjectId, DutyEntry>();
        Calendar c = Calendar.getInstance();
        DutySetEntry dutySetEntry = dutyDao.selDutySetInfo(schoolId, c.get(Calendar.YEAR), getIndex());
        List<DutyEntry> dutyEntryList = dutyDao.selDutyInfo(schoolId, getIndex(), c.get(Calendar.YEAR));
        List<ObjectId> dutyIds = new ArrayList<ObjectId>();
        List<MyDutyDTO> myDutyDTOs = new ArrayList<MyDutyDTO>();
        List<MyDutyDTO> myDutyDTOs2 = new ArrayList<MyDutyDTO>();
        MyDutyDTO myDutyDTO = new MyDutyDTO();
        List<Date> dateList = WeekUtil.dateToWeek(new Date());
        List<DutyEntry> dutyEntryList1 = new ArrayList<DutyEntry>();
        Map<ObjectId, Integer> typeMap = new HashMap<ObjectId, Integer>();
        if (dutyEntryList != null && dutyEntryList.size() != 0) {
            for (DutyEntry dutyEntry : dutyEntryList) {
                dutyIds.add(dutyEntry.getID());
                dts.put(dutyEntry.getID(), dutyEntry);
            }
            List<DutyUserEntry> dutyUserEntryList = dutyDao.selDutyUserByUserId(dutyIds, userId);
            if (dutyUserEntryList != null && dutyUserEntryList.size() != 0) {
                for (DutyUserEntry dutyUserEntry : dutyUserEntryList) {
                    dutyEntryList1.add(dts.get(dutyUserEntry.getDutyId()));
                    typeMap.put(dutyUserEntry.getDutyId(), dutyUserEntry.getType());
                }
            }
            for (int i = 0; i < dateList.size(); i++) {
                myDutyDTO = new MyDutyDTO();
                myDutyDTO.setTimes(sdf.format(dateList.get(i)));
                myDutyDTO.setIsToday(0);
                if (sdf.format(dateList.get(i)).equals(sdf.format(new Date()))) {
                    myDutyDTO.setIsToday(1);
                }
                myDutyDTOs2 = new ArrayList<MyDutyDTO>();
                for (DutyEntry dutyEntry : dutyEntryList1) {
                    if (sdf.format(dutyEntry.getDate()).equals(sdf.format(dateList.get(i)))) {
                        MyDutyDTO myDutyDTO2 = new MyDutyDTO();
                        DutyTimeEntry dutyTimeEntry = dutyDao.selDutyTimeById(dutyEntry.getDutyTimeId());
                        myDutyDTO2.setTimes(sdf.format(dutyEntry.getDate()));
                        if (dutyEntry.getProjectId() != null) {
                            DutyProjectEntry dutyProjectEntry = dutyDao.selDutyProjectById(dutyEntry.getProjectId());
                            if (!StringUtils.isEmpty(dutyProjectEntry.getOrgId())) {
                                DutyProjectEntry dutyProjectEntry2 = dutyDao.selDutyProjectById(new ObjectId(dutyProjectEntry.getOrgId()));
                                myDutyDTO2.setDutyProject(dutyProjectEntry2.getContent() + "-" + dutyProjectEntry.getContent());
                            } else {
                                myDutyDTO2.setDutyProject(dutyProjectEntry.getContent());
                            }
                        }
                        myDutyDTO2.setType(typeMap.get(dutyEntry.getID()));
                        myDutyDTO2.setType3(4);
                        DutyShiftEntry dutyShiftEntry = dutyDao.selDutyShiftDetail(dutyEntry.getID(), userId);
                        if (dutyShiftEntry != null) {
                            myDutyDTO2.setType3(dutyShiftEntry.getType());
                        }

                        myDutyDTO2.setType2(0);
                        if (myDutyDTO2.getType() == 0) {
                            if (DateTimeUtils.getLongToStrTime(dutyEntry.getDate()).equals(DateTimeUtils.getLongToStrTime(System.currentTimeMillis()))) {
                                myDutyDTO2.setType2(1);
                            }
                        }
                        long times = dutyEntry.getDate();
                        if (Integer.valueOf(dutyTimeEntry.getStartTime().split(":")[0]) > Integer.valueOf(dutyTimeEntry.getEndTime().split(":")[0])) {
                            c.setTime(DateTimeUtils.longToDate(times, DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));   //设置日期
                            c.add(Calendar.DATE, 1);
                            times = c.getTime().getTime();
                        }
                        String date = DateTimeUtils.convert(times, "yyyy/MM/dd");
                        String time = "";
                        if (dutySetEntry.getType() == 1) {
                            //值班后
                            time = date + " " + dutyTimeEntry.getEndTime();
                            long dates = DateTimeUtils.getStrToLongTime(time, DateTimeUtils.DATE_YYYY_MM_DD_HH_MM) + dutySetEntry.getNum() * 60 * 1000;
                            time = DateTimeUtils.convert(dates, DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS);
                        } else if (dutySetEntry.getType() == 2) {
                            //当天
                            time = date + " " + "23:59:59";
                        } else if (dutySetEntry.getType() == 3) {
                            //本周
                            Date dateTime = DateTimeUtils.getLastDayOfWeek(DateTimeUtils.longToDate2(dutyEntry.getDate(), DateTimeUtils.DATE_YYYY_MM_DD_N));
                            time = DateTimeUtils.convert(dateTime.getTime(), "yyyy/MM/dd") + " " + "23:59:59";
                        } else if (dutySetEntry.getType() == 4) {
                            //本月
                            Date dateTime = DateTimeUtils.getLastDayOfMonth(DateTimeUtils.longToDate2(dutyEntry.getDate(), DateTimeUtils.DATE_YYYY_MM_DD_N));
                            time = DateTimeUtils.convert(dateTime.getTime(), "yyyy/MM/dd") + " " + "23:59:59";
                        }
                        if (!StringUtils.isEmpty(time)) {
                            if (DateTimeUtils.compare_date(System.currentTimeMillis(), DateTimeUtils.getStrToLongTime(time, DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS)) > 0) {
                                myDutyDTO2.setType(3);
                            }
                        }
                        myDutyDTO2.setTimeDuan(dutyTimeEntry.getStartTime() + "-" + dutyTimeEntry.getEndTime());
                        myDutyDTO2.setTimeDesc(dutyTimeEntry.getTimeDesc());
                        myDutyDTO2.setId(dutyEntry.getID().toString());
                        myDutyDTOs2.add(myDutyDTO2);

                    }
                }
                myDutyDTO.setMyDutyDTOs(myDutyDTOs2);
                myDutyDTO.setCount(myDutyDTOs2.size());
                myDutyDTOs.add(myDutyDTO);
            }
        } else {
            for (int i = 0; i < dateList.size(); i++) {
                myDutyDTO = new MyDutyDTO();
                myDutyDTO.setTimes(sdf.format(dateList.get(i)));
                myDutyDTO.setIsToday(0);
                if (sdf.format(dateList.get(i)).equals(sdf.format(new Date()))) {
                    myDutyDTO.setIsToday(1);
                }
                myDutyDTOs.add(myDutyDTO);
            }
        }

        map.put("myDutys", myDutyDTOs);


    }

    /**
     * @param year
     * @param month
     * @param map
     */
    public void selMyDutyHistory(int year, int month, int type, int curYear, int week, Map<String, Object> map, ObjectId schoolId, ObjectId userId) {
        String startTime = "";
        String endTime = "";
        if (type == 2) {
            startTime = DateTimeUtils.dateToStrLong(WeekUtils.getFirstDayOfWeek(year, week - 1), DateTimeUtils.DATE_YYYY_MM_DD_N) + " " + "00:00";
            endTime = DateTimeUtils.dateToStrLong(WeekUtils.getLastDayOfWeek(year, week - 1), DateTimeUtils.DATE_YYYY_MM_DD_N) + " " + "23:59";
        } else {
            startTime = String.valueOf(year) + "/" + ((String.valueOf(month).length() != 2) ? ("0" + month) : String.valueOf(month)) + "/" + "01" + " " + "00:00";
            endTime = LastDayOfMonth.getLastDayOfMonth(year, month) + " " + "23:59";
        }
        long stime = DateTimeUtils.getStrToLongTime(startTime, DateTimeUtils.DATE_YYYY_MM_DD_HH_MM);
        long etime = DateTimeUtils.getStrToLongTime(endTime, DateTimeUtils.DATE_YYYY_MM_DD_HH_MM);
        List<ObjectId> dutyIds = new ArrayList<ObjectId>();
        List<ObjectId> users = new ArrayList<ObjectId>();
        List<DutyEntry> dutyEntryList = dutyDao.selDutyByProjectId(stime, etime, null, schoolId);
        Map<ObjectId, DutyEntry> dutyEntryMap = new HashMap<ObjectId, DutyEntry>();
        if (dutyEntryList != null && dutyEntryList.size() != 0) {
            for (DutyEntry dutyEntry : dutyEntryList) {
                dutyIds.add(dutyEntry.getID());
                dutyEntryMap.put(dutyEntry.getID(), dutyEntry);
            }
        }
        users.add(userId);
        List<DutyUserDTO> dutyUserDTOs = new ArrayList<DutyUserDTO>();
        if (dutyIds != null && dutyIds.size() != 0) {
            List<DutyUserEntry> dutyUserEntryList = dutyDao.selDutyUserByUserIds(dutyIds, users);
            if (dutyUserEntryList != null && dutyUserEntryList.size() != 0) {
                for (DutyUserEntry dutyUserEntry : dutyUserEntryList) {
                    DutyUserDTO userDTO = new DutyUserDTO();
                    userDTO.setId(dutyUserEntry.getID().toString());
                    userDTO.setDate(DateTimeUtils.convert(dutyEntryMap.get(dutyUserEntry.getDutyId()).getDate(), "yyyy/MM/dd"));
                    userDTO.setProjectName(getDutyProject(schoolId).get(dutyEntryMap.get(dutyUserEntry.getDutyId()).getProjectId()).getContent());
                    userDTO.setTimeDesc(getDutyTime(schoolId).get(dutyEntryMap.get(dutyUserEntry.getDutyId()).getDutyTimeId()).getTimeDesc());
                    String st = getDutyTime(schoolId).get(dutyEntryMap.get(dutyUserEntry.getDutyId()).getDutyTimeId()).getStartTime();
                    String et = getDutyTime(schoolId).get(dutyEntryMap.get(dutyUserEntry.getDutyId()).getDutyTimeId()).getEndTime();
                    userDTO.setContent(dutyUserEntry.getContent());
                    userDTO.setTimeDuan(st + "-" + et);
                    dutyUserDTOs.add(userDTO);
                }
            }
        }
        map.put("rows", dutyUserDTOs);
    }

    /**
     * @param dutyId
     * @param userId
     */
    public String selDutyShiftDetail(String dutyId, ObjectId userId) {
        DutyShiftEntry dutyShiftEntry = dutyDao.selDutyShiftDetail(new ObjectId(dutyId), userId);
        String cause = "";
        if (dutyShiftEntry != null) {
            cause = dutyShiftEntry.getCause();
        }
        return cause;
    }

    /**
     * @param dutyId
     * @param cause
     * @param userId
     * @param schoolId
     */
    public void addDutyShiftInfo(String dutyId, String cause, ObjectId userId, String timeDesc, ObjectId schoolId) {
        DutyShiftEntry dutyShiftEntry = dutyDao.selDutyShiftDetail(new ObjectId(dutyId), userId);
        if (dutyShiftEntry == null) {
            dutyDao.addDutyShiftInfo(new DutyShiftEntry(new ObjectId(dutyId), schoolId, userId, timeDesc, cause, 0));
        } else {
            dutyDao.updateDutyShiftInfo(dutyShiftEntry.getID(), cause);
        }

    }

    /**
     * 签到
     *
     * @param dutyId
     * @param userId
     * @param type
     * @param map
     */
    public void checkInOut(String dutyId, ObjectId userId, int type, int year, int week, Map<String, Object> map, String ip) throws SocketException {

        DutyEntry dutyEntry = dutyDao.selDutyInfoById(new ObjectId(dutyId));
        DutySetEntry dutySetEntry = dutyDao.selDutySetInfo(dutyEntry.getSchoolId(), year, week);
        if (!StringUtils.isEmpty(dutySetEntry.getIp()) && !ip.contains(dutySetEntry.getIp())) {
            map.put("flag", false);
            map.put("mesg", "IP不在学校范围内，不能签到签退！");
            return;
        }
        DutyTimeEntry dutyTimeEntry = dutyDao.selDutyTimeById(dutyEntry.getDutyTimeId());
        if (type == 1) {
            if (!sdf2.format(dutyEntry.getDate()).equals(sdf2.format(new Date()))) {
                map.put("flag", false);
                map.put("mesg", "还没有到签到时间！");
                return;
            }
            Calendar c = Calendar.getInstance();
            long times = dutyEntry.getDate();
            if (Integer.valueOf(dutyTimeEntry.getStartTime().split(":")[0]) > Integer.valueOf(dutyTimeEntry.getEndTime().split(":")[0])) {
                c.setTime(DateTimeUtils.longToDate(times, DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));   //设置日期
                c.add(Calendar.DATE, 1);
                times = c.getTime().getTime();
            }
            String date = DateTimeUtils.convert(times, "yyyy/MM/dd");
            String date2 = date + " " + dutyTimeEntry.getEndTime();
            String date3 = DateTimeUtils.convert(dutyEntry.getDate(), "yyyy/MM/dd") + " " + dutyTimeEntry.getStartTime();
            if (DateTimeUtils.compare_date(date3, new Date()) == 1) {
                map.put("flag", false);
                map.put("mesg", "还没有到签到时间！");
                return;
            }
            if (DateTimeUtils.compare_date(date2, new Date()) == -1) {
                map.put("flag", false);
                map.put("mesg", "已超过了上班时间，不能签到！");
                return;
            }
        }
        dutyDao.updateDutyUser(type, dutyId, userId, LocalIp.getRealIp());
        map.put("flag", true);
    }

    /**
     * @param startDate
     * @param endDate
     * @param name
     * @param schoolId
     */
    public List<DutyShiftDTO> selShiftCheckList(String startDate, String endDate, String name, ObjectId schoolId) {
        long stime = 0;
        long etime = 0;
        if (!StringUtils.isEmpty(startDate)) {
            String sdate = startDate + " " + "00:00";
            stime = DateTimeUtils.getStrToLongTime(sdate, DateTimeUtils.DATE_YYYY_MM_DD_HH_MM);
        }
        if (!StringUtils.isEmpty(endDate)) {
            String edate = endDate + " " + "23:59";
            etime = DateTimeUtils.getStrToLongTime(edate, DateTimeUtils.DATE_YYYY_MM_DD_HH_MM);
        }

        List<DutyEntry> dutyEntryList = dutyDao.selDutyByDate(stime, etime, "", schoolId);
        List<ObjectId> dutyIds = new ArrayList<ObjectId>();
        List<ObjectId> dutyTimeIds = new ArrayList<ObjectId>();
        List<ObjectId> users = new ArrayList<ObjectId>();
        Map<ObjectId, DutyEntry> dutyEntryMap = new HashMap<ObjectId, DutyEntry>();
        if (dutyEntryList != null && dutyEntryList.size() != 0) {
            for (DutyEntry dutyEntry : dutyEntryList) {
                dutyIds.add(dutyEntry.getID());
                dutyEntryMap.put(dutyEntry.getID(), dutyEntry);
                dutyTimeIds.add(dutyEntry.getDutyTimeId());
            }
        }
        Map<ObjectId, UserEntry> userEntryMap = new HashMap<ObjectId, UserEntry>();
        List<UserEntry> userEntryList = userDao.searchUsersWithSchool(name, schoolId, new BasicDBObject("nm", 1));
        if (userEntryList != null && userEntryList.size() != 0) {
            for (UserEntry userEntry : userEntryList) {
                users.add(userEntry.getID());
                userEntryMap.put(userEntry.getID(), userEntry);
            }
        }
        List<DutyShiftDTO> dutyShiftDTOs = new ArrayList<DutyShiftDTO>();
        List<DutyShiftEntry> dutyShiftEntryList = dutyDao.selDutyShiftByUserIds(dutyIds, users);
        if (dutyShiftEntryList != null && dutyShiftEntryList.size() != 0) {
            for (DutyShiftEntry dutyShiftEntry : dutyShiftEntryList) {
                DutyShiftDTO dutyShiftDTO = new DutyShiftDTO();
                dutyShiftDTO.setCause(dutyShiftEntry.getCause());
                dutyShiftDTO.setUserId(dutyShiftEntry.getUserId().toString());
                dutyShiftDTO.setId(dutyShiftEntry.getID().toString());
                dutyShiftDTO.setType(dutyShiftEntry.getType());
                dutyShiftDTO.setUserName(userEntryMap.get(dutyShiftEntry.getUserId()).getUserName());
                dutyShiftDTO.setDate(DateTimeUtils.convert(dutyEntryMap.get(dutyShiftEntry.getDutyId()).getDate(), "yyyy/MM/dd"));
                dutyShiftDTO.setTimeDesc(dutyShiftEntry.getTimeDesc());
                dutyShiftDTO.setProjectName(getDutyProject(schoolId).get(dutyEntryMap.get(dutyShiftEntry.getDutyId()).getProjectId()).getContent());
                String st = getDutyTime(schoolId).get(dutyEntryMap.get(dutyShiftEntry.getDutyId()).getDutyTimeId()).getStartTime();
                String et = getDutyTime(schoolId).get(dutyEntryMap.get(dutyShiftEntry.getDutyId()).getDutyTimeId()).getEndTime();
                dutyShiftDTO.setTimeDuan(st + "-" + et);
                dutyShiftDTOs.add(dutyShiftDTO);
            }
        }
        return dutyShiftDTOs;

    }

    /**
     * @param schoolId
     * @return
     */
    public Map<ObjectId, DutyTimeEntry> getDutyTime(ObjectId schoolId) {
        Map<ObjectId, DutyTimeEntry> dutyTimeEntryMap = new HashMap<ObjectId, DutyTimeEntry>();
        List<DutySetEntry> dutySetEntryList = dutyDao.selDutySetInfo(schoolId);
        if (dutySetEntryList != null && dutySetEntryList.size() != 0) {
            for (DutySetEntry dutySetEntry : dutySetEntryList) {
                List<DutyTimeEntry> dutyTimeEntries = dutyDao.selDutyTimeLogInfo(dutySetEntry.getID());
                if (dutyTimeEntries != null && dutyTimeEntries.size() != 0) {
                    for (DutyTimeEntry dutyTimeEntry : dutyTimeEntries) {
                        dutyTimeEntryMap.put(dutyTimeEntry.getID(), dutyTimeEntry);
                    }
                }
            }
        }
        return dutyTimeEntryMap;
    }

    public Map<ObjectId, DutyProjectEntry> getDutyProject(ObjectId schoolId) {
        Map<ObjectId, DutyProjectEntry> dutyProjectEntryMap = new HashMap<ObjectId, DutyProjectEntry>();
        List<DutySetEntry> dutySetEntryList = dutyDao.selDutySetInfo(schoolId);
        if (dutySetEntryList != null && dutySetEntryList.size() != 0) {
            for (DutySetEntry dutySetEntry : dutySetEntryList) {
                List<DutyProjectEntry> dutyProjectEntryList = dutyDao.selDutyProjectLogInfo(dutySetEntry.getID(), 2);
                if (dutyProjectEntryList != null && dutyProjectEntryList.size() != 0) {
                    for (DutyProjectEntry dutyProjectEntry : dutyProjectEntryList) {
                        dutyProjectEntryMap.put(dutyProjectEntry.getID(), dutyProjectEntry);
                    }
                }
            }
        }
        return dutyProjectEntryMap;
    }

    /**
     * @param dutyShiftId
     * @param type
     */
    public void isTongGuo(String dutyShiftId, int type) {
        dutyDao.isTongGuo(dutyShiftId, type);
    }

    /**
     * 值班记录
     *
     * @param startTime
     * @param endTime
     * @param name
     * @param schoolId
     */
    public List<DutyUserDTO> selDutyUserSarlaryList(String startTime, String endTime, String name, ObjectId schoolId, int page, int pageSize) {
        long stime = 0;
        long etime = 0;
        if (!StringUtils.isEmpty(startTime)) {
            String sdate = startTime + " " + "00:00";
            stime = DateTimeUtils.getStrToLongTime(sdate, DateTimeUtils.DATE_YYYY_MM_DD_HH_MM);
        }
        if (!StringUtils.isEmpty(endTime)) {
            String edate = endTime + " " + "23:59";
            etime = DateTimeUtils.getStrToLongTime(edate, DateTimeUtils.DATE_YYYY_MM_DD_HH_MM);
        }
        List<ObjectId> dutyIds = new ArrayList<ObjectId>();
        List<ObjectId> dutyProjectIds = new ArrayList<ObjectId>();
        List<ObjectId> users = new ArrayList<ObjectId>();
        List<DutyUserDTO> dutyUserDTOs = new ArrayList<DutyUserDTO>();
        List<DutyEntry> dutyEntryList = dutyDao.selDutyByProjectId(stime, etime, "", schoolId);
        Map<ObjectId, DutyEntry> dutyEntryMap = new HashMap<ObjectId, DutyEntry>();
        if (dutyEntryList != null && dutyEntryList.size() != 0) {
            for (DutyEntry dutyEntry : dutyEntryList) {
                dutyIds.add(dutyEntry.getID());
                dutyEntryMap.put(dutyEntry.getID(), dutyEntry);
                dutyProjectIds.add(dutyEntry.getProjectId());
            }
            Map<ObjectId, UserEntry> userEntryMap = new HashMap<ObjectId, UserEntry>();
            List<UserEntry> userEntryList = userDao.searchUsersWithSchool(name, schoolId, new BasicDBObject("nm", 1));
            if (userEntryList != null && userEntryList.size() != 0) {
                for (UserEntry userEntry : userEntryList) {
                    users.add(userEntry.getID());
                    userEntryMap.put(userEntry.getID(), userEntry);
                }
            }
            List<DutyUserEntry> dutyUserEntryList = null;
            if (page == 0 && pageSize == 0) {
                dutyUserEntryList = dutyDao.selDutyUserByUserIds(dutyIds, !StringUtils.isEmpty(name) ? users : null);
            } else {
                dutyUserEntryList = dutyDao.selDutyUserByUserIds(dutyIds, !StringUtils.isEmpty(name) ? users : null, page < 1 ? 0 : ((page - 1) * pageSize), pageSize);
            }

            if (dutyUserEntryList != null && dutyUserEntryList.size() != 0) {
                for (DutyUserEntry dutyUserEntry : dutyUserEntryList) {
                    DutyUserDTO userDTO = new DutyUserDTO();
                    userDTO.setFileUploadDTOList(dutyUserEntry.getLessonWareList());
                    userDTO.setFileCnt(dutyUserEntry.getLessonWareList().size());
                    userDTO.setId(dutyUserEntry.getID().toString());
                    userDTO.setSalary(dutyUserEntry.getPay());
                    userDTO.setUserId(dutyUserEntry.getUserId().toString());
                    userDTO.setUserName(userEntryMap.get(dutyUserEntry.getUserId()) == null ? "" : userEntryMap.get(dutyUserEntry.getUserId()).getUserName());
                    userDTO.setDate(DateTimeUtils.convert(dutyEntryMap.get(dutyUserEntry.getDutyId()).getDate(), "yyyy/MM/dd"));
                    userDTO.setProjectName(getDutyProject(schoolId).get(dutyEntryMap.get(dutyUserEntry.getDutyId()).getProjectId()).getContent());
                    userDTO.setTimeDesc(getDutyTime(schoolId).get(dutyEntryMap.get(dutyUserEntry.getDutyId()).getDutyTimeId()).getTimeDesc());
                    if (dutyUserEntry.getInTime() != 0) {
                        userDTO.setCheckIn(DateTimeUtils.convert(dutyUserEntry.getInTime(), DateTimeUtils.DATE_HH_MM));
                    }
                    if (dutyUserEntry.getOutTime() != 0) {
                        userDTO.setCheckOut(DateTimeUtils.convert(dutyUserEntry.getOutTime(), DateTimeUtils.DATE_MM_DD_HH_MM));
                    }
                    String st = getDutyTime(schoolId).get(dutyEntryMap.get(dutyUserEntry.getDutyId()).getDutyTimeId()).getStartTime();
                    String et = getDutyTime(schoolId).get(dutyEntryMap.get(dutyUserEntry.getDutyId()).getDutyTimeId()).getEndTime();
                    userDTO.setTimeDuan(st + "-" + et);
                    int se = Integer.valueOf(et.split(":")[0]) - Integer.valueOf(st.split(":")[0]);
                    String time = "";
                    DecimalFormat df = new DecimalFormat("#.00");
                    if (Integer.valueOf(st.split(":")[1]) > Integer.valueOf(et.split(":")[1])) {
                        double a = (double) (60 + Integer.valueOf(et.split(":")[1]) - Integer.valueOf(st.split(":")[1]));
                        double b = 0;
                        if (a != 0) {
                            b = a / 60;
                        }
                        time = String.valueOf(se - 1 + df.format(b));
                    } else {
                        double a = (double) (Integer.valueOf(et.split(":")[1]) - Integer.valueOf(st.split(":")[1]));
                        double b = 0;
                        if (a != 0) {
                            b = a / 60;
                        }
                        time = String.valueOf(se + df.format(b));
                    }
                    userDTO.setTimes(time);
                    userDTO.setContent(dutyUserEntry.getContent());
                    dutyUserDTOs.add(userDTO);
                }
            }
        }
        return dutyUserDTOs;
    }

    /**
     * 更新工资
     *
     * @param dutyUserId
     * @param sarlay
     */
    public void updateSarlary(String dutyUserId, double sarlay) {
        dutyDao.updateSarlary(new ObjectId(dutyUserId), sarlay);
    }

//    /**
//     * 查询值班时段
//     * @param schoolId
//     * @return
//     */
//    public List<DutyTimeDTO> selDutyTimeList(ObjectId schoolId) {
//        DutySetEntry dutySetEntry = dutyDao.selDutySetInfo(schoolId);
//        List<DutyTimeDTO> dutyTimeDTOList = new ArrayList<DutyTimeDTO>();
//        if (dutySetEntry!=null) {
//            List<DutyTimeEntry> dutyTimeEntries = dutyDao.selDutyTimeInfo(dutySetEntry.getID());
//            if(dutyTimeEntries!=null && dutyTimeEntries.size()!=0) {
//                for (DutyTimeEntry dutyTimeEntry : dutyTimeEntries) {
//                    dutyTimeDTOList.add(new DutyTimeDTO(dutyTimeEntry));
//                }
//            }
//        }
//        return dutyTimeDTOList;
//    }

    /**
     * 查询我的薪水
     *
     * @param year
     * @param month
     * @param userId
     * @param schoolId
     * @return
     */
    public List<DutyUserDTO> selMySarlaryList(int year, int month, ObjectId userId, ObjectId schoolId) {
        String startTime = String.valueOf(year) + "/" + ((String.valueOf(month).length() != 2) ? ("0" + month) : String.valueOf(month)) + "/" + "01" + " " + "00:00";
        String endTime = LastDayOfMonth.getLastDayOfMonth(year, month) + " " + "23:59";
        long stime = DateTimeUtils.getStrToLongTime(startTime, DateTimeUtils.DATE_YYYY_MM_DD_HH_MM);
        long etime = DateTimeUtils.getStrToLongTime(endTime, DateTimeUtils.DATE_YYYY_MM_DD_HH_MM);
        List<ObjectId> dutyIds = new ArrayList<ObjectId>();
        List<ObjectId> dutyProjectIds = new ArrayList<ObjectId>();
        List<ObjectId> users = new ArrayList<ObjectId>();
        List<DutyEntry> dutyEntryList = dutyDao.selDutyByProjectId(stime, etime, null, schoolId);
        Map<ObjectId, DutyEntry> dutyEntryMap = new HashMap<ObjectId, DutyEntry>();
        if (dutyEntryList != null && dutyEntryList.size() != 0) {
            for (DutyEntry dutyEntry : dutyEntryList) {
                dutyIds.add(dutyEntry.getID());
                dutyEntryMap.put(dutyEntry.getID(), dutyEntry);
                dutyProjectIds.add(dutyEntry.getProjectId());
            }
        }
        users.add(userId);
        List<DutyUserDTO> dutyUserDTOs = new ArrayList<DutyUserDTO>();
        if (dutyIds != null && dutyIds.size() != 0) {
            List<DutyUserEntry> dutyUserEntryList = dutyDao.selDutyUserByUserIds(dutyIds, users);
            if (dutyUserEntryList != null && dutyUserEntryList.size() != 0) {
                for (DutyUserEntry dutyUserEntry : dutyUserEntryList) {
                    DutyUserDTO userDTO = new DutyUserDTO();
                    userDTO.setId(dutyUserEntry.getID().toString());
                    userDTO.setSalary(dutyUserEntry.getPay());
                    userDTO.setDate(DateTimeUtils.convert(dutyEntryMap.get(dutyUserEntry.getDutyId()).getDate(), "yyyy/MM/dd"));
                    userDTO.setProjectName(getDutyProject(schoolId).get(dutyEntryMap.get(dutyUserEntry.getDutyId()).getProjectId()).getContent());
                    userDTO.setTimeDesc(getDutyTime(schoolId).get(dutyEntryMap.get(dutyUserEntry.getDutyId()).getDutyTimeId()).getTimeDesc());
                    if (dutyUserEntry.getInTime() != 0) {
                        userDTO.setCheckIn(DateTimeUtils.convert(dutyUserEntry.getInTime(), DateTimeUtils.DATE_HH_MM));
                    }
                    if (dutyUserEntry.getOutTime() != 0) {
                        userDTO.setCheckOut(DateTimeUtils.convert(dutyUserEntry.getOutTime(), DateTimeUtils.DATE_MM_DD_HH_MM));
                    }
                    String st = getDutyTime(schoolId).get(dutyEntryMap.get(dutyUserEntry.getDutyId()).getDutyTimeId()).getStartTime();
                    String et = getDutyTime(schoolId).get(dutyEntryMap.get(dutyUserEntry.getDutyId()).getDutyTimeId()).getEndTime();
                    userDTO.setTimeDuan(st + "-" + et);
                    int se = Integer.valueOf(et.split(":")[0]) - Integer.valueOf(st.split(":")[0]);
                    String time = "";
                    DecimalFormat df = new DecimalFormat("#.00");
                    if (Integer.valueOf(st.split(":")[1]) > Integer.valueOf(et.split(":")[1])) {
                        double a = (double) (60 + Integer.valueOf(et.split(":")[1]) - Integer.valueOf(st.split(":")[1]));
                        double b = 0;
                        if (a != 0) {
                            b = a / 60;
                        }
                        time = String.valueOf(se - 1 + df.format(b));
                    } else {
                        double a = Integer.valueOf(et.split(":")[1]) - Integer.valueOf(st.split(":")[1]);
                        double b = 0;
                        if (a != 0) {
                            b = a / 60;
                        }
                        time = String.valueOf(se + df.format(b));
                    }
                    userDTO.setTimes(time);
                    dutyUserDTOs.add(userDTO);
                }
            }
        }
        return dutyUserDTOs;
    }

    /**
     * 值班薪酬表
     *
     * @param startTime
     * @param endTime
     * @param name
     * @param schoolId
     * @param response
     */
    public void exportDutyUserSarlaryList(String startTime, String endTime, String name, ObjectId schoolId, HttpServletResponse response) {
        List<DutyUserDTO> dutyUserDTOs = selDutyUserSarlaryList(startTime, endTime, name, schoolId, 0, 0);
        HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("值班薪酬表");
        //为sheet1生成第一行，用于放表头信息
        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("值班日期");
        cell = row.createCell(1);
        cell.setCellValue("值班人员");
        cell = row.createCell(2);
        cell.setCellValue("值班时间");
        cell = row.createCell(3);
        cell.setCellValue("值班项目");
        cell = row.createCell(4);
        cell.setCellValue("值班时长(h)");
        cell = row.createCell(5);
        cell.setCellValue("值班薪酬(元)");
        int page = 0;
        if (dutyUserDTOs != null && dutyUserDTOs.size() != 0) {
            for (DutyUserDTO dutyUserDTO : dutyUserDTOs) {
                page++;
                row = sheet.createRow(page);
                cell = row.createCell(0);
                cell.setCellValue(dutyUserDTO.getDate());
                cell = row.createCell(1);
                cell.setCellValue(dutyUserDTO.getUserName());
                cell = row.createCell(2);
                cell.setCellValue(dutyUserDTO.getTimeDuan());
                cell = row.createCell(3);
                cell.setCellValue(dutyUserDTO.getProjectName());
                cell = row.createCell(4);
                cell.setCellValue(dutyUserDTO.getTimes());
                cell = row.createCell(5);
                cell.setCellValue(dutyUserDTO.getSalary());
            }
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("值班薪酬表.xls", "UTF-8"));
            response.setContentLength(content.length);
            outputStream.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                    outputStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 换班审核表
     *
     * @param startDate
     * @param endDate
     * @param name
     * @param schoolId
     * @param response
     */
    public void exportShiftCheckList(String startDate, String endDate, String name, ObjectId schoolId, HttpServletResponse response) {
        List<DutyShiftDTO> dutyShiftDTOs = selShiftCheckList(startDate, endDate, name, schoolId);
        HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("换班审核表");
        //为sheet1生成第一行，用于放表头信息
        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("申请人");
        cell = row.createCell(1);
        cell.setCellValue("日期");
        cell = row.createCell(2);
        cell.setCellValue("时段");
        cell = row.createCell(3);
        cell.setCellValue("理由");
        cell = row.createCell(4);
        cell.setCellValue("审核状态");
        int page = 0;
        if (dutyShiftDTOs != null && dutyShiftDTOs.size() != 0) {
            for (DutyShiftDTO dutyShiftDTO : dutyShiftDTOs) {
                page++;
                row = sheet.createRow(page);
                cell = row.createCell(0);
                cell.setCellValue(dutyShiftDTO.getUserName());
                cell = row.createCell(1);
                cell.setCellValue(dutyShiftDTO.getDate());
                cell = row.createCell(2);
                cell.setCellValue(dutyShiftDTO.getTimeDesc());
                cell = row.createCell(3);
                cell.setCellValue(dutyShiftDTO.getCause());
                cell = row.createCell(4);
                String str = "";
                if (dutyShiftDTO.getType() == 0) {
                    str = "未审核";
                } else if (dutyShiftDTO.getType() == 1) {
                    str = "审核通过";
                } else if (dutyShiftDTO.getType() == 2) {
                    str = "审核驳回";
                }
                cell.setCellValue(str);
            }
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("换班审核表.xls", "UTF-8"));
            response.setContentLength(content.length);
            outputStream.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                    outputStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 值班薪酬表
     *
     * @param year
     * @param month
     * @param userId
     * @param schoolId
     * @param response
     */
    public void exportMySarlaryList(int year, int month, ObjectId userId, ObjectId schoolId, HttpServletResponse response) {
        List<DutyUserDTO> dutyUserDTOs = selMySarlaryList(year, month, userId, schoolId);
        HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("值班薪酬表");
        //为sheet1生成第一行，用于放表头信息
        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("值班日期");
        cell = row.createCell(1);
        cell.setCellValue("值班时段");
        cell = row.createCell(2);
        cell.setCellValue("值班时间");
        cell = row.createCell(3);
        cell.setCellValue("值班内容");
        cell = row.createCell(4);
        cell.setCellValue("值班时长(h)");
        cell = row.createCell(5);
        cell.setCellValue("值班薪酬(元)");
        int page = 0;
        if (dutyUserDTOs != null && dutyUserDTOs.size() != 0) {
            for (DutyUserDTO dutyUserDTO : dutyUserDTOs) {
                page++;
                row = sheet.createRow(page);
                cell = row.createCell(0);
                cell.setCellValue(dutyUserDTO.getDate());
                cell = row.createCell(1);
                cell.setCellValue(dutyUserDTO.getTimeDesc());
                cell = row.createCell(2);
                cell.setCellValue(dutyUserDTO.getTimeDuan());
                cell = row.createCell(3);
                cell.setCellValue(dutyUserDTO.getContent());
                cell = row.createCell(4);
                cell.setCellValue(dutyUserDTO.getTimes());
                cell = row.createCell(5);
                cell.setCellValue(dutyUserDTO.getSalary());
            }
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("值班薪酬表.xls", "UTF-8"));
            response.setContentLength(content.length);
            outputStream.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                    outputStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param dutyShiftId
     * @param type
     */
    public void updDutyShiftInfo(String dutyShiftId, String userId, int type) {
        if (type == 1) {
            DutyShiftEntry dutyShiftEntry = dutyDao.selDutyShiftById(new ObjectId(dutyShiftId));
            dutyDao.updateUserIdDutyInfo(dutyShiftEntry.getDutyId(), dutyShiftEntry.getUserId(), new ObjectId(userId));
        }
        dutyDao.updDutyShiftInfo(new ObjectId(dutyShiftId), type);
    }

    /**
     * @param dutyUserId
     * @param log
     * @param filepath
     */
    public void addDutyLog(String dutyUserId, ObjectId userId, String log, String[] filepath, String[] realName) {
        List<BasicDBObject> basicDBObjects = new ArrayList<BasicDBObject>();
        if (filepath != null && filepath.length != 0 && realName != null && realName.length != 0) {
            for (int i = 0; i < filepath.length; i++) {
                if (!StringUtils.isEmpty(realName[i])) {
                    String fileKey = filepath[i].substring(filepath[i].lastIndexOf('/') + 1);
                    String href = "/commonupload/doc/down.do?type=2&fileKey=" + fileKey + "&fileName=" + realName[i];
                    basicDBObjects.add(new LessonWare(href, realName[i], filepath[i]).getBaseEntry());
                }
            }
        }
        dutyDao.addDutyLog(new ObjectId(dutyUserId), userId, log, basicDBObjects);
    }

    /**
     * @param startTime
     * @param endTime
     * @param name
     * @param schoolId
     * @param response
     */
    public void exportDutyInfoList(String startTime, String endTime, String name, ObjectId schoolId, HttpServletResponse response) {
        List<DutyUserDTO> dutyUserDTOs = selDutyUserSarlaryList(startTime, endTime, name, schoolId, 0, 0);
        HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("值班计划管理表");
        //为sheet1生成第一行，用于放表头信息
        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("值班日期");
        cell = row.createCell(1);
        cell.setCellValue("值班人员");
        cell = row.createCell(2);
        cell.setCellValue("值班时段");
        cell = row.createCell(3);
        cell.setCellValue("值班时间");
        cell = row.createCell(4);
        cell.setCellValue("值班项目");
        cell = row.createCell(5);
        cell.setCellValue("值班记录");
        int page = 0;
        if (dutyUserDTOs != null && dutyUserDTOs.size() != 0) {
            for (DutyUserDTO dutyUserDTO : dutyUserDTOs) {
                page++;
                row = sheet.createRow(page);
                cell = row.createCell(0);
                cell.setCellValue(dutyUserDTO.getDate());
                cell = row.createCell(1);
                cell.setCellValue(dutyUserDTO.getUserName());
                cell = row.createCell(2);
                cell.setCellValue(dutyUserDTO.getTimeDesc());
                cell = row.createCell(3);
                cell.setCellValue(dutyUserDTO.getTimeDuan());
                cell = row.createCell(4);
                cell.setCellValue(dutyUserDTO.getProjectName());
                cell = row.createCell(5);
                cell.setCellValue(dutyUserDTO.getContent());
            }
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("值班计划管理表.xls", "UTF-8"));
            response.setContentLength(content.length);
            outputStream.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                    outputStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param dutyUserId
     */
    public DutyUserEntry selDutyUserByDutyId(String dutyUserId, ObjectId userId) {
        return dutyDao.selDutyUserById(new ObjectId(dutyUserId), userId);
    }

    /**
     * @param schoolId
     * @param modelName
     * @return
     */
    public int checkModelNameCount(ObjectId schoolId, String modelName, String modelId) {
        return dutyDao.checkModelNameCount(schoolId, modelName, modelId);
    }

    /**
     * @param startTime
     * @param endTime
     * @param name
     * @param response
     */
    public void exportDutyTotal(String startTime, String endTime, String name, ObjectId schoolId, HttpServletResponse response) {
        List<DutyUserDTO> dutyUserDTOList = selDutyUserSarlaryList(startTime, endTime, name, schoolId, 0, 0);
        List<OverTimeDTO> overTimeDTOList = overTimeService.selJiaBanList(startTime, endTime, name, null, schoolId, 2, 0);
        List<Long> times = new ArrayList<Long>();
        if (dutyUserDTOList != null && dutyUserDTOList.size() != 0) {
            for (DutyUserDTO dutyUserDTO : dutyUserDTOList) {
                times.add(DateTimeUtils.getStrToLongTime(dutyUserDTO.getDate(), DateTimeUtils.DATE_YYYY_MM_DD_N));
            }
        }
        if (overTimeDTOList != null && overTimeDTOList.size() != 0) {
            for (OverTimeDTO overTimeDTO : overTimeDTOList) {
                times.add(DateTimeUtils.getStrToLongTime(overTimeDTO.getDate(), DateTimeUtils.DATE_YYYY_MM_DD_N));
            }
        }
        List<Date> datelist = getDateList(startTime, endTime, times);


    }


    /**
     * 获取date列表
     *
     * @param startTime
     * @param endTime
     * @param times
     * @return
     */
    public List<Date> getDateList(String startTime, String endTime, List<Long> times) {
        if (!StringUtils.isEmpty(startTime)) {
            times.add(DateTimeUtils.getStrToLongTime(startTime, DateTimeUtils.DATE_YYYY_MM_DD_N));
        }
        if (!StringUtils.isEmpty(endTime)) {
            times.add(DateTimeUtils.getStrToLongTime(endTime, DateTimeUtils.DATE_YYYY_MM_DD_N));
        }
        times = removeDuplicate(times);
        long maxTime = 0l;
        long minTime = 0l;
        if (times != null && times.size() != 0) {
            maxTime = times.get(0);
            minTime = times.get(times.size() - 1);
        }
        int count = DateTimeUtils.daysOfTwo(DateTimeUtils.longToDate(minTime, DateTimeUtils.DATE_YYYY_MM_DD_N), DateTimeUtils.longToDate(maxTime, DateTimeUtils.DATE_YYYY_MM_DD_N));
        List<Date> datelist = WeekUtil.dateToWeek(DateTimeUtils
                .longToDate(minTime, DateTimeUtils.DATE_YYYY_MM_DD_N), count);
        for (Date date : datelist) {

        }
        return datelist;
    }

    /**
     * List去重
     *
     * @param list
     * @return
     */
    public List<Long> removeDuplicate(List<Long> list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        Collections.reverse(list);
        return list;
    }

    /**
     * 平安校园值班记录统计
     *
     * @param startTime
     * @param endTime
     * @param name
     * @param schoolId
     * @param response
     */
    public void exportDutyLog(String startTime, String endTime, String name, ObjectId schoolId, HttpServletResponse response) throws Exception {
        List<DutyUserDTO> dutyUserDTOList = selDutyUserSarlaryList(startTime, endTime, name, schoolId, 0, 0);
        List<Long> times = new ArrayList<Long>();
        if (dutyUserDTOList != null && dutyUserDTOList.size() != 0) {
            for (DutyUserDTO dutyUserDTO : dutyUserDTOList) {
                times.add(DateTimeUtils.getStrToLongTime(dutyUserDTO.getDate(), DateTimeUtils.DATE_YYYY_MM_DD_N));
            }
        }
        List<String> times2 = removeDuplicateCover(times);
        XWPFDocument doc = new XWPFDocument();
        XWPFParagraph p1 = doc.createParagraph();
        // 设置字体对齐方式
        p1.setAlignment(ParagraphAlignment.CENTER);
        p1.setVerticalAlignment(TextAlignment.TOP);
        // 第一页要使用p1所定义的属性
        XWPFRun r1 = p1.createRun();
        // 设置字体是否加粗
        r1.setBold(true);
        r1.setFontSize(15);
        // 设置使用何种字体
        r1.setFontFamily("Courier");
        // 设置上下两行之间的间距
        r1.setTextPosition(10);
        r1.setText("平安校园记录");
        // 设置个人信息
        XWPFParagraph p2 = doc.createParagraph();
        p2.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun r2 = p2.createRun();
        r2.setFontFamily("Courier");
        r2.setTextPosition(10);
        r2.setText("记录日期" + DateTimeUtils.convert(DateTimeUtils.getStrToLongTime(times2.get(0), DateTimeUtils.DATE_YYYY_MM_DD_N), DateTimeUtils.CHINESE_DATE)
                + "至" + DateTimeUtils.convert(DateTimeUtils.getStrToLongTime(times2.get(times.size() - 1), DateTimeUtils.DATE_YYYY_MM_DD_N), DateTimeUtils.CHINESE_DATE));
        r2.addCarriageReturn();
        XWPFParagraph p3 = doc.createParagraph();
        p3.setWordWrap(true);
        XWPFRun r3 = p3.createRun();
        r3.setTextPosition(10);
        r3.setFontSize(8);
        if (times2 != null && times2.size() != 0) {
            for (String time : times2) {
                r3.setText(DateTimeUtils.convert(DateTimeUtils.getStrToLongTime(time, DateTimeUtils.DATE_YYYY_MM_DD_N), DateTimeUtils.CHINESE_DATE));
                r3.addCarriageReturn();
                r3.setText("事项：");
                r3.addCarriageReturn();
                for (DutyUserDTO dutyUserDTO : dutyUserDTOList) {
                    if (time.equals(dutyUserDTO.getDate())) {
                        r3.setText(dutyUserDTO.getTimeDesc() + " " + dutyUserDTO.getTimeDuan() + " " + dutyUserDTO.getProjectName() + " 值班人：" + dutyUserDTO.getUserName());
                        r3.addCarriageReturn();
                        r3.setText("签到时间：" + dutyUserDTO.getCheckIn() + " 签退时间：" + dutyUserDTO.getCheckOut() + " 值班时长：" + dutyUserDTO.getTimes());
                        r3.addCarriageReturn();
                        r3.setText("值班记录：" + dutyUserDTO.getContent());
                        r3.addCarriageReturn();
                    }
                }
                r3.addCarriageReturn();
            }
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            doc.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("平安校园记录.doc", "UTF-8"));
            response.setContentLength(content.length);
            outputStream.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                    outputStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * List去重
     *
     * @param list
     * @return
     */
    public List<String> removeDuplicateCover(List<Long> list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        Collections.sort(list, new Comparator() {

            @Override

            public int compare(Object o1, Object o2) {

                return new Long(Long.toString((Long) o1)).compareTo(new Long(Long.toString((Long) o2)));

            }

        });
        List<String> sts = new ArrayList<String>();
        for (Long ls : list) {
            sts.add(DateTimeUtils.convert(ls, DateTimeUtils.DATE_YYYY_MM_DD_N));
        }
        return sts;
    }

    /**
     * 设置ip
     *
     * @param schoolId
     * @param ip
     */
    public void updateDutySetIp(ObjectId schoolId, String ip, int year, int week) {
        dutyDao.updateDutySetIp(schoolId, ip, year, week);
    }

    /**
     * 编辑值班说明
     *
     * @param schoolId
     * @param explain
     */
    public void addDutyExplain(ObjectId schoolId, int year, int week, String explain) {
        dutyDao.addDutyExplain(schoolId, year, week, explain);
    }

    /**
     * 查询值班记录数量
     *
     * @param startTime
     * @param endTime
     * @param name
     * @param schoolId
     * @return
     */
    public int selDutyUserSarlaryCount(String startTime, String endTime, String name, ObjectId schoolId) {
        int count = 0;
        long stime = 0;
        long etime = 0;
        if (!StringUtils.isEmpty(startTime)) {
            String sdate = startTime + " " + "00:00";
            stime = DateTimeUtils.getStrToLongTime(sdate, DateTimeUtils.DATE_YYYY_MM_DD_HH_MM);
        }
        if (!StringUtils.isEmpty(endTime)) {
            String edate = endTime + " " + "23:59";
            etime = DateTimeUtils.getStrToLongTime(edate, DateTimeUtils.DATE_YYYY_MM_DD_HH_MM);
        }
        List<ObjectId> dutyIds = new ArrayList<ObjectId>();
        List<ObjectId> dutyProjectIds = new ArrayList<ObjectId>();
        List<ObjectId> users = new ArrayList<ObjectId>();
        List<DutyUserDTO> dutyUserDTOs = new ArrayList<DutyUserDTO>();
        List<DutyEntry> dutyEntryList = dutyDao.selDutyByProjectId(stime, etime, "", schoolId);
        Map<ObjectId, DutyEntry> dutyEntryMap = new HashMap<ObjectId, DutyEntry>();
        if (dutyEntryList != null && dutyEntryList.size() != 0) {
            for (DutyEntry dutyEntry : dutyEntryList) {
                dutyIds.add(dutyEntry.getID());
                dutyEntryMap.put(dutyEntry.getID(), dutyEntry);
                dutyProjectIds.add(dutyEntry.getProjectId());
            }
            Map<ObjectId, UserEntry> userEntryMap = new HashMap<ObjectId, UserEntry>();
            List<UserEntry> userEntryList = userDao.searchUsersWithSchool(name, schoolId, new BasicDBObject("nm", 1));
            if (userEntryList != null && userEntryList.size() != 0) {
                for (UserEntry userEntry : userEntryList) {
                    users.add(userEntry.getID());
                    userEntryMap.put(userEntry.getID(), userEntry);
                }
            }
            count = dutyDao.selDutyUserByUserIdsCount(dutyIds, !StringUtils.isEmpty(name) ? users : null);
        }
        return count;
    }
}
