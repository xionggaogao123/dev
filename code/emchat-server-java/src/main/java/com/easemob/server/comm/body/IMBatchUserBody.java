package com.easemob.server.comm.body;

import com.easemob.server.comm.wrapper.BodyWrapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

/**
 * Created by jerry on 2016/11/10.
 */
public class IMBatchUserBody implements BodyWrapper {

    private List<String> userNames;

    public IMBatchUserBody(List<String> userNames) {
        super();
        this.userNames = userNames;
    }

    public List<String> getUserNames() {
        return userNames;
    }

    public void setUserNames(List<String> userNames) {
        this.userNames = userNames;
    }

    public ContainerNode<?> getBody() {

        ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
        for (String userName : userNames) {
            arrayNode.add(userName);
        }
        objectNode.put("usernames", arrayNode);
        return objectNode;
    }

    public Boolean validate() {
        return userNames != null && userNames.size() > 0;
    }
}
