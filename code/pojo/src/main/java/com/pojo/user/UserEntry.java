package com.pojo.user;


import com.mongodb.BasicDBList;
import com.pojo.app.IdValuePair;
import com.pojo.utils.MongoUtils;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户信息表,包括以前系统学生和老师等信息
 * <pre>
 * collectionName:users
 * </pre>
 * <pre>
 * {
 *  nm:用户名称
 *  pw:密码
 *  sex: 0女 1 男  -1：未知
 *  nnm:昵称
 *  r:角色
 *  per:角色外具有的权限；用“,”分割
 *  rper:角色内去除的权限；用“,”分割
 *  mn:手机
 *  pn:电话
 *  e:邮箱
 *  qq:
 *  wx:微信
 *  wb:微博
 *  add:地址
 *  pc:邮编
 *  by:血型
 *  rt:注册时间,long
 *  ri:注册IP
 *  si:学校ID
 *  bal:余额;迁移至mysql
 *  bir:生日.long
 *  lad:最后登录日期，long
 *  exp:经验值
 *  cid:关联账号(如果是家长，此字段是子女ID；如果是子女，此字段是家长ID)
 *  [
 *
 *  ]
 *  avt:头像
 *  gli:群组列表;IdValuePair
 *    [
 *        {
 *         id:String
 *         v:群名称
 *        }
 *    ]
 *
 *    kt:是否是k6kt用户  0不是  1是
 *
 *
 *  //----------学生字段begin----------
 *  sn:学号
 *  rn:学籍号
 *  //----------学生字段end----------
 *
 *
 *  //----------老师字段begin----------
 *  int:简介
 *  jnb:工号
 *  //----------老师字段end----------
 *
 *  syn：是否需要同步 0:需要 1已经完成
 *  ir：是否被删除 1已经被删除 0 没有
 *  chatid : 环信聊天id
 *
 *  shd: 微家园更新时间
 *  fhd: 微校园更新时间
 *  ijy:禁言
 *  jydt:禁言时间
 *
 *
 *  //----------学籍管理begin----------
 *  民族 : sra(studentRace)
 *  健康状态 : she(studentHealth)
 *  户籍地址 : rad(residenceAddress)
 *  变动日期：cdt(changeDate)
 *  变动说明：cps(changePostscript)
 *  新学校     : nsc(newSchool)
 *  //----------学籍管理end----------
 *
 *
 *  uty:用户类型 0默认用户  1：有效时间用户
 *  vabt:此用户的有效开始时间
 *  vatl:此用户的有效时长，从第一次开始登录算起，单位s(秒)
 *  pos:编制
 *  posdec:职务
 *  sis:学校集合.用于学校分校区情况，每个校区都单独看作一个学校;用于大校长，分区校长不用；
 *  [
 *
 *  ]
 *  logn:登录名
 *  bnm:绑定用户名 用于ahedu.cn的绑定
 *  psc:postCount 发帖数及回帖数总和
 *  frs:forumScore 论坛积分
 *  fexp:forumExperience 论坛经验值
 *
 *  ivp:interviewIP 上次访问IP
 *  sti:statisticTime 统计在线时间
 *  iti：interviewTime 上次登录时间
 *  qti：quitTime 上次退出时间
 *  pti: interviewPostTime 上次发表时间
 *
 *  //用户管理
 *  sls:silencedStatus 禁言状态0:正常 状态 1:禁止发言 2：禁止访问
 *  slti:silencedTime 禁言时间
 *  slr:silencedReason 禁言理由
 *  slt:silencedTen 禁言时间状态 0：永久 1：一天
 *
 *  //邮箱激活
 *  ems:emailStatus 邮箱激活状态1：激活 0：未激活
 *  //激活码
 *  emv:emailValidateCode 邮箱激活码
 *
 *  //新添加
 *  ieasd: 是否注册了环信id  0:没有,1注册了
 *  comms: 我的社区--包含我加入的和我创建的
 *
 *  usts: 用户标签  {cd:1,tag:'跑步‘}
 *
 *  qrCode:个人二维码
 *
 *
 * }
 * </pre>
 *
 * @author fourer
 */
public class UserEntry extends BaseDBObject {

    /**
     *
     */
    private static final long serialVersionUID = 4002785167984267964L;

    public UserEntry() {
    }

    public UserEntry(DBObject dbo) {
        this((BasicDBObject) dbo);
    }


    public UserEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }


    /**
     * 这个构造方法用来保存第三方登陆的用户
     *
     * @param userName
     * @param nickName
     * @param sex
     */
    public UserEntry(String userName, String nickName, String password, String avatar, int sex) {

        this(userName,  //userName
                password,  //password
                sex,    //sex
                nickName, //nickName
                Constant.DEFAULT_VALUE_INT,//role
                Constant.EMPTY,//permission
                Constant.EMPTY,//removePermission
                Constant.EMPTY,//mobileNumber
                Constant.EMPTY, //phoneNumber
                Constant.EMPTY, //email
                Constant.EMPTY, //qq
                Constant.EMPTY, //weiXin
                Constant.EMPTY, //weiBo
                Constant.EMPTY, //address
                Constant.EMPTY, //postCode
                Constant.NEGATIVE_ONE, //bloodType
                System.currentTimeMillis(), //registerTime
                Constant.EMPTY, //registerIP
                null, //schoolID
                Constant.DEFAULT_VALUE_LONG, //birthDate
                0L, //lastActiveDate
                Constant.ZERO, //experiencevalue
                Constant.EMPTY,//studyNum
                Constant.EMPTY,//registerNum
                Constant.EMPTY,//job
                Constant.EMPTY,//introduce
                Constant.EMPTY,//jobnumber
                Constant.SYN_YES_NEED,//syn，
                null, //connectId,
                avatar, //avatar
                null,
                Constant.DEFAULT_VALUE_LONG,
                Constant.DEFAULT_VALUE_LONG,
                Constant.DEFAULT_VALUE_INT,
                Constant.DEFAULT_VALUE_LONG,
                1,
                Constant.EMPTY, 0);
    }

    public UserEntry(String userName, String nickName, String password, int sex, String qq) {
        this(userName,  //userName
                password,  //password
                sex,    //sex
                nickName, //nickName
                Constant.DEFAULT_VALUE_INT,//role
                Constant.EMPTY,//permission
                Constant.EMPTY,//removePermission
                Constant.EMPTY,//mobileNumber
                Constant.EMPTY, //phoneNumber
                Constant.EMPTY, //email
                qq, //qq
                Constant.EMPTY, //weiXin
                Constant.EMPTY, //weiBo
                Constant.EMPTY, //address
                Constant.EMPTY, //postCode
                Constant.NEGATIVE_ONE, //bloodType
                System.currentTimeMillis(), //registerTime
                Constant.EMPTY, //registerIP
                null, //schoolID
                Constant.DEFAULT_VALUE_LONG, //birthDate
                0L, //lastActiveDate
                Constant.ZERO, //experiencevalue
                Constant.EMPTY,//studyNum
                Constant.EMPTY,//registerNum
                Constant.EMPTY,//job
                Constant.EMPTY,//introduce
                Constant.EMPTY,//jobnumber
                Constant.SYN_YES_NEED,//syn，
                null, //connectId,
                Constant.EMPTY, //avatar
                null,
                Constant.DEFAULT_VALUE_LONG,
                Constant.DEFAULT_VALUE_LONG,
                Constant.DEFAULT_VALUE_INT,
                Constant.DEFAULT_VALUE_LONG,
                1,
                Constant.EMPTY, 0);
    }

    public UserEntry(String userName, String password,
                     String mobile, String email) {
        this(userName,  //userName
                password,  //password
                Constant.NEGATIVE_ONE,    //sex
                Constant.EMPTY, //nickName
                Constant.DEFAULT_VALUE_INT,//role
                Constant.EMPTY,//permission
                Constant.EMPTY,//removePermission
                mobile,//mobileNumber
                Constant.EMPTY, //phoneNumber
                email, //email
                Constant.EMPTY, //qq
                Constant.EMPTY, //weiXin
                Constant.EMPTY, //weiBo
                Constant.EMPTY, //address
                Constant.EMPTY, //postCode
                Constant.NEGATIVE_ONE, //bloodType
                System.currentTimeMillis(), //registerTime
                Constant.EMPTY, //registerIP
                null, //schoolID
                Constant.DEFAULT_VALUE_LONG, //birthDate
                0L, //lastActiveDate
                Constant.ZERO, //experiencevalue
                Constant.EMPTY,//studyNum
                Constant.EMPTY,//registerNum
                Constant.EMPTY,//job
                Constant.EMPTY,//introduce
                Constant.EMPTY,//jobnumber
                Constant.SYN_YES_NEED,//syn，
                null, //connectId,
                Constant.EMPTY, //avatar
                null,
                Constant.DEFAULT_VALUE_LONG,
                Constant.DEFAULT_VALUE_LONG,
                Constant.DEFAULT_VALUE_INT,
                Constant.DEFAULT_VALUE_LONG,
                1,
                Constant.EMPTY, 0);
    }

    public UserEntry(String userName, String password,
                     String mobile, String email,String nickName) {
        this(userName,  //userName
                password,  //password
                Constant.NEGATIVE_ONE,    //sex
                nickName, //nickName
                Constant.DEFAULT_VALUE_INT,//role
                Constant.EMPTY,//permission
                Constant.EMPTY,//removePermission
                mobile,//mobileNumber
                Constant.EMPTY, //phoneNumber
                email, //email
                Constant.EMPTY, //qq
                Constant.EMPTY, //weiXin
                Constant.EMPTY, //weiBo
                Constant.EMPTY, //address
                Constant.EMPTY, //postCode
                Constant.NEGATIVE_ONE, //bloodType
                System.currentTimeMillis(), //registerTime
                Constant.EMPTY, //registerIP
                null, //schoolID
                Constant.DEFAULT_VALUE_LONG, //birthDate
                0L, //lastActiveDate
                Constant.ZERO, //experiencevalue
                Constant.EMPTY,//studyNum
                Constant.EMPTY,//registerNum
                Constant.EMPTY,//job
                Constant.EMPTY,//introduce
                Constant.EMPTY,//jobnumber
                Constant.SYN_YES_NEED,//syn，
                null, //connectId,
                Constant.EMPTY, //avatar
                null,
                Constant.DEFAULT_VALUE_LONG,
                Constant.DEFAULT_VALUE_LONG,
                Constant.DEFAULT_VALUE_INT,
                Constant.DEFAULT_VALUE_LONG,
                1,
                Constant.EMPTY, 0);
    }


    public UserEntry(String userName, String password,
                     int sex, List<IdValuePair> groupInfoList) {
        this(userName,  //userName
                password,  //password
                sex,    //sex
                Constant.EMPTY, //nickName
                Constant.DEFAULT_VALUE_INT,//role
                Constant.EMPTY,//permission
                Constant.EMPTY,//removePermission
                Constant.EMPTY,//mobileNumber
                Constant.EMPTY, //phoneNumber
                Constant.EMPTY, //email
                Constant.EMPTY, //qq
                Constant.EMPTY, //weiXin
                Constant.EMPTY, //weiBo
                Constant.EMPTY, //address
                Constant.EMPTY, //postCode
                Constant.NEGATIVE_ONE, //bloodType
                System.currentTimeMillis(), //registerTime
                Constant.EMPTY, //registerIP
                null, //schoolID
                Constant.DEFAULT_VALUE_LONG, //birthDate
                0L, //lastActiveDate
                Constant.ZERO, //experiencevalue
                Constant.EMPTY,//studyNum
                Constant.EMPTY,//registerNum
                Constant.EMPTY,//job
                Constant.EMPTY,//introduce
                Constant.EMPTY,//jobnumber
                Constant.SYN_YES_NEED,//syn，
                null, //connectId,
                Constant.EMPTY, //avatar
                groupInfoList,
                Constant.DEFAULT_VALUE_LONG,
                Constant.DEFAULT_VALUE_LONG,
                Constant.DEFAULT_VALUE_INT,
                Constant.DEFAULT_VALUE_LONG,
                1,
                Constant.EMPTY, 0);
    }

    public UserEntry(String userName, String password,
                     int sex, List<IdValuePair> groupInfoList, String postionDec) {
        this(userName,  //userName
                password,  //password
                sex,    //sex
                Constant.EMPTY, //nickName
                Constant.DEFAULT_VALUE_INT,//role
                Constant.EMPTY,//permission
                Constant.EMPTY,//removePermission
                Constant.EMPTY,//mobileNumber
                Constant.EMPTY, //phoneNumber
                Constant.EMPTY, //email
                Constant.EMPTY, //qq
                Constant.EMPTY, //weiXin
                Constant.EMPTY, //weiBo
                Constant.EMPTY, //address
                Constant.EMPTY, //postCode
                Constant.NEGATIVE_ONE, //bloodType
                System.currentTimeMillis(), //registerTime
                Constant.EMPTY, //registerIP
                null, //schoolID
                Constant.DEFAULT_VALUE_LONG, //birthDate
                0L, //lastActiveDate
                Constant.ZERO, //experiencevalue
                Constant.EMPTY,//studyNum
                Constant.EMPTY,//registerNum
                Constant.EMPTY,//job
                Constant.EMPTY,//introduce
                Constant.EMPTY,//jobnumber
                Constant.SYN_YES_NEED,//syn，
                null, //connectId,
                Constant.EMPTY, //avatar
                groupInfoList,
                Constant.DEFAULT_VALUE_LONG,
                Constant.DEFAULT_VALUE_LONG,
                Constant.DEFAULT_VALUE_INT,
                Constant.DEFAULT_VALUE_LONG,
                1,
                postionDec, 0);
    }


    public UserEntry(String userName, String password,
                     int sex, String nickName, int role, String permission, String removePermission,
                     String mobileNumber, String phoneNumber, String email, String qq,
                     String weiXin, String weiBo, String address, String postCode,
                     int bloodType, long registerTime, String registerIP, ObjectId schoolID,
                     long birthDate, long lastActiveDate,
                     int experiencevalue, String studyNum, String registerNum, String job,

                     String introduce, String jobnumber, int syn, List<ObjectId> connectId, String avatar, List<IdValuePair> groupInfoList, long schoolHomeDate, long familyHomeDate, int jinyan, long jinyanDate, int postion, String postionDec, int chat) {

        super();

        BasicDBObject dbo = new BasicDBObject()
                .append("nm", userName.toLowerCase())
                .append("pw", password)
                .append("sex", sex)
                .append("nnm", nickName)
                .append("r", role)
                .append("per", permission)
                .append("rper", removePermission)
                .append("mn", mobileNumber)
                .append("pn", phoneNumber)
                .append("e", email)
                .append("qq", qq)
                .append("wx", weiXin)
                .append("wb", weiBo)
                .append("add", address)
                .append("pc", postCode)
                .append("by", bloodType)
                .append("rt", registerTime)
                .append("ri", registerIP)
                .append("si", schoolID)
                .append("bir", birthDate)
                .append("lad", lastActiveDate)
                .append("exp", experiencevalue)
                .append("sn", studyNum)
                .append("rn", registerNum)
                .append("jo", job)
                .append("int", introduce)
                .append("jnb", jobnumber)
                .append(Constant.FIELD_SYN, syn)
                .append("cid", MongoUtils.convert(connectId))
                .append("avt", avatar)
                .append("gli", MongoUtils.convert(MongoUtils.fetchDBObjectList(groupInfoList)))
                .append("ir", Constant.ZERO)
                .append("chatid", Constant.EMPTY)
                .append("shd", schoolHomeDate)
                .append("fhd", familyHomeDate)
                .append("ijy", jinyan)
                .append("jydt", jinyanDate)
                .append("pos", postion)
                .append("posdec", postionDec)
                .append("ic", chat);


        setBaseEntry(dbo);
    }

    public int getPostion() {
        return getSimpleIntegerValueDef("pos", 1);
    }

    public void setPostion(int postion) {
        setSimpleValue("pos", postion);
    }

    public String getChatId() {
        String chatid = getSimpleStringValue("chatid");
        if (StringUtils.isBlank(chatid)) {
            ObjectId id = getID();
            if (null != id) {
                return id.toString();
            }
            return Constant.EMPTY;
        }
        return chatid;
    }

    public void setChatId(String chatId) {
        setSimpleValue("chatid", chatId);
    }

    public String getUserName() {
        return getSimpleStringValue("nm");
    }

    public void setUserName(String userName) {
        setSimpleValue("nm", userName);
    }

    public String getPassword() {
        return getSimpleStringValue("pw");
    }

    public void setPassword(String password) {
        setSimpleValue("pw", password);
    }

    public int getSex() {
        return getSimpleIntegerValue("sex");
    }

    public void setSex(int sex) {
        setSimpleValue("sex", sex);
    }

    public String getNickName() {
        return getSimpleStringValue("nnm");
    }

    public boolean isRegisterHuanXin() {
        if (this.getBaseEntry().containsField("ieasd")) {
            return getSimpleIntegerValue("ieasd") == 1;
        }
        return false;
    }

    public void setNickName(String nickName) {
        setSimpleValue("nnm", nickName);
    }

    public int getRole() {
        return getSimpleIntegerValueDef("r", 0);
    }

    public void setRole(int role) {
        setSimpleValue("r", role);
    }

    public String getPermission() {
        return getSimpleStringValue("per");
    }

    public void setPermission(String permission) {
        setSimpleValue("per", permission);
    }

    public String getRemovePermission() {
        return getSimpleStringValue("rper");
    }

    public void setRemovePermission(String removePermission) {
        setSimpleValue("rper", removePermission);
    }

    public String getMobileNumber() {
        String mob = getSimpleStringValue("mn");
        if (null == mob) {
            return Constant.EMPTY;
        }
        return mob;
    }

    public void setMobileNumber(String mobileNumber) {
        setSimpleValue("mn", mobileNumber);
    }

    public String getPhoneNumber() {
        return getSimpleStringValue("pn");
    }

    public void setPhoneNumber(String phoneNumber) {
        setSimpleValue("pn", phoneNumber);
    }

    public String getEmail() {
        String e = getSimpleStringValue("e");
        if (null == e) {
            return Constant.EMPTY;
        }
        return e;
    }


    public void setEmail(String email) {
        setSimpleValue("e", email);
    }

    public String getQq() {
        return getSimpleStringValue("qq");
    }

    public void setQq(String qq) {
        setSimpleValue("qq", qq);
    }

    public String getWeiXin() {
        return getSimpleStringValue("wx");
    }

    public void setWeiXin(String weiXin) {
        setSimpleValue("wx", weiXin);
    }

    public String getWeiBo() {
        return getSimpleStringValue("wb");
    }

    public void setWeiBo(String weiBo) {
        setSimpleValue("wb", weiBo);
    }

    public String getAddress() {
        return getSimpleStringValue("add");
    }

    public void setAddress(String address) {
        setSimpleValue("add", address);
    }

    public String getPostCode() {
        return getSimpleStringValue("pc");
    }

    public void setPostCode(String postCode) {
        setSimpleValue("pc", postCode);
    }

    public int getBloodType() {
        return getSimpleIntegerValue("by");
    }

    public void setBloodType(int bloodType) {
        setSimpleValue("by", bloodType);
    }


    public long getRegisterTime() {
        return getSimpleLongValue("rt");
    }

    public void setRegisterTime(long registerTime) {
        setSimpleValue("rt", registerTime);
    }

    public String getRegisterIP() {
        return getSimpleStringValue("ri");
    }

    public void setRegisterIP(String registerIP) {
        setSimpleValue("ri", registerIP);
    }


    public ObjectId getSchoolID() {
        return getSimpleObjecIDValue("si");
    }

    public void setSchoolID(ObjectId schoolID) {
        setSimpleValue("si", schoolID);
    }

    public long getBirthDate() {
        return getSimpleLongValue("bir");
    }

    public void setBirthDate(long birthDate) {
        setSimpleValue("bir", birthDate);
    }

    public long getLastActiveDate() {
        return getSimpleLongValueDef("lad", 0L);
    }

    public void setLastActiveDate(long lastActiveDate) {
        setSimpleValue("lad", lastActiveDate);
    }

    public int getExperiencevalue() {
        return getSimpleIntegerValueDef("exp", 0);
    }

    public void setExperiencevalue(int experiencevalue) {
        setSimpleValue("exp", experiencevalue);
    }

    public String getStudyNum() {
        if (this.getBaseEntry().containsField("sn")) {
            return getSimpleStringValue("sn");
        }
        return "";
    }

    public void setStudyNum(String studyNum) {
        setSimpleValue("sn", studyNum);
    }

    public String getRegisterNum() {
        return getSimpleStringValue("rn");
    }

    public void setRegisterNum(String registerNum) {
        setSimpleValue("rn", registerNum);
    }

    public String getIntroduce() {
        return getSimpleStringValue("int");
    }

    public void setIntroduce(String introduce) {
        setSimpleValue("int", introduce);
    }

    public String getJobnumber() {
        return getSimpleStringValue("jnb");
    }

    public void setJobnumber(String jobnumber) {
        setSimpleValue("jnb", jobnumber);
    }

    public int getChat() {
        return getSimpleIntegerValue("ic");
    }

    public void setChat(int chat) {
        setSimpleValue("ic", chat);
    }

    //----------------兼容之前的版本-------------------//
//	public ObjectId getConnectId() {
//		return getSimpleObjecIDValue("cid");
//	}
//	public void setConnectId(ObjectId connectId) {
//		setSimpleValue("cid", connectId);
//	}
    //----------------兼容之前的版本-------------------//


    public List<ObjectId> getConnectIds() {
        List<ObjectId> retList = new ArrayList<ObjectId>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("cid");
        if (null != list && !list.isEmpty()) {
            for (Object o : list) {
                retList.add(((ObjectId) o));
            }
        }
        return retList;
    }

    public void setConnectIds(List<ObjectId> connectId) {
        setSimpleValue("cid", MongoUtils.convert(connectId));
    }

    public String getAvatar() {
        return getSimpleStringValue("avt");
    }

    public void setAvatar(String avatar) {
        setSimpleValue("avt", avatar);
    }


    public long getSchoolHomeDate() {
        return getSimpleLongValue("shd");
    }

    public void setSchoolHomeDate(long schoolHomeDate) {
        setSimpleValue("shd", schoolHomeDate);
    }

    public long getFamilyHomeDate() {
        return getSimpleLongValue("fhd");
    }

    public void setFamilyHomeDate(long familyHomeDate) {
        setSimpleValue("fhd", familyHomeDate);
    }

    public List<IdValuePair> getGroupInfoList() {
        List<IdValuePair> retList = new ArrayList<IdValuePair>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("gli");
        if (null != list && !list.isEmpty()) {
            for (Object o : list) {
                retList.add(new IdValuePair((BasicDBObject) o));
            }
        }
        return retList;
    }

    public void setGroupInfoList(List<IdValuePair> groupInfoList) {
        List<DBObject> list = MongoUtils.fetchDBObjectList(groupInfoList);
        setSimpleValue("gli", MongoUtils.convert(list));
    }

    public void setJob(String job) {
        setSimpleValue("jo", job);
    }

    public String getJob() {
        if (this.getBaseEntry().containsField("jo")) {
            return getSimpleStringValue("jo");
        }
        return Constant.EMPTY;
    }

    public int getIsRemove() {
        if (this.getBaseEntry().containsField("ir")) {
            return getSimpleIntegerValue("ir");
        }
        return Constant.ZERO;
    }

    public void setIsRemove(int isRemove) {
        setSimpleValue("ir", isRemove);
    }

    //----------学籍管理begin----------

    public String getStudentRace() {
        return getSimpleStringValue("sra");
    }

    public void setStudentRace(String studentRace) {
        setSimpleValue("sra", studentRace);
    }

    public String getStudentHealth() {
        return getSimpleStringValue("she");
    }

    public void setStudentHealth(String studentHealth) {
        setSimpleValue("she", studentHealth);
    }


    public String getResidenceAddress() {
        return getSimpleStringValue("rad");
    }

    public void setResidenceAddress(String residenceAddress) {
        setSimpleValue("rad", residenceAddress);
    }

    public long getChangeDate() {
        return getSimpleLongValueDef("cdt", -1L);
    }

    public void setChangeDate(String changeDate) {
        setSimpleValue("cdt", changeDate);
    }

    public String getChagePostscript() {
        return getSimpleStringValue("cps");
    }

    public void setChagePostscript(String chagePostscript) {
        setSimpleValue("cps", chagePostscript);
    }

    public String getNewSchool() {
        return getSimpleStringValue("nsc");
    }

    public void setNewSchool(String newSchool) {
        setSimpleValue("nsc", newSchool);
    }
    //----------学籍管理end----------


    public int getK6KT() {
        return getSimpleIntegerValueDef("kt", 1);
    }

    public void setK6KT(int kt) {
        setSimpleValue("kt", kt);
    }

    public int getJinyan() {
        return getSimpleIntegerValueDef("ijy", 0);
    }

    public void setJinyan(int jinyan) {
        setSimpleValue("ijy", jinyan);
    }

    public long getJinyanDate() {
        return getSimpleLongValue("jydt");
    }

    public void setJinyanDate(long jinyanDate) {
        setSimpleValue("jydt", jinyanDate);
    }

    public long getValidBeginTime() {
        return getSimpleLongValueDef("vabt", 0L);
    }

    public void setValidBeginTime(long validBeginTime) {
        setSimpleValue("vabt", validBeginTime);
    }

    public long getValidTime() {
        return getSimpleLongValueDef("vatl", Constant.SECONDS_IN_DAY * 5);
    }

    public void setValidTime(long validTime) {
        setSimpleValue("vatl", validTime);
    }

    public int getUserType() {
        return getSimpleIntegerValueDef("uty", 0);
    }

    public void setUserType(int userType) {
        setSimpleValue("uty", userType);
    }

    public List<ObjectId> getSchoolIds() {
        List<ObjectId> retList = new ArrayList<ObjectId>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("sis");
        if (null != list && !list.isEmpty()) {
            for (Object o : list) {
                retList.add(((ObjectId) o));
            }
        }
        return retList;
    }

    public void setSchoolIds(List<ObjectId> schoolIds) {
        setSimpleValue("sis", MongoUtils.convert(schoolIds));
    }

    public String getLoginName() {
        String s = getSimpleStringValue("logn");
        if (null == s) {
            return Constant.EMPTY;
        }
        return s;
    }

    public void setLoginName(String loginName) {
        setSimpleValue("logn", loginName);
    }


    public String getBindName() {
        return getSimpleStringValue("bnm");
    }

    public void setBindName(String bindName) {
        setSimpleValue("bnm", bindName);
    }

    public String getPostionDec() {
        return getSimpleStringValue("posdec");
    }

    public void setPostionDec(String postionDec) {
        setSimpleValue("posdec", postionDec);
    }

    public long getPostCount() {
        if (getBaseEntry().containsField("psc")) {
            return getSimpleLongValue("psc");
        } else {
            return 0L;
        }

    }

    public void setPostCount(long postCount) {
        setSimpleValue("psc", postCount);
    }


    public long getForumScore() {
        if (getBaseEntry().containsField("frs")) {
            return getSimpleLongValue("frs");
        } else {
            return 0L;
        }

    }

    public void setForumScore(long forumScore) {
        setSimpleValue("frs", forumScore);
    }

    public long getForumExperience() {
        if (getBaseEntry().containsField("fexp")) {
            return getSimpleLongValue("fexp");
        } else {
            return 0L;
        }

    }

    public void setForumExperience(long forumExperience) {
        setSimpleValue("fexp", forumExperience);
    }

    public String getInterviewIP() {
        if (getBaseEntry().containsField("ivp")) {
            return getSimpleStringValue("ivp");
        } else {
            return "";
        }
    }

    public void setInterviewIP(String interviewIP) {
        setSimpleValue("ivp", interviewIP);
    }

    public long getStatisticTime() {
        if (getBaseEntry().containsField("sti")) {
            return getSimpleLongValue("sti");
        } else {
            return -1L;
        }
    }

    public void setStatisticTime(Long statisticTime) {
        setSimpleValue("sti", statisticTime);
    }

    public long getInterviewTime() {
        if (getBaseEntry().containsField("iti")) {
            return getSimpleLongValue("iti");
        } else {
            return -1L;
        }
    }

    public void setInterviewTime(Long interviewTime) {
        setSimpleValue("iti", interviewTime);
    }

    public long getQuitTime() {
        if (getBaseEntry().containsField("qti")) {
            return getSimpleLongValue("qti");
        } else {
            return -1L;
        }
    }

    public void setQuitTime(Long quitTime) {
        setSimpleValue("qti", quitTime);
    }

    public long getInterviewPostTime() {
        if (getBaseEntry().containsField("pti")) {
            return getSimpleLongValue("pti");
        } else {
            return -1L;
        }
    }

    public void setInterviewPostTime(Long interviewPostTime) {
        setSimpleValue("pti", interviewPostTime);
    }

    public int getSilencedStatus() {
        if (getBaseEntry().containsField("sls")) {
            return getSimpleIntegerValue("sls");
        } else {
            return 0;
        }
    }

    public void setSilencedStatus(int silencedStatus) {
        setSimpleValue("sls", silencedStatus);
    }

    /**
     * 得到禁言时间
     *
     * @return
     */
    public long getSilencedTime() {
        if (getBaseEntry().containsField("slti")) {
            return getSimpleLongValue("slti");
        } else {
            return 0L;
        }
    }

    public void setSilencedTime(long silencedTime) {
        setSimpleValue("slti", silencedTime);
    }

    public String getSilencedReason() {
        if (getBaseEntry().containsField("slr")) {
            return getSimpleStringValue("slr");
        } else {
            return "";
        }
    }

    public void setSilencedReason(String silencedReason) {
        setSimpleValue("slr", silencedReason);
    }

    public int getSilencedTen() {
        if (getBaseEntry().containsField("slt")) {
            return getSimpleIntegerValue("slt");
        } else {
            return -1;
        }
    }

    public void setSilencedTen(int silencedTen) {
        setSimpleValue("slt", silencedTen);
    }

    public int getEmailStatus() {
        if (getBaseEntry().containsField("ems")) {
            return getSimpleIntegerValue("ems");
        } else {
            return 0;
        }
    }

    public void setEmailStatus(int emailStatus) {
        setSimpleValue("ems", emailStatus);
    }

    public String getEmailValidateCode() {
        if (getBaseEntry().containsField("emv")) {
            return getSimpleStringValue("emv");
        } else {
            return "";
        }
    }

    public void setSids(ObjectId sid) {
        setSimpleValue("sdis", sid);
    }

    public ObjectId getSidS(ObjectId sid) {
        return getSimpleObjecIDValue("sids");
    }

    public void setEmailValidateCode(String emailValidateCode) {
        setSimpleValue("emv", emailValidateCode);
    }

    public List<UserTagEntry> getUserTag() {
        List<UserTagEntry> userTagEntries = new ArrayList<UserTagEntry>();
        if (!getBaseEntry().containsField("ustg")) {
            return userTagEntries;
        } else {
            Object object = getSimpleObjectValue("ustg");
            if (null == object) {
                return userTagEntries;
            } else {
                BasicDBList list = (BasicDBList) object;
                if (null != list && !list.isEmpty()) {
                    for (Object o : list) {
                        userTagEntries.add(new UserTagEntry((BasicDBObject) o));
                    }
                }
                return userTagEntries;
            }
        }
    }


    public String getQRCode() {
        if (getBaseEntry().containsField("qrc")) {
            return getSimpleStringValue("qrc");
        } else {
            return Constant.EMPTY;
        }
    }

    public void setQRCode(String qrCode) {
        setSimpleValue("qrc", qrCode);
    }

    public static class UserTagEntry extends BaseDBObject {
        public UserTagEntry(BasicDBObject dbObject) {
            setBaseEntry(dbObject);
        }

        public UserTagEntry(int code, String tag) {
            BasicDBObject dbObject = new BasicDBObject()
                    .append("co", code)
                    .append("tg", tag);
            setBaseEntry(dbObject);
        }

        public int getCode() {
            return getSimpleIntegerValue("co");
        }

        public String getTag() {
            return getSimpleStringValue("tg");
        }

    }

    public String getGenerateUserCode(){
        if(getBaseEntry().containsField("gugc")) {
            return getSimpleStringValue("gugc");
        }else{
            return Constant.EMPTY;
        }
    }

    public void setGenerateUserCode(String generateUserCode){
        setSimpleValue("gugc",generateUserCode);
    }




}
