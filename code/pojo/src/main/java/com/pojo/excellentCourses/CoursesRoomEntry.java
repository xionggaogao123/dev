package com.pojo.excellentCourses;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018-05-08.
 * 直播间
 *
 *
 userid	                            CC账户ID
 name	                            直播间名称
 roomId                             直播间id
 contactId                          关联课程
 publishUrl                         推流地址
 desc	                            直播间描述
 templatetype	                    直播模板类型，请求模板信息接口可获得模板类型的详细信息。
 authtype	                        验证方式，0：接口验证，需要填写下面的checkurl；1：密码验证，需要填写下面的playpass；2：免密码验证
 publisherpass	                    推流端密码，即讲师密码
 assistantpass	                    助教端密码
 playpass	                        播放端密码	可选
 checkurl	                        验证地址	可选
 barrage	            bar         是否开启弹幕。0：不开启；1：开启	可选，默认为0
 foreignpublish	        for         是否开启第三方推流。0：不开启；1：开启	可选，默认为0
 openlowdelaymode	    olm         开启直播低延时模式。0为关闭；1为开启	可选，默认为关闭
 showusercount	        sho         在页面显示当前在线人数。0表示不显示；1表示显示	可选，默认显示当前人数，模板一暂不支持此设置
 openhostmode	        ohm         开启主持人模式，"0"表示不开启；"1"表示开启	可选，默认不开启，开通主持人模式权限后方可使用
 warmvideoid	        war         插播暖场视频，填写同一账号下云点播视频vid	可选，默认关闭；参数值为空，表示关闭
 livestarttime	        liv         直播开始时间；格式；yyyy-MM-dd	可选
 playerbackgroundhint	ply         播放器提示语。未直播时播放器将显示该提示语	可选，最多15个字符
 manuallyrecordmode	    man         手动录制模式。0：关闭；1：开启	可选，默认关闭
 clientdocpermissions	cli         讲师文档权限。0：关闭；1：开启	可选，默认关闭；
 repeatedloginsetting	rep         重复登录设置；0：允许后进入者登录;1:禁止后进入者登录，对讲师端和观看端生效	可选，默认0
 maxaudiencenum	        max         直播间并发人数上限	可选，默认为0，表示不做限制
 documentdisplaymode	doc         文档显示模式。1：适合窗口;2:适合宽度	可选，适合窗口
 openlivecountdown	    old         倒计时功能。0：关闭；1：开启	可选，默认关闭
 showlectueronlinenum	sll         讲师端显示在线人数。0：不显示；1：显示	可选，默认显示
 showassistonlinenum	sal         助教主持人端显示在线人数。0：不显示；1：显示	可选，默认显示
 *
 *
 * 直播模板
 *
 1	模板一 视频直播
 2	模板二 视频直播+聊天互动+直播问答
 3	模板三 视频直播+聊天互动
 4	模板四 视频直播+聊天互动+直播文档
 5	模板五 视频直播+聊天互动+直播文档+直播问答
 6	模板六 视频直播+直播问答
 *
 *
 */
public class CoursesRoomEntry extends BaseDBObject {

    public CoursesRoomEntry(){

    }

    public CoursesRoomEntry(BasicDBObject dbObject){
        super(dbObject);
    }

    public CoursesRoomEntry(
            String  userId,     //CC账户ID
            String  name,       //直播间名称
            String roomId,      //直播间id
            ObjectId contactId, //关联id
            String description, //直播间描述
            int authtype,       //验证方式
            int templatetype ,  //直播模板
            String publisherpass,//讲师密码
            String assistantpass,//助教密码
            String playpass,     //播放密码
            String checkurl,    //接口验证地址
            String dateTime,        //开始时间
            String playerbackgroundhint //提示语
    ){
        BasicDBObject dbObject = new BasicDBObject()
                .append("ser", userId)
                .append("rid", roomId)
                .append("nam", name)
                .append("cid", contactId)
                .append("dec", description)
                .append("aut", authtype)
                .append("tem", templatetype)
                .append("pub", publisherpass)
                .append("ass", assistantpass)
                .append("pla", playpass)
                .append("che", checkurl)
                .append("bar", 0)// 是否开启弹幕。0：不开启；1：开启	可选，默认为0
                .append("for", 0)//是否开启第三方推流。0：不开启；1：开启	可选，默认为0
                .append("olm", 0)//开启直播低延时模式。0为关闭；1为开启	可选，默认为关闭
                .append("sho", 0)// 在页面显示当前在线人数。0表示不显示；1表示显示	可选，默认显示当前人数，模板一暂不支持此设置
                .append("ohm", 0)//开启主持人模式，"0"表示不开启；"1"表示开启	可选，默认不开启，开通主持人模式权限后方可使用
                .append("liv", dateTime)//直播开始时间；格式；yyyy-MM-dd	可选
                .append("ply", playerbackgroundhint)//播放器提示语。未直播时播放器将显示该提示语	可选，最多15个字符
                .append("man", 0) //手动录制模式。0：关闭；1：开启	可选，默认关闭
                .append("cli", 0) //讲师文档权限。0：关闭；1：开启	可选，默认关闭；
                .append("rep", 0) //重复登录设置；0：允许后进入者登录;1:禁止后进入者登录，对讲师端和观看端生效	可选，默认0
                .append("max", 0) //直播间并发人数上限	可选，默认为0，表示不做限制
                .append("doc", 1) // 文档显示模式。1：适合窗口;2:适合宽度	可选，适合窗口
                .append("old", 0) // 倒计时功能。0：关闭；1：开启	可选，默认关闭
                .append("sll", 0) //讲师端显示在线人数。0：不显示；1：显示	可选，默认显示
                .append("sal", 0)  //助教主持人端显示在线人数。0：不显示；1：显示	可选，默认显示
                 .append("isr",0);
        setBaseEntry(dbObject);
    }

    public String getUserId(){
        return getSimpleStringValue("ser");
    }
    public void setUserId(String userId){
        setSimpleValue("ser",userId);
    }
    public ObjectId getContactId(){
        return getSimpleObjecIDValue("cid");
    }
    public void setContactId(ObjectId contactId){
        setSimpleValue("cid",contactId);
    }
    public String getDescription(){
        return getSimpleStringValue("dec");
    }
    public void setDescription(String description){
        setSimpleValue("dec", description);
    }

    public String getRoomId(){
        return getSimpleStringValue("rid");
    }
    public void setRoomId(String roomId){
        setSimpleValue("rid", roomId);
    }
    public String getName(){
        return getSimpleStringValue("nam");
    }
    public void setName(String name){
        setSimpleValue("nam", name);
    }

    public int getAuthtype(){
        return getSimpleIntegerValue("aut");
    }

    public void setAuthtype(int authtype){
        setSimpleValue("aut",authtype);
    }

    public int getTemplatetype(){
        return getSimpleIntegerValue("tem");
    }

    public void setTemplatetype(int templatetype){
        setSimpleValue("tem",templatetype);
    }

    public String getPublisherpass(){
        return getSimpleStringValue("pub");
    }
    public void setPublisherpass(String publisherpass){
        setSimpleValue("pub", publisherpass);
    }
    public String getAssistantpass(){
        return getSimpleStringValue("ass");
    }
    public void setAssistantpass(String assistantpass){
        setSimpleValue("ass", assistantpass);
    }
    public String getPlaypass(){
        return getSimpleStringValue("pla");
    }
    public void setPlaypass(String playpass){
        setSimpleValue("pla", playpass);
    }
    public String getCheckurl(){
        return getSimpleStringValue("che");
    }
    public void setCheckurl(String checkurl){
        setSimpleValue("che", checkurl);
    }

    public long getCreateTime(){
        return getSimpleLongValue("ctm");
    }

    public void setCreateTime(long createTime){
        setSimpleValue("ctm",createTime);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }

    public String getDateTime(){
        return getSimpleStringValue("liv");
    }

}
