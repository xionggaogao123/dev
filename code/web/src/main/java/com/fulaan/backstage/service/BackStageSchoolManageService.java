package com.fulaan.backstage.service;

import com.db.backstage.LogMessageDao;
import com.db.backstage.SchoolControlTimeDao;
import com.db.backstage.TeacherApproveDao;
import com.db.business.ModuleTimeDao;
import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.GroupDao;
import com.db.fcommunity.MemberDao;
import com.db.fcommunity.MineCommunityDao;
import com.db.jiaschool.HomeSchoolDao;
import com.db.jiaschool.SchoolCommunityDao;
import com.db.jiaschool.SchoolFunctionDao;
import com.db.jiaschool.SchoolPersionDao;
import com.db.smalllesson.SmallLessonDao;
import com.db.user.UserDao;
import com.fulaan.backstage.dto.LogMessageDTO;
import com.fulaan.backstage.dto.RoleJurisdictionSettingDto;
import com.fulaan.backstage.dto.SchoolControlTimeDTO;
import com.fulaan.community.dto.CommunityDTO;
import com.fulaan.dto.MemberDTO;
import com.fulaan.jiaschool.dto.HomeSchoolDTO;
import com.fulaan.jiaschool.dto.SchoolCommunityDTO;
import com.fulaan.utils.HSSFUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.backstage.LogMessageType;
import com.pojo.backstage.SchoolControlTimeEntry;
import com.pojo.backstage.TeacherApproveEntry;
import com.pojo.base.BaseDBObject;
import com.pojo.business.ModuleTimeEntry;
import com.pojo.fcommunity.CommunityEntry;
import com.pojo.fcommunity.MemberEntry;
import com.pojo.fcommunity.MineCommunityEntry;
import com.pojo.instantmessage.ApplyTypeEn;
import com.pojo.jiaschool.HomeSchoolEntry;
import com.pojo.jiaschool.SchoolCommunityEntry;
import com.pojo.jiaschool.SchoolFunctionEntry;
import com.pojo.jiaschool.SchoolPersionEntry;
import com.pojo.smalllesson.SmallLessonEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Auther: taotao.chan
 * @Date: 2018/9/10 11:14
 * @Description:
 */
@Service
public class BackStageSchoolManageService {

    @Autowired
    private BackStageRoleManageService backStageRoleManageService;

    private HomeSchoolDao homeSchoolDao = new HomeSchoolDao();

    private LogMessageDao logMessageDao = new LogMessageDao();

    private CommunityDao communityDao = new CommunityDao();

    private SchoolCommunityDao schoolCommunityDao= new SchoolCommunityDao();

    private MemberDao memberDao = new MemberDao();

    private UserDao userDao = new UserDao();

    private TeacherApproveDao teacherApproveDao = new TeacherApproveDao();

    private SmallLessonDao smallLessonDao = new SmallLessonDao();

    private ModuleTimeDao moduleTimeDao = new ModuleTimeDao();

    private SchoolPersionDao schoolPersionDao = new SchoolPersionDao();

    private SchoolFunctionDao schoolFunctionDao = new SchoolFunctionDao();

    private MineCommunityDao mineCommunityDao = new MineCommunityDao();

    private GroupDao groupDao = new GroupDao();

    //新版校管控
    private SchoolControlTimeDao schoolControlTimeDao = new SchoolControlTimeDao();


    public Map<String, Object> getSchoolList(int schoolType, String provincesName, String cityName, int page, int pageSize, String schoolName) {
        Map<String, Object> map = homeSchoolDao.getBackStageSchoolList(schoolType, provincesName, cityName, page, pageSize, schoolName);

        List<HomeSchoolEntry> entries = (ArrayList)map.get("entryList");
        List<HomeSchoolDTO> dtos = new ArrayList<HomeSchoolDTO>();
        List<ObjectId> schoolIdList = new ArrayList<ObjectId>();
        for (HomeSchoolEntry entry : entries) {
            dtos.add(new HomeSchoolDTO(entry));
            //筛选学校的Id 作为下面学校分组的参数
            if (!schoolIdList.contains(entry.getID())){
                schoolIdList.add(entry.getID());
            }
        }

        //以学校分组 获取学校下面的社群Map<schoolId,List<communityId>>
        Map<ObjectId,List<ObjectId>> schoolIdCommunityIdListMap = schoolCommunityDao.getAllIdGroupBySchoolId(schoolIdList);
        for (HomeSchoolDTO dto : dtos){
            dto.setCommunityCount(schoolIdCommunityIdListMap.get(new ObjectId(dto.getId())).size()+"");
        }
        map.put("dtos", dtos);
        map.remove("entryList");
        return map;
    }

    public Map<String,Object> selectAllSchool(int page,int pageSize,String keyword){
        Map<String,Object> map = new HashMap<String, Object>();
        List<HomeSchoolEntry> entries = homeSchoolDao.getAllList(page, pageSize, keyword);
        List<HomeSchoolDTO> dtos = new ArrayList<HomeSchoolDTO>();
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        for(HomeSchoolEntry entry : entries){
            objectIdList.add(entry.getID());
        }
        Map<ObjectId,Integer> omap = schoolFunctionDao.getOneRoleList(objectIdList,1);
        for(HomeSchoolEntry entry : entries){
            Integer open = omap.get(entry.getID());
            HomeSchoolDTO dto = new HomeSchoolDTO(entry);
            if(open!=null){
                dto.setOpen(open);
            }else{
                dto.setOpen(0);
            }
            dtos.add(dto);
        }
        int count = homeSchoolDao.getAllListCount(page,pageSize,keyword);
        map.put("list",dtos);
        map.put("count",count);
        return map;
    }

    public List<HomeSchoolDTO> getSimpleSchoolList(ObjectId userId){
        List<HomeSchoolDTO> homeSchoolDTOs = new ArrayList<HomeSchoolDTO>();
        List<HomeSchoolEntry> entries = homeSchoolDao.getSchoolList();
        for(HomeSchoolEntry homeSchoolEntry : entries){
            if(homeSchoolEntry.getName()!=null && !homeSchoolEntry.getName().equals("复兰大学")){
                homeSchoolDTOs.add(new HomeSchoolDTO(homeSchoolEntry));
            }
        }
        return homeSchoolDTOs;
    }



    public String saveNewSchoolEntry(HomeSchoolDTO dto){
        if(dto.getId() != null &&!dto.getId().equals("")){
            //更新
            ObjectId oid = homeSchoolDao.addEntry(
                    new HomeSchoolEntry(
                            new ObjectId(dto.getId()),
                            dto.getSchoolType(),
                            dto.getName(),
                            dto.getSort(),
                            dto.getProvince(),
                            dto.getAddress(),
                            dto.getCity(),
                            dto.getCreationDate(),
                            Arrays.asList(dto.getSchoolParagraphStr().split("-"))
                    )
            );
            return oid.toString();
        }else{
            //新增
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            int count = homeSchoolDao.getSortCount();
            dto.setSort(count+1000);
            ObjectId oid = homeSchoolDao.addEntry(
                    new HomeSchoolEntry(
                            dto.getSchoolType(),
                            dto.getName(),
                            dto.getSort(),
                            dto.getProvince(),
                            dto.getAddress(),
                            dto.getCity(),
                            f.format(now),
                            Arrays.asList(dto.getSchoolParagraphStr().split("-"))
                    )
            );
            //新增学校增加默认校管控时间
            //获取默认时间
            List<SchoolControlTimeEntry> schoolControlTimeEntryList = schoolControlTimeDao.getDefaultSchoolControlSettingList();
            List<DBObject> schoolControlDBObjectList = new ArrayList<DBObject>();
            if (schoolControlTimeEntryList.size() >0){
                for (SchoolControlTimeEntry entry : schoolControlTimeEntryList){
                    entry.setSchoolId(oid);//封装进schoolId
                    entry.setID(new ObjectId());//封装进新主键Id
                    schoolControlDBObjectList.add(entry.getBaseEntry());
                }
                schoolControlTimeDao.saveNewSchoolAddControlTime(schoolControlDBObjectList);
            }
            return oid.toString();
        }

    }

    public void delNewSchoolEntry(ObjectId id,ObjectId userId){
        HomeSchoolEntry entry =  homeSchoolDao.getEntryById(id);
        if(entry!=null){
            homeSchoolDao.delEntry(id);
            schoolCommunityDao.delEntryBySchoolId(id);
            this.addLogMessage(id.toString(),"删除了学校"+entry.getName(), LogMessageType.school.getDes(),userId.toString());
        }
    }

    public CommunityDTO selectNewCommunityEntry(String searchId){
        CommunityEntry communityEntry = communityDao.findBySearchId(searchId);
        //MemberEntry memberEntry = memberDao.getHead(communityEntry.getGroupId());
        if(communityEntry!=null) {
            CommunityDTO communityDTO = new CommunityDTO(communityEntry);
            SchoolCommunityEntry schoolCommunityEntry = schoolCommunityDao.getEntryById(communityEntry.getID());
            if (schoolCommunityEntry != null) {//已绑定
                HomeSchoolEntry homeSchoolEntry = homeSchoolDao.getEntryById(schoolCommunityEntry.getSchoolId());
                if(homeSchoolEntry==null){
                    communityDTO.setMemberCount(2);
                    communityDTO.setOwerName("");
                }else{
                    communityDTO.setMemberCount(1);
                    communityDTO.setOwerName(homeSchoolEntry.getName());
                }
            }else{//未绑定
                communityDTO.setMemberCount(2);
                communityDTO.setOwerName("");
            }
            return communityDTO;
        }
        return null;
    }

    public void addSchoolSort(String communityId,String schoolId){
        SchoolCommunityEntry schoolCommunityEntry = schoolCommunityDao.getEntryById(new ObjectId(communityId));
        if(schoolCommunityEntry==null){
            SchoolCommunityDTO dto = new SchoolCommunityDTO();
            dto.setCommunityId(communityId);
            dto.setSchoolId(schoolId);
            schoolCommunityDao.addEntry(dto.buildAddEntry());
        }else{
            schoolCommunityEntry.setSchoolId(new ObjectId(schoolId));
            schoolCommunityDao.addEntry(schoolCommunityEntry);
        }
    }

    public void delSchoolSort(ObjectId communityId){
        SchoolCommunityEntry schoolCommunityEntry = schoolCommunityDao.getEntryById(communityId);
        if(schoolCommunityEntry!=null) {
            schoolCommunityDao.delEntry(communityId);
        }
    }

    public void addLogMessage(String contactId,String content,String function,String userId){
        LogMessageDTO dto = new LogMessageDTO();
        dto.setType(1);
        dto.setContactId(contactId);
        dto.setContent(content);
        dto.setFunction(function);
        dto.setUserId(userId);
        logMessageDao.addEntry(dto.buildAddEntry());
    }

    public Map<String,Object> getCommunityListBySchoolId(ObjectId schoolId,String communityName,int page,int pageSize){
        //学校信息
        HomeSchoolEntry homeSchoolEntry= homeSchoolDao.getEntryById(schoolId);

        Map<String,Object> map= new HashMap<String, Object>();
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        objectIdList.add(schoolId);
        List<SchoolCommunityEntry> schoolCommunityEntries  = schoolCommunityDao.getReviewList(objectIdList);
        List<ObjectId> communityIdList = new ArrayList<ObjectId>();
        for(SchoolCommunityEntry entry :schoolCommunityEntries){
            communityIdList.add(entry.getCommunityId());
        }


        List<CommunityEntry> communityEntries = communityDao.findPageByObjectIdsName(communityIdList,communityName, page, pageSize);
        int count = communityDao.getNotDelNumber(communityIdList);
        List<CommunityDTO> communityDTOs = new ArrayList<CommunityDTO>();
        for(CommunityEntry communityEntry : communityEntries){
            CommunityDTO communityDTO =new CommunityDTO(communityEntry);
            communityDTO.setLogo(getNewLogo(communityDTO.getLogo()));
            communityDTO.setSchoolName(homeSchoolEntry.getName());
            communityDTOs.add(communityDTO);
        }
        //根据community分组 获取Map<communityId,List<MemberDTO>>
        Map<ObjectId,List<MemberEntry>> communityIdMemberEntryMap = memberDao.getMembersGroupByCommunity(communityIdList);
        for (CommunityDTO dto : communityDTOs){
            List<MemberEntry> memberEntryList = communityIdMemberEntryMap.get(new ObjectId(dto.getId()));
            for (MemberEntry memberEntry : memberEntryList){
                //社长
                if (Constant.TWO == memberEntry.getRole()){
                    dto.setHead(new MemberDTO(memberEntry));
                }
            }
        }

        if ("".equals(communityName)){
            map.put("count",count);
        }else {
            map.put("count",communityEntries.size());
        }

        map.put("list",communityDTOs);
        return map;
    }

    //处理社区logo
    public static String getNewLogo(String url){
        String str = "";
        if(url != null && url.contains("http://www.fulaan.com/")){
            str = url.replace("http://www.fulaan.com/", "http://appapi.jiaxiaomei.com/");
        }else{
            str = url;
        }
        if(url != null && url.contains("/static/images/community/upload.png")){
            str = str.replace("upload.png", "head_group.png");
        }
        return str;
    }


    /**
     * 学校大V用户数据统计
     */
    public  List<Map<String,Object>> getTeacherList(/*long time,long otime,*/String communityId,ObjectId schoolId){
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        List<ObjectId> communityList = new ArrayList<ObjectId>();
        if(communityId!=null && !communityId.equals("")){
            communityList.add(new ObjectId(communityId));
        }else{
            objectIdList.add(schoolId);
            List<SchoolCommunityEntry> schoolCommunityEntries  = schoolCommunityDao.getReviewList(objectIdList);
            for(SchoolCommunityEntry entry :schoolCommunityEntries){
                communityList.add(entry.getCommunityId());
            }
        }
        //查询groupId
        List<ObjectId> groupIds = groupDao.getCommunitysIdsList(communityList);
        List<ObjectId> memberList = memberDao.getAllCommunityIdsMembers(groupIds);
        //所有该学校大V
        List<ObjectId> objectIdList2 = teacherApproveDao.selectMap(memberList);
        List<Integer> integers = new ArrayList<Integer>();
        integers.add(ApplyTypeEn.operation.getType());//
        integers.add(ApplyTypeEn.notice.getType());
        integers.add(ApplyTypeEn.hot.getType());
        integers.add(ApplyTypeEn.repordcard.getType());
        integers.add(ApplyTypeEn.school.getType());
        integers.add(ApplyTypeEn.smallLesson.getType());
        List<ModuleTimeEntry> moduleTimeEntries = moduleTimeDao.getEntryListNotTimeRange(objectIdList2, communityList, integers);
        //userDao.
        Map<String,Integer>  map = new HashMap<String, Integer>();
        for(ModuleTimeEntry entry : moduleTimeEntries){
            String key = entry.getUserId().toString()+"&"+entry.getModuleType();
            Integer integer = map.get(key);
            if(integer==null){
                map.put(key,1);
            }else{
                map.put(key,integer+1);
            }
        }
        //小课堂使用时间
        List<SmallLessonEntry> smallLessonEntries = smallLessonDao.getTeacherLessonListNotTimeRange(objectIdList2);
        for(SmallLessonEntry smallLessonEntry:smallLessonEntries){
            String key = smallLessonEntry.getUserId().toString()+"&"+ApplyTypeEn.smallDuring.getType();
            Integer integer = map.get(key);
            if(integer==null){
                map.put(key,smallLessonEntry.getNodeTime());
            }else{
                map.put(key,integer+smallLessonEntry.getNodeTime());
            }
        }


        List<UserEntry> userEntries = userDao.getUserEntryList(objectIdList2, Constant.FIELDS);
        List<Map<String,Object>> mapList = new ArrayList<Map<String, Object>>();
        for(UserEntry userEntry : userEntries){
            Map<String,Object> smap = new HashMap<String, Object>();
            String name = StringUtils.isNotBlank(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
            smap.put("userName",name);
            smap.put("avatar", AvatarUtils.getAvatar(userEntry.getAvatar(),userEntry.getRole(),userEntry.getSex()));
            smap.put("userId",userEntry.getID().toString());
            smap.put("phone",userEntry.getMobileNumber());
            smap.put("jid",userEntry.getGenerateUserCode());
            //作业创建
            String key1 = userEntry.getID().toString()+"&"+ApplyTypeEn.operation.getType();
            Integer integer1 = map.get(key1);
            if(integer1==null){
                smap.put("operation",0);
            }else{
                smap.put("operation",integer1);
            }
            //通知创建
            String key2 = userEntry.getID().toString()+"&"+ApplyTypeEn.notice.getType();
            Integer integer2 = map.get(key2);
            if(integer2==null){
                smap.put("notice",0);
            }else{
                smap.put("notice",integer2);
            }
            //火热分享创建
            String key3 = userEntry.getID().toString()+"&"+ApplyTypeEn.hot.getType();
            Integer integer3 = map.get(key3);
            if(integer3==null){
                smap.put("hot",0);
            }else{
                smap.put("hot",integer3);
            }
            //成绩单创建
            String key4 = userEntry.getID().toString()+"&"+ApplyTypeEn.repordcard.getType();
            Integer integer4 = map.get(key4);
            if(integer4==null){
                smap.put("repordcard",0);
            }else{
                smap.put("repordcard",integer4);
            }
            //校管控推送应用
            String key5 = userEntry.getID().toString()+"&"+ApplyTypeEn.school.getType();
            Integer integer5 = map.get(key5);
            if(integer5==null){
                smap.put("school",0);
            }else{
                smap.put("school",integer5);
            }
            //小课堂登陆
            String key6 = userEntry.getID().toString()+"&"+ApplyTypeEn.smallLesson.getType();
            Integer integer6 = map.get(key6);
            if(integer6==null){
                smap.put("smallLesson",0);
            }else{
                smap.put("smallLesson",integer6);
            }
            //小课堂使用时间
            String key7 = userEntry.getID().toString()+"&"+ApplyTypeEn.smallDuring.getType();
            Integer integer7 = map.get(key7);
            if(integer7==null){
                smap.put("smallDuring",0);
            }else{
                smap.put("smallDuring",integer7);
            }
            mapList.add(smap);
        }

        return mapList;
    }


    public void exportTemplate(HttpServletRequest request, HttpServletResponse response, List<Map<String,Object>> list, long startTime, long endTime, ObjectId schoolId) {
        // List<GroupExamUserRecordDTO> recordDTOs = searchRecordStudentScores(examGroupDetailId, -1, -1, 1);
        //  GroupExamDetailEntry detailEntry = groupExamDetailDao.getGroupExamDetailEntry(examGroupDetailId);
        HomeSchoolEntry homeSchoolEntry = homeSchoolDao.getEntryById(schoolId);
        if(homeSchoolEntry==null){
            return;
        }
        String s = DateTimeUtils.getLongToStrTimeTwo(startTime).substring(0, 11);
        String e = DateTimeUtils.getLongToStrTimeTwo(endTime).substring(0,11);
        if (list.size()>0) {
            String sheetName = "“家校美”"+homeSchoolEntry.getName()+"教师使用报告（"+s+"-"+e+"）";
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet(sheetName);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0,15));
            sheet.setColumnWidth(1, 10 * 256);
            HSSFRow rowZero = sheet.createRow(0);
            HSSFCell cellZero = rowZero.createCell(0);
            cellZero.setCellValue("");

            HSSFRow row = sheet.createRow(1);
            HSSFCell cell = row.createCell(0);
            cell.setCellValue("老师名");

            cell = row.createCell(1);
            cell.setCellValue("家校美id");

            cell = row.createCell(2);
            cell.setCellValue("联系电话");

            cell = row.createCell(3);
            cell.setCellValue("通知");

            cell = row.createCell(4);
            cell.setCellValue("作业");

            cell = row.createCell(5);
            cell.setCellValue("成绩单");

            cell = row.createCell(6);
            cell.setCellValue("火热分享");

            cell = row.createCell(7);
            cell.setCellValue("推送应用");

            cell = row.createCell(8);
            cell.setCellValue("小课堂登陆");

            cell = row.createCell(9);
            cell.setCellValue("小课堂在线（小时）");

            int rowLine = 2;

            HSSFRow rowItem;
            HSSFCell cellItem;
            for (Map<String,Object> recordDTO : list) {

                rowItem = sheet.createRow(rowLine);

                cellItem = rowItem.createCell(0);
                cellItem.setCellValue((String)recordDTO.get("userName"));

                cellItem = rowItem.createCell(1);
                cellItem.setCellValue((String)recordDTO.get("jid"));

                cellItem = rowItem.createCell(2);
                cellItem.setCellValue((String)recordDTO.get("phone"));

                cellItem = rowItem.createCell(3);
                cellItem.setCellValue((Integer)recordDTO.get("notice"));

                cellItem = rowItem.createCell(4);
                cellItem.setCellValue((Integer)recordDTO.get("operation"));

                cellItem = rowItem.createCell(5);
                cellItem.setCellValue((Integer)recordDTO.get("repordcard"));

                cellItem = rowItem.createCell(6);
                cellItem.setCellValue((Integer)recordDTO.get("hot"));

                cellItem = rowItem.createCell(7);
                cellItem.setCellValue((Integer)recordDTO.get("school"));

                cellItem = rowItem.createCell(8);
                cellItem.setCellValue((Integer)recordDTO.get("smallLesson"));

                cellItem = rowItem.createCell(9);
                cellItem.setCellValue((Integer)recordDTO.get("smallDuring"));

                rowLine++;
            }

            String fileName = sheetName + ".xls";
            String userAgent = request.getHeader("USER-AGENT");
            HSSFUtils.exportExcel(userAgent, response, wb, fileName);
        }
    }

    public Map<String,Object> getPersonListBySchoolId(ObjectId schoolId,int page,int pageSize){
        Map<String,Object> map = new HashMap<String, Object>();
        HomeSchoolEntry homeSchoolEntry = homeSchoolDao.getEntryById(schoolId);
        if(homeSchoolEntry==null){
            return map;
        }
        //获取角色列表信息
        List<RoleJurisdictionSettingDto> roleJurisdictionList = backStageRoleManageService.getRoleJurisdictionList(map);
        Map<String,String> roleMap = new HashMap<String, String>();
        for (RoleJurisdictionSettingDto dto : roleJurisdictionList){
            roleMap.put(dto.getId(),dto.getRoleName());
        }
        List<SchoolPersionEntry> entries = schoolPersionDao.getAllMemberBySchoolId(schoolId, page, pageSize);
        int count = schoolPersionDao.countAllMemberBySchoolId(schoolId);
        List<Map<String,Object>> mapList = new ArrayList<Map<String, Object>>();
        List<ObjectId> objectIdList2 = new ArrayList<ObjectId>();
        Map<ObjectId,SchoolPersionEntry> smap = new HashMap<ObjectId, SchoolPersionEntry>();
        for(SchoolPersionEntry schoolPersionEntry:entries){
            objectIdList2.add(schoolPersionEntry.getUserId());
            smap.put(schoolPersionEntry.getUserId(), schoolPersionEntry);
        }
        List<UserEntry> userEntries = userDao.getUserEntryList(objectIdList2, Constant.FIELDS);
        for(UserEntry entry:userEntries){
            Map<String,Object> map1 = new HashMap<String, Object>();
            map1.put("userId",entry.getID().toString());
            map1.put("userName",entry.getNickName());
            map1.put("jiaId",entry.getGenerateUserCode());
            map1.put("avatar", AvatarUtils.getAvatar(entry.getAvatar(), entry.getRole(), entry.getSex()));
            SchoolPersionEntry schoolPersionEntry = smap.get(entry.getID());
            if(schoolPersionEntry!=null){
                map1.put("id",schoolPersionEntry.getID().toString());
                map1.put("name",schoolPersionEntry.getName());
                map1.put("type",schoolPersionEntry.getType());
                map1.put("schoolName",homeSchoolEntry.getName());
                map1.put("role",schoolPersionEntry.getRole());
                //新增
                map1.put("roleId",schoolPersionEntry.getRoleId());
                map1.put("roleName",roleMap.get(schoolPersionEntry.getRoleId()));
            }
            mapList.add(map1);
        }
        map.put("list",mapList);
        map.put("count",count);
        return map;
    }

    public void savePersonToSchool(ObjectId uuid,ObjectId userId,ObjectId schoolId,String name,int type,int role,String roleId){
        HomeSchoolEntry homeSchoolEntry = homeSchoolDao.getEntryById(schoolId);
        if(homeSchoolEntry==null){
            return;
        }
        SchoolPersionEntry schoolPersionEntry2 = schoolPersionDao.getEntry(userId,schoolId);
        if(schoolPersionEntry2==null){
            SchoolPersionEntry schoolPersionEntry = new SchoolPersionEntry(userId,schoolId,name,type,role,roleId);
            ObjectId id  = schoolPersionDao.addEntry(schoolPersionEntry);
            this.addLogMessage(id.toString(),"添加了学校"+homeSchoolEntry.getName()+"管理用户："+name, LogMessageType.schoolRole.getDes(),uuid.toString());
        }else{
            schoolPersionEntry2.setRoleId(roleId);
            schoolPersionDao.addEntry(schoolPersionEntry2);
            this.addLogMessage(schoolPersionEntry2.getID().toString(),"修改了学校"+homeSchoolEntry.getName()+"管理用户："+name, LogMessageType.schoolRole.getDes(),uuid.toString());
        }

    }

    public void deletePersonToSchool(ObjectId userId,ObjectId id){
        SchoolPersionEntry schoolPersionEntry = schoolPersionDao.getEntryById(id);
        if(schoolPersionEntry==null){
            return;
        }
        HomeSchoolEntry homeSchoolEntry = homeSchoolDao.getEntryById(schoolPersionEntry.getSchoolId());
        if(homeSchoolEntry==null){
            return;
        }
        this.addLogMessage(id.toString(),"删除了学校"+homeSchoolEntry.getName()+"管理用户："+schoolPersionEntry.getName(), LogMessageType.schoolRole.getDes(),userId.toString());
        schoolPersionDao.delEntry(id);
    }


    public void updateSchoolOpen(ObjectId id,int open){
        SchoolFunctionEntry schoolFunctionEntry = schoolFunctionDao.getEntry(id);
        if(schoolFunctionEntry!=null){
            schoolFunctionDao.updateEntry(id, open);
        }else{
            SchoolFunctionEntry schoolFunctionEntry1 = new SchoolFunctionEntry(id,1,open);
            schoolFunctionDao.addEntry(schoolFunctionEntry1);
        }
    }

    public int getAllRole(ObjectId userId){
        //是否是大V
        TeacherApproveEntry teacherApproveEntry = teacherApproveDao.getEntry(userId);
        if(teacherApproveEntry==null || teacherApproveEntry.getType()!=2){
            return 0;
        }
        //获得已被允许的学校
        List<ObjectId> schoolIds = schoolFunctionDao.getAllSchoolIdList(1, 1);
        //已绑定的社群集合
        List<ObjectId> communityIds2 =  schoolCommunityDao.getCommunityIdsList(schoolIds);
        //通知逻辑
        List<MineCommunityEntry> allMineCommunitys = mineCommunityDao.findAll(userId, 1, 100);
        List<ObjectId> communityIds = new ArrayList<ObjectId>();
        for (MineCommunityEntry mineCommunityEntry : allMineCommunitys) {
            communityIds.add(mineCommunityEntry.getCommunityId());
        }
        List<CommunityEntry> communityEntries = communityDao.findByNotObjectIds(communityIds);
        int flage = 0;
        for(CommunityEntry communityEntry:communityEntries){
            if(communityIds2.contains(communityEntry.getID())){
                flage = 1;
                break;
            }
        }
        return flage;
    }

    /**
     * 校管控设置保存
     */
    public String saveSchoolControlSetting(Map map) {
        String result = "";
        if (map.get("id") == null) {
            //新增
            SchoolControlTimeEntry schoolControlEntry = new SchoolControlTimeEntry(
                    map.get("type") == null ? null : Integer.parseInt(map.get("type").toString()),
                    map.get("week") == null ? null : Integer.parseInt(map.get("week").toString()),
                    map.get("dateFrom") == null ? "" : map.get("dateFrom").toString(),
                    map.get("dateTo") == null ? "" : map.get("dateTo").toString(),
                    map.get("schoolTimeFrom") == null ? "" : map.get("schoolTimeFrom").toString(),
                    map.get("schoolTimeTo") == null ? "" : map.get("schoolTimeTo").toString(),
                    map.get("bedTimeFrom") == null ? "" : map.get("bedTimeFrom").toString(),
                    map.get("bedTimeTo") == null ? "" : map.get("bedTimeTo").toString(),
                    map.get("schoolId") == null ? null : new ObjectId(map.get("schoolId").toString()),
                    map.get("holidayName") == null ? "" : map.get("holidayName").toString()
            );
            result = schoolControlTimeDao.saveSchoolControlSetting(schoolControlEntry);
        } else {
            //修改
            SchoolControlTimeEntry schoolControlEntry = new SchoolControlTimeEntry(
                    map.get("id") == null ? null : new ObjectId(map.get("id").toString()),
                    map.get("type") == null ? null : Integer.parseInt(map.get("type").toString()),
                    map.get("week") == null ? null : Integer.parseInt(map.get("week").toString()),
                    map.get("dateFrom") == null ? "" : map.get("dateFrom").toString(),
                    map.get("dateTo") == null ? "" : map.get("dateTo").toString(),
                    map.get("schoolTimeFrom") == null ? "" : map.get("schoolTimeFrom").toString(),
                    map.get("schoolTimeTo") == null ? "" : map.get("schoolTimeTo").toString(),
                    map.get("bedTimeFrom") == null ? "" : map.get("bedTimeFrom").toString(),
                    map.get("bedTimeTo") == null ? "" : map.get("bedTimeTo").toString(),
                    map.get("schoolId") == null ? null : new ObjectId(map.get("schoolId").toString()),
                    map.get("holidayName") == null ? "" : map.get("holidayName").toString()
            );
            result = schoolControlTimeDao.saveSchoolControlSetting(schoolControlEntry);
        }
        return result;
    }

    /**
     * 校管控列表查询
     * 查询当前学校的管控设置列表展示
     */
    public List<SchoolControlTimeDTO> getEachSchoolControlSettingList(ObjectId schoolId) {
        List<SchoolControlTimeDTO> schoolControlTimeDTOList = new ArrayList<SchoolControlTimeDTO>();
        List<SchoolControlTimeEntry> schoolControlTimeEntryList = schoolControlTimeDao.getEachSchoolControlSettingList(schoolId);
        for (SchoolControlTimeEntry entry : schoolControlTimeEntryList){
            if (entry != null){
                schoolControlTimeDTOList.add(new SchoolControlTimeDTO(entry));
            }
        }
        return schoolControlTimeDTOList;
    }

    public String delSchoolControlSetting(Map map) {
        return schoolControlTimeDao.delSchoolControlSetting(map);
    }

    public String getSchoolIdByManageUid(ObjectId userId) {
        return schoolPersionDao.getSchoolIdByManageUid(userId);
    }

    /**
     * 校管控列表查询
     * 系统默认管控设置列表展示
     */
    public List<SchoolControlTimeDTO> getDefaultSchoolControlSettingList() {
        List<SchoolControlTimeDTO> schoolControlTimeDTOList = new ArrayList<SchoolControlTimeDTO>();
        List<SchoolControlTimeEntry> schoolControlTimeEntryList = schoolControlTimeDao.getDefaultSchoolControlSettingList();
        for (SchoolControlTimeEntry entry : schoolControlTimeEntryList){
            if (entry != null){
                schoolControlTimeDTOList.add(new SchoolControlTimeDTO(entry));
            }
        }
        return schoolControlTimeDTOList;
    }

    /**
     * 修复存在学校没有默认管控时间
     */
    public String fixOldSchoolControlTimeSetting() {
        String result = "";
        try {
            //获取已经设置过校管控时间的学校Id集合
            List<ObjectId> schoolControlIdList = schoolControlTimeDao.getSchoolControlIdList();
            if (schoolControlIdList.size()>0){
                //获取所有学校Id（除去设置过校管控时间的学校Id集合）
                List<ObjectId> schoolNotControlIdList = homeSchoolDao.getSchoolNotControlIdList(schoolControlIdList);
                //获取默认时间
                List<SchoolControlTimeEntry> schoolControlTimeEntryList = schoolControlTimeDao.getDefaultSchoolControlSettingList();

                List<DBObject> schoolControlDBObjectList = new ArrayList<DBObject>();
                if (schoolControlTimeEntryList.size() >0){
                    for (ObjectId sid : schoolNotControlIdList){
                        for (SchoolControlTimeEntry entry : schoolControlTimeEntryList){
                            SchoolControlTimeEntry entryTemp = new SchoolControlTimeEntry();
                            entryTemp.setSchoolId(sid);//封装进schoolId
                            entryTemp.setBedTimeFrom(entry.getBedTimeFrom());
                            entryTemp.setBedTimeTo(entry.getBedTimeTo());
                            entryTemp.setSchoolTimeFrom(entry.getSchoolTimeFrom());
                            entryTemp.setSchoolTimeTo(entry.getSchoolTimeTo());
                            entryTemp.setType(entry.getType());
                            entryTemp.setWeek(entry.getWeek());
                            entryTemp.setDateFrom(entry.getDateFrom());
                            entryTemp.setDateTo(entry.getDateTo());
                            entryTemp.setHolidayName(entry.getHolidayName());
//                            entry.setSchoolId(sid);//封装进schoolId
//                            System.out.println(new ObjectId());
//                            entry.setID(new ObjectId());//封装进新主键Id
                            schoolControlDBObjectList.add(entryTemp.getBaseEntry());
                        }
//                        schoolControlTimeDao.saveNewSchoolAddControlTime(schoolControlDBObjectList);
                    }
                    schoolControlTimeDao.saveNewSchoolAddControlTime(schoolControlDBObjectList);
                }
            }
            result = "修复成功！";
        }catch (Exception e){
            e.printStackTrace();
            result = "修复失败！";
        }
        return result;
    }
}
