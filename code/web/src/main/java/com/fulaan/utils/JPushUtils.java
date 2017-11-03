package com.fulaan.utils;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.push.model.notification.WinphoneNotification;
import com.pojo.app.IdValuePair;
import com.pojo.notice.NoticeEntry;
import com.pojo.notice.NoticeEntry.NoticeAllInfo;
import com.pojo.school.HomeWorkEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.props.Resources;
import org.bson.types.ObjectId;

import java.util.*;


/**
 * @author fourer
 *         mType 1:作业 2：通知
 */
public class JPushUtils {

    private Boolean apnsProduction = Boolean.valueOf(Resources.getProperty("JPush.apnsProduction"));


    private String masterSecret_android = Resources.getProperty("JPush.masterSecret.android");
    private String appKey_android = Resources.getProperty("JPush.appKey.android");

    private String masterSecret_ios = Resources.getProperty("JPush.masterSecret.ios");
    private String appKey_ios = Resources.getProperty("JPush.appKey.ios");

    private String masterSecret_ios_busywork=Resources.getProperty("JPush.masterSecret.ios.busywork");
    private String appKey_ios_busywork=Resources.getProperty("JPush.appKey.ios.busywork");


    /**
     * 发送通知到手机端
     *
     * @param e
     * @param teacherName
     * @param schoolId
     */
    public void pushNotice(final NoticeEntry e, final String teacherName, final String schoolId) {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                NoticeAllInfo info = e.getAllSchool();
                List<String> tags = new ArrayList<String>();

                Map<String, String> param = new HashMap<String, String>();
                param.put("mType", "2");
                if (null != info) {
                }

                List<IdValuePair> userList = e.getUsers();

                if (userList.size() > Constant.ZERO) {
                    tags.clear();
                    List<ObjectId> userIds = MongoUtils.getFieldObjectIDs(userList, "id");
                    for (ObjectId id : userIds) {
                        tags.add(id.toString());
                    }


                    int i = 0;
                    for (int a = 0; a < 50; a++) {
                        try {
                            int end = (i + 1) * 100;
                            if (end > tags.size()) {
                                end = tags.size();
                            }

                            List<String> thistags = tags.subList(i * 100, end);
                            Audience audience = Audience.alias(thistags);
                            pushRestAndroid(audience, e.getName(), teacherName, "您有新的通知", param);
                            pushRestIos(audience, e.getName(), param);
                            pushRestWinPhone(audience, e.getName());

                            if (end >= (tags.size())) {
                                break;
                            }
                            i = i + 1;
                        } catch (Exception ex) {

                        }
                    }
                }
            }
        }
        );
        t.start();
    }


    /**
     * 发送作业到手机端
     */
    public void pushHomeWork(final HomeWorkEntry e, final String teacher) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {


                Set<ObjectId> userIdSet = new HashSet<ObjectId>();

                List<ObjectId> classIds = MongoUtils.getFieldObjectIDs(e.getClasses(), "id");

                List<String> tags = new ArrayList<String>();

                for (ObjectId id : userIdSet) {
                    tags.add(id.toString());
                }


                Map<String, String> param = new HashMap<String, String>();
                param.put("mType", "1");


                String message = e.getName();
                Audience audience = Audience.alias(tags);
                try {
                    pushRestAndroid(audience, message, teacher, "您有新的作业", param);
                } catch (Exception ex) {

                }

                try {

                    pushRestIos(audience, message, param);
                } catch (Exception ex) {

                }
                try {
                    pushRestWinPhone(audience, message);
                } catch (Exception ex) {

                }
            }
        }
        );
        t.start();
    }



    private PushPayload buildPushObject_all_tag_alert_Android(Audience audience,
                                                              String mesgname, String userName, String title, Map<String, String> parms) {

        AndroidNotification notification=AndroidNotification.newBuilder()
                .setAlert(mesgname)
                .setTitle(title)
                .addExtras(parms)
                .build();


        return PushPayload.newBuilder()
                .setAudience(audience)
                .setPlatform(Platform.android())
                .setNotification(Notification.newBuilder().addPlatformNotification(notification).build())
                .setMessage(Message.newBuilder()
                        .addExtra("title", title)
                        .addExtra("value", mesgname)
                        .addExtra("username", userName)
                        .addExtras(parms)
                        .setMsgContent(mesgname).build()).build();
    }


    private PushPayload buildPushObject_all_tag_alert_IOS(Audience audience, String mesgname, Map<String, String> parms) {


        IosNotification ifc = IosNotification.newBuilder()
                .setAlert(mesgname)
                .setSound("default")
                .addExtras(parms)
                .build();

        return PushPayload.newBuilder().setPlatform(Platform.ios()).setAudience(audience)
                .setNotification(Notification.newBuilder().addPlatformNotification(ifc).build()
                ).setOptions(Options.newBuilder().setApnsProduction(apnsProduction).build()).build();
    }

    private PushPayload buildPushObject_all_tag_alert_WindowsPhone(Audience audience, String mesgname) {
        return PushPayload.newBuilder().setPlatform(Platform.winphone()).setAudience(audience)
                .setNotification(
                        Notification.newBuilder()
                                .addPlatformNotification(
                                        WinphoneNotification.newBuilder()
                                                .setAlert(mesgname)
                                                .setTitle("K6KT")
                                                .build()
                                ).build()
                ).build();
    }


    public  void pushRestIosbusywork(Audience audience, String mesgname, Map<String, String> parms){
        JPushClient jpushClient = new JPushClient(masterSecret_ios_busywork, appKey_ios_busywork, 3);
        PushPayload payload = buildPushObject_all_tag_alert_IOS(audience, mesgname, parms);
        try {
            PushResult result = jpushClient.sendPush(payload);
            System.out.println(result);
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }
    }

    /**
     * 极光推送
     *
     * @param audience
     * @param mesgname
     * @param parms
     */
    public void pushRestIos(Audience audience, String mesgname, Map<String, String> parms) {
        JPushClient jpushClient = new JPushClient(masterSecret_ios, appKey_ios, 3);
        PushPayload payload = buildPushObject_all_tag_alert_IOS(audience, mesgname, parms);
        try {
            PushResult result = jpushClient.sendPush(payload);
            System.out.println(result);
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }
    }

    /**
     * 极光推送
     *
     * @param audience
     * @param mesgname
     */
    public void pushRestWinPhone(Audience audience, String mesgname) {
        JPushClient jpushClient = new JPushClient(masterSecret_android, appKey_android, 3);
        PushPayload payload = buildPushObject_all_tag_alert_WindowsPhone(audience, mesgname);
        try {
            PushResult result = jpushClient.sendPush(payload);
            System.out.println(result);
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }
    }

    /**
     * 极光推送
     *
     * @param audience
     * @param mesgname
     * @param username
     * @param title
     * @param parms
     */
    public void pushRestAndroid(Audience audience, String mesgname, String username, String title, Map<String, String> parms) {
        JPushClient jpushClient = new JPushClient(masterSecret_android, appKey_android, 3);
        PushPayload payload = buildPushObject_all_tag_alert_Android(audience, mesgname, username, title, parms);
        try {
            PushResult result = jpushClient.sendPush(payload);
            System.out.println(result);
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }
    }
}
