package com.fulaan.picturetext.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.green.model.v20170112.ImageAsyncScanRequest;
import com.aliyuncs.green.model.v20170112.ImageSyncScanRequest;
import com.aliyuncs.green.model.v20170112.TextScanRequest;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.db.backstage.TeacherApproveDao;
import com.db.backstage.UnlawfulPictureTextDao;
import com.fulaan.backstage.dto.UnlawfulPictureTextDTO;
import com.fulaan.picturetext.BaseSample;
import com.pojo.backstage.TeacherApproveEntry;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by James on 2017/11/14.
 */
public class CheckTextAndPicture extends BaseSample {

    public static void main(String[] args){
        CheckTextAndPicture c = new CheckTextAndPicture();
        String content = "各班主任注意：1.今天上午各班还要统计下周六返校的学生名单2.学生返校后在本班级上自习3.周日早上所有学生 （包括周日早上返校学生）7点30分从学校统一出发，不得迟到。4.周日返校的学生要尽量早点，不然道路不通5.本次活动重在参与";
        //String content = "习大大";
        try{
            c.checkText(content,new ObjectId());
        }catch(Exception e){

        }

    }

    /**
     * 同步检测文本垃圾
     * @param content
     * @throws Exception
     */
    public static Map<String,Object> checkText(String content,ObjectId userId) throws Exception {
        Map<String,Object> map =new HashMap<String, Object>();
        TeacherApproveDao  teacherApproveDao = new TeacherApproveDao();
        TeacherApproveEntry entry = teacherApproveDao.getEntry(userId);
        if(entry !=null && entry.getType()==2){
            map.put("bl", "2");
            map.put("text","正常");
            return map;
        }

        //请替换成你自己的accessKeyId、accessKeySecret
        IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint(getEndPointName(), regionId, "Green", getDomain());

        IAcsClient client = new DefaultAcsClient(profile);

        TextScanRequest textScanRequest = new TextScanRequest();
        textScanRequest.setAcceptFormat(FormatType.JSON); // 指定api返回格式
        textScanRequest.setContentType(FormatType.JSON);
        textScanRequest.setMethod(com.aliyuncs.http.MethodType.POST); // 指定请求方法
        textScanRequest.setEncoding("UTF-8");
        textScanRequest.setRegionId(regionId);


        List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();


        Map<String, Object> task1 = new LinkedHashMap<String, Object>();
        task1.put("dataId", UUID.randomUUID().toString());
       /* String oldStr = "蒙汗药法轮功";
        String newStr = new String(oldStr.getBytes(), "UTF-8");*/
        task1.put("content", content);

        tasks.add(task1);

        JSONObject data = new JSONObject();
        //String[] arg = {"antispam","keyword"};
        //data.put("scenes", Arrays.asList("antispam"));
       // data.put("scenes", arg);
        data.put("scenes", Arrays.asList("keyword"));
        data.put("tasks", tasks);

        textScanRequest.setContent(data.toJSONString().getBytes("UTF-8"), "UTF-8", FormatType.JSON);

        /**
         * 请务必设置超时时间
         */
        textScanRequest.setConnectTimeout(10000);
        textScanRequest.setReadTimeout(60000);
        try {
            HttpResponse httpResponse = client.doAction(textScanRequest);

            if(httpResponse.isSuccess()){
                JSONObject scrResponse = JSON.parseObject(new String(httpResponse.getContent(), "UTF-8"));
                System.out.println(JSON.toJSONString(scrResponse, true));
                if (200 == scrResponse.getInteger("code")) {
                    JSONArray taskResults = scrResponse.getJSONArray("data");
                    for (Object taskResult : taskResults) {
                        if(200 == ((JSONObject)taskResult).getInteger("code")){
                            JSONArray sceneResults = ((JSONObject)taskResult).getJSONArray("results");
                            for (Object sceneResult : sceneResults) {
                                //String scene = ((JSONObject)sceneResult).getString("scene");
                                //String suggestion = ((JSONObject)sceneResult).getString("suggestion");
                                String label = ((JSONObject)sceneResult).getString("label");
                               // JSONObject sceneResults2 = ((JSONObject)sceneResult).getJSONObject("extras");
                               // System.out.print(sceneResults);
                                //根据scene和suggetion做相关的处理
                                //do something
                              /*  System.out.println("args = [" + scene + "]");
                                System.out.println("args = [" + suggestion + "]");*/
                                if(label.equals("normal")){
                                    map.put("bl", "2");
                                    map.put("text","正常");
                                    return map;
                                }else if(label.equals("spam")){
                                  /*  String keyword = sceneResults2.getString("hitContext");
                                    map.put("bl", "1");
                                    map.put("text",keyword+"含违规信息");*/
                                    JSONArray details = ((JSONObject)sceneResult).getJSONArray("details");
                                    Object ob = details.get(0);
                                    JSONArray contexts = ((JSONObject)ob).getJSONArray("contexts");
                                    Object ob2 = contexts.get(0);
                                    String keyword = ((JSONObject)ob2).getString("context");
                                    map.put("bl", "1");
                                    map.put("text",keyword+"--含违规信息");
                                    return map;
                                }else if(label.equals("ad")){
                                    JSONArray details = ((JSONObject)sceneResult).getJSONArray("details");
                                    Object ob = details.get(0);
                                    JSONArray contexts = ((JSONObject)ob).getJSONArray("contexts");
                                    Object ob2 = contexts.get(0);
                                    String keyword = ((JSONObject)ob2).getString("context");
                                    map.put("bl", "1");
                                    map.put("text",keyword+"--含广告信息");
                                    return map;
                                }else if(label.equals("politics")){
                                    JSONArray details = ((JSONObject)sceneResult).getJSONArray("details");
                                    Object ob = details.get(0);
                                    JSONArray contexts = ((JSONObject)ob).getJSONArray("contexts");
                                    Object ob2 = contexts.get(0);
                                    String keyword = ((JSONObject)ob2).getString("context");
                                    map.put("bl", "1");
                                    map.put("text",keyword+"--含渉政信息");
                                    return map;
                                }else if(label.equals("terrorism")){
                                    JSONArray details = ((JSONObject)sceneResult).getJSONArray("details");
                                    Object ob = details.get(0);
                                    JSONArray contexts = ((JSONObject)ob).getJSONArray("contexts");
                                    Object ob2 = contexts.get(0);
                                    String keyword = ((JSONObject)ob2).getString("context");
                                    map.put("bl", "1");
                                    map.put("text",keyword+"--含暴恐信息");
                                    return map;
                                }else if(label.equals("porn")){
                                    JSONArray details = ((JSONObject)sceneResult).getJSONArray("details");
                                    Object ob = details.get(0);
                                    JSONArray contexts = ((JSONObject)ob).getJSONArray("contexts");
                                    Object ob2 = contexts.get(0);
                                    String keyword = ((JSONObject)ob2).getString("context");
                                    map.put("bl", "1");
                                    map.put("text",keyword+"--含色情信息");
                                    return map;
                                }else if(label.equals("contraband")){
                                    JSONArray details = ((JSONObject)sceneResult).getJSONArray("details");
                                    Object ob = details.get(0);
                                    JSONArray contexts = ((JSONObject)ob).getJSONArray("contexts");
                                    Object ob2 = contexts.get(0);
                                    String keyword = ((JSONObject)ob2).getString("context");
                                    map.put("bl", "1");
                                    map.put("text",keyword+"--含违禁信息");
                                    return map;
                                }else{
                                    JSONArray details = ((JSONObject)sceneResult).getJSONArray("details");
                                    Object ob = details.get(0);
                                    JSONArray contexts = ((JSONObject)ob).getJSONArray("contexts");
                                    Object ob2 = contexts.get(0);
                                    String keyword = ((JSONObject)ob2).getString("context");
                                    map.put("bl", "1");
                                    map.put("text",keyword+"--含其他信息");
                                    return map;
                                }
                            }
                        }else{
                            System.out.println("task process fail:" + ((JSONObject)taskResult).getInteger("code"));
                        }
                    }
                } else {
                    System.out.println("detect not success. code:" + scrResponse.getInteger("code"));
                }
            }else{
                System.out.println("response not success. status:" + httpResponse.getStatus());
            }
        } catch (ServerException e) {
            e.printStackTrace();
            map.put("bl", "2");
            map.put("text","含敏感词汇");
            return map;
        } catch (ClientException e) {
            e.printStackTrace();
            map.put("bl", "2");
            map.put("text", "含敏感词汇");
            return map;
        } catch (Exception e){
            e.printStackTrace();
            map.put("bl", "2");
            map.put("text", "含敏感词汇");
            return map;
        }
        map.put("bl", "2");
        map.put("text","含敏感词汇");
        return map;
    }

    /**
     * 异步检测图片
     * @param content
     * @throws Exception
     */
    public static void checkPicture(final String contactId,final String userId,final int function,final int type,final String content) throws Exception {
        //请替换成你自己的accessKeyId、accessKeySecret
        IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint(getEndPointName(), regionId, "Green", getDomain());
        IAcsClient client = new DefaultAcsClient(profile);

        ImageAsyncScanRequest imageAsyncScanRequest = new ImageAsyncScanRequest();
        imageAsyncScanRequest.setAcceptFormat(FormatType.JSON); // 指定api返回格式
        imageAsyncScanRequest.setContentType(FormatType.JSON);
        imageAsyncScanRequest.setMethod(com.aliyuncs.http.MethodType.POST); // 指定请求方法
        imageAsyncScanRequest.setEncoding("utf-8");
        imageAsyncScanRequest.setRegionId(regionId);


        List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
        Map<String, Object> task1 = new LinkedHashMap<String, Object>();
        task1.put("dataId", UUID.randomUUID().toString());
        task1.put("url",content);
        task1.put("time", new Date());

        tasks.add(task1);
        JSONObject data = new JSONObject();
        /**
         * porn: 色情
         * terrorism: 暴恐
         * qrcode: 二维码
         * ad: 图片广告
         * ocr: 文字识别
         */
        data.put("scenes", Arrays.asList("porn", "ocr", "qrcode", "sface"));
        data.put("tasks", tasks);

        imageAsyncScanRequest.setContent(data.toJSONString().getBytes("UTF-8"), "UTF-8", FormatType.JSON);

        /**
         * 请务必设置超时时间
         */
        imageAsyncScanRequest.setConnectTimeout(3000);
        imageAsyncScanRequest.setReadTimeout(6000);

        try {
            HttpResponse httpResponse = client.doAction(imageAsyncScanRequest);

            if(httpResponse.isSuccess()){
                JSONObject scrResponse = JSON.parseObject(new String(httpResponse.getContent(), "UTF-8"));
                System.out.println(JSON.toJSONString(scrResponse, true));
                if (200 == scrResponse.getInteger("code")) {
                    JSONArray taskResults = scrResponse.getJSONArray("data");
                    for (Object taskResult : taskResults) {
                        if(200 == ((JSONObject)taskResult).getInteger("code")){
                            String taskId = ((JSONObject)taskResult).getString("taskId");
                            // 将taskId 保存下来，间隔一段时间来轮询结果, 参照ImageAsyncScanResultsRequest
                            System.out.println("args = [" + taskId + "]");
                        }else{
                            System.out.println("task process fail:" + ((JSONObject)taskResult).getInteger("code"));
                        }
                    }
                } else {
                    System.out.println("detect not success. code:" + scrResponse.getInteger("code"));
                }
            }else{
                System.out.println("response not success. status:" + httpResponse.getStatus());
            }
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //同步检测
    public static void syncScanCheck(final String contactId,final String userId,final int function,final int type,final String content) throws Exception {
        //请替换成你自己的accessKeyId、accessKeySecret
        IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint(getEndPointName(), regionId, "Green", getDomain());
        IAcsClient client = new DefaultAcsClient(profile);

        ImageSyncScanRequest imageSyncScanRequest = new ImageSyncScanRequest();
        imageSyncScanRequest.setAcceptFormat(FormatType.JSON); // 指定api返回格式
        imageSyncScanRequest.setContentType(FormatType.JSON);
        imageSyncScanRequest.setMethod(com.aliyuncs.http.MethodType.POST); // 指定请求方法
        imageSyncScanRequest.setEncoding("utf-8");
        imageSyncScanRequest.setRegionId(regionId);


        List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
        Map<String, Object> task = new LinkedHashMap<String, Object>();
        task.put("dataId", UUID.randomUUID().toString());
        task.put("url", content);
        task.put("time", new Date());

        tasks.add(task);
        JSONObject data = new JSONObject();
        /**
         * porn: 色情
         * terrorism: 暴恐
         * qrcode: 二维码
         * ad: 图片广告
         * ocr: 文字识别
         */
        data.put("scenes", Arrays.asList("porn", "terrorism"));
        data.put("tasks", tasks);

        imageSyncScanRequest.setContent(data.toJSONString().getBytes("UTF-8"), "UTF-8", FormatType.JSON);

        /**
         * 请务必设置超时时间
         */
        imageSyncScanRequest.setConnectTimeout(3000);
        imageSyncScanRequest.setReadTimeout(6000);

        try {
            HttpResponse httpResponse = client.doAction(imageSyncScanRequest);

            if (httpResponse.isSuccess()) {
                JSONObject scrResponse = JSON.parseObject(new String(httpResponse.getContent(), "UTF-8"));
                System.out.println(JSON.toJSONString(scrResponse, true));
                if (200 == scrResponse.getInteger("code")) {
                    JSONArray taskResults = scrResponse.getJSONArray("data");
                    for (Object taskResult : taskResults) {
                        if(200 == ((JSONObject)taskResult).getInteger("code")){
                            JSONArray sceneResults = ((JSONObject)taskResult).getJSONArray("results");
                            for (Object sceneResult : sceneResults) {
                                String scene = ((JSONObject)sceneResult).getString("scene");
                                String label = ((JSONObject)sceneResult).getString("label");
                                String suggestion = ((JSONObject)sceneResult).getString("suggestion");
                                //根据scene和suggetion做相关的处理
                                //do something
                                System.out.println("args = [" + scene + "]");
                                System.out.println("args = [" + suggestion + "]");
                                if(label != null && label != "" && !label.equals("normal")) {
                                    UnlawfulPictureTextDao unlawfulPictureTextDao = new UnlawfulPictureTextDao();
                                    UnlawfulPictureTextDTO dto = new UnlawfulPictureTextDTO();
                                    dto.setType(type);
                                    dto.setContent(content);
                                    dto.setUserId(userId.toString());
                                    dto.setFunction(function);
                                    dto.setIsCheck(0);
                                    dto.setContactId(contactId.toString());
                                    unlawfulPictureTextDao.addEntry(dto.buildAddEntry());
                                }
                            }
                        }else{
                            System.out.println("task process fail:" + ((JSONObject)taskResult).getInteger("code"));
                        }
                    }
                } else {
                    System.out.println("detect not success. code:" + scrResponse.getInteger("code"));
                }
            } else {
                System.out.println("response not success. status:" + httpResponse.getStatus());
            }
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
