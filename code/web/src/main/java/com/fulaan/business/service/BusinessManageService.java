package com.fulaan.business.service;

import com.db.business.BusinessManageDao;
import com.db.business.BusinessRoleDao;
import com.db.business.VersionOpenDao;
import com.db.excellentCourses.ClassOrderDao;
import com.db.excellentCourses.ExcellentCoursesDao;
import com.db.excellentCourses.HourClassDao;
import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.LoginLogDao;
import com.db.user.UserDao;
import com.db.wrongquestion.SubjectClassDao;
import com.fulaan.backstage.service.BackStageService;
import com.fulaan.business.dto.BusinessManageDTO;
import com.fulaan.excellentCourses.dto.ClassOrderDTO;
import com.fulaan.excellentCourses.dto.ExcellentCoursesDTO;
import com.fulaan.excellentCourses.dto.HourClassDTO;
import com.fulaan.excellentCourses.service.CoursesRoomService;
import com.fulaan.picturetext.runnable.PictureRunNable;
import com.fulaan.pojo.User;
import com.fulaan.user.service.UserService;
import com.pojo.backstage.LogMessageType;
import com.pojo.business.BusinessManageEntry;
import com.pojo.business.BusinessRoleEntry;
import com.pojo.business.RoleType;
import com.pojo.business.VersionOpenEntry;
import com.pojo.excellentCourses.ClassOrderEntry;
import com.pojo.excellentCourses.ExcellentCoursesEntry;
import com.pojo.excellentCourses.HourClassEntry;
import com.pojo.fcommunity.CommunityEntry;
import com.pojo.fcommunity.FLoginLogEntry;
import com.pojo.user.UserEntry;
import com.pojo.wrongquestion.SubjectClassEntry;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        List<UserEntry> userEntries = userDao.findEntryByName("nm", keyword);
        List<UserEntry> userEntries2 = userDao.findEntryByName("nnm", keyword);
        userEntries.addAll(userEntries2);
        List<ObjectId> oisd = new ArrayList<ObjectId>();
        for(UserEntry userEntry: userEntries){
            //String userName,String nickName,String userId,String avator,int sex,String time
            if(!oisd.contains(userEntry.getID())){
                User user = new User(userEntry.getUserName(),userEntry.getNickName(),userEntry.getID().toString(),AvatarUtils.getAvatar(userEntry.getAvatar(),userEntry.getRole(),userEntry.getSex()),userEntry.getSex(),"");
                userList.add(user);
                oisd.add(userEntry.getID());
            }

        }
        return userList;
    }


    //查询所有开的课程
    public Map<String,Object> selectAllCourses(int page,int pageSize,String name,String subjectId){
        Map<String,Object> map = new HashMap<String, Object>();
        List<ExcellentCoursesEntry> excellentCoursesEntries = excellentCoursesDao.getAllWebEntryList(subjectId, name, page, pageSize);
        int count = excellentCoursesDao.selectAllWebEntryList(subjectId,name);
        List<ExcellentCoursesDTO> dtos = new ArrayList<ExcellentCoursesDTO>();
        for(ExcellentCoursesEntry excellentCoursesEntry:excellentCoursesEntries){
            ExcellentCoursesDTO dto = new ExcellentCoursesDTO(excellentCoursesEntry);
            dtos.add(dto);
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
        List<ClassOrderDTO> classOrderDTOs = new ArrayList<ClassOrderDTO>();
        List<ClassOrderEntry> classOrderEntries = classOrderDao.getCoursesUserList(id);
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        for(ClassOrderEntry classOrderEntry:classOrderEntries){
            userIds.add(classOrderEntry.getUserId());
        }
        List<UserEntry> userEntries = userDao.getUserEntryList(userIds, Constant.FIELDS);
        List<Map<String,Object>> listMap = new ArrayList<Map<String, Object>>();
        int order = 0;
        for(UserEntry userEntry:userEntries){
            Map<String,Object> userMap = new HashMap<String, Object>();
            int score = 0;
            int number = 0;
            order++;
            long createTime= 0l;
            for(ClassOrderEntry classOrderEntry:classOrderEntries){
                if(userEntry.getID().equals(classOrderEntry.getUserId())){
                    score = score + classOrderEntry.getPrice();
                    number++;
                    createTime = classOrderEntry.getCreateTime();
                }
            }
            String ctm = "";
            if(createTime!=0l){
                ctm = DateTimeUtils.getLongToStrTimeTwo(createTime);
            }
            userMap.put("order",order);
            userMap.put("price","¥ "+score);
            userMap.put("number",number);
            userMap.put("createTime",ctm);
            String name = StringUtils.isNotEmpty(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
            userMap.put("userName",name);
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
        Map<String,Integer> map = new HashMap<String, Integer>();
        for(int i = 0;i<str.length;i++){
            String tr = str[i];
            String[] strings = tr.split("#");
            map.put(strings[0],Integer.parseInt(strings[1]));
        }
        List<HourClassEntry> hourClassEntries = hourClassDao.getEntryList(id);
        int all = 0;
        for(HourClassEntry hourClassEntry : hourClassEntries){
            Integer price = map.get(hourClassEntry.getID().toString());
            if(price!=null){
                hourClassDao.updatePriceEntry(hourClassEntry.getID(),price);
                all = all + price;
            }
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
                map.put("userRole",getNewString(businessRoleEntry.getRoleType()));
                list.add(map);
            }
        }

        return list;
    }

    //添加管理员用户
    public void  addRoleUser(ObjectId userId,ObjectId roleId){
        /*ObjectId userId,
            int type,
            List<ObjectId> communityIdList,
            List<String> roleType*/
        List<String>  stringList = new ArrayList<String>();
        //加入基础权限
        stringList.add(RoleType.updateCommunityName.getEname());
        stringList.add(RoleType.commentAndZan.getEname());
        BusinessRoleEntry businessRoleEntry=  new BusinessRoleEntry(roleId,0,new ArrayList<ObjectId>(),stringList);
        String str = businessRoleDao.addEntry(businessRoleEntry);
        backStageService.addLogMessage(str, "添加运营管理员：" + roleId.toString(), LogMessageType.yunRole.getDes(), userId.toString());
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

}
