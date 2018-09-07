package com.fulaan.business.service;

import com.db.backstage.PushMessageDao;
import com.db.backstage.TeacherApproveDao;
import com.db.business.*;
import com.db.excellentCourses.*;
import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.LoginLogDao;
import com.db.fcommunity.MemberDao;
import com.db.jiaschool.HomeSchoolDao;
import com.db.jiaschool.SchoolPersionDao;
import com.db.user.NewVersionUserRoleDao;
import com.db.user.UserDao;
import com.db.wrongquestion.SubjectClassDao;
import com.fulaan.backstage.service.BackStageService;
import com.fulaan.business.dto.BusinessManageDTO;
import com.fulaan.excellentCourses.dto.BackOrderDTO;
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
import com.pojo.business.*;
import com.pojo.excellentCourses.*;
import com.pojo.fcommunity.CommunityEntry;
import com.pojo.fcommunity.FLoginLogEntry;
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
                User user = new User(userEntry.getUserName(),userEntry.getNickName(),userEntry.getID().toString(),userEntry.getMobileNumber(),userEntry.getSex(),userEntry.getGenerateUserCode());
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
        List<HourClassDTO> hourClassDTOs = new ArrayList<HourClassDTO>();
        for(HourClassEntry hourClassEntry : hourClassEntries){
            hourClassDTOs.add(new HourClassDTO(hourClassEntry));
        }
        map.put("hourList",hourClassDTOs);
        //用户订单查询
        List<ClassOrderEntry> classOrderEntries = classOrderDao.getCoursesUserList(id);
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        for(ClassOrderEntry classOrderEntry:classOrderEntries){
            userIds.add(classOrderEntry.getUserId());
        }
        List<UserEntry> userEntries = userDao.getUserEntryList(userIds, Constant.FIELDS);
        List<Map<String,Object>> listMap = new ArrayList<Map<String, Object>>();
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
        }
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
            if(start<=userIds.size()&& userIds.size() <= end){
                ids.add(classOrderEntry.getUserId());
            }
            if(start<=userIds.size()&& userIds.size() <=end || ids.contains(classOrderEntry.getUserId())){
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
        HSSFSheet sheet = wb.createSheet(sheetName);
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
            if(order2.equals("")){
                if(money==1){
                    cellItem.setCellValue("后台付费");
                }else{
                    cellItem.setCellValue("后台免费");
                }
            }else{
                cellItem.setCellValue(order2);
            }

            cellItem = rowItem.createCell(7);
            if(function==1){
                cellItem.setCellValue("支付宝");
            }else if(function==2){
                cellItem.setCellValue("微信");
            }else{
                cellItem.setCellValue("后台添加");
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
        }else{
            sheetName = "课程《"+excellentCoursesEntry.getTitle()+"》退课订单列表";
        }

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(sheetName);
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
            cellItem.setCellValue(dto.getPrice()+"");

            cellItem = rowItem.createCell(5);
            cellItem.setCellValue(dto.getOrderId());

            cellItem = rowItem.createCell(6);
            if(dto.getOrderType()==1){
                cellItem.setCellValue("支付宝");
            }else if(dto.getOrderType()==2){
                cellItem.setCellValue("微信");
            }else{
                cellItem.setCellValue("后台添加");
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
            obMap.put("level","0");
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
            if(endTime<current){

            }else{
                if(classOrderEntry!=null && ids.contains(hourClassEntry.getID().toString())){
                    index++;
                    price = sum(price,classOrderEntry.getPrice());
                }
            }

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
            long endTime = hourClassEntry.getStartTime()+hourClassEntry.getCurrentTime();
            if(endTime<current){

            }else{
                if(classOrderEntry!=null && ids.contains(hourClassEntry.getID().toString())){
                    hourIds.add(hourClassEntry.getID());
                    objectIdList.add(classOrderEntry.getID());
                    price = sum(price,classOrderEntry.getPrice());
                    orderId= classOrderEntry.getOrderId();
                    orderType = classOrderEntry.getFunction();
                    role = classOrderEntry.getRole();
                    money = classOrderEntry.getMoney();
                }
            }
        }
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
        BackOrderEntry backOrderEntry = new BackOrderEntry(id,userId,uid,hourIds,objectIdList,price,orderId,orderType,status,role,money);
        backOrderDao.addEntry(backOrderEntry);
        // 退课
        //订单置为退款中
        classOrderDao.updateEntry(objectIdList);
        backStageService.addLogMessage(id.toString(), "退课：" + excellentCoursesEntry.getTitle() + ",用户ID:" + userId.toString(), LogMessageType.courses.getDes(), uid.toString());
    }

    public String daoOrder(ObjectId id,String roomId){
        CoursesRoomEntry coursesRoomEntry = coursesRoomDao.getRoomEntry(roomId);
        if(coursesRoomEntry!=null){
            ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(coursesRoomEntry.getContactId());
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
                excellentCoursesEntry.setStudentNumber(excellentCoursesEntry.getStudentNumber()+num);
                excellentCoursesDao.addEntry(excellentCoursesEntry);
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
        List<ObjectId> hourClassEntries = hourClassDao.getObjectIdList(id);
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
        for(ClassOrderEntry classOrderEntry:classOrderEntries){
            price = sum(price, classOrderEntry.getPrice());
            number++;
            if(number==1){
                orderId = classOrderEntry.getOrderId();
                orderType = classOrderEntry.getFunction();
                money = classOrderEntry.getMoney();
                role = classOrderEntry.getRole();
            }
            orderIds.add(classOrderEntry.getID());

        }
        if(excellentCoursesEntry.getCourseType()==1){
            price= excellentCoursesEntry.getNewPrice();
        }
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
        BackOrderEntry backOrderEntry = new BackOrderEntry(id,userId,uid,hourClassEntries,orderIds,price,orderId,orderType,status,role,money);
        backOrderDao.addEntry(backOrderEntry);
        //2. 删除订单，并添加说明
        classOrderDao.delOrderEntry(id, userId);
        backStageService.addLogMessage(id.toString(), "删除直播课堂订单：" + excellentCoursesEntry.getTitle() + ",ID:" + jiaId, LogMessageType.courses.getDes(), uid.toString());
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
        List<String>  stringList = new ArrayList<String>();
        //加入基础权限
        BusinessRoleEntry businessRoleEntry2 = businessRoleDao.getEntry(roleId);
        if(businessRoleEntry2==null){
            stringList.add(RoleType.updateCommunityName.getEname());
            stringList.add(RoleType.commentAndZan.getEname());
            BusinessRoleEntry businessRoleEntry=  new BusinessRoleEntry(roleId,0,new ArrayList<ObjectId>(),stringList);
            String str = businessRoleDao.addEntry(businessRoleEntry);
            backStageService.addLogMessage(str, "添加运营管理员：" + roleId.toString(), LogMessageType.yunRole.getDes(), userId.toString());
            return "添加成功";
        }else{
            return "用户已添加";
        }
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
