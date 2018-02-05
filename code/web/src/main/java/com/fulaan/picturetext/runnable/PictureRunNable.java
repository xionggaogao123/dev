package com.fulaan.picturetext.runnable;

import com.db.backstage.UnlawfulPictureTextDao;
import com.db.business.BusinessManageDao;
import com.db.business.ModuleNumberDao;
import com.db.controlphone.ControlAppUserDao;
import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.LoginLogDao;
import com.db.fcommunity.MemberDao;
import com.db.newVersionGrade.NewVersionSubjectDao;
import com.db.user.NewVersionUserRoleDao;
import com.db.user.UserDao;
import com.fulaan.backstage.dto.UnlawfulPictureTextDTO;
import com.fulaan.business.dto.BusinessManageDTO;
import com.fulaan.picturetext.service.CheckTextAndPicture;
import com.fulaan.user.dao.ThirdLoginDao;
import com.fulaan.user.model.ThirdLoginEntry;
import com.pojo.business.BusinessManageEntry;
import com.pojo.business.ModuleNumberEntry;
import com.pojo.controlphone.ControlAppUserEntry;
import com.pojo.fcommunity.CommunityEntry;
import com.pojo.fcommunity.FLoginLogEntry;
import com.pojo.newVersionGrade.NewVersionSubjectEntry;
import com.pojo.user.NewVersionUserRoleEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import net.sf.json.JSONObject;
import org.bson.types.ObjectId;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by James on 2017/11/24.
 *
 * //异步线程处理
 */
public class PictureRunNable{
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(PictureRunNable.class);




    public static void send(final String contactId,final String userId,final int function,final int type,final String content) {
        new Thread(){
            public void run() {
                UnlawfulPictureTextDao unlawfulPictureTextDao = new UnlawfulPictureTextDao();
                System.out.println("新的线程在执行...");
                //System.out.println(prtNo);
                try{
                    CheckTextAndPicture.syncScanCheck(contactId,userId,function,type,content);
                }catch(Exception e){
                    UnlawfulPictureTextDTO dto = new UnlawfulPictureTextDTO();
                    dto.setType(type);
                    dto.setContent(content);
                    dto.setUserId(userId.toString());
                    dto.setFunction(function);
                    dto.setIsCheck(0);
                    dto.setContactId(contactId.toString());
                    unlawfulPictureTextDao.addEntry(dto.buildAddEntry());
                    logger.error("error",e);
                }
            }
        }.start();
    }

    public static void addApp(final ObjectId appId) {
        new Thread(){
            public void run() {
                ControlAppUserDao controlAppUserDao = new ControlAppUserDao();
                System.out.println("新的线程在执行...");
                //System.out.println(prtNo);
                boolean flag =true;
                int page = 1;
                try{
                    while(flag) {
                        List<ControlAppUserEntry> entryList =  controlAppUserDao.getUserList(page,10);
                        if(entryList.size()>0){
                            for(ControlAppUserEntry entry : entryList){
                                if(entry.getAppIdList() !=null && !entry.getAppIdList().contains(appId)){
                                    List<ObjectId> oids = entry.getAppIdList();
                                    oids.add(appId);
                                    entry.setAppIdList(oids);
                                    controlAppUserDao.updEntry(entry);
                                }
                            }
                        }else{
                            flag=false;
                        }
                        page++;
                    }
                }catch(Exception e){
                    logger.error("error",e);
                }
            }
        }.start();
    }
    public static void addFeiBusinessManageEntry(final ObjectId userId,final int type) {
        BusinessManageDao businessManageDao = new BusinessManageDao();
        UserDao userDao = new UserDao();
        MemberDao memberDao = new MemberDao();
        CommunityDao communityDao =  new CommunityDao();
        NewVersionSubjectDao newVersionSubjectDao = new NewVersionSubjectDao();
        NewVersionUserRoleDao newVersionUserRoleDao = new NewVersionUserRoleDao();
        System.out.println("新的线程在执行...");
        try{
            UserEntry entry = userDao.findByUserId(userId);
            BusinessManageEntry businessManageEntry = businessManageDao.getEntry(userId);
            ThirdLoginDao thirdLoginDao = new ThirdLoginDao();
            ThirdLoginEntry thirdLoginEntry = thirdLoginDao.getEntry(userId);

            if(businessManageEntry!=null){

            }else{
                BusinessManageDTO dto = new BusinessManageDTO();
                dto.setUserId(entry.getID().toString());
                dto.setHomeId(entry.getGenerateUserCode());
                dto.setPhone(entry.getMobileNumber());
                if(thirdLoginEntry!=null){
                    dto.setType(thirdLoginEntry.getType());
                    dto.setOpenId(thirdLoginEntry.getOid()+"&"+thirdLoginEntry.getUnionid());
                }else{
                    dto.setType(3);
                    dto.setOpenId("");
                }
                List<String> oids = memberDao.getMyCommunityIdsByUserId(userId);
                dto.setCommunityIdList(oids);
                List<ObjectId> objectIdList = new ArrayList<ObjectId>();
                for(String str:oids){
                    objectIdList.add(new ObjectId(str));
                }
                List<ObjectId> obids = memberDao.getMyRoleCommunityIdsByUserId(userId);
                List<CommunityEntry> communityEntries = communityDao.findByObjectIds(objectIdList);
                List<String> objectStr1 = new ArrayList<String>();//我创建的
                List<String> objectStr2 = new ArrayList<String>();
                for(CommunityEntry entry1 : communityEntries){
                    if(entry1.getID() != null && obids.contains(entry1.getID())){
                        objectStr1.add(entry1.getSearchId());
                    }else{
                        objectStr2.add(entry1.getSearchId());
                    }
                }
                dto.setCommunityNumbers(objectStr2);
                dto.setCommunityRoles(objectStr1);

                NewVersionUserRoleEntry newVersionUserRoleEntry = newVersionUserRoleDao.getEntry(userId);
                if (null != newVersionUserRoleEntry) {
                    if(newVersionUserRoleEntry.getNewRole() == Constant.ONE||newVersionUserRoleEntry.getNewRole() == Constant.TWO){
                        dto.setRole(1);
                    }else{
                        List<ObjectId> objectIdList1 = memberDao.getGroupIdsList(userId);
                        if(objectIdList1.size()>0){
                            dto.setRole(3);
                        }else{
                            dto.setRole(2);
                        }
                    }

                }else{
                    List<ObjectId> objectIdList1 = memberDao.getGroupIdsList(userId);
                    if(objectIdList1.size()>0){
                        dto.setRole(3);
                    }else{
                        dto.setRole(2);
                    }
                }
                List<String> stringList =  new ArrayList<String>();
                NewVersionSubjectEntry newVersionSubjectEntry = newVersionSubjectDao.getEntryByUserId(userId);
                if(null != newVersionSubjectEntry && newVersionSubjectEntry.getSubjectList()!=null){
                    for(ObjectId objectId : newVersionSubjectEntry.getSubjectList()){
                        stringList.add(objectId.toString());
                    }
                }
                dto.setPhoneType(type);
                Map<String,String> phoneMap = getPhone(entry.getMobileNumber());
                dto.setRegionType(phoneMap.get("province"));
                dto.setStoreType(phoneMap.get("catName"));
                dto.setOnlineTime(entry.getStatisticTime());
                BusinessManageEntry businessManageEntry1 = dto.buildAddEntry();
                businessManageEntry1.setCreateTime(entry.getRegisterTime());
                businessManageDao.addEntry(businessManageEntry1);
            }
        }catch(Exception e){
            logger.error("error",e);
        }
    }
    public static void main(String[] args){
        addBusinessManageEntry(new ObjectId("5a1cf1793d4df901bd9fa983"),1);
    }
    public static void addBusinessManageEntry(final ObjectId userId,final int type) {
        new Thread(){
            public void run() {
                BusinessManageDao businessManageDao = new BusinessManageDao();
                UserDao userDao = new UserDao();
                MemberDao memberDao = new MemberDao();
                CommunityDao communityDao =  new CommunityDao();
                NewVersionSubjectDao newVersionSubjectDao = new NewVersionSubjectDao();
                NewVersionUserRoleDao newVersionUserRoleDao = new NewVersionUserRoleDao();
                System.out.println("新的线程在执行...");
                try{
                    UserEntry entry = userDao.findByUserId(userId);
                    BusinessManageEntry businessManageEntry = businessManageDao.getEntry(userId);
                    ThirdLoginDao thirdLoginDao = new ThirdLoginDao();
                    ThirdLoginEntry thirdLoginEntry = thirdLoginDao.getEntry(userId);

                    if(businessManageEntry!=null){
                        if(thirdLoginEntry!=null){
                            businessManageEntry.setType(thirdLoginEntry.getType());
                            businessManageEntry.setOpenId(thirdLoginEntry.getOid()+"&"+thirdLoginEntry.getUnionid());
                        }else{
                            businessManageEntry.setType(3);
                            businessManageEntry.setOpenId("");
                        }
                        if(businessManageEntry.getAddressIp()==null){
                            LoginLogDao loginLogDao = new LoginLogDao();
                            FLoginLogEntry fLoginLogEntry = loginLogDao.getEntry(entry.getUserName());
                            if(fLoginLogEntry!=null){
                                businessManageEntry.setAddressIp(fLoginLogEntry.getLoginIp());
                            }
                        }
                        businessManageDao.updEntry(businessManageEntry);
                    }else{
                        BusinessManageDTO dto = new BusinessManageDTO();
                        dto.setUserId(entry.getID().toString());
                        dto.setHomeId(entry.getGenerateUserCode());
                        dto.setPhone(entry.getMobileNumber());
                        if(thirdLoginEntry!=null){
                            dto.setType(thirdLoginEntry.getType());
                            dto.setOpenId(thirdLoginEntry.getOid()+"&"+thirdLoginEntry.getUnionid());
                        }else{
                            dto.setType(3);
                            dto.setOpenId("");
                        }
                        List<String> oids = memberDao.getMyCommunityIdsByUserId(userId);
                        dto.setCommunityIdList(oids);
                        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
                        for(String str:oids){
                            objectIdList.add(new ObjectId(str));
                        }
                        List<ObjectId> obids = memberDao.getMyRoleCommunityIdsByUserId(userId);
                        List<CommunityEntry> communityEntries = communityDao.findByObjectIds(objectIdList);
                        List<String> objectStr1 = new ArrayList<String>();//我创建的
                        List<String> objectStr2 = new ArrayList<String>();
                        for(CommunityEntry entry1 : communityEntries){
                            if(entry1.getID() != null && obids.contains(entry1.getID())){
                                objectStr1.add(entry1.getSearchId());
                            }else{
                                objectStr2.add(entry1.getSearchId());
                            }
                        }
                        dto.setCommunityNumbers(objectStr2);
                        dto.setCommunityRoles(objectStr1);

                        NewVersionUserRoleEntry newVersionUserRoleEntry = newVersionUserRoleDao.getEntry(userId);
                        if (null != newVersionUserRoleEntry) {
                            if(newVersionUserRoleEntry.getNewRole() == Constant.ONE||newVersionUserRoleEntry.getNewRole() == Constant.TWO){
                                dto.setRole(1);
                            }else{
                                List<ObjectId> objectIdList1 = memberDao.getGroupIdsList(userId);
                                if(objectIdList1.size()>0){
                                    dto.setRole(3);
                                }else{
                                    dto.setRole(2);
                                }
                            }

                        }else{
                            List<ObjectId> objectIdList1 = memberDao.getGroupIdsList(userId);
                            if(objectIdList1.size()>0){
                                dto.setRole(3);
                            }else{
                                dto.setRole(2);
                            }
                        }
                        List<String> stringList =  new ArrayList<String>();
                        NewVersionSubjectEntry newVersionSubjectEntry = newVersionSubjectDao.getEntryByUserId(userId);
                        if(null != newVersionSubjectEntry && newVersionSubjectEntry.getSubjectList()!=null){
                            for(ObjectId objectId : newVersionSubjectEntry.getSubjectList()){
                                stringList.add(objectId.toString());
                            }
                        }
                        dto.setSubjectIdList(stringList);
                        dto.setPhoneType(type);
                        Map<String,String> phoneMap = getPhone(entry.getMobileNumber());
                        dto.setRegionType(phoneMap.get("province"));
                        dto.setStoreType(phoneMap.get("catName"));
                        dto.setOnlineTime(entry.getStatisticTime());
                        BusinessManageEntry businessManageEntry1 = dto.buildAddEntry();
                        businessManageEntry1.setCreateTime(entry.getRegisterTime());
                        businessManageDao.addEntry(businessManageEntry1);
                    }
                 }catch(Exception e){
                    logger.error("error",e);
                }
            }
        }.start();
    }

    public static void updateBusinessManageEntry() {
        new Thread(){
            public void run() {
                BusinessManageDao businessManageDao = new BusinessManageDao();
                UserDao userDao = new UserDao();
                MemberDao memberDao = new MemberDao();
                CommunityDao communityDao =  new CommunityDao();
                ModuleNumberDao moduleNumberDao = new ModuleNumberDao();
                NewVersionUserRoleDao newVersionUserRoleDao = new NewVersionUserRoleDao();
                LoginLogDao loginLogDao = new LoginLogDao();
                System.out.println("新的线程在执行...");
                try{
                    int size = 1;
                    int page = 1;
                    int pageSize = 10;
                    while(size>0){
                        List<BusinessManageEntry> businessManageEntries = businessManageDao.getPageList(page, pageSize);
                        size = businessManageEntries.size();
                        page++;
                        for(BusinessManageEntry businessManageEntry : businessManageEntries){
                            if(businessManageEntry!=null){
                                ObjectId userId = businessManageEntry.getUserId();
                                UserEntry entry = userDao.findByUserId(userId);
                                //社区相关
                                BusinessManageDTO dto = new BusinessManageDTO(businessManageEntry);
                                List<String> oids = memberDao.getMyCommunityIdsByUserId(userId);
                                dto.setCommunityIdList(oids);
                                List<ObjectId> objectIdList = new ArrayList<ObjectId>();
                                for(String str:oids){
                                    objectIdList.add(new ObjectId(str));
                                }
                                List<ObjectId> obids = memberDao.getMyRoleCommunityIdsByUserId(userId);
                                List<CommunityEntry> communityEntries = communityDao.findByObjectIds(objectIdList);
                                List<String> objectStr1 = new ArrayList<String>();//我创建的
                                List<String> objectStr2 = new ArrayList<String>();
                                for(CommunityEntry entry1 : communityEntries){
                                    if(entry1.getID() != null && obids.contains(entry1.getID())){
                                        objectStr1.add(entry1.getSearchId());
                                    }else{
                                        objectStr2.add(entry1.getSearchId());
                                    }
                                }
                                dto.setCommunityNumbers(objectStr2);
                                dto.setCommunityRoles(objectStr1);
                                //角色相关
                                NewVersionUserRoleEntry newVersionUserRoleEntry = newVersionUserRoleDao.getEntry(userId);
                                if (null != newVersionUserRoleEntry) {
                                    if(newVersionUserRoleEntry.getNewRole() == Constant.ONE||newVersionUserRoleEntry.getNewRole() == Constant.TWO){
                                        dto.setRole(1);
                                    }else{
                                        List<ObjectId> objectIdList1 = memberDao.getGroupIdsList(userId);
                                        if(objectIdList1.size()>0){
                                            dto.setRole(3);
                                        }else{
                                            dto.setRole(2);
                                        }
                                    }

                                }else{
                                    List<ObjectId> objectIdList1 = memberDao.getGroupIdsList(userId);
                                    if(objectIdList1.size()>0){
                                        dto.setRole(3);
                                    }else{
                                        dto.setRole(2);
                                    }
                                }
                                //模块相关
                                List<ModuleNumberEntry> moduleNumberEntries =  moduleNumberDao.getPageList(userId);
                                List<String> stringList1 = new ArrayList<String>();
                                if(moduleNumberEntries.size()>0){
                                    for(ModuleNumberEntry moduleNumberEntry : moduleNumberEntries){
                                        stringList1.add(moduleNumberEntry.getModuleName()+" "+moduleNumberEntry.getNumber()+" ");
                                    }
                                }
                                dto.setFunctionList(stringList1);

                                //频率相关
                                dto.setWeekNumber(loginLogDao.selectWeekNumber(entry.getUserName()));
                                dto.setMonthNumber(loginLogDao.selectMonthNumber(entry.getUserName()));
                                dto.setOnlineTime(entry.getStatisticTime());
                                BusinessManageEntry businessManageEntry1 = dto.buildAddEntry();
                                businessManageEntry1.setID(new ObjectId(dto.getId()));
                                businessManageEntry1.setCreateTime(entry.getRegisterTime());
                                businessManageDao.updEntry(businessManageEntry1);
                            }
                        }
                    }

                }catch(Exception e){
                    logger.error("error",e);
                }
            }
        }.start();
    }

    public static void createBusinessManage() {
        new Thread(){
            public void run() {
                BusinessManageDao businessManageDao = new BusinessManageDao();
                UserDao userDao = new UserDao();
                MemberDao memberDao = new MemberDao();
                CommunityDao communityDao =  new CommunityDao();
                ModuleNumberDao moduleNumberDao = new ModuleNumberDao();
                NewVersionSubjectDao newVersionSubjectDao = new NewVersionSubjectDao();
                NewVersionUserRoleDao newVersionUserRoleDao = new NewVersionUserRoleDao();
                LoginLogDao loginLogDao = new LoginLogDao();
                System.out.println("新的线程在执行...");
                try{
                    int size = 1;
                    int page = 1;
                    int pageSize = 10;
                    while(size>0){
                        List<BusinessManageEntry> businessManageEntries = businessManageDao.getPageList(page,pageSize);
                        size = businessManageEntries.size();
                        page++;
                        for(BusinessManageEntry businessManageEntry : businessManageEntries){
                            if(businessManageEntry!=null){
                                ObjectId userId = businessManageEntry.getUserId();
                                UserEntry entry = userDao.findByUserId(userId);
                                ThirdLoginDao thirdLoginDao = new ThirdLoginDao();
                                //社区相关
                                BusinessManageDTO dto = new BusinessManageDTO(businessManageEntry);
                                List<String> oids = memberDao.getMyCommunityIdsByUserId(userId);
                                dto.setCommunityIdList(oids);
                                List<ObjectId> objectIdList = new ArrayList<ObjectId>();
                                for(String str:oids){
                                    objectIdList.add(new ObjectId(str));
                                }
                                List<ObjectId> obids = memberDao.getMyRoleCommunityIdsByUserId(userId);
                                List<CommunityEntry> communityEntries = communityDao.findByObjectIds(objectIdList);
                                List<String> objectStr1 = new ArrayList<String>();//我创建的
                                List<String> objectStr2 = new ArrayList<String>();
                                for(CommunityEntry entry1 : communityEntries){
                                    if(entry1.getID() != null && obids.contains(entry1.getID())){
                                        objectStr1.add(entry1.getSearchId());
                                    }else{
                                        objectStr2.add(entry1.getSearchId());
                                    }
                                }
                                dto.setCommunityNumbers(objectStr2);
                                dto.setCommunityRoles(objectStr1);
                                //角色相关
                                NewVersionUserRoleEntry newVersionUserRoleEntry = newVersionUserRoleDao.getEntry(userId);
                                if (null != newVersionUserRoleEntry && newVersionUserRoleEntry.getNewRole() == Constant.ONE||newVersionUserRoleEntry.getNewRole() == Constant.TWO) {
                                    dto.setRole(1);
                                }else{
                                    List<ObjectId> objectIdList1 = memberDao.getGroupIdsList(userId);
                                    if(objectIdList1.size()>0){
                                        dto.setRole(3);
                                    }else{
                                        dto.setRole(2);
                                    }
                                }
                                //课程相关
                                List<String> stringList =  new ArrayList<String>();
                                NewVersionSubjectEntry newVersionSubjectEntry = newVersionSubjectDao.getEntryByUserId(userId);
                                if(null != newVersionSubjectEntry && newVersionSubjectEntry.getSubjectList()!=null){
                                    for(ObjectId objectId : newVersionSubjectEntry.getSubjectList()){
                                        stringList.add(objectId.toString());
                                    }
                                }
                                //模块相关
                                List<ModuleNumberEntry> moduleNumberEntries =  moduleNumberDao.getPageList(userId);
                                List<String> stringList1 = new ArrayList<String>();
                                if(moduleNumberEntries.size()>0){
                                    for(ModuleNumberEntry moduleNumberEntry : moduleNumberEntries){
                                        stringList1.add(moduleNumberEntry.getModuleName());
                                    }
                                }
                                dto.setFunctionList(stringList1);

                                //频率相关
                                dto.setWeekNumber(loginLogDao.selectWeekNumber(entry.getUserName()));
                                dto.setMonthNumber(loginLogDao.selectMonthNumber(entry.getUserName()));
                                dto.setOnlineTime(entry.getStatisticTime());
                                BusinessManageEntry businessManageEntry1 = dto.buildAddEntry();
                                businessManageEntry1.setCreateTime(entry.getRegisterTime());
                                businessManageDao.updEntry(businessManageEntry1);
                            }
                        }
                    }

                }catch(Exception e){
                    logger.error("error",e);
                }
            }
        }.start();
    }


    public static Map<String,String> getPhone(String phoneName){
        Map<String,String> map = new HashMap<String, String>();
        String url = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel="+phoneName;
        try {
            String result = callUrlByGet(url,"GBK");
            JSONObject json_test = JSONObject.fromObject(result);

            map.put("province", json_test.getString("province"));
            map.put("catName", json_test.getString("catName"));
        }catch (Exception e){
            map.put("province", "未知");
            map.put("catName", "未知");
        }
        return map;
    }
    public static String callUrlByGet(String callurl,String charset){
        String result = "";
        try {
            URL url = new URL(callurl);
            URLConnection connection = url.openConnection();
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),charset));
            String line;
            while((line = reader.readLine())!= null){
                result += line;
                result += "\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        if(result!=null&&!"".equals(result)){
            result = result.substring(result.indexOf("{"), (result.indexOf("}")+1) );
        }
        return result;
    }

    public static void sendMessage(final String contactId,final String userId,final int function,final int type,final String content) {
        new Thread(){
            public void run() {
                UnlawfulPictureTextDao unlawfulPictureTextDao = new UnlawfulPictureTextDao();
                System.out.println("新的线程在执行...");
                //System.out.println(prtNo);
                try{
                    CheckTextAndPicture.syncScanCheck(contactId,userId,function,type,content);
                }catch(Exception e){
                    UnlawfulPictureTextDTO dto = new UnlawfulPictureTextDTO();
                    dto.setType(type);
                    dto.setContent(content);
                    dto.setUserId(userId.toString());
                    dto.setFunction(function);
                    dto.setIsCheck(0);
                    dto.setContactId(contactId.toString());
                    unlawfulPictureTextDao.addEntry(dto.buildAddEntry());
                    logger.error("error",e);
                }
            }
        }.start();
    }
}
