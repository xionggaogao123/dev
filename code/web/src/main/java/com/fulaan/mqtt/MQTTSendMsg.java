package com.fulaan.mqtt;

import com.fulaan.mqtt.util.MacSignature;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class MQTTSendMsg {
    public static void sendMessage(final String code,final String clientBiao) throws IOException {
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
        final String clientId ="GID_jxm@@@ClientID_00001212";
        String sign;
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            final MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            final MqttConnectOptions connOpts = new MqttConnectOptions();
            //System.out.println("Connecting to broker: " + broker);
            /**
             * 计算签名，将签名作为MQTT的password。
             * 签名的计算方法，参考工具类MacSignature，第一个参数是ClientID的前半部分，即GroupID
             * 第二个参数阿里云的SecretKey
             */
            sign = MacSignature.macSignature(clientId.split("@@@")[0], secretKey);
            connOpts.setUserName(acessKey);
            connOpts.setServerURIs(new String[] { broker });
            connOpts.setPassword(sign.toCharArray());
            connOpts.setCleanSession(false);
            connOpts.setKeepAliveInterval(90);
            connOpts.setAutomaticReconnect(true);
            sampleClient.setCallback(new MqttCallbackExtended() {
                public void connectComplete(boolean reconnect, String serverURI) {
                    System.out.println("connect success");
                    //连接成功，需要上传客户端所有的订阅关系
                }
                public void connectionLost(Throwable throwable) {
                    System.out.println("mqtt connection lost");
                }
                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                    System.out.println("messageArrived:" + topic + "------" + new String(mqttMessage.getPayload()));
                }
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    System.out.println("deliveryComplete:" + iMqttDeliveryToken.getMessageId());
                    return;
                }
            });
            sampleClient.connect(connOpts);
            for (int i = 0; i < 1; i++) {
                try {
                    String scontent = new Date().getTime()+"&" + code;
                    //此处消息体只需要传入byte数组即可，对于其他类型的消息，请自行完成二进制数据的转换
                    final MqttMessage message = new MqttMessage(scontent.getBytes());
                    /*QoS级别	  cleanSession=true	                  cleanSession=false
                    QoS 0无离线消息，在线消息只尝试推一次        无离线消息,在线消息只尝试推一次
                    QoS 1无离线消息，在线消息保证可达             有离线消息,所有消息保证可达
                    QoS 2无离线消息，在线消息保证只推一次           暂不支持*/
                    message.setQos(1);
                    //System.out.println(i+" pushed at "+"扥东方"+" "+ scontent);
                    /**
                     *消息发送到某个主题Topic，所有订阅这个Topic的设备都能收到这个消息。
                     * 遵循MQTT的发布订阅规范，Topic也可以是多级Topic。此处设置了发送到二级Topic
                     */
//                    sampleClient.publish(topic+"/notice/", message);
                    //sampleClient.publish(topic, message);
                    /**
                     * 如果发送P2P消息，二级Topic必须是“p2p”，三级Topic是目标的ClientID
                     * 此处设置的三级Topic需要是接收方的ClientID
                     */
                    String p2pTopic =topic+"/p2p/GID_jxm@@@ClientID_"+clientBiao;
                    sampleClient.publish(p2pTopic,message);
                } catch (Exception e) {
                    System.out.print(e.getMessage());
                    e.printStackTrace();

                }
            }
            //关闭连接
            sampleClient.disconnect();
        } catch (Exception me) {
            System.out.print(me.getMessage());
            me.printStackTrace();
        }

    }

    public static void sendMessageList(final String code,final List<String> clientBiaos) throws IOException {
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
        final String clientId ="GID_jxm@@@ClientID_00001212333";
        String sign;
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            final MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            final MqttConnectOptions connOpts = new MqttConnectOptions();
            //System.out.println("Connecting to broker: " + broker);
            /**
             * 计算签名，将签名作为MQTT的password。
             * 签名的计算方法，参考工具类MacSignature，第一个参数是ClientID的前半部分，即GroupID
             * 第二个参数阿里云的SecretKey
             */
            sign = MacSignature.macSignature(clientId.split("@@@")[0], secretKey);
            connOpts.setUserName(acessKey);
            connOpts.setServerURIs(new String[] { broker });
            connOpts.setPassword(sign.toCharArray());
            connOpts.setCleanSession(false);
            connOpts.setKeepAliveInterval(90);
            connOpts.setAutomaticReconnect(true);
            sampleClient.setCallback(new MqttCallbackExtended() {
                public void connectComplete(boolean reconnect, String serverURI) {
                    System.out.println("connect success");
                    //连接成功，需要上传客户端所有的订阅关系
                }
                public void connectionLost(Throwable throwable) {
                    System.out.println("mqtt connection lost");
                }
                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                    System.out.println("messageArrived:" + topic + "------" + new String(mqttMessage.getPayload()));
                }
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    System.out.println("deliveryComplete:" + iMqttDeliveryToken.getMessageId());
                    return;
                }
            });
            sampleClient.connect(connOpts);
            for (int i = 0; i < 1; i++) {
                try {
                    String scontent = new Date().getTime()+"&" + code;
                    //此处消息体只需要传入byte数组即可，对于其他类型的消息，请自行完成二进制数据的转换
                    final MqttMessage message = new MqttMessage(scontent.getBytes());
                    /*QoS级别	  cleanSession=true	                  cleanSession=false
                    QoS 0无离线消息，在线消息只尝试推一次        无离线消息,在线消息只尝试推一次
                    QoS 1无离线消息，在线消息保证可达             有离线消息,所有消息保证可达
                    QoS 2无离线消息，在线消息保证只推一次           暂不支持*/
                    message.setQos(1);
                    //System.out.println(i+" pushed at "+"扥东方"+" "+ scontent);
                    /**
                     *消息发送到某个主题Topic，所有订阅这个Topic的设备都能收到这个消息。
                     * 遵循MQTT的发布订阅规范，Topic也可以是多级Topic。此处设置了发送到二级Topic
                     */
//                    sampleClient.publish(topic+"/notice/", message);
                    //sampleClient.publish(topic, message);
                    /**
                     * 如果发送P2P消息，二级Topic必须是“p2p”，三级Topic是目标的ClientID
                     * 此处设置的三级Topic需要是接收方的ClientID
                     */
                    if(clientBiaos.size()>0){
                        for(String str : clientBiaos){
                            String p2pTopic =topic+"/p2p/GID_jxm@@@ClientID_"+str;
                            sampleClient.publish(p2pTopic,message);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //关闭连接
            sampleClient.disconnect();
        } catch (Exception me) {
            me.printStackTrace();
        }

    }
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
        final String clientId ="GID_jxm@@@ClientID_0006";
        String sign;
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            final MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            final MqttConnectOptions connOpts = new MqttConnectOptions();
            System.out.println("Connecting to broker: " + broker);
            /**
             * 计算签名，将签名作为MQTT的password。
             * 签名的计算方法，参考工具类MacSignature，第一个参数是ClientID的前半部分，即GroupID
             * 第二个参数阿里云的SecretKey
             */
            sign = MacSignature.macSignature(clientId.split("@@@")[0], secretKey);
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
                }
                public void connectionLost(Throwable throwable) {
                    System.out.println("mqtt connection lost");
                }
                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                    System.out.println("messageArrived:" + topic + "------" + new String(mqttMessage.getPayload()));
                }
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    System.out.println("deliveryComplete:" + iMqttDeliveryToken.getMessageId());
                    System.out.println("time:" + new Date().getTime());
                }
            });
            sampleClient.connect(connOpts);
            for (int i = 0; i < 10; i++) {
                try {
                    String scontent = new Date()+"11932914803" + i;
                    //此处消息体只需要传入byte数组即可，对于其他类型的消息，请自行完成二进制数据的转换
                    final MqttMessage message = new MqttMessage(scontent.getBytes());
                    message.setQos(0);
                    System.out.println(i+" pushed at "+"扥东方"+" "+ scontent);
                    /**
                     *消息发送到某个主题Topic，所有订阅这个Topic的设备都能收到这个消息。
                     * 遵循MQTT的发布订阅规范，Topic也可以是多级Topic。此处设置了发送到二级Topic
                     */
//                    sampleClient.publish(topic+"/notice/", message);
                   /* sampleClient.publish(topic, message);*/
                    /**
                     * 如果发送P2P消息，二级Topic必须是“p2p”，三级Topic是目标的ClientID
                     * 此处设置的三级Topic需要是接收方的ClientID
                     */
                    String p2pTopic =topic+"/p2p/GID_jxm@@@ClientID_lsq000";
                    sampleClient.publish(p2pTopic,message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception me) {
            me.printStackTrace();
        }
    }
}