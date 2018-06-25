package com.fulaan.mqtt;

import java.io.IOException;

/**
 * Created by James on 2017/11/2.
 */
public class MQTTMain {

    public static void main(String[] args)throws IOException {
        String code = "我是方法调用的";
        String[] str = {};
        try{
            //MQTTSendMsg.main(str);
            //MQTTSendMsg.sendMessage(code,"55934c26f6f28b7261c1bae1",0l);
            MQTTRecvMsg.receMessage();
        }catch (Exception e){

        }
    }
}
