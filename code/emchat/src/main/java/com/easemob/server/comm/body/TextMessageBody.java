package com.easemob.server.comm.body;

import com.easemob.server.comm.constant.MsgType;
import com.fasterxml.jackson.databind.node.ContainerNode;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class TextMessageBody extends MessageBody {
    private Map<String,String> msg;

    public TextMessageBody(String targetType, String[] targets, String from, Map<String, String> ext, Map<String,String> msg) {
        super(targetType, targets, from, ext,msg);
        this.msg = msg;
    }

    public Map<String,String> getMsg() {
        return msg;
    }

    public ContainerNode<?> getBody() {
        if (!isInit()) {
//            this.getMsgBody().put("type", MsgType.TEXT);
//            this.getMsgBody().put("msg", msg);
            this.setInit(true);
        }

        return this.getMsgBody();
    }

    public Boolean validate() {
        String sendMessage=msg.get("msg");
        return super.validate() && StringUtils.isNotBlank(sendMessage);
    }
}
