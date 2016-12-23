package com.fulaan.fgroup.service;

import com.easemob.server.EaseMobAPI;
import com.easemob.server.comm.wrapper.ResponseWrapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

/**
 * Created by jerry on 2016/11/21.
 * 环信服务
 */
@Service
public class EmService {

    /**
     * 创建环信群组
     *
     * @param owerId
     * @return
     */
    public String createEmGroup(ObjectId owerId) {
        ResponseWrapper responseWrapper = EaseMobAPI.createGroup("", "", owerId.toString());
        if (responseWrapper.getResponseStatus() == 200) {
            return ((ObjectNode) responseWrapper.getResponseBody()).get("data").get("groupid").asText();
        }
        return null;
    }

    /**
     * 把人加到环信群组
     *
     * @param emChatId
     * @param userId
     * @return
     */
    public boolean addUserToEmGroup(String emChatId, ObjectId userId) {
        ResponseWrapper responseWrapper = EaseMobAPI.addSingleUserToChatGroup(emChatId, userId.toString());
        return responseWrapper.getResponseStatus() == 200;
    }

    /**
     * 把用户从环信群组移除
     *
     * @param emChatId
     * @param userId
     * @return
     */
    public boolean removeUserFromEmGroup(String emChatId, ObjectId userId) {
        ResponseWrapper responseWrapper = EaseMobAPI.removeSingleUserFromChatGroup(emChatId, userId.toString());
        return responseWrapper.getResponseStatus() == 200;
    }
}