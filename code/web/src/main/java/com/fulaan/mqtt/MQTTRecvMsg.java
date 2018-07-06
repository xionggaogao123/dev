package com.fulaan.mqtt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.db.controlphone.ControlVersionDao;
import com.fulaan.mqtt.util.MacSignature;
import org.bson.types.ObjectId;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.IOException;
import java.util.Date;

public class MQTTRecvMsg {
   public static void main(String[] args) throws IOException {
        /**
         * 设置当前用户私有的MQTT的接入点。例如此处示意使用XXX，实际使用请替换用户自己的接入点。接入点的获取方法是，在控制台申请MQTT实例，每个实例都会分配一个接入点域名。
         */
        final String broker ="tcp://post-cn-v0h0bwv350e.mqtt.aliyuncs.com:1883";
        /**
         * 设置阿里云的AccessKey，用于鉴权
         */
        final String acessKey ="LTAI9yxNEdqLZsNi";
        /**
         * 设置阿里云的SecretKey，用于鉴权
         */
        final String secretKey ="Yvx4F4j1Cy3Ozmg5hHIyDT5VlT7ZTw";
        /**
         * 发消息使用的一级Topic，需要先在MQ控制台里申请
         */
        final String topic ="jxm-push";
        /**
         * MQTT的ClientID，一般由两部分组成，GroupID@@@DeviceID
         * 其中GroupID在MQ控制台里申请
         * DeviceID由应用方设置，可能是设备编号等，需要唯一，否则服务端拒绝重复的ClientID连接
         */
        final String clientId ="GID_jxm@@@ClientID_000822";
        String sign;
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            final MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            final MqttConnectOptions connOpts = new MqttConnectOptions();
            System.out.println("Connecting to broker: " + broker);
            /**
             * 计算签名，将签名作为MQTT的password
             * 签名的计算方法，参考工具类MacSignature，第一个参数是ClientID的前半部分，即GroupID
             * 第二个参数阿里云的SecretKey
             */
            sign = MacSignature.macSignature(clientId.split("@@@")[0], secretKey);
            /**
             * 设置订阅方订阅的Topic集合，此处遵循MQTT的订阅规则，可以是一级Topic，二级Topic，P2P消息请订阅/p2p
             */
            //final String[] topicFilters=new String[]{topic+"/notice/",topic+"/p2p"};
            final String[] topicFilters=new String[]{topic,topic+"/p2p"};
            final int[]qos={0,0};
            connOpts.setUserName(acessKey);
            connOpts.setServerURIs(new String[] { broker });
            connOpts.setPassword(sign.toCharArray());
            connOpts.setCleanSession(true);
            connOpts.setKeepAliveInterval(90);
            connOpts.setAutomaticReconnect(true);
            sampleClient.setCallback(new MqttCallbackExtended() {
                public void connectComplete(boolean reconnect, String serverURI) {
                    System.out.println("connect success");
                    //连接成功，需要上传客户端所有的订阅关系
                    try {
                        sampleClient.subscribe(topicFilters, qos);
                    }catch(Exception e){

                    }
                }
                public void connectionLost(Throwable throwable) {
                    System.out.println("mqtt connection lost");
                }
                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                    System.out.println("messageArrived:" + topic + "------" + new String(mqttMessage.getPayload()));
                    System.out.println("time:" + new Date().getTime());
                }
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    System.out.println("deliveryComplete:" + iMqttDeliveryToken.getMessageId());
                }
            });
            //客户端每次上线都必须上传自己所有涉及的订阅关系，否则可能会导致消息接收延迟
            sampleClient.connect(connOpts);
            //每个客户端最多允许存在30个订阅关系，超出限制可能会丢弃导致收不到部分消息
            sampleClient.subscribe(topicFilters,qos);
            Thread.sleep(Integer.MAX_VALUE);
        } catch (Exception me) {
            me.printStackTrace();
        }
    }

    public static void receMessage() throws IOException {
        final ControlVersionDao controlVersionDao = new ControlVersionDao();
        /**
         * 设置当前用户私有的MQTT的接入点。例如此处示意使用XXX，实际使用请替换用户自己的接入点。接入点的获取方法是，在控制台申请MQTT实例，每个实例都会分配一个接入点域名。
         */
        final String broker ="tcp://post-cn-v0h0bwv350e.mqtt.aliyuncs.com:1883";
        /**
         * 设置阿里云的AccessKey，用于鉴权
         */
        final String acessKey ="LTAI9yxNEdqLZsNi";
        /**
         * 设置阿里云的SecretKey，用于鉴权
         */
        final String secretKey ="Yvx4F4j1Cy3Ozmg5hHIyDT5VlT7ZTw";
        /**
         * 发消息使用的一级Topic，需要先在MQ控制台里申请
         */
        final String topic ="jxm-push";
        /**
         * MQTT的ClientID，一般由两部分组成，GroupID@@@DeviceID
         * 其中GroupID在MQ控制台里申请
         * DeviceID由应用方设置，可能是设备编号等，需要唯一，否则服务端拒绝重复的ClientID连接
         */
        final String clientId ="GID_jxm@@@ClientID_0465455";
        String sign;
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            final MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            final MqttConnectOptions connOpts = new MqttConnectOptions();
           // System.out.println("Connecting to broker: " + broker);
            /**
             * 计算签名，将签名作为MQTT的password
             * 签名的计算方法，参考工具类MacSignature，第一个参数是ClientID的前半部分，即GroupID
             * 第二个参数阿里云的SecretKey
             */
            sign = MacSignature.macSignature(clientId.split("@@@")[0], secretKey);
            /**
             * 设置订阅方订阅的Topic集合，此处遵循MQTT的订阅规则，可以是一级Topic，二级Topic，P2P消息请订阅/p2p
             */
            //final String[] topicFilters=new String[]{topic+"/notice/",topic+"/p2p"};
            String topic2 = "GID_jxm_MQTT";
            final String[] topicFilters=new String[]{topic,topic2};
            final int[]qos={0,0};
            connOpts.setUserName(acessKey);
            connOpts.setServerURIs(new String[] { broker });
            connOpts.setPassword(sign.toCharArray());
            connOpts.setCleanSession(true);
            connOpts.setKeepAliveInterval(90);
            connOpts.setAutomaticReconnect(true);
            sampleClient.setCallback(new MqttCallbackExtended() {
                public void connectComplete(boolean reconnect, String serverURI) {
                  //  System.out.println("connect success");
                    //连接成功，需要上传客户端所有的订阅关系
                    try {
                        sampleClient.subscribe(topicFilters, qos);
                    }catch(Exception e){

                    }
                }
                public void connectionLost(Throwable throwable) {
                    //System.out.println("mqtt connection lost");
                }
                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {

                    try{
                        if(topic.equals("GID_jxm_MQTT")){
                            JSONObject scrResponse = JSON.parseObject(new String(mqttMessage.getPayload(), "UTF-8"));
                            String uid = scrResponse.getString("clientId").replace("GID_jxm@@@ClientID_", "");
                            String eventType = scrResponse.getString("eventType");
                            String channelId  = scrResponse.getString("channelId");
                            long time = Long.parseLong(scrResponse.getString("time"));
                            if(ObjectId.isValid(uid)){
                                int status = 0;
                                if(eventType.equals("tcpclean")){//离线
                                    status = 0;
                                }else if(eventType.equals("connect")){//上线
                                    status = 1;
                                }else{
                                    status = 0;
                                }
                              /*  if(uid.equals("5a17dafd0a9d324986663c9a")){
                                    System.out.print("33");
                                    System.out.println(uid+"---"+eventType+"---"+time);
                                    System.out.println("messageArrived:" + topic + "------" + new String(mqttMessage.getPayload()));
                                }*/
                                if(status==1){
                                    controlVersionDao.updateEntry(new ObjectId(uid),time,status,channelId);
                                }else{
                                    controlVersionDao.updateNewEntry(new ObjectId(uid), time, status, channelId);
                                }

                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                 System.out.println("messageArrived:" + topic + "------" + new String(mqttMessage.getPayload()));
                 //   System.out.println("time:" + new Date().getTime());
                }
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                  //  System.out.println("deliveryComplete:" + iMqttDeliveryToken.getMessageId());
                }
            });
            //客户端每次上线都必须上传自己所有涉及的订阅关系，否则可能会导致消息接收延迟
            sampleClient.connect(connOpts);
            //每个客户端最多允许存在30个订阅关系，超出限制可能会丢弃导致收不到部分消息
            sampleClient.subscribe(topicFilters,qos);
            Thread.sleep(Integer.MAX_VALUE);
        } catch (Exception me) {
            me.printStackTrace();
        }
    }

}