package com.fulaan.excellentCourses.service;

import com.alibaba.druid.support.json.JSONUtils;
import com.db.excellentCourses.CoursesRoomDao;
import com.db.user.UserDao;
import com.fulaan.excellentCourses.api.CoursesRoomAPI;
import com.fulaan.excellentCourses.dto.CCLoginDTO;
import com.fulaan.excellentCourses.dto.ReplayDTO;
import com.fulaan.excellentCourses.util.RoomUtil;
import com.pojo.excellentCourses.CoursesRoomEntry;
import com.pojo.excellentCourses.UserCCLoinEntry;
import com.pojo.user.UserEntry;
import com.sys.props.Resources;
import com.sys.utils.AvatarUtils;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/**
 * 直播间相关
 * Created by James on 2018-05-08.
 */
@Service
public class CoursesRoomService {
    //userId
    private static final String CC_USERID = "A39B161DFE593520";
    //apikey
    private static final String CC_API_KEY = "s521Bt59I02irf0UKQjuwhpyCAyuYJNI";
    //直播模板  模板四 视频直播+聊天互动+直播文档
    private static final String CC_TEMPLATETYPE = "4";
    //验证方式  默认 0：接口验证，需要填写下面的checkurl；1：密码验证，需要填写下面的playpass；2：免密码验证
    private static final String CC_AUTHTYPE = "1";

    private static final String CC_BACKAUTHTYPE = "0";
    //验证地址
    private static final String CC_CHECKURL = "";
    //回调验证地址
   // private static final String CC_BACKCHECKURL = "http://appapi.jiaxiaomei.com/web/excellentCourses/openBackCreate.do";
    private static final String CC_BACKCHECKURL = Resources.getProperty("cc.login.back.url");
    //private static final String CC_BACKCHECKURL = "http://215q5w1385.iask.in:25460/web/excellentCourses/openBackCreate.do";
    //private static final String CC_BACKCHECKURL = "http://118.242.18.202:84/web/excellentCourses/openBackCreate.do";
    //讲师端密码
    private static final String CC_PUBLISHERPASS = "123456";
    //助教端密码
    private static final String CC_ASSISTANTPASS = "123456";
    //播放端密码
    private static final String CC_PLAYPASS = "123456";
    //提示语
    private static final String CC_PLAYERBACKGROUNDHINT = "直播时间还未开始，请耐心等待！";
    //  文档显示模式。1：适合窗口;2:适合宽度
    private static final String CC_DOCUMENTDISPLAYMODE = "2";
    //重复登录   repeatedloginsetting
    private static final String CC_REPEATEDLOGINSETTING = "0";

    private CoursesRoomDao coursesRoomDao = new CoursesRoomDao();

    private static final Logger logger =Logger.getLogger(CoursesRoomService.class);

    private UserDao userDao = new UserDao();

    //cc登陆回调
    public void backCreate(Map<String,String> map){


    }


    /**
     * 创建密码登陆直播间
     */
    public  void createCourses(String name,String description,ObjectId contactId,String dateTime){
        Map<String,String> map = new TreeMap<String, String>();
        map.put("userid",CC_USERID);
        map.put("templatetype",CC_TEMPLATETYPE);
        map.put("authtype",CC_AUTHTYPE);
        map.put("publisherpass",CC_PUBLISHERPASS);
        map.put("assistantpass",CC_ASSISTANTPASS);
        map.put("playpass",CC_PLAYPASS);
        map.put("checkurl",CC_CHECKURL);
        map.put("documentdisplaymode",CC_DOCUMENTDISPLAYMODE);
        String id = "";

        try{
            List<Map<String,String>> mapList = new ArrayList<Map<String, String>>();
            Map<String,String> alarms = new HashMap<String, String>();
            alarms.put("time","95");
            alarms.put("desc", "敬爱的老师,还有5分钟下课,请您注意休息");
            mapList.add(alarms);
            String str2 = JSONUtils.toJSONString(mapList);
            map.put("alarms", URLEncoder.encode(str2, "utf-8"));
            map.put("playerbackgroundhint", URLEncoder.encode(CC_PLAYERBACKGROUNDHINT, "utf-8"));
            map.put("name", URLEncoder.encode(name, "utf-8"));
            map.put("desc",URLEncoder.encode(description, "utf-8"));
            map.put("livestarttime", URLEncoder.encode(dateTime,"utf-8"));
            //处理
            String sysCode = RoomUtil.createHashedQueryString(map,CC_API_KEY);
            String str3 = URLDecoder.decode(sysCode, "utf-8");
            String str4 =  str3.substring(0,str3.indexOf("&"));
            String str6 = str4.substring(str3.indexOf("=")+1,str4.length());
            String str5 =  str3.substring(str3.indexOf("&")+1,str3.length());
           // String str =  CoursesRoomAPI.createRoom(str3);
            String str =  CoursesRoomAPI.createNewRoom(str5, str6);
            JSONObject dataJson = new JSONObject(str);
            String rows = dataJson.getString("result");
            if(rows.equals("OK")){
                JSONObject rows2 =dataJson.getJSONObject("room");
                id =  rows2.getString("id");
                CoursesRoomEntry coursesRoomEntry = new CoursesRoomEntry(
                        CC_USERID,
                        name,
                        id,
                        contactId,
                        description,
                        Integer.parseInt(CC_AUTHTYPE),
                        Integer.parseInt(CC_TEMPLATETYPE),
                        CC_PUBLISHERPASS,
                        CC_ASSISTANTPASS,
                        CC_PLAYPASS,
                        CC_CHECKURL,
                        dateTime,
                        CC_PLAYERBACKGROUNDHINT);
                coursesRoomDao.addEntry(coursesRoomEntry);
            }else{

            }

        }catch(Exception e){
            e.printStackTrace();

        }

    }
/*
参数	说明	备注
roomid	直播间id
userid	CC账户ID
pagenum	每页显示的个数	可选，系统默认值为50
pageindex	页码	可选，系统默认值为1
starttime	查询起始时间,如需按时间范围查询可添加该参数和下面的endtime参数，该查询是按直播的开始时间作为查询条件的。	可选，如果填写该参数则endtime参数必填；精确到分钟，例："2015-01-01 12:30"
endtime	查询截止时间	可选 ，如果填写该参数则starttime必填；精确到分钟，例："2015-01-02 12:30"
liveid	直播id*/
    /**
     * 获取回放列表
     */
    public List<ReplayDTO> getBackList(ObjectId cid,String userName,String teacherName,long stm,long etm,ObjectId userId){
        List<ReplayDTO> replayDTOList =  new ArrayList<ReplayDTO>();
        CoursesRoomEntry coursesRoomEntry = coursesRoomDao.getEntry(cid);
        if(coursesRoomEntry==null){
            return replayDTOList;
        }
        String roomid = coursesRoomEntry.getRoomId();
        Map<String,String> map = new TreeMap<String, String>();
        map.put("userid",CC_USERID);
        map.put("roomid",roomid);
        try{
            String sysCode = RoomUtil.createHashedQueryString(map,CC_API_KEY);
            String str3 = URLDecoder.decode(sysCode, "utf-8");
            String str =  CoursesRoomAPI.getRoomList(str3);
            JSONObject dataJson = new JSONObject(str);
            String rows = dataJson.getString("result");
            if(rows.equals("OK")){
                JSONArray jsonArray = dataJson.getJSONArray("records");
                if(jsonArray!=null&&jsonArray.length()>0) {
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject rows2 = jsonArray.getJSONObject(j);
                        String id =  rows2.getString("id");
                        String liveId =  rows2.getString("liveId");
                        String stopTime =  rows2.getString("stopTime");
                        String startTime =  rows2.getString("startTime");
                        int recordStatus =  rows2.getInt("recordStatus");
                        String recordVideoId =  rows2.getString("recordVideoId");
                        String replayUrl =  rows2.getString("replayUrl");
                        String password = CC_PLAYPASS;
                        if(coursesRoomEntry.getAuthtype()==0){//接口认证
                            password = userId.toString();
                        }
                        ReplayDTO dto = new ReplayDTO(id,liveId,roomid,recordVideoId,CC_USERID,userName,password,startTime,stopTime,recordStatus,replayUrl,teacherName);
                        if(checkTime(stm,etm,startTime,stopTime)){
                            replayDTOList.add(dto);
                        }

                    }
                }
            }else{
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        Collections.reverse(replayDTOList);
        return replayDTOList;

    }

    /**
     * 获取回放列表
     */
    public List<ReplayDTO> getAllBackList(ObjectId cid,String userName,String teacherName,ObjectId userId){
        List<ReplayDTO> replayDTOList =  new ArrayList<ReplayDTO>();
        CoursesRoomEntry coursesRoomEntry = coursesRoomDao.getEntry(cid);
        if(coursesRoomEntry==null){
            return replayDTOList;
        }
        String roomid = coursesRoomEntry.getRoomId();
        Map<String,String> map = new TreeMap<String, String>();
        map.put("userid",CC_USERID);
        map.put("roomid",roomid);
        try{
            String sysCode = RoomUtil.createHashedQueryString(map,CC_API_KEY);
            String str3 = URLDecoder.decode(sysCode, "utf-8");
            String str =  CoursesRoomAPI.getRoomList(str3);
            JSONObject dataJson = new JSONObject(str);
            String rows = dataJson.getString("result");
            if(rows.equals("OK")){
                JSONArray jsonArray = dataJson.getJSONArray("records");
                if(jsonArray!=null&&jsonArray.length()>0) {
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject rows2 = jsonArray.getJSONObject(j);
                        String id =  rows2.getString("id");
                        String liveId =  rows2.getString("liveId");
                        String stopTime =  rows2.getString("stopTime");
                        String startTime =  rows2.getString("startTime");
                        int recordStatus =  rows2.getInt("recordStatus");
                        String recordVideoId =  rows2.getString("recordVideoId");
                        String replayUrl =  rows2.getString("replayUrl");
                        String password = CC_PLAYPASS;
                        if(coursesRoomEntry.getAuthtype()==0){//接口认证
                            password = userId.toString();
                        }
                        ReplayDTO dto = new ReplayDTO(id,liveId,roomid,recordVideoId,CC_USERID,userName,password,startTime,stopTime,recordStatus,replayUrl);
                        replayDTOList.add(dto);
                    }
                }
            }else{
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        Collections.reverse(replayDTOList);
        return replayDTOList;

    }

    /**
     * 获取回放列表
     */
    public void getOneBack(){
        List<ReplayDTO> replayDTOList =  new ArrayList<ReplayDTO>();

        Map<String,String> map = new TreeMap<String, String>();
        map.put("userid",CC_USERID);
        map.put("recordid","3965C7840E3C5234");
        try{
            String sysCode = RoomUtil.createHashedQueryString(map,CC_API_KEY);
            String str3 = URLDecoder.decode(sysCode, "utf-8");
            String str =  CoursesRoomAPI.getOneBack(str3);
            JSONObject dataJson = new JSONObject(str);
            String rows = dataJson.getString("result");
            if(rows.equals("OK")){
                JSONObject rows2 = dataJson.getJSONObject("record");
                String id =  rows2.getString("id");
                String liveId =  rows2.getString("liveId");
                String stopTime =  rows2.getString("stopTime");
                String startTime =  rows2.getString("startTime");
                int recordStatus =  rows2.getInt("recordStatus");
                String recordVideoId =  rows2.getString("recordVideoId");
                String replayUrl =  rows2.getString("replayUrl");
            }else{
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取某个课节的在线情况
     */
    public void getUserList(){
        Map<String,String> map = new TreeMap<String, String>();
        map.put("userid",CC_USERID);
        map.put("roomid","E288A331245C1F7C9C33DC5901307461");

        try{
            map.put("starttime",URLEncoder.encode("2018-08-20 08:00", "utf-8"));

            map.put("endtime",URLEncoder.encode("2018-08-25 08:00", "utf-8"));

            String sysCode = RoomUtil.createHashedQueryString(map,CC_API_KEY);
            String str3 = URLDecoder.decode(sysCode, "utf-8");
            String str =  CoursesRoomAPI.getUserList(str3);
            JSONObject dataJson = new JSONObject(str);
            String rows = dataJson.getString("result");
            if(rows.equals("OK")){
                JSONArray rows2 = dataJson.getJSONArray("userActions");
                FileOutputStream bos = new FileOutputStream("D://output.txt");
                System.setOut(new PrintStream(bos));
                System.out.println(rows2.toString());
                for(int i = 0;i<rows2.length();i++){

                    /*"userId": "0cda7ng03j9502ian",
          "userName": "苍井满",
          "userIp": "9.5.2.7",
          "enterTime": "2016-11-28 20:30:30",
          "leaveTime": "2016-11-28 20:33:61"*/

                }
            }else{
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 批量获取
     */
    public List<UserCCLoinEntry> getAllUserList(String roomId,ObjectId contactId,long startTime,long endTime){
        Map<String,String> map = new TreeMap<String, String>();
        map.put("userid",CC_USERID);
        map.put("roomid",roomId);
        List<UserCCLoinEntry> entries = new ArrayList<UserCCLoinEntry>();
        try{
            String stm = DateTimeUtils.getLongToStrTimeTwo(startTime).substring(0,17);
            String etm = DateTimeUtils.getLongToStrTimeTwo(endTime).substring(0,17);
            map.put("starttime",URLEncoder.encode(stm, "utf-8"));

            map.put("endtime",URLEncoder.encode(etm, "utf-8"));

            String sysCode = RoomUtil.createHashedQueryString(map,CC_API_KEY);
            String str3 = URLDecoder.decode(sysCode, "utf-8");
            String str =  CoursesRoomAPI.getUserList(str3);
            JSONObject dataJson = new JSONObject(str);
            String rows = dataJson.getString("result");
            if(rows.equals("OK")){
                JSONArray rows2 = dataJson.getJSONArray("userActions");
                for(int i = 0;i<rows2.length();i++){
                    JSONObject rows3 = rows2.getJSONObject(i);
                    String uid = rows3.getString("userId");
                    if(ObjectId.isValid(uid)){
                        UserCCLoinEntry userCCLoinEntry = new UserCCLoinEntry(
                                roomId,
                                new ObjectId(uid),
                                contactId,
                                rows3.getString("userName"),
                                rows3.getString("userIp"),
                                rows3.getString("enterTime"),
                                rows3.getString("leaveTime")
                                );
                        entries.add(userCCLoinEntry);
                    }
                }
            }else{
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return entries;
    }

    /**
     * 获取直播间状态
     */
    public int getRoomStatus(String roomId){
        Map<String,String> map = new TreeMap<String, String>();
        map.put("userid",CC_USERID);
        map.put("roomids","C649821FE31E44509C33DC5901307461");
        //map.put("roomids",roomId);
        int status = 0;
        try{
            map.put("starttime",URLEncoder.encode("2018-07-16 08:00", "utf-8"));

            map.put("endtime",URLEncoder.encode("2018-07-23 08:00", "utf-8"));

            String sysCode = RoomUtil.createHashedQueryString(map,CC_API_KEY);
            String str3 = URLDecoder.decode(sysCode, "utf-8");
            String str =  CoursesRoomAPI.getRoomStatus(str3);
            JSONObject dataJson = new JSONObject(str);
            String rows = dataJson.getString("result");
            if(rows.equals("OK")){
                JSONArray rows2 = dataJson.getJSONArray("rooms");
                for(int i = 0;i<rows2.length();i++){
                    JSONObject rows3 = rows2.getJSONObject(i);
                    status = rows3.getInt("liveStatus");

                }
            }else{
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return status;
    }

    /**
     * 获取直播间回看状况
     */
    public int getCoursesBackList(){
        Map<String,String> map = new TreeMap<String, String>();
        map.put("userid",CC_USERID);
        //map.put("recordid","C649821FE31E44509C33DC5901307461");
        map.put("pageindex","1");
        map.put("pagenum","1000");
        //map.put("roomids",roomId);
        int status = 0;
        try{
            map.put("starttime",URLEncoder.encode("2018-10-06 08:00", "utf-8"));

            map.put("endtime",URLEncoder.encode("2018-10-22 08:00", "utf-8"));

            String sysCode = RoomUtil.createHashedQueryString(map,CC_API_KEY);
            String str3 = URLDecoder.decode(sysCode, "utf-8");
            String str =  CoursesRoomAPI.getRoomBackStatus(str3);
            JSONObject dataJson = new JSONObject(str);
            String rows = dataJson.getString("result");
            if(rows.equals("OK")){
                JSONArray rows2 = dataJson.getJSONArray("userActions");
                FileOutputStream bos = new FileOutputStream("D://output2.txt");
                System.setOut(new PrintStream(bos));
                System.out.println(rows2.toString());
                /*JSONArray rows2 = dataJson.getJSONArray("rooms");
                for(int i = 0;i<rows2.length();i++){
                    JSONObject rows3 = rows2.getJSONObject(i);
                    status = rows3.getInt("liveStatus");

                }*/
            }else{
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return status;
    }

    public boolean checkTime(long stm,long etm,String ostm,String estm){
        if(ostm==null || ostm.equals("")){
            return false;
        }
        if(estm==null || estm.equals("")){
            return false;
        }
        long stm2 = DateTimeUtils.getStrToLongTime(ostm, "yyyy-MM-dd HH:mm:ss");
        long etm2 = DateTimeUtils.getStrToLongTime(estm, "yyyy-MM-dd HH:mm:ss");
        if(stm2>stm && etm2<etm ){//1
            return true;
        }

        if(etm2 > stm && etm2 < etm ){//2
            return true;
        }

        if(stm2 < stm && etm2 > etm){//3
            return true;
        }

        if(stm2 >stm && stm2 < etm){//4
            return true;
        }
        if(stm==stm2 || stm== etm2 || etm == stm2 || etm ==etm2){
            return true;
        }
        return false;
    }


    public static void main(String[] args){
        CoursesRoomService coursesRoomService = new CoursesRoomService();
        coursesRoomService.getCoursesBackList();
    }



    /**
     * 创建回调登陆直播间
     */
    public  void createBackCourses(String name,String description,ObjectId contactId,String dateTime,int minute){
        Map<String,String> map = new TreeMap<String, String>();
        map.put("userid",CC_USERID);
        map.put("templatetype",CC_TEMPLATETYPE);
        map.put("authtype",CC_BACKAUTHTYPE);
        map.put("publisherpass",CC_PUBLISHERPASS);
        map.put("assistantpass",CC_ASSISTANTPASS);
        map.put("playpass",CC_PLAYPASS);
        map.put("documentdisplaymode",CC_DOCUMENTDISPLAYMODE);
        map.put("repeatedloginsetting",CC_REPEATEDLOGINSETTING);
        String id = "";
        try{
            List<Map<String,String>> mapList = new ArrayList<Map<String, String>>();
            Map<String,String> alarms = new HashMap<String, String>();
            alarms.put("time",minute+"");
            alarms.put("desc", "敬爱的老师,还有5分钟下课,请您注意休息");
            mapList.add(alarms);
            String str2 = JSONUtils.toJSONString(mapList);
            map.put("alarms", URLEncoder.encode(str2, "utf-8"));
            map.put("checkurl",URLEncoder.encode(CC_BACKCHECKURL, "utf-8"));
            map.put("playerbackgroundhint", URLEncoder.encode(CC_PLAYERBACKGROUNDHINT, "utf-8"));
            map.put("name", URLEncoder.encode(name, "utf-8"));
            map.put("desc",URLEncoder.encode(description, "utf-8"));
            map.put("livestarttime", URLEncoder.encode(dateTime,"utf-8"));
            String sysCode = RoomUtil.createHashedQueryString(map,CC_API_KEY);
            String str3 = URLDecoder.decode(sysCode, "utf-8");
            String str4 =  str3.substring(0,str3.indexOf("&"));
            String str6 = str4.substring(str3.indexOf("=")+1,str4.length());
            String str5 =  str3.substring(str3.indexOf("&")+1,str3.length());
            // String str =  CoursesRoomAPI.createRoom(str3);
            String str =  CoursesRoomAPI.createNewRoom(str5, str6);
            JSONObject dataJson = new JSONObject(str);
            String rows = dataJson.getString("result");
            if(rows.equals("OK")){
                JSONObject rows2 =dataJson.getJSONObject("room");
                id =  rows2.getString("id");
                CoursesRoomEntry coursesRoomEntry = new CoursesRoomEntry(
                        CC_USERID,
                        name,
                        id,
                        contactId,
                        description,
                        Integer.parseInt(CC_BACKAUTHTYPE),
                        Integer.parseInt(CC_TEMPLATETYPE),
                        CC_PUBLISHERPASS,
                        CC_ASSISTANTPASS,
                        CC_PLAYPASS,
                        CC_BACKCHECKURL,
                        dateTime,
                        CC_PLAYERBACKGROUNDHINT);
                coursesRoomDao.addEntry(coursesRoomEntry);
            }else{
                logger.error(rows);
            }

        }catch(Exception e){
            e.printStackTrace();
            logger.error("error",e);
        }

    }

    /**
     * 处理cc接口登陆的验证信息
     * @return
    result	字符串	验证结果，除“ok”外，云平台将其他结果均认为验证失败，即不允许登陆
    message	字符串	当用户不能登陆（验证结果不为“ok”）时，展示给登陆用户的提示信息。(长度不能超过40个字符)
    id	字符串	用户的唯一标示(长度不能超过40个字符)
    name	字符串	用户的名称，在聊天室中显示该名称(长度不能超过20个字符)
    avatar	字符串	可选，用户的头像，在直播页面中显示该用户头像信息(长度不能超过400个字符，如果超过400个字符，登录会提示参数错误)
    customua	字符串	可选，用户自定义UA信息（该信息不能包含\、/、|等特殊字符，长度不能超过50个字符）
    viewercustommark	字符串	可选，用户自定义标识信息（该信息不能包含\、/、|等特殊字符，长度不能超过300个字符）
    marquee	字符串	可选，跑马灯信息(长度不能超过2000个字符)
     */
    public Map<String,Object> openBackCreate(CCLoginDTO ccLoginDTO){
        Map<String,Object> map = new HashMap<String, Object>();
        String userId = ccLoginDTO.getUserid();
        String roomId = ccLoginDTO.getRoomid();
        String viewerName = ccLoginDTO.getViewername();
        String viewerToken = ccLoginDTO.getViewertoken();
        logger.error("登陆回调："+userId+"--"+roomId+"--"+viewerName+"--"+viewerToken);
        map.put("result","no");
        map.put("message","登陆失败");
        Map<String,Object> userMap = new HashMap<String, Object>();
        if(userId.equals(CC_USERID)){//验证cc用户通过
            CoursesRoomEntry coursesRoomEntry = coursesRoomDao.getRoomEntry(roomId);
            if(coursesRoomEntry!=null){//验证直播间存在
                if(coursesRoomEntry.getAuthtype()==0){
                    if(ObjectId.isValid(viewerToken)){//验证码是否为用户id
                        UserEntry userEntry = userDao.findByUserId(new ObjectId(viewerToken));
                        if(userEntry!=null){//验证用户成功
                            map.put("result","ok");
                            map.put("message","登陆成功");
                            userMap.put("id",viewerToken);
                            String name = StringUtils.isNotBlank(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
                            if(viewerName !=null && !viewerName.equals("")){
                                name = viewerName;
                            }
                            userMap.put("name",name);
                            userMap.put("avatar", AvatarUtils.getAvatar(userEntry.getAvatar(),userEntry.getRole(),userEntry.getSex()));
                            userMap.put("customua","");
                            userMap.put("viewercustommark","");
                            userMap.put("marquee","");
                        }
                    }else{
                        map.put("result","ok");
                        map.put("message","登陆成功");
                        userMap.put("id",new ObjectId().toString());
                        String name = "用户";
                        userMap.put("name",name);
                        userMap.put("avatar", "http://doc.k6kt.com/head-0.7453231568799419.jpg");
                        userMap.put("customua","");
                        userMap.put("viewercustommark","");
                        userMap.put("marquee","");
                    }
                }else{
                    map.put("result","ok");
                    map.put("message","登陆成功");
                    userMap.put("id",new ObjectId().toString());
                    String name = "用户";
                    userMap.put("name",name);
                    userMap.put("avatar", "http://doc.k6kt.com/head-0.7453231568799419.jpg");
                    userMap.put("customua","");
                    userMap.put("viewercustommark","");
                    userMap.put("marquee","");
                }

            }
        }
        map.put("user",userMap);
        logger.error("登陆成功");
        return map;
    }
    //会看id
    public Map<String,Object> openLookBackCreate(CCLoginDTO ccLoginDTO){
        Map<String,Object> map = new HashMap<String, Object>();
        String userId = ccLoginDTO.getUserid();
        String roomId = ccLoginDTO.getRoomid();
        String viewerName = ccLoginDTO.getViewername();
        String viewerToken = ccLoginDTO.getViewertoken();
        logger.error("登陆回调："+userId+"--"+roomId+"--"+viewerName+"--"+viewerToken);
        map.put("result","no");
        map.put("message","登陆失败");
        Map<String,Object> userMap = new HashMap<String, Object>();
        if(userId.equals(CC_USERID)){//验证cc用户通过
            CoursesRoomEntry coursesRoomEntry = coursesRoomDao.getRoomEntry(roomId);
            if(coursesRoomEntry!=null){//验证直播间存在
                if(ObjectId.isValid(viewerToken)){//验证码是否为用户id
                    UserEntry userEntry = userDao.findByUserId(new ObjectId(viewerToken));
                    if(userEntry!=null){//验证用户成功
                        map.put("result","ok");
                        map.put("message","登陆成功");
                        userMap.put("id",viewerToken);
                        String name = StringUtils.isNotBlank(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
                        userMap.put("name",name);
                        userMap.put("avatar", AvatarUtils.getAvatar(userEntry.getAvatar(),userEntry.getRole(),userEntry.getSex()));
                        userMap.put("customua","");
                        userMap.put("viewercustommark","");
                        userMap.put("marquee","");
                    }
                }
            }
        }
        map.put("user",userMap);
        logger.error("登陆成功");
        return map;
    }
}
