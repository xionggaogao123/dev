package com.fulaan.business.service;

import com.db.backstage.PushMessageDao;
import com.db.backstage.TeacherApproveDao;
import com.db.business.*;
import com.db.excellentCourses.*;
import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.LoginLogDao;
import com.db.fcommunity.MemberDao;
import com.db.fcommunity.MineCommunityDao;
import com.db.jiaschool.HomeSchoolDao;
import com.db.jiaschool.SchoolCommunityDao;
import com.db.jiaschool.SchoolFunctionDao;
import com.db.jiaschool.SchoolPersionDao;
import com.db.user.NewVersionUserRoleDao;
import com.db.user.UserDao;
import com.db.wrongquestion.SubjectClassDao;
import com.fulaan.backstage.service.BackStageService;
import com.fulaan.business.dto.BusinessManageDTO;
import com.fulaan.controlphone.dto.CoursesBusinessDTO;
import com.fulaan.excellentCourses.dto.BackOrderDTO;
import com.fulaan.excellentCourses.dto.CoursesOrderResultDTO;
import com.fulaan.excellentCourses.dto.ExcellentCoursesDTO;
import com.fulaan.excellentCourses.dto.HourClassDTO;
import com.fulaan.excellentCourses.service.CoursesRoomService;
import com.fulaan.fgroup.service.EmService;
import com.fulaan.jiaschool.dto.HomeSchoolDTO;
import com.fulaan.picturetext.runnable.PictureRunNable;
import com.fulaan.pojo.User;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.HSSFUtils;
import com.mongodb.DBObject;
import com.pojo.backstage.LogMessageType;
import com.pojo.backstage.PushMessageEntry;
import com.pojo.backstage.TeacherApproveEntry;
import com.pojo.business.*;
import com.pojo.excellentCourses.*;
import com.pojo.fcommunity.CommunityEntry;
import com.pojo.fcommunity.FLoginLogEntry;
import com.pojo.fcommunity.MineCommunityEntry;
import com.pojo.jiaschool.HomeSchoolEntry;
import com.pojo.user.NewVersionUserRoleEntry;
import com.pojo.user.UserEntry;
import com.pojo.wrongquestion.SubjectClassEntry;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang.StringUtils;
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
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by James on 2018/1/16.
 */
@Service
public class BusinessManageService {

    private BusinessManageDao businessManageDao =new BusinessManageDao();
    private LoginLogDao loginLogDao = new LoginLogDao();
    private UserDao userDao = new UserDao();

    private UserService userService = new UserService();

    private SubjectClassDao subjectClassDao  = new SubjectClassDao();

    private BusinessRoleDao businessRoleDao = new BusinessRoleDao();

    private ExcellentCoursesDao excellentCoursesDao = new ExcellentCoursesDao();

    private ClassOrderDao classOrderDao = new ClassOrderDao();

    private CommunityDao communtiyDao = new CommunityDao();

    private HourClassDao hourClassDao = new HourClassDao();
    @Autowired
    private CoursesRoomService coursesRoomService;
    @Autowired
    private BackStageService backStageService;

    private VersionOpenDao versionOpenDao = new VersionOpenDao();

    private BanningSpeakingDao banningSpeakingDao = new BanningSpeakingDao();

    private CommunitySpeakingDao communitySpeakingDao = new CommunitySpeakingDao();

    private CommunityDao communityDao = new CommunityDao();

    private PushMessageDao pushMessageDao = new PushMessageDao();

    private MemberDao memberDao = new MemberDao();

    private TeacherApproveDao teacherApproveDao = new TeacherApproveDao();

    private SchoolPersionDao schoolPersionDao = new SchoolPersionDao();

    private HomeSchoolDao homeSchoolDao = new HomeSchoolDao();
    private UserAgreementDao userAgreementDao = new UserAgreementDao();

    private CoursesRoomDao coursesRoomDao = new CoursesRoomDao();

    private AccountOrderDao accountOrderDao = new AccountOrderDao();

    private BackOrderDao backOrderDao = new BackOrderDao();

    private NewVersionUserRoleDao newVersionUserRoleDao= new NewVersionUserRoleDao();

    private CoursesBusinessDao coursesBusinessDao = new CoursesBusinessDao();

    private CoursesOrderResultDao coursesOrderResultDao = new CoursesOrderResultDao();

    private SchoolCommunityDao schoolCommunityDao  = new SchoolCommunityDao();


    private MineCommunityDao mineCommunityDao = new MineCommunityDao();

    private SchoolFunctionDao schoolFunctionDao = new SchoolFunctionDao();

    @Autowired
    private EmService emService;

    //登陆生成
    public void getLoginInfo(ObjectId userId,int type ){
        PictureRunNable.addBusinessManageEntry(userId,type);
    }


    //自检
    public void checkBusinessManage(){
        PictureRunNable.updateBusinessManageEntry();
    }
    public void checkBusinessManage2(){
        PictureRunNable.updateBusinessManageEntry();
    }
    public static void main(String[] args){
        PictureRunNable.updateBusinessManageEntry();
    }
    //脚本
    public void createBusinessManage(){
        List<FLoginLogEntry> fLoginLogEntryList = loginLogDao.getUserResultList();
        Map<String,FLoginLogEntry> map = new HashMap<String, FLoginLogEntry>();
        Set<String> stringList = new HashSet<String>();
        if(fLoginLogEntryList.size()>0){
            for(FLoginLogEntry entry:fLoginLogEntryList){
                map.put(entry.getUserName(),entry);
                stringList.add(entry.getUserName());
            }
        }
        List<UserEntry> userEntries =   userDao.searchUsersByUserNames(stringList);
        if(userEntries.size()>0){
            for(UserEntry userEntry :userEntries){
                FLoginLogEntry fLoginLogEntry = map.get(userEntry.getUserName());
                int type = 1;
                if(fLoginLogEntry.getLoginPf().equals("Android")){
                    type =2;
                }else if(fLoginLogEntry.getLoginPf().equals("IOS")){
                    type =3;
                }else if(fLoginLogEntry.getLoginPf().equals("PC")){
                    type =1;
                }
                PictureRunNable.addFeiBusinessManageEntry(userEntry.getID(), type);
            }
        }
        //PictureRunNable.createBusinessManage();
    }

    //添加在线 时间
    public void addDuringTime(ObjectId userId,String userName){
        FLoginLogEntry fLoginLogEntry = loginLogDao.getEntry(userName);
        long time = System.currentTimeMillis();
        BusinessManageEntry businessManageEntry = businessManageDao.getEntry(userId);
        long durTime = time-fLoginLogEntry.getLoginTime();
        if(null!= businessManageEntry){
            if(durTime>0){
                businessManageEntry.setOnlineTime(businessManageEntry.getOnlineTime()+durTime);
            }
        }
    }

    //获得列表
    public Map<String,Object> getList(String jiaId,int page,int pageSize){
        String str = "";

        if(jiaId != null && !jiaId.equals("")){
            UserEntry userEntry = userDao.getJiaUserEntry(jiaId);
            if(userEntry !=null){
                str = userEntry.getID().toString();
            }
        }
        Map<String,Object> map = new HashMap<String, Object>();
        List<BusinessManageEntry> entries =  businessManageDao.getPageList(str,page, pageSize);
        List<BusinessManageDTO> list = new ArrayList<BusinessManageDTO>();
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        List<SubjectClassEntry> entries1 =  subjectClassDao.getList();
        Map<ObjectId,SubjectClassEntry> map1 = new HashMap<ObjectId, SubjectClassEntry>();
        for(SubjectClassEntry entry : entries1){
            map1.put(entry.getID(),entry);
        }
        if(entries.size()>0){
            for(BusinessManageEntry entry:entries){
                BusinessManageDTO dto = new BusinessManageDTO(entry);
                List<String> sstr = new ArrayList<String>();
                if(entry.getSubjectIdList()!=null){
                    for(ObjectId oid : entry.getSubjectIdList()){
                        SubjectClassEntry subjectClassEntry =  map1.get(oid);
                        if(subjectClassEntry!=null){
                            sstr.add(subjectClassEntry.getName()+" ");
                        }
                    }
                }
                dto.setSubjectIdList(sstr);
                list.add(dto);
                userIds.add(entry.getUserId());
            }
        }
        Map<ObjectId,UserEntry> userEntryMap=userService.getUserEntryMap(userIds, Constant.FIELDS);
        if(list.size()>0){
            for(BusinessManageDTO dto:list){
                UserEntry userEntry=userEntryMap.get(new ObjectId(dto.getUserId()));
                if(null!=userEntry){
                    dto.setAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(), userEntry.getSex()));
                    dto.setUserName(userEntry.getUserName());
                }
            }
        }
        map.put("list",list);
        int count = businessManageDao.getReviewListCount();
        map.put("count",count);
        return map;
    }



    public BusinessRoleEntry getUserBusinessRoleEntry(ObjectId userId){
         BusinessRoleEntry businessRoleEntry = businessRoleDao.getEntry(userId);
         return businessRoleEntry;
    }

   /* public List<Map<String,Object>>  getRoleList(){
        List<Map<String,Object>> mapList = new ArrayList<Map<String, Object>>();
        List<BusinessRoleEntry> list =  businessRoleDao.getPageList();




        return mapList;
    }*/


    public List<User>  selectUser(String keyword){
        List<User> userList =new ArrayList<User>();
        //用户名查找
        List<UserEntry> userEntries = userDao.findEntryByName("nm", keyword);
        //昵称查找
        List<UserEntry> userEntries2 = userDao.findEntryByName("nnm", keyword);
        //家校美id查找
        UserEntry entry= userDao.findByPersonalID(keyword);
        userEntries.addAll(userEntries2);
        if(entry!=null){
            userEntries.add(entry);
        }
        List<ObjectId> oisd = new ArrayList<ObjectId>();
        for(UserEntry userEntry: userEntries){
            //String userName,String nickName,String userId,String avator,int sex,String time
            if(!oisd.contains(userEntry.getID())){
                User user = new User(userEntry.getUserName(),userEntry.getNickName(),userEntry.getID().toString(),AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(), userEntry.getSex()),userEntry.getSex(),userEntry.getGenerateUserCode(), userEntry.getMobileNumber());
                userList.add(user);
                oisd.add(userEntry.getID());
            }

        }
        oisd =null;
        return userList;
    }


    //查询所有开的课程
    public Map<String,Object> selectAllCourses(int page,int pageSize,String name,String subjectId){
        Map<String,Object> map = new HashMap<String, Object>();
        List<ExcellentCoursesEntry> excellentCoursesEntries = excellentCoursesDao.getAllWebEntryList(subjectId, name, page, pageSize);
        int count = excellentCoursesDao.selectAllWebEntryList(subjectId,name);
        List<ExcellentCoursesDTO> dtos = new ArrayList<ExcellentCoursesDTO>();
        long current = System.currentTimeMillis();
        List<ObjectId>  oid = new ArrayList<ObjectId>();
        for(ExcellentCoursesEntry excellentCoursesEntry:excellentCoursesEntries){
            ExcellentCoursesDTO dto = new ExcellentCoursesDTO(excellentCoursesEntry);
            oid.add(excellentCoursesEntry.getID());
            dtos.add(dto);
            if(excellentCoursesEntry.getEndTime()<current){
                dto.setType(2);
            }
        }
        Map<ObjectId,String> map2 = coursesRoomDao.getPageList(oid);
        for(ExcellentCoursesDTO dt : dtos){
            String roomId = map2.get(new ObjectId(dt.getId()));
            dt.setCommunitName(roomId);
        }
        map.put("count",count);
        map.put("list",dtos);
        return map;
    }

    public Map<String,Object> selectAllOrderCourses(int page,int pageSize,String name,String subjectId){
        Map<String,Object> map = new HashMap<String, Object>();
        List<ExcellentCoursesEntry> excellentCoursesEntries = excellentCoursesDao.getAllWebEntryList(subjectId, name, page, pageSize);
        int count = excellentCoursesDao.selectAllWebEntryList(subjectId,name);
        List<ExcellentCoursesDTO> dtos = new ArrayList<ExcellentCoursesDTO>();
        long current = System.currentTimeMillis();
        List<ObjectId>  oid = new ArrayList<ObjectId>();
        for(ExcellentCoursesEntry excellentCoursesEntry:excellentCoursesEntries){
            ExcellentCoursesDTO dto = new ExcellentCoursesDTO(excellentCoursesEntry);
            oid.add(excellentCoursesEntry.getID());
            dtos.add(dto);
            if(excellentCoursesEntry.getEndTime()<current){
                dto.setType(2);
            }
        }
        map.put("count",count);
        map.put("list",dtos);
        return map;
    }

    public Map<String,Object> selectCoursesDetails(ObjectId id){
        Map<String,Object> map = new HashMap<String, Object>();
        ExcellentCoursesEntry excellentCoursesEntry=excellentCoursesDao.getEntry(id);
        if(excellentCoursesEntry==null){
            return map;
        }
        ExcellentCoursesDTO dto2 = new ExcellentCoursesDTO(excellentCoursesEntry);
        List<CommunityEntry> communityEntries = communtiyDao.findByObjectIds(excellentCoursesEntry.getCommunityIdList());
        String stringList = "";
        for(CommunityEntry communityEntry:communityEntries){
            stringList = stringList +communityEntry.getCommunityName()+ "、";
        }
        if(stringList.equals("")){
            dto2.setCommunitName("无");
        }else{
            String name2 = stringList.substring(0, stringList.length()-1);
            dto2.setCommunitName(name2);
        }

        map.put("dto",dto2);
        //课时相关
        List<HourClassEntry> hourClassEntries = hourClassDao.getEntryList(id);
        CoursesRoomEntry coursesRoomEntry = coursesRoomDao.getEntry(id);
        List<HourClassDTO> hourClassDTOs = new ArrayList<HourClassDTO>();
        for(HourClassEntry hourClassEntry : hourClassEntries){
            HourClassDTO houc = new HourClassDTO(hourClassEntry);
            if(hourClassEntry.getRoomId()!=null&& !hourClassEntry.getRoomId().equals("")){
                houc.setRoomId(hourClassEntry.getRoomId());
            }else{
                if(coursesRoomEntry!=null){
                    houc.setRoomId(coursesRoomEntry.getRoomId());
                }

            }
            hourClassDTOs.add(houc);

        }
        map.put("hourList",hourClassDTOs);
        //用户订单查询
        List<Map<String,Object>> listMap = new ArrayList<Map<String, Object>>();
     /*   List<ClassOrderEntry> classOrderEntries = classOrderDao.getCoursesUserList(id);
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        for(ClassOrderEntry classOrderEntry:classOrderEntries){
            userIds.add(classOrderEntry.getUserId());
        }
        List<UserEntry> userEntries = userDao.getUserEntryList(userIds, Constant.FIELDS);

        int order = 0;
        String order2 = "";
        for(UserEntry userEntry:userEntries){
            Map<String,Object> userMap = new HashMap<String, Object>();
            double score = 0.00;
            int number = 0;
            order++;
            long createTime= 0l;
            for(ClassOrderEntry classOrderEntry:classOrderEntries){
                if(userEntry.getID().equals(classOrderEntry.getUserId())){
                    score = sum(score,classOrderEntry.getPrice());
                    number++;
                    createTime = classOrderEntry.getCreateTime();
                    order2 =classOrderEntry.getOrderId();
                }
            }
            String ctm = "";
            if(createTime!=0l){
                ctm = DateTimeUtils.getLongToStrTimeTwo(createTime);
            }
            userMap.put("userId",userEntry.getID().toString());
            userMap.put("order",order);
            userMap.put("avatar",AvatarUtils.getAvatar(userEntry.getAvatar(),userEntry.getRole(),userEntry.getSex()));
            userMap.put("price","¥ "+score);
            userMap.put("jiaId",userEntry.getGenerateUserCode());
            userMap.put("number",number);
            userMap.put("createTime",ctm);
            String name = StringUtils.isNotEmpty(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
            userMap.put("userName",name);
            userMap.put("orderId",order2);
            listMap.add(userMap);
        }*/
        /*for(ClassOrderEntry classOrderEntry : classOrderEntries){
            ClassOrderDTO dto = new ClassOrderDTO(classOrderEntry);
            UserEntry userEntry = mainUserEntryMap.get(classOrderEntry.getUserId());
            String name = StringUtils.isNotEmpty(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
            dto.setUserName(name);
            classOrderDTOs.add(dto);
        }*/
        map.put("userList",listMap);

        return map;
    }

    public Map<String,Object> selectCoursesSimpleDetails(ObjectId id){
        Map<String,Object> map = new HashMap<String, Object>();
        ExcellentCoursesEntry excellentCoursesEntry=excellentCoursesDao.getEntry(id);
        if(excellentCoursesEntry==null){
            return map;
        }
        ExcellentCoursesDTO dto2 = new ExcellentCoursesDTO(excellentCoursesEntry);
        List<CommunityEntry> communityEntries = communtiyDao.findByObjectIds(excellentCoursesEntry.getCommunityIdList());
        String stringList = "";
        for(CommunityEntry communityEntry:communityEntries){
            stringList = stringList +communityEntry.getCommunityName()+ "、";
        }
        if(stringList.equals("")){
            dto2.setCommunitName("无");
        }else{
            String name2 = stringList.substring(0, stringList.length()-1);
            dto2.setCommunitName(name2);
        }

        map.put("dto",dto2);
        //课时相关
        List<HourClassEntry> hourClassEntries = hourClassDao.getEntryList(id);
        List<HourClassDTO> hourClassDTOs = new ArrayList<HourClassDTO>();
        for(HourClassEntry hourClassEntry : hourClassEntries){
            hourClassDTOs.add(new HourClassDTO(hourClassEntry));
        }
        map.put("hourList",hourClassDTOs);
        return map;
    }

    public Map<String,Object> selectOrderPageList(ObjectId id,int page,int pageSize){
        Map<String,Object> map = new HashMap<String, Object>();
        //用户订单查询
        List<ClassOrderEntry> classOrderEntries = classOrderDao.getCoursesUserList(id);
        Set<ObjectId> userIds = new HashSet<ObjectId>();
        List<ObjectId> ids = new ArrayList<ObjectId>();
        List<ClassOrderEntry> entries = new ArrayList<ClassOrderEntry>();
        int start = (page-1)*pageSize +1;
        int end  = page*pageSize;
        //自主分页
        for(ClassOrderEntry classOrderEntry:classOrderEntries){
            userIds.add(classOrderEntry.getUserId());
        }
        int index = 0;
        for(ObjectId i : userIds){
            index++;
            if(start<=index && index <= end){
                ids.add(i);
            }
        }
        for(ClassOrderEntry classOrderEntry:classOrderEntries){
            if(ids.contains(classOrderEntry.getUserId())){
                entries.add(classOrderEntry);
            }
        }
        int count = userIds.size();
        map.put("count",count);
        List<UserEntry> userEntries = userDao.getUserEntryList(ids, Constant.FIELDS);
        Map<ObjectId,Integer> roleMap = newVersionUserRoleDao.getUserRoleMap(ids);
        List<Map<String,Object>> listMap = new ArrayList<Map<String, Object>>();
        int order = 0;
        String order2 = "";
        for(UserEntry userEntry:userEntries){
            Map<String,Object> userMap = new HashMap<String, Object>();
            double score = 0.00;
            int number = 0;
            order++;
            long createTime= 0l;
            int role = 0;
            int function = 3;
            int money = 1;
            for(ClassOrderEntry classOrderEntry:entries){
                if(userEntry.getID().equals(classOrderEntry.getUserId())){
                    score = sum(score,classOrderEntry.getPrice());
                    number++;
                    if(number==1){
                        createTime = classOrderEntry.getCreateTime();
                        order2 =classOrderEntry.getOrderId();
                        role = classOrderEntry.getRole();
                        function = classOrderEntry.getFunction();
                        money = classOrderEntry.getMoney();
                    }
                }
            }
            String ctm = "";
            if(createTime!=0l){
                ctm = DateTimeUtils.getLongToStrTimeTwo(createTime);
            }
            userMap.put("userId",userEntry.getID().toString());
            userMap.put("order",order);
            userMap.put("avatar",AvatarUtils.getAvatar(userEntry.getAvatar(),userEntry.getRole(),userEntry.getSex()));
            userMap.put("price","¥ "+score);
            userMap.put("newPrice",score);
            userMap.put("jiaId",userEntry.getGenerateUserCode());
            userMap.put("number",number);
            userMap.put("createTime",ctm);
            String name = StringUtils.isNotEmpty(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
            userMap.put("userName",name);
            if(order2.equals("")){
                if(function==3){//后台
                    if(money==1){//现金
                        userMap.put("orderId","后台付费");
                        userMap.put("newPrice",1);
                    }else{
                        userMap.put("orderId","后台免费");
                        userMap.put("newPrice",0);
                    }
                }else if(function==0){//自定义购买
                    userMap.put("orderId","");
                    userMap.put("newPrice",1);

                }

            }else{
                userMap.put("orderId",order2);
            }
            if(role==0) {
                Integer i = roleMap.get(userEntry.getID());
                if(i!=null){
                    userMap.put("role",i);
                }else{
                    userMap.put("role",0);
                }
            }else{
               userMap.put("role",role);
            }
            userMap.put("function",function);
            listMap.add(userMap);
        }
        map.put("userList",listMap);
        return map;
    }

    /**
     * 批量导出数据
     * @param id
     * @return
     */
    public void exportTemplate(HttpServletRequest request, HttpServletResponse response,ObjectId id) {
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        if(excellentCoursesEntry==null){
            return;
        }
        //用户订单查询
        List<ClassOrderEntry> classOrderEntries = classOrderDao.getCoursesUserList(id);
        Set<ObjectId> userIds = new HashSet<ObjectId>();
        List<ObjectId> ids = new ArrayList<ObjectId>();
        List<ClassOrderEntry> entries = new ArrayList<ClassOrderEntry>();
        //自主分页
        for(ClassOrderEntry classOrderEntry:classOrderEntries){
            ids.add(classOrderEntry.getUserId());
            userIds.add(classOrderEntry.getUserId());
            entries.add(classOrderEntry);
        }
        int count = userIds.size();
        List<UserEntry> userEntries = userDao.getUserEntryList(ids, Constant.FIELDS);
        Map<ObjectId,Integer> roleMap = newVersionUserRoleDao.getUserRoleMap(ids);
        String order2 = "";

        String sheetName = "课程《"+excellentCoursesEntry.getTitle()+"》订单列表";
        
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet;
        try {
            sheet = wb.createSheet(sheetName);
        } catch (Exception e) {
            // TODO: handle exception
            sheet = wb.createSheet("课程订单列表");
        }
        
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
        HSSFRow rowZero = sheet.createRow(0);
        HSSFCell cellZero = rowZero.createCell(0);
        cellZero.setCellValue("共计"+count+"份订单");

        HSSFRow row = sheet.createRow(1);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("序号");

        cell = row.createCell(1);
        cell.setCellValue("姓名");

        cell = row.createCell(2);
        cell.setCellValue("身份");

        cell = row.createCell(3);
        cell.setCellValue("家校美ID");

        cell = row.createCell(4);
        cell.setCellValue("课节");

        cell = row.createCell(5);
        cell.setCellValue("价格");

        cell = row.createCell(6);
        cell.setCellValue("订单号");

        cell = row.createCell(7);
        cell.setCellValue("订单来源");

        cell = row.createCell(8);
        cell.setCellValue("购买时间");


        int rowLine = 2;

        HSSFRow rowItem;
        HSSFCell cellItem;
        int index = 0;
        for(UserEntry userEntry:userEntries){
            index++;
            rowItem = sheet.createRow(rowLine);

            cellItem = rowItem.createCell(0);
            cellItem.setCellValue(index+"");

            double score = 0.00;
            int number = 0;
            long createTime= 0l;
            int role = 0;
            int function = 3;
            int money = 1;
            for(ClassOrderEntry classOrderEntry:entries){
                if(userEntry.getID().equals(classOrderEntry.getUserId())){
                    score = sum(score,classOrderEntry.getPrice());
                    number++;
                    if(number==1){
                        createTime = classOrderEntry.getCreateTime();
                        order2 =classOrderEntry.getOrderId();
                        role = classOrderEntry.getRole();
                        function = classOrderEntry.getFunction();
                        money = classOrderEntry.getMoney();
                    }
                }
            }
            String ctm = "";
            if(createTime!=0l){
                ctm = DateTimeUtils.getLongToStrTimeTwo(createTime);
            }
            String name = StringUtils.isNotEmpty(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();

            cellItem = rowItem.createCell(1);
            cellItem.setCellValue(name);

            cellItem = rowItem.createCell(2);
            if(role==0) {
                Integer i = roleMap.get(userEntry.getID());
                if(i!=null){
                    role=i;
                }else{
                    role=0;
                }
            }
            if(role==0 || role==7){
                cellItem.setCellValue("家长");
            }else if(role==3 || role==6){
                cellItem.setCellValue("老师");
            }else if(role==8){
                cellItem.setCellValue("助教");
            }else if(role==9){
                cellItem.setCellValue("内务");
            }else{
                cellItem.setCellValue("学生");
            }

            cellItem = rowItem.createCell(3);
            cellItem.setCellValue(userEntry.getGenerateUserCode());

            cellItem = rowItem.createCell(4);
            cellItem.setCellValue(number+"");

            cellItem = rowItem.createCell(5);
            cellItem.setCellValue("¥ "+score);

            cellItem = rowItem.createCell(6);
            int newPrice = 0;
            if(order2.equals("")){
                if(function==3){//后台
                    if(money==1){//现金
                        cellItem.setCellValue("后台付费");
                        newPrice = 1;
                    }else{
                        cellItem.setCellValue("后台免费");
                        newPrice = 0;
                    }
                }else if(function==0){//自定义购买
                    cellItem.setCellValue("");
                }
            } else {
                cellItem.setCellValue(order2);
            }
            /*if(order2.equals("")){
                if(money==1){
                    cellItem.setCellValue("后台付费");
                }else{
                    cellItem.setCellValue("后台免费");
                }
            }else{
                cellItem.setCellValue(order2);
            }*/

            cellItem = rowItem.createCell(7);
            /*	<span v-if="scope.row.function==3 && scope.row.newPrice==0" width="40" height="40" >免费</span>
						<span v-if="scope.row.function==3 && scope.row.newPrice==1" width="40" height="40" >现金</span>
						<span v-if="scope.row.function==0 && scope.row.newPrice==1" width="40" height="40" >免费</span>
						<span v-if="scope.row.function==1" width="40" height="40" >支付宝</span>
						<span v-if="scope.row.function==2" width="40" height="40" >微信</span>*/
            if(function==1){
                cellItem.setCellValue("支付宝");
            }else if(function==2){
                cellItem.setCellValue("微信");
            }else if(function==0){
                cellItem.setCellValue("免费");
            }else if(function==3){
                if(newPrice==0){
                    cellItem.setCellValue("免费");
                }else{
                    cellItem.setCellValue("现金");
                }
            }else{
                cellItem.setCellValue("免费");
            }

            cellItem = rowItem.createCell(8);
            cellItem.setCellValue(ctm);
            rowLine++;
        }
        String fileName = sheetName + ".xls";
        String userAgent = request.getHeader("USER-AGENT");
        HSSFUtils.exportExcel(userAgent, response, wb, fileName);
    }

    /**
     * 批量导出数据
     * @param id
     * @return
     */
    public void exportTemplate2(HttpServletRequest request, HttpServletResponse response,ObjectId id,int status) {
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        int count =backOrderDao.countTuiOrderList(id,status);
        List<BackOrderEntry> entries = backOrderDao.getTuiOrderList(id,status, 1, count);

        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        for(BackOrderEntry backOrderEntry : entries){
            objectIdList.add(backOrderEntry.getUserId());
        }

        Map<ObjectId, UserEntry> mainUserEntryMap = userDao.getUserEntryMap(objectIdList, Constant.FIELDS);

        String sheetName = "";
        if(status==1){//删除
            sheetName = "课程《"+excellentCoursesEntry.getTitle()+"》删除订单列表";
        }else{//退课
            sheetName = "课程《"+excellentCoursesEntry.getTitle()+"》退课订单列表";
        }

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet;
        try {
            sheet = wb.createSheet(sheetName);
        } catch (Exception e) {
            // TODO: handle exception
            if(status==1){//删除
                sheet = wb.createSheet("课程删除订单列表");
            }else{//退课
                sheet = wb.createSheet("课程退课订单列表");
            }
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
        HSSFRow rowZero = sheet.createRow(0);
        HSSFCell cellZero = rowZero.createCell(0);
        cellZero.setCellValue("共计"+count+"份订单");

        HSSFRow row = sheet.createRow(1);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("序号");

        cell = row.createCell(1);
        cell.setCellValue("姓名");

        cell = row.createCell(2);
        cell.setCellValue("家校美ID");

        cell = row.createCell(3);
        if(status==1){
            cell.setCellValue("课节数量");
        }else{
            cell.setCellValue("退课数量");
        }

        cell = row.createCell(4);
        if(status==1){
            cell.setCellValue("购买价格");
        }else{
            cell.setCellValue("退款金额");
        }

        cell = row.createCell(5);
        cell.setCellValue("订单号");

        cell = row.createCell(6);
        cell.setCellValue("订单来源");

        cell = row.createCell(7);
        if(status==1){
            cell.setCellValue("删除时间");
        }else{
            cell.setCellValue("退课时间");
        }

        int rowLine = 2;

        HSSFRow rowItem;
        HSSFCell cellItem;
        int index = 0;

        for(BackOrderEntry backOrderEntry:entries){
            index++;
            rowItem = sheet.createRow(rowLine);

            cellItem = rowItem.createCell(0);
            cellItem.setCellValue(index+"");

            BackOrderDTO dto = new BackOrderDTO(backOrderEntry);
            UserEntry userEntry = mainUserEntryMap.get(backOrderEntry.getUserId());
            if(userEntry!=null){
                String name = StringUtils.isNotBlank(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();

                cellItem = rowItem.createCell(1);
                cellItem.setCellValue(name);

                cellItem = rowItem.createCell(2);
                cellItem.setCellValue(userEntry.getGenerateUserCode());

            }else{
                cellItem = rowItem.createCell(1);
                cellItem.setCellValue("");

                cellItem = rowItem.createCell(2);
                cellItem.setCellValue("");
            }
            cellItem = rowItem.createCell(3);
            cellItem.setCellValue(dto.getClassIdList().size()+"");

            cellItem = rowItem.createCell(4);
            cellItem.setCellValue("¥ "+dto.getPrice());

            cellItem = rowItem.createCell(5);
            if(dto.getOrderType()==0){
                cellItem.setCellValue("");
            }else{
                cellItem.setCellValue(dto.getOrderId());
            }

            /*<span v-if="scope.row.orderType==3 && scope.row.money==0" width="40" height="40" >免费</span>
						<span v-if="scope.row.orderType==3 && scope.row.money==1" width="40" height="40" >现金</span>
						<span v-if="scope.row.orderType==0" width="40" height="40" >免费</span>
						<span v-if="scope.row.orderType==1" width="40" height="40" >支付宝</span>
						<span v-if="scope.row.orderType==2" width="40" height="40" >微信</span>*/
            cellItem = rowItem.createCell(6);
            if(dto.getOrderType()==1){
                cellItem.setCellValue("支付宝");
            }else if(dto.getOrderType()==2){
                cellItem.setCellValue("微信");
            }else if(dto.getOrderType()==0){
                cellItem.setCellValue("免费");
            }else if(dto.getOrderType()==3){
                if(dto.getMoney()==0){
                    cellItem.setCellValue("免费");
                }else if(dto.getMoney()==1){
                    cellItem.setCellValue("现金");
                }else{
                    cellItem.setCellValue("免费");
                }
            }else{
                cellItem.setCellValue("免费");
            }

            cellItem = rowItem.createCell(7);
            cellItem.setCellValue(dto.getCreateTime());

            rowLine++;
        }
        String fileName = sheetName + ".xls";
        String userAgent = request.getHeader("USER-AGENT");
        HSSFUtils.exportExcel(userAgent, response, wb, fileName);
    }

    public Map<String,Object> selectCoursesOrderDetails(ObjectId id,ObjectId userId){
        Map<String,Object> map = new HashMap<String, Object>();
        //用户订单查询
        Map<ObjectId,ClassOrderEntry> entryMap = classOrderDao.getEntryList(userId, id);
        List<HourClassEntry> hourClassEntries =  hourClassDao.getEntryList(id);
        long current = System.currentTimeMillis();
        List<String>  stringList = new ArrayList<String>();
        List<Map<String,Object>> mapList  = new ArrayList<Map<String, Object>>();
        for(HourClassEntry hourClassEntry:hourClassEntries){
            ClassOrderEntry classOrderEntry =  entryMap.get(hourClassEntry.getID());
            long endTime = hourClassEntry.getStartTime()+hourClassEntry.getCurrentTime();
            HourClassDTO dto = new HourClassDTO(hourClassEntry);
            Map<String,Object> obMap = new HashMap<String, Object>();
            obMap.put("id",hourClassEntry.getID().toString());
            obMap.put("content",hourClassEntry.getContent());
            obMap.put("startTime",dto.getStartTime());
            obMap.put("classNewPrice",hourClassEntry.getClassNewPrice());
            obMap.put("order",hourClassEntry.getOrder());
            obMap.put("level","1");
            if(endTime<current){
                obMap.put("status","已 过 期 ");//1 已过期
                obMap.put("orderId","已 过 期 ");
                if(classOrderEntry!=null){
                    if(!classOrderEntry.getOrderId().equals("")){
                        obMap.put("orderId",classOrderEntry.getOrderId());
                        stringList.add(classOrderEntry.getOrderId());
                    }
                }
            }else{
                obMap.put("status","未 购 买 ");//0 未购买
                obMap.put("orderId","未 购 买");
                if(classOrderEntry!=null){
                    obMap.put("status","可 退 课 ");//已购买
                    if(!classOrderEntry.getOrderId().equals("")){
                        obMap.put("orderId",classOrderEntry.getOrderId());
                        stringList.add(classOrderEntry.getOrderId());
                    }else{
                        obMap.put("orderId","后台添加");
                    }
                    obMap.put("level","1");
                }
            }
            mapList.add(obMap);
        }
        map.put("list",mapList);
        String str = accountOrderDao.getEntryListByOrderId(stringList);
        if(str.contains("alipay_sdk=")){
            map.put("type","支 付 宝");
        }else if(str.contains("wx")){
            map.put("type","微    信");
        }else{
            map.put("type","后    台");
        }
        return map;
    }
    //退课查询
    public Map<String,Object> selectCoursesTuiOrder(ObjectId id,ObjectId userId,String ids){
        Map<String,Object> map = new HashMap<String, Object>();
        //用户订单查询
        Map<ObjectId,ClassOrderEntry> entryMap = classOrderDao.getEntryList(userId, id);
        List<HourClassEntry> hourClassEntries =  hourClassDao.getEntryList(id);
        long current = System.currentTimeMillis();
        int  index = 0;
        double price = 0;
        for(HourClassEntry hourClassEntry:hourClassEntries){
            ClassOrderEntry classOrderEntry =  entryMap.get(hourClassEntry.getID());
            long endTime = hourClassEntry.getStartTime()+hourClassEntry.getCurrentTime();
           /* if(endTime<current){

            }else{*/
                if(classOrderEntry!=null && ids.contains(hourClassEntry.getID().toString())){
                    index++;
                    price = sum(price,classOrderEntry.getPrice());
                }
//            }

        }
       // map.put("list",);
        map.put("count",index);
        map.put("price",price);
        return map;
    }

    public Map<String,Object> selectOrderDeleteList(ObjectId id,int status,int page,int pageSize){
        Map<String,Object> map = new HashMap<String, Object>();
        List<BackOrderEntry> entries = backOrderDao.getTuiOrderList(id,status, page, pageSize);
        int count =backOrderDao.countTuiOrderList(id,status);
        List<BackOrderDTO> dtos = new ArrayList<BackOrderDTO>();
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        for(BackOrderEntry backOrderEntry : entries){
            objectIdList.add(backOrderEntry.getUserId());
        }
        Map<ObjectId, UserEntry> mainUserEntryMap = userDao.getUserEntryMap(objectIdList, Constant.FIELDS);
        for(BackOrderEntry backOrderEntry:entries){
            BackOrderDTO dto = new BackOrderDTO(backOrderEntry);
            UserEntry userEntry = mainUserEntryMap.get(backOrderEntry.getUserId());
            if(userEntry!=null){
                String name = StringUtils.isNotBlank(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
                dto.setUserName(name);
                dto.setJiaId(userEntry.getGenerateUserCode());
                dto.setCount(dto.getClassIdList().size());
            }
            if(dto.getOrderType()==0){
                dto.setOrderId("");
            }
            dto.setNewPrice("¥ "+dto.getPrice());
            dtos.add(dto);
        }
        map.put("count",count);
        map.put("list",dtos);
        return map;
    }


    //退课
    public void tuiOrder(ObjectId id,ObjectId userId,String ids,ObjectId uid){
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        if(excellentCoursesEntry==null){
            return;
        }
        Map<ObjectId,ClassOrderEntry> entryMap = classOrderDao.getEntryList(userId, id);
        List<HourClassEntry> hourClassEntries =  hourClassDao.getEntryList(id);
        long current = System.currentTimeMillis();
        List<ObjectId>  objectIdList = new ArrayList<ObjectId>();
        List<ObjectId> hourIds = new ArrayList<ObjectId>();
        double price = 0;
        String orderId = "";
        int orderType = 0;
        int status = 2;
        int role = 0;
        int money = 1;
        for(HourClassEntry hourClassEntry:hourClassEntries){
            ClassOrderEntry classOrderEntry =  entryMap.get(hourClassEntry.getID());
           // long endTime = hourClassEntry.getStartTime()+hourClassEntry.getCurrentTime();
           /* if(endTime<current){

            }else{*/
                if(classOrderEntry!=null && ids.contains(hourClassEntry.getID().toString())){
                    hourIds.add(hourClassEntry.getID());
                    objectIdList.add(classOrderEntry.getID());
                    price = sum(price,classOrderEntry.getPrice());
                    orderId= classOrderEntry.getOrderId();
                    orderType = classOrderEntry.getFunction();
                    role = classOrderEntry.getRole();
                    money = classOrderEntry.getMoney();
                }
//            }
        }
        if(role==0){
            NewVersionUserRoleEntry newVersionUserRoleEntry = newVersionUserRoleDao.getEntry(userId);
            role = newVersionUserRoleEntry.getNewRole();
        }
        String or = orderId;
        if(orderId.equals("")){//后台
            if(orderType==3){
                if(money==1){//现金
                    orderId = "后台付费";
                }else{
                    orderId = "后台免费";
                }
            }else if(orderType==0){//自定义购买
                orderId = "";
            }

        }
        //1. 添加删除订单记录
        BackOrderEntry backOrderEntry = new BackOrderEntry(id,userId,uid,hourIds,objectIdList,price,orderId,orderType,status,role,money);
        backOrderDao.addEntry(backOrderEntry);
        // 退课
        //订单置为退款中
        classOrderDao.updateEntry(objectIdList);
        backStageService.addLogMessage(id.toString(), "退课：" + excellentCoursesEntry.getTitle() + ",用户ID:" + userId.toString(), LogMessageType.courses.getDes(), uid.toString());
        //用户订单查询
        Set<ObjectId> userIds = classOrderDao.getUserIdEntry(id);
        //List<UserEntry> userEntries = userDao.getUserEntryList(userIds, Constant.FIELDS);
        excellentCoursesEntry.setStudentNumber(userIds.size());
        excellentCoursesDao.addEntry(excellentCoursesEntry);
        //退课支出

        if(or.equals("")){
            if(orderType==3){
                if(money==1){
                    or = "后台付费";
                }else{
                    or = "后台免费";
                    price = 0;
                }
            }else if(orderType==0){
                or = "免费购买";
            }
        }
        this.addCoursesOrderEntry(userId,id,Constant.TWO,hourIds,price,or,orderType);
    }

    public String daoOrder(ObjectId id,String roomId){
        CoursesRoomEntry coursesRoomEntry = coursesRoomDao.getRoomEntry(roomId);
        if(coursesRoomEntry!=null){
            ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(coursesRoomEntry.getContactId());
            ExcellentCoursesEntry excellentCoursesEntry1 = excellentCoursesDao.getEntry(id);
            if(excellentCoursesEntry!=null){
                long current = System.currentTimeMillis();
                //课节列表
                List<HourClassEntry> hourClassEntries  = hourClassDao.getEntryList(id);
                //已下订单用户
                Set<ObjectId> userIds = classOrderDao.getUserIdEntry(id);
                //将下订单用户
                Set<ObjectId> userIds2 = classOrderDao.getUserIdEntry(excellentCoursesEntry.getID());
                List<ClassOrderEntry> classOrderEntries1 = new ArrayList<ClassOrderEntry>();
                int num = 0;
                for(ObjectId oid:userIds2){
                    if(!userIds.contains(oid)){
                        for(HourClassEntry hourClassEntry: hourClassEntries){
                            ClassOrderEntry classOrderEntry = new ClassOrderEntry();
                            //购买  1  未购买
                            classOrderEntry.setIsBuy(1);
                            //下单
                            classOrderEntry.setType(1);
                            classOrderEntry.setCreateTime(current);
                            classOrderEntry.setContactId(id);
                            classOrderEntry.setOrderId("");
                            classOrderEntry.setIsRemove(0);
                            classOrderEntry.setParentId(hourClassEntry.getID());
                            classOrderEntry.setFunction(3);
                            classOrderEntry.setPrice(hourClassEntry.getClassNewPrice());
                            classOrderEntry.setUserId(oid);//孩子的订单
                            classOrderEntries1.add(classOrderEntry);
                            //相加
                        }
                        num++;
                    }
                }
                this.addClassEntryBatch(classOrderEntries1);
                excellentCoursesEntry1.setStudentNumber(excellentCoursesEntry1.getStudentNumber()+num);
                excellentCoursesDao.addEntry(excellentCoursesEntry1);
            }else{
                return "课程不存在";
            }
        }else{
            return "直播间id不存在";
        }
        return "添加成功";
    }

    /**
     * 批量增加课时
     * @param list
     */
    public void addClassEntryBatch(List<ClassOrderEntry> list) {
        List<DBObject> dbList = new ArrayList<DBObject>();
        for (int i = 0; list != null && i < list.size(); i++) {
            ClassOrderEntry si = list.get(i);
            dbList.add(si.getBaseEntry());
        }
        //导入新纪录
        if(dbList.size()>0) {
            classOrderDao.addBatch(dbList);
        }
    }

    //删除
    public void  deleteCourses(ObjectId id,ObjectId userId){
        //课程相关
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        if(excellentCoursesEntry==null){
            return;
        }
        //2. 删除课程
        excellentCoursesDao.delEntry(id);
        //3. 删除课节
        hourClassDao.delEntry(id,excellentCoursesEntry.getUserId());
        backStageService.addLogMessage(id.toString(), "删除直播课堂：" + excellentCoursesEntry.getTitle(), LogMessageType.courses.getDes(), userId.toString());
    }

    public void  deleteCoursesOrder(ObjectId id,ObjectId userId,String jiaId,ObjectId uid){
        //课程相关
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        if(excellentCoursesEntry==null){
            return;
        }
       // List<ObjectId> hourClassEntries = hourClassDao.getObjectIdList(id);
        double price = 0;
        String orderId = "";
        int orderType = 0;
        int status = 1;
        int role = 0;
        int money = 1;
        List<ObjectId> orderIds = new ArrayList<ObjectId>();
        //获得要删除的订单
        List<ClassOrderEntry> classOrderEntries = classOrderDao.getAllEntryIdList(userId, id);
        int number = 0;
        Set<ObjectId> oids = new HashSet<ObjectId>();
        for(ClassOrderEntry classOrderEntry:classOrderEntries){
            price = sum(price, classOrderEntry.getPrice());
            number++;
            if(number==1){
                orderId = classOrderEntry.getOrderId();
                orderType = classOrderEntry.getFunction();
                money = classOrderEntry.getMoney();
                role = classOrderEntry.getRole();
            }
            oids.add(classOrderEntry.getParentId());
            orderIds.add(classOrderEntry.getID());

        }
        /*if(excellentCoursesEntry.getCourseType()==1){
            price= excellentCoursesEntry.getNewPrice();
        }*/
        if(role==0){
            NewVersionUserRoleEntry newVersionUserRoleEntry = newVersionUserRoleDao.getEntry(userId);
            role = newVersionUserRoleEntry.getNewRole();
        }
        if(orderId.equals("")){
            if(money==1){
                orderId = "后台付费";
            }else{
                orderId = "后台免费";
            }
        }
        //1. 添加删除订单记录
        List<ObjectId> hourList = new ArrayList<ObjectId>();
        hourList.addAll(oids);
        BackOrderEntry backOrderEntry = new BackOrderEntry(id,userId,uid,hourList,orderIds,price,orderId,orderType,status,role,money);
        backOrderDao.addEntry(backOrderEntry);
        //2. 删除订单，并添加说明
        classOrderDao.delOrderEntry(id, userId);
        backStageService.addLogMessage(id.toString(), "删除直播课堂订单：" + excellentCoursesEntry.getTitle() + ",ID:" + jiaId, LogMessageType.courses.getDes(), uid.toString());
        //3. 删除对应的每日订单
        //用户订单查询
        Set<ObjectId> userIds = classOrderDao.getUserIdEntry(id);
        //List<UserEntry> userEntries = userDao.getUserEntryList(userIds, Constant.FIELDS);
        excellentCoursesEntry.setStudentNumber(userIds.size());
        excellentCoursesDao.addEntry(excellentCoursesEntry);
        //coursesOrderResultDao.delEntry(userId,id);
    }

    /**
     * double 相加
     * @param d1
     * @param d2
     * @return
     */
    public static double sum(double d1,double d2){
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.add(bd2).doubleValue();
    }

    //审核通过
    public String finish(ObjectId id,String word,ObjectId userId){
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        if(excellentCoursesEntry==null){
            return "课程不存在";
        }
        if(word.equals("")){
            return "无设置";
        }
        String[] str = word.split(",");
        Map<String,Double> map = new HashMap<String, Double>();
        for(int i = 0;i<str.length;i++){
            String tr = str[i];
            String[] strings = tr.split("#");
            map.put(strings[0],getTwoDouble(Double.parseDouble(strings[1])));
        }
        List<HourClassEntry> hourClassEntries = hourClassDao.getEntryList(id);
        double all = 0.00;
        for(HourClassEntry hourClassEntry : hourClassEntries){
            Double price = map.get(hourClassEntry.getID().toString());
            if(price!=null){
                hourClassDao.updatePriceEntry(hourClassEntry.getID(),price);
                all = sum(all,price);
            }
            //课程提醒
            String str2 = DateTimeUtils.getLongToStrTimeTwo(hourClassEntry.getStartTime()).substring(0,11);
            long strNum = DateTimeUtils.getStrToLongTime(str2, "yyyy-MM-dd");
            sendMessage(excellentCoursesEntry.getTitle(),hourClassEntry.getID(),hourClassEntry.getStartTime(),strNum);
        }
        //课程改为进行中
        excellentCoursesEntry.setNewPrice(all);
        excellentCoursesEntry.setStatus(2);
        excellentCoursesDao.addEntry(excellentCoursesEntry);



        //创建直播间
        long start  = excellentCoursesEntry.getStartTime();
        String startTime = "";
        if(start!=0l){
            startTime = DateTimeUtils.getLongToStrTimeTwo(start);
        }
        //todo
        coursesRoomService.createCourses(excellentCoursesEntry.getTitle(),excellentCoursesEntry.getTarget(),excellentCoursesEntry.getID(),startTime);
        backStageService.addLogMessage(id.toString(), "审核直播课堂：" + excellentCoursesEntry.getTitle(), LogMessageType.courses.getDes(), userId.toString());
        return "1";
    }

    //审核通过回调接口
    public String newFinish(ObjectId id,String word,ObjectId userId){
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        if(excellentCoursesEntry==null){
            return "课程不存在";
        }
        if(word.equals("")){
            return "无设置";
        }
        String[] str = word.split(",");
        Map<String,Double> map = new HashMap<String, Double>();
        for(int i = 0;i<str.length;i++){
            String tr = str[i];
            String[] strings = tr.split("#");
            map.put(strings[0],getTwoDouble(Double.parseDouble(strings[1])));
        }
        List<HourClassEntry> hourClassEntries = hourClassDao.getEntryList(id);
        double all = 0.00;
        int minute = 0;
        for(HourClassEntry hourClassEntry : hourClassEntries){
            Double price = map.get(hourClassEntry.getID().toString());
            if(price!=null){
                hourClassDao.updatePriceEntry(hourClassEntry.getID(),price);
                all = sum(all,price);
            }
            //课程提醒
            String str2 = DateTimeUtils.getLongToStrTimeTwo(hourClassEntry.getStartTime()).substring(0,11);
            long strNum = DateTimeUtils.getStrToLongTime(str2, "yyyy-MM-dd");
            sendMessage(excellentCoursesEntry.getTitle(),hourClassEntry.getID(),hourClassEntry.getStartTime(),strNum);
            minute = hourClassEntry.getCurrentTime()/60000 - 5;
        }
        //课程改为进行中
        excellentCoursesEntry.setNewPrice(all);
        excellentCoursesEntry.setStatus(2);
        excellentCoursesDao.addEntry(excellentCoursesEntry);



        //创建直播间
        long start  = excellentCoursesEntry.getStartTime();
        String startTime = "";
        if(start!=0l){
            startTime = DateTimeUtils.getLongToStrTimeTwo(start);
        }
        //todo
        if(minute>5){

        }else{
            minute = 5;
        }
        coursesRoomService.createBackCourses(excellentCoursesEntry.getTitle(),excellentCoursesEntry.getTarget(),excellentCoursesEntry.getID(),startTime,minute);
        backStageService.addLogMessage(id.toString(), "审核直播课堂：" + excellentCoursesEntry.getTitle(), LogMessageType.courses.getDes(), userId.toString());
        return "1";
    }

    //审核通过回调接口
    public String threeFinish(ObjectId id,String word,ObjectId userId,int type){
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        if(excellentCoursesEntry==null){
            return "课程不存在";
        }
        if(word.equals("")){
            return "无设置";
        }
        String[] str = word.split(",");
        Map<String,Double> map = new HashMap<String, Double>();
        for(int i = 0;i<str.length;i++){
            String tr = str[i];
            String[] strings = tr.split("#");
            map.put(strings[0],getTwoDouble(Double.parseDouble(strings[1])));
        }
        List<HourClassEntry> hourClassEntries = hourClassDao.getEntryList(id);
        double all = 0.00;
        int minute = 0;
        for(HourClassEntry hourClassEntry : hourClassEntries){
            Double price = map.get(hourClassEntry.getID().toString());
            if(price!=null){
                hourClassDao.updatePriceEntry(hourClassEntry.getID(),price);
                all = sum(all,price);
            }
            //课程提醒
            String str2 = DateTimeUtils.getLongToStrTimeTwo(hourClassEntry.getStartTime()).substring(0,11);
            long strNum = DateTimeUtils.getStrToLongTime(str2, "yyyy-MM-dd");
            sendMessage(excellentCoursesEntry.getTitle(),hourClassEntry.getID(),hourClassEntry.getStartTime(),strNum);
            minute = hourClassEntry.getCurrentTime()/60000 - 5;
        }
        //课程改为进行中
        excellentCoursesEntry.setNewPrice(all);
        excellentCoursesEntry.setStatus(2);
        if(type==2){
            excellentCoursesEntry.setCourseType(2);
        }
        excellentCoursesDao.addEntry(excellentCoursesEntry);



        //创建直播间
        long start  = excellentCoursesEntry.getStartTime();
        String startTime = "";
        if(start!=0l){
            startTime = DateTimeUtils.getLongToStrTimeTwo(start);
        }
        //todo
        if(minute>5){

        }else{
            minute = 5;
        }
        coursesRoomService.createBackCourses(excellentCoursesEntry.getTitle(),excellentCoursesEntry.getTarget(),excellentCoursesEntry.getID(),startTime,minute);
        backStageService.addLogMessage(id.toString(), "审核直播课堂：" + excellentCoursesEntry.getTitle(), LogMessageType.courses.getDes(), userId.toString());
        return "1";
    }

    //绑定直播社群
    public void addCommunityToFinish(ObjectId id,ObjectId communityId,String emid){
        CoursesBusinessEntry coursesBusinessEntry =  coursesBusinessDao.getOneEntry(id);
        if(coursesBusinessEntry==null){

        }else{
            coursesBusinessEntry.setCommunityId(communityId);
            coursesBusinessEntry.setEmid(emid);
            coursesBusinessDao.addEntry(coursesBusinessEntry);
        }
    }


    //审核通过回调接口
    public String backFinish(ObjectId id,String word,ObjectId userId,int type){
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        if(excellentCoursesEntry==null){
            return "课程不存在";
        }
        if(word.equals("")){
            return "无设置";
        }
        String[] str = word.split(",");
        Map<String,Double> map = new HashMap<String, Double>();
        for(int i = 0;i<str.length;i++){
            String tr = str[i];
            String[] strings = tr.split("#");
            map.put(strings[0],getTwoDouble(Double.parseDouble(strings[1])));
        }
        List<HourClassEntry> hourClassEntries = hourClassDao.getEntryList(id);
        double all = 0.00;
        int minute = 0;
        for(HourClassEntry hourClassEntry : hourClassEntries){
            Double price = map.get(hourClassEntry.getID().toString());
            if(price!=null){
                hourClassDao.updatePriceEntry(hourClassEntry.getID(),price);
                all = sum(all,price);
            }
            //课程提醒
            String str2 = DateTimeUtils.getLongToStrTimeTwo(hourClassEntry.getStartTime()).substring(0,11);
            long strNum = DateTimeUtils.getStrToLongTime(str2, "yyyy-MM-dd");
            sendMessage(excellentCoursesEntry.getTitle(),hourClassEntry.getID(),hourClassEntry.getStartTime(),strNum);
            minute = hourClassEntry.getCurrentTime()/60000 - 5;
        }
        //课程改为进行中
        excellentCoursesEntry.setNewPrice(all);
        excellentCoursesEntry.setStatus(2);
        if(type==2){
            excellentCoursesEntry.setCourseType(2);
        }
        excellentCoursesDao.addEntry(excellentCoursesEntry);



        //创建直播间
        long start  = excellentCoursesEntry.getStartTime();
        String startTime = "";
        if(start!=0l){
            startTime = DateTimeUtils.getLongToStrTimeTwo(start);
        }
        //todo
        if(minute>5){

        }else{
            minute = 5;
        }
        coursesRoomService.createBackCourses(excellentCoursesEntry.getTitle(),excellentCoursesEntry.getTarget(),excellentCoursesEntry.getID(),startTime,minute);
        backStageService.addLogMessage(id.toString(), "审核直播课堂：" + excellentCoursesEntry.getTitle(), LogMessageType.courses.getDes(), userId.toString());
        return "1";
    }

    public void addCoursesBusinessEntry(CoursesBusinessDTO dto){
        int count = excellentCoursesDao.selectAllWebEntryList(null, null);
        String code = dto.getClassNumber()+Plus(count);
        dto.setClassNumber(code);
        CoursesBusinessEntry coursesBusinessEntry = dto.buildAddEntry();
        coursesBusinessDao.addEntry(coursesBusinessEntry);
     }

    public static String Plus(int i){
        i++;
        String s = "00000"+i;
        return s.substring(s.length()-6);
    }

    /*public static void main(String[] args){

    }*/

    public void refuseFinish(ObjectId id,ObjectId userId){
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        if(excellentCoursesEntry==null){
            return;
        }
        excellentCoursesDao.juEntry(id);
        backStageService.addLogMessage(id.toString(), "拒绝直播课堂：" + excellentCoursesEntry.getTitle(), LogMessageType.courses.getDes(), userId.toString());
    }

    public Map<String,Object> selectCourseBusinessList(String businessName,String province,String city,int page,int pageSize){
        Map<String,Object> map = new HashMap<String, Object>();
        List<CoursesBusinessEntry> entries = coursesBusinessDao.getAllEntryList(businessName, province, city, page, pageSize);
        int count = coursesBusinessDao.selectMyCount(businessName, province, city);
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        for(CoursesBusinessEntry coursesBusinessEntry: entries){
            objectIdList.add(coursesBusinessEntry.getContactId());
        }
        List<ExcellentCoursesEntry> excellentCoursesEntries = excellentCoursesDao.getEntryListById4(objectIdList);
        Map<ObjectId,ExcellentCoursesEntry> map1 = new HashMap<ObjectId, ExcellentCoursesEntry>();
        List<ObjectId> courseIds = new ArrayList<ObjectId>();
        for(ExcellentCoursesEntry excellentCoursesEntry:excellentCoursesEntries){
            map1.put(excellentCoursesEntry.getID(),excellentCoursesEntry);
            courseIds.add(excellentCoursesEntry.getID());
        }
        List<HourClassEntry> hourClassEntries = hourClassDao.getParentEntryList(courseIds);
        Map<ObjectId,List<HourClassEntry>>  hourMap = new HashMap<ObjectId, List<HourClassEntry>>();
        for(HourClassEntry hourClassEntry : hourClassEntries){
            List<HourClassEntry> hourClassEntries1 = hourMap.get(hourClassEntry.getParentId());
            if(hourClassEntries1==null){
                List<HourClassEntry> newHour = new ArrayList<HourClassEntry>();
                newHour.add(hourClassEntry);
                hourMap.put(hourClassEntry.getParentId(),newHour);
            }else{
                hourClassEntries1.add(hourClassEntry);
                hourMap.put(hourClassEntry.getParentId(),hourClassEntries1);
            }
        }
        List<Map<String,Object>> mapList = new ArrayList<Map<String, Object>>();
        long current = System.currentTimeMillis();
        for(CoursesBusinessEntry coursesBusinessEntry: entries){
            ExcellentCoursesEntry entry = map1.get(coursesBusinessEntry.getContactId());
            if(entry!=null){
                Map<String,Object> map2 = new HashMap<String, Object>();
                map2.put("courseId",coursesBusinessEntry.getClassNumber());
                map2.put("id",entry.getID().toString());
                map2.put("businessId",coursesBusinessEntry.getID().toString());
                map2.put("courseName",entry.getTitle());
                if(entry.getStartTime()!=0l && entry.getEndTime()!=0l){
                    String date = DateTimeUtils.getLongToStrTimeTwo(entry.getStartTime()).substring(0, 11)+" 至 "+DateTimeUtils.getLongToStrTimeTwo(entry.getEndTime()).substring(0, 11);
                    map2.put("startTime", date);
                }else{
                    map2.put("startTime","");
                }
                map2.put("count",entry.getAllClassCount());
                List<HourClassEntry> hourClassEntries2 =  hourMap.get(coursesBusinessEntry.getContactId());
                if(hourClassEntries2!=null){
                    int index = 0;
                    for(HourClassEntry hourClassEntry : hourClassEntries2){
                        long end = hourClassEntry.getStartTime()+hourClassEntry.getCurrentTime();
                        if(current>end){
                            index++;
                        }
                    }
                    map2.put("endCount",index);
                }else{
                    map2.put("endCount",0);
                }
                map2.put("studentNumber",entry.getStudentNumber());
                map2.put("price",entry.getNewPrice());
                map2.put("area",coursesBusinessEntry.getProvince() +" "+coursesBusinessEntry.getCity());
                map2.put("sellName",coursesBusinessEntry.getSellName());
                if(coursesBusinessEntry.getAssistantName()==null || coursesBusinessEntry.getAssistantName().equals("")){
                    map2.put("assistantName","无");
                }else{
                    map2.put("assistantName",coursesBusinessEntry.getAssistantName());
                }

                mapList.add(map2);
            }
        }
        map.put("list",mapList);
        map.put("count",count);
        return map;
    }

    public void updateAss(ObjectId id,String name,ObjectId userId){
        CoursesBusinessEntry coursesBusinessEntry = coursesBusinessDao.getEntry(id);
        if(coursesBusinessEntry!=null){
            coursesBusinessDao.updateEntry(id,name);
            backStageService.addLogMessage(id.toString(), "修改直播课堂助教:"+name, LogMessageType.courses.getDes(), userId.toString());
        }
    }

    /**
     * 生成推送消息
     * @return
     */
    public void sendMessage(String title,ObjectId id,long startTime,long dateTime){
        if(pushMessageDao.isNotHave(id)){
            PushMessageEntry pushMessageEntry = new PushMessageEntry(
                    "复兰课堂",
                    new ArrayList<ObjectId>(),
                    id,
                    "未上课提醒",
                    title,
                    startTime,
                    dateTime,
                    1,
                    0);
            pushMessageDao.addEntry(pushMessageEntry);
        }
    }

    public double getTwoDouble(double d){
        BigDecimal b = new BigDecimal(d);
        d = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return d;
    }

    public String getNewString(List<String> userRole){
        String str = "";
        int index = 1;
        for(String str2:userRole){
            str = str + index+"."+RoleType.getD(str2) + "  ";
            index++;
        }
        return str;
    }

    public List<Map<String,Object>> selectRoleUser(){
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        List<BusinessRoleEntry> roleEntries = businessRoleDao.getPageList();
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        for(BusinessRoleEntry  entry : roleEntries){
            userIds.add(entry.getUserId());
        }
        Map<ObjectId,UserEntry> userEntryMap=userService.getUserEntryMap(userIds, Constant.FIELDS);
        for(BusinessRoleEntry businessRoleEntry:roleEntries){
            Map<String,Object>  map = new HashMap<String, Object>();
            UserEntry userEntry = userEntryMap.get(businessRoleEntry.getUserId());
            if(userEntry!=null){
                String name = StringUtils.isNotEmpty(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
                map.put("userId",userEntry.getID().toString());
                map.put("userName",name);
                if(businessRoleEntry.getRoleType()!=null && businessRoleEntry.getRoleType().size()!=0){
                    map.put("userRole",getNewString(businessRoleEntry.getRoleType()));
                }
                list.add(map);
            }
        }

        return list;
    }

    //添加管理员用户
    public String  addRoleUser(ObjectId userId,ObjectId roleId){
        UserEntry entry1 =  userDao.findByUserId(roleId);
        List<String>  stringList = new ArrayList<String>();
        //加入基础权限
        BusinessRoleEntry businessRoleEntry2 = businessRoleDao.getEntry(roleId);
        String ava = entry1.getAvatar().replaceAll("-blueheadv1", "");
        ava = ava.replaceAll("-headv1", "");
        String oldUrl = AvatarUtils.getAvatar(ava,entry1.getRole(),entry1.getSex());
        String newUrl = oldUrl+"-blueheadv1";
        //加运营人员大v
        userDao.updateAvater(roleId,newUrl);
        memberDao.updateAllAvatar(roleId, newUrl);
        if(businessRoleEntry2==null){
            stringList.add(RoleType.updateCommunityName.getEname());
            stringList.add(RoleType.commentAndZan.getEname());
            BusinessRoleEntry businessRoleEntry=  new BusinessRoleEntry(roleId,0,new ArrayList<ObjectId>(),stringList,oldUrl, newUrl);
            String str = businessRoleDao.addEntry(businessRoleEntry);
            
            backStageService.addLogMessage(str, "添加运营管理员：" + roleId.toString(), LogMessageType.yunRole.getDes(), userId.toString());
            return "添加成功";
        }else{
            if (StringUtils.isBlank(businessRoleEntry2.getOldAvatar()) || StringUtils.isBlank(businessRoleEntry2.getNewAvatar())) {
                businessRoleEntry2.setOldAvatar(oldUrl);
                businessRoleEntry2.setNewAvatar(newUrl);
                businessRoleDao.updEntry(businessRoleEntry2);
            }
            return "用户已添加";
        }
    }
    
  //删除管理员用户
    public String  delRoleUser(ObjectId userId,ObjectId roleId) throws Exception{
        //加入基础权限
        BusinessRoleEntry businessRoleEntry2 = businessRoleDao.getEntry(roleId);
        if (businessRoleEntry2 != null) {
            String imgpath1 = businessRoleEntry2.getOldAvatar();
            TeacherApproveEntry entry = teacherApproveDao.getEntry(roleId);
            if(entry!=null && entry.getType()==2){
                String imagpage3 = imgpath1.replaceAll("-headv1","");
                String imagpage2 = imagpage3.replaceAll("-blueheadv1","");
                imagpage2 = imagpage2+"-headv1";
                teacherApproveDao.updateEntry4(roleId, entry.getType(), imgpath1, imagpage2);
                //加运营人员大v
                userDao.updateAvater(roleId,imagpage2);
                memberDao.updateAllAvatar(roleId, imagpage2);
            }else{
                if (StringUtils.isNotBlank(businessRoleEntry2.getOldAvatar())) {
                    String oldUrl = businessRoleEntry2.getOldAvatar();
                    //加运营人员大v
                    userDao.updateAvater(roleId,oldUrl);
                    memberDao.updateAllAvatar(roleId, oldUrl);
                }
            }

            businessRoleDao.delEntry(roleId);
        }
        
        return "用户已删除";
    }

    public List<Map<String,Object>> getRoleList(ObjectId userId){
        List<Map<String,Object>>  mapList = new ArrayList<Map<String, Object>>();
        BusinessRoleEntry businessRoleEntry =  businessRoleDao.getEntry(userId);
        List<String> stringList = businessRoleEntry.getRoleType();
        for(RoleType lt : RoleType.values())
        {
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("name",lt.getDes());
            map.put("ename",lt.getEname());
            if(stringList.contains(lt.getEname())){
                map.put("status",1);
            }else{
                map.put("status",0);
            }
            mapList.add(map);
        }
        return mapList;

    }

    public void updateRole(ObjectId userId,ObjectId roleId,String number){
        BusinessRoleEntry businessRoleEntry = businessRoleDao.getEntry(roleId);
        if(businessRoleEntry==null){
            return;
        }
        List<String> list =new ArrayList<String>();
        String[] str = number.split(",");
        for(int i =0;i<str.length;i++){
            if(list.contains(str[i])){

            }else{
                list.add(str[i]);
            }
        }
        businessRoleEntry.setRoleType(list);
        businessRoleDao.addEntry(businessRoleEntry);
        backStageService.addLogMessage(businessRoleEntry.getID().toString(), "修改运营管理员权限：" + roleId.toString(), LogMessageType.yunRole.getDes(), userId.toString());
    }

    public int getVersion(String moduleName){
        VersionOpenEntry versionOpenEntry = versionOpenDao.getEntry(moduleName);
        if(versionOpenEntry!=null){
            return versionOpenEntry.getModuleType();
        }
        return 0;
    }

    public  Map<String,Object> getNewVersion(String moduleName,ObjectId userId){
        Map<String,Object>  map =  new HashMap<String, Object>();
        VersionOpenEntry versionOpenEntry = versionOpenDao.getEntry(moduleName);
        map.put("status",0);
        if(versionOpenEntry!=null){
            map.put("status",1);
        }
        map.put("agree",1);
      /*  UserAgreementEntry userAgreementEntry = userAgreementDao.getEntry(userId);
        if (userAgreementEntry != null) {
            if (userAgreementEntry.getAgree() == 1) {
                map.put("agree",1);
            }
        }*/
        return map;
    }

    public void addVersion(String moduleName){
        VersionOpenEntry versionOpenEntry = versionOpenDao.getEntry(moduleName);
        if(versionOpenEntry==null){
            VersionOpenEntry entry = new VersionOpenEntry(Constant.ZERO,moduleName);
            versionOpenDao.addEntry(entry);
        }
    }


    public void updateVersion(String moduleName,int type){
        VersionOpenEntry versionOpenEntry = versionOpenDao.getEntry(moduleName);
        if(versionOpenEntry!=null){
            versionOpenEntry.setModuleType(type);
            versionOpenDao.addEntry(versionOpenEntry);
        }
    }

    /**
     * 获取禁言
     * @param userId
     * @param moduleType
     * @return
     */
    public long getSpeak(ObjectId userId,int moduleType){
        BanningSpeakingEntry banningSpeakingEntry = banningSpeakingDao.getEntry(userId, moduleType);
        long current = System.currentTimeMillis();
        if(banningSpeakingEntry==null){
            return 0l;
        }else{
            if(banningSpeakingEntry.getEndTime()>current){
                return banningSpeakingEntry.getEndTime()-current;
            }
        }
        return 0l;
    }

   /* *//**
     * 获取禁言
     * @param userId
     * @param groupId
     * @return
     */
    public  Map<String,Object> getCommunitySpeak(ObjectId userId,ObjectId groupId,ObjectId id){
        Map<String,Object> map = new HashMap<String, Object>();
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        objectIdList.add(groupId);
        Map<ObjectId,Map<ObjectId,Integer>> groupMap = memberDao.getMemberGroupManage(objectIdList);
        Map<ObjectId,Integer> groupUserIds =  groupMap.get(groupId);
        List<ObjectId> objectIds = new ArrayList<ObjectId>();
        objectIds.add(userId);
        objectIds.add(id);
        List<ObjectId> objectIdList1 = teacherApproveDao.selectMap(objectIds);
        map.put("show",Constant.ZERO);
        if(null!=groupUserIds.get(id)){
            int role = groupUserIds.get(id);
            if(null!=groupUserIds.get(userId)){
                int userRole = groupUserIds.get(userId);
                if(role>userRole){
                    map.put("show",Constant.ONE);
                }else{
                    if(userRole==0){//同为普通成员
                        if(objectIdList1.contains(id) && !objectIdList1.contains(userId)){
                            map.put("show", Constant.ONE);//我是大V        你不是
                        }
                    }
                }
            }else{
                map.put("show", Constant.ZERO);
            }
        }
        if(!userId.equals(id)){
            BusinessRoleEntry businessRoleEntry = businessRoleDao.getEntry(id);
            if(businessRoleEntry !=null && businessRoleEntry.getRoleType().contains(RoleType.communityjinyan.getEname())){
                map.put("show", Constant.ONE);
            }
        }
        CommunitySpeakingEntry communitySpeakingEntry = communitySpeakingDao.getEntry(userId, groupId);
        long current = System.currentTimeMillis();
        map.put("time",0);
        if(communitySpeakingEntry==null){
            map.put("time",0);
        }else{
            if(communitySpeakingEntry.getEndTime()>current){
                map.put("time",communitySpeakingEntry.getEndTime()-current);
            }
        }
        return map;
    }

    /**
     * 禁言   兴趣小组 ----ApplyTypeEn.happy.getType()
     * @param userId
     * @param moduleType
     * @return
     */
    public void banningSpeak(ObjectId userId,int moduleType,long time){
        if(time!=0){//禁言
            banningSpeakingDao.updateEntry(userId,moduleType);
            long current = System.currentTimeMillis();
            long endTime = current+time;
            BanningSpeakingEntry banningSpeakingEntry = new BanningSpeakingEntry(userId,moduleType,Constant.ZERO,current,endTime);
            banningSpeakingDao.addEntry(banningSpeakingEntry);
        }else {//取消禁言
            banningSpeakingDao.updateEntry(userId, moduleType);
        }
    }

    /**
     * 环信发送透传消息
     */
    public void sendJinMessage(String emchatId,String userId,String type){
        List<String> targets = new ArrayList<String>();
        Map<String, String> ext = new HashMap<String, String>();
        Map<String, String> sendMessage = new HashMap<String, String>();
        //接受群组
        targets.add(emchatId);
        ext.put("userId", userId.toString());
        sendMessage.put("type", "cmd");
        sendMessage.put("action", type);
        //sendMessage.put("msg", "作业通知：\n社长 张老师 发布了一条新作业 \n请各位家长及时查看！");
        emService.sendTextMessage("chatrooms", targets, userId, ext, sendMessage);
    }


    /**
     * 禁言   社群 ----ApplyTypeEn.happy.getType()
     * @param userId
     * @param time
     * @return
     */
    public void banningCommunitySpeak(ObjectId memberId,ObjectId userId,ObjectId groupId,long time){
        if(time!=0){//禁言
            communitySpeakingDao.updateEntry(userId,groupId);
            long current = System.currentTimeMillis();
            long endTime = current+time;
            CommunityEntry communityEntry = communityDao.findCommunityByObjectId(groupId);
            if(communityEntry!=null){
                ObjectId communityId = communityEntry.getID();
                String emChatId = communityEntry.getEmChatId();
                CommunitySpeakingEntry communitySpeakingEntry = new CommunitySpeakingEntry(userId,memberId,groupId,communityId,Constant.ZERO,current,endTime);
                communitySpeakingDao.addEntry(communitySpeakingEntry);
                //发送消息
                sendJinMessage(emChatId,userId.toString(),"BANDTALK_ON");
            }
        }else {//取消禁言
            CommunityEntry communityEntry = communityDao.findCommunityByObjectId(groupId);
            if(communityEntry!=null){
                String emChatId = communityEntry.getEmChatId();
                communitySpeakingDao.updateEntry(userId, groupId);
                //发送消息
                sendJinMessage(emChatId,userId.toString(),"BANDTALK_OFF");
            }

        }
    }


    /**
     * 角色判断和禁言判断
     */
    public Map<String,Object> getPersonSpeak(ObjectId userId,int moduleType,ObjectId roleId){
        Map<String,Object> map = new HashMap<String, Object>();
        BusinessRoleEntry businessRoleEntry = businessRoleDao.getEntry(roleId);
        int role = 0;
        if(businessRoleEntry!=null && businessRoleEntry.getRoleType().contains(RoleType.jinyan.getEname())){
            if(!userId.toString().equals(roleId.toString())){
                role = 1;
            }
        }
        long time = 0l;
        BanningSpeakingEntry banningSpeakingEntry = banningSpeakingDao.getEntry(userId, moduleType);
        long current = System.currentTimeMillis();
        if(banningSpeakingEntry==null){
            time= 0l;
        }else{
            if(banningSpeakingEntry.getEndTime()>current){
                time =  banningSpeakingEntry.getEndTime()-current;
            }
        }

        map.put("role",role);
        map.put("time",time);
        return map;
    }

    public static String formatDuring(long mss) {
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        return days + " 天 " + hours + "小时" + minutes + " 分钟 "
                + seconds + " 秒 ";
    }


    public List<HomeSchoolDTO> getMySchoolList(ObjectId userId){
        List<HomeSchoolDTO> dtos = new ArrayList<HomeSchoolDTO>();
        List<ObjectId> objectIdList = schoolPersionDao.getOneRoleList(userId);
        List<HomeSchoolEntry> schoolEntries = homeSchoolDao.getSchoolList(objectIdList);
        for(HomeSchoolEntry entry : schoolEntries){
            dtos.add(new HomeSchoolDTO(entry));
        }
        return dtos;
    }


    //同意用户协议
    public void agreeUserAgreement(ObjectId userId,int type){
        UserAgreementEntry userAgreementEntry = userAgreementDao.getEntry(userId);
        if(userAgreementEntry==null){
            UserAgreementEntry userAgreementEntry1 = new UserAgreementEntry(userId,type,Constant.ONE);
            userAgreementDao.addEntry(userAgreementEntry1);
        }else{
            if(userAgreementEntry.getAgree()!=1){
                userAgreementEntry.setAgree(Constant.ONE);
                userAgreementDao.addEntry(userAgreementEntry);
            }
        }
    }

    //查询每日订单
    public Map<String,Object> getDayOrderList(ObjectId userId,int type,String startTime,String endTime,String schoolId,String coursesId,int page,int pageSize){
        Map<String,Object> map = new HashMap<String, Object>();
        List<CoursesOrderResultEntry> coursesOrderResultEntries = coursesOrderResultDao.getEntryList(type,startTime,endTime,schoolId,coursesId,page,pageSize);
        int count = coursesOrderResultDao.countEntryList(type,startTime,endTime,schoolId,coursesId);
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        for(CoursesOrderResultEntry coursesOrderResultEntry : coursesOrderResultEntries){
            objectIdList.add(coursesOrderResultEntry.getUserId());
        }
        Map<ObjectId, UserEntry> mainUserEntryMap = userDao.getUserEntryMap(objectIdList, Constant.FIELDS);
        List<CoursesOrderResultDTO>  dtos  =  new ArrayList<CoursesOrderResultDTO>();
        for(CoursesOrderResultEntry coursesOrderResultEntry: coursesOrderResultEntries){
            CoursesOrderResultDTO coursesOrderResultDTO = new CoursesOrderResultDTO(coursesOrderResultEntry);
            UserEntry userEntry = mainUserEntryMap.get(coursesOrderResultEntry.getUserId());
            String name = "";
            if(userEntry!=null){
                name = StringUtils.isNotBlank(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
            }
            coursesOrderResultDTO.setUserId(userEntry.getGenerateUserCode());
            coursesOrderResultDTO.setUserName(name);
            coursesOrderResultDTO.setCount(coursesOrderResultDTO.getClassList().size());
            dtos.add(coursesOrderResultDTO);
        }
        map.put("list",dtos);
        map.put("count",count);
        List<CoursesOrderResultEntry> coursesOrderResultEntries2 = coursesOrderResultDao.getAllEntryList(startTime, endTime, schoolId, coursesId);
        Set<ObjectId> schoolSet = new HashSet<ObjectId>();
        Set<ObjectId> coursesSet = new HashSet<ObjectId>();
        double addPrice = 0;
        double manPrice = 0;
        for(CoursesOrderResultEntry coursesOrderResultEntry:coursesOrderResultEntries2){
            if(coursesOrderResultEntry.getSchoolId() != null && !coursesOrderResultEntry.getSchoolId().equals("")){
                schoolSet.add(coursesOrderResultEntry.getSchoolId());
            }
            coursesSet.add(coursesOrderResultEntry.getCoursesId());
            if(coursesOrderResultEntry.getType()==1){
                addPrice = sum(addPrice,coursesOrderResultEntry.getPrice());
            }else{
                manPrice = sum(manPrice,coursesOrderResultEntry.getPrice());
            }
        }
        map.put("schoolCount",schoolSet.size());
        map.put("coursesCount",coursesSet.size());
        map.put("addPrice",addPrice);
        map.put("manPrice",manPrice);

        return map;
    }

    /**
     * 批量导出数据  type,startTime,endTime,schoolId,coursesId
     * @return
     */
    public void exportOrderTemplate(HttpServletRequest request, HttpServletResponse response,int type,String startTime,String endTime,String schoolId,String coursesId) {
        List<CoursesOrderResultEntry> coursesOrderResultEntries2 = coursesOrderResultDao.getNewAllEntryList(type, startTime, endTime, schoolId, coursesId);
        Set<ObjectId> schoolSet = new HashSet<ObjectId>();
        Set<ObjectId> coursesSet = new HashSet<ObjectId>();
        double addPrice = 0;
        double manPrice = 0;
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        for(CoursesOrderResultEntry coursesOrderResultEntry:coursesOrderResultEntries2){
            if(coursesOrderResultEntry.getSchoolId() != null && !coursesOrderResultEntry.getSchoolId().equals("")) {
                schoolSet.add(coursesOrderResultEntry.getSchoolId());
            }
            coursesSet.add(coursesOrderResultEntry.getCoursesId());
            userIds.add(coursesOrderResultEntry.getUserId());
            if(coursesOrderResultEntry.getType()==1){
                addPrice = sum(addPrice,coursesOrderResultEntry.getPrice());
            }else{
                manPrice = sum(manPrice,coursesOrderResultEntry.getPrice());
            }
        }

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet;
        String sheetName = startTime+"至"+endTime+"支出订单统计";
        if(type==1){
            sheetName = startTime+"至"+endTime+"收入订单统计";
        }
        try {
            sheet = wb.createSheet(sheetName);
        } catch (Exception e) {
            sheet = wb.createSheet("订单统计");
        }

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
        HSSFRow rowZero = sheet.createRow(0);
        HSSFCell cellZero = rowZero.createCell(0);
        if(type==1){
            cellZero.setCellValue("共计"+coursesOrderResultEntries2.size()+"份订单，当前条件下，共有"+schoolSet.size()+"所学校"+coursesSet.size()+"门课程有交易记录，共收入"+String.format("%.2f",addPrice)+"元");
        }else{
            cellZero.setCellValue("共计"+coursesOrderResultEntries2.size()+"份订单，当前条件下，共有"+schoolSet.size()+"所学校"+coursesSet.size()+"门课程有退款记录，共支出"+String.format("%.2f",manPrice)+"元");
        }


        HSSFRow row = sheet.createRow(1);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("序号");

        cell = row.createCell(1);
        cell.setCellValue("时间");

        cell = row.createCell(2);
        cell.setCellValue("课程名称");

        cell = row.createCell(3);
        cell.setCellValue("所属学校");

        cell = row.createCell(4);
        cell.setCellValue("购买用户");

        cell = row.createCell(5);
        cell.setCellValue("用户ID");

        if(type==1){
            cell = row.createCell(6);
            cell.setCellValue("购买课节");
            cell = row.createCell(7);
            cell.setCellValue("购买金额");
        }else{
            cell = row.createCell(6);
            cell.setCellValue("退课节数");
            cell = row.createCell(7);
            cell.setCellValue("退款金额");
        }
        cell = row.createCell(8);
        cell.setCellValue("订单号");

        cell = row.createCell(9);
        cell.setCellValue("订单来源");

        int rowLine = 2;

        HSSFRow rowItem;
        HSSFCell cellItem;
        int index = 0;
        Map<ObjectId,UserEntry> userEntryMap = userDao.getUserEntryMap(userIds, Constant.FIELDS);
        for(CoursesOrderResultEntry coursesOrderResultEntry:coursesOrderResultEntries2){
            CoursesOrderResultDTO coursesOrderResultDTO = new CoursesOrderResultDTO(coursesOrderResultEntry);
            index++;
            rowItem = sheet.createRow(rowLine);

            cellItem = rowItem.createCell(0);
            cellItem.setCellValue(index+"");

            cellItem = rowItem.createCell(1);
            cellItem.setCellValue(coursesOrderResultDTO.getOrderTime());

            cellItem = rowItem.createCell(2);
            cellItem.setCellValue(coursesOrderResultDTO.getCoursesName());


            cellItem = rowItem.createCell(3);
            cellItem.setCellValue(coursesOrderResultDTO.getSchoolName());

            UserEntry userEntry = userEntryMap.get(coursesOrderResultEntry.getUserId());
            if(userEntry!=null){
                String name = StringUtils.isNotBlank(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();

                cellItem = rowItem.createCell(4);
                cellItem.setCellValue(name);

                cellItem = rowItem.createCell(5);
                cellItem.setCellValue(userEntry.getGenerateUserCode());
            }else{
                cellItem = rowItem.createCell(4);
                cellItem.setCellValue("");

                cellItem = rowItem.createCell(5);
                cellItem.setCellValue("");
            }

            cellItem = rowItem.createCell(6);
            cellItem.setCellValue(coursesOrderResultDTO.getClassList().size()+"");

            cellItem = rowItem.createCell(7);
            cellItem.setCellValue(String.format("%.2f", coursesOrderResultEntry.getPrice()));

            cellItem = rowItem.createCell(8);
            cellItem.setCellValue(coursesOrderResultDTO.getOrder());

            cellItem = rowItem.createCell(9);
            if(coursesOrderResultDTO.getSource()==1){
                cellItem.setCellValue("支付宝");
            }else if(coursesOrderResultDTO.getSource()==2){
                cellItem.setCellValue("微信");
            }else{
                if(coursesOrderResultDTO.getPrice()==0){
                    cellItem.setCellValue("免费");
                }else{
                    cellItem.setCellValue("现金");
                }
            }

            rowLine++;
        }
        String fileName = sheetName + ".xls";
        String userAgent = request.getHeader("USER-AGENT");
        HSSFUtils.exportExcel(userAgent, response, wb, fileName);
    }

    public List<ObjectId> getCommunitys3(ObjectId uid, int page, int pageSize) {
        List<MineCommunityEntry> allMineCommunitys = mineCommunityDao.findAll(uid, page, pageSize);
        List<ObjectId> list = new ArrayList<ObjectId>();
        List<ObjectId> communityIds = new ArrayList<ObjectId>();
        for (MineCommunityEntry mineCommunityEntry : allMineCommunitys) {
            communityIds.add(mineCommunityEntry.getCommunityId());
        }
        List<CommunityEntry> communityEntries = communityDao.findByNotObjectIds(communityIds);
        for(CommunityEntry communityEntry:communityEntries){
            list.add(communityEntry.getID());
        }
        return list;
    }


    //添加每日订单
    public void addCoursesOrderEntry(ObjectId userId,ObjectId coursesId,int type,List<ObjectId> classList,double price,String order,int source){
        long orderTime = System.currentTimeMillis();
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(coursesId);
        if(excellentCoursesEntry==null){
            return;
        }
        //查询该用户的社群(使用开课老师的学校)
        List<ObjectId>  communityIds =this.getCommunitys3(excellentCoursesEntry.getUserId(), 1, 100);
        //List<ObjectId> communityIds = excellentCoursesEntry.getCommunityIdList();
        ObjectId schoolId = null;
        String schoolName = "";
        if(communityIds!=null && communityIds.size()>0){
            //所在学校
            List<ObjectId> schoolIdsList = schoolCommunityDao.getSchoolIdsList(communityIds);
            //获得已被允许的学校
            List<ObjectId> schoolIds = schoolFunctionDao.getNewAllSchoolIdList(schoolIdsList,1, 1);
            if(schoolIds.size()>0){
                HomeSchoolEntry homeSchoolEntry =  homeSchoolDao.getEntryById(schoolIds.get(0));
                schoolId = homeSchoolEntry.getID();
                schoolName = homeSchoolEntry.getName();
            }
        }
        CoursesOrderResultEntry coursesOrderResultEntry = new CoursesOrderResultEntry(
                orderTime,userId,schoolId,schoolName,coursesId,excellentCoursesEntry.getTitle(),type
                ,classList,price,order,source);
        coursesOrderResultDao.addEntry(coursesOrderResultEntry);
    }

    //查询某个学校下的课程信息
    public List<ExcellentCoursesDTO> getSchoolCourses(ObjectId schoolId){
        List<ExcellentCoursesDTO> dtos = new ArrayList<ExcellentCoursesDTO>();
        //获得学校下的社群
       // List<ObjectId> communityIds = schoolCommunityDao.getCommunityIdsListBySchoolId(schoolId);
        List<ObjectId> objectIdList = coursesOrderResultDao.getAllLsit(schoolId);
      /*  List<ObjectId> groupIds = communityDao.getGroupIdsByCommunityIds(communityIds);
        List<ObjectId> memberList = memberDao.getAllGroupIdsMembers(groupIds);
        List<ObjectId> objectIdList = teacherApproveDao.selectMap(memberList);*/
        List<ExcellentCoursesEntry> excellentCoursesEntries = excellentCoursesDao.getMyEntryListById(objectIdList);
        for(ExcellentCoursesEntry excellentCoursesEntry : excellentCoursesEntries){
            dtos.add(new ExcellentCoursesDTO(excellentCoursesEntry));
        }
        return dtos;
    }


    public void heBingRoom(ObjectId id,String roomId){
        HourClassEntry hourClassEntry = hourClassDao.getEntry(id);
        if(hourClassEntry!=null){
            hourClassEntry.setRoomId(roomId);
            hourClassEntry.setIsHe(1);
            hourClassDao.addEntry(hourClassEntry);
        }
    }

    public String selectBingRoom(ObjectId id){
        HourClassEntry hourClassEntry = hourClassDao.getEntry(id);
        String roomId = "";
        if(hourClassEntry!=null){
            if(hourClassEntry.getRoomId()!=null && !hourClassEntry.getRoomId().equals("")){
                roomId = hourClassEntry.getRoomId();
            }else{
                CoursesRoomEntry coursesRoomEntry = coursesRoomDao.getEntry(hourClassEntry.getParentId());
                if(coursesRoomEntry!=null){
                    roomId  = coursesRoomEntry.getRoomId();
                }
            }

        }
        return roomId;
    }


    public boolean booleanUserAgreement(ObjectId userId) {
     /*   UserAgreementEntry userAgreementEntry = userAgreementDao.getEntry(userId);
        if (userAgreementEntry != null) {
            if (userAgreementEntry.getAgree() == 1) {
                return true;
            }
        }*/
        return true;
    }
}
