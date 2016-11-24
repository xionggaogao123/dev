package com.easemob.server;

import com.easemob.server.api.ChatGroupAPI;
import com.easemob.server.api.IMUserAPI;
import com.easemob.server.comm.ClientContext;
import com.easemob.server.comm.EasemobRestAPIFactory;
import com.easemob.server.comm.body.*;
import com.easemob.server.comm.wrapper.BodyWrapper;
import com.easemob.server.comm.wrapper.ResponseWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jerry on 2016/10/24.
 * 操作环信 API
 */
public class EaseMobAPI {

  /**
   * 创建用户 - 无昵称
   * @param userId 用户id
   */
  public static void createUser(String userId){
    EasemobRestAPIFactory factory = ClientContext.getInstance().init(ClientContext.INIT_FROM_PROPERTIES).getAPIFactory();
    IMUserAPI user = (IMUserAPI)factory.newInstance(EasemobRestAPIFactory.USER_CLASS);
    BodyWrapper userBody = new IMUserBody(userId, "123456",userId);
    user.createNewIMUserSingle(userBody);
  }

  /**
   * 创建用户 - 有昵称
   * @param userId 用户id
   */
  public static void createUser(String userId,String nickName){
    EasemobRestAPIFactory factory = ClientContext.getInstance().init(ClientContext.INIT_FROM_PROPERTIES).getAPIFactory();
    IMUserAPI user = (IMUserAPI)factory.newInstance(EasemobRestAPIFactory.USER_CLASS);
    BodyWrapper userBody = new IMUserBody(userId, "123456",nickName);
    user.createNewIMUserSingle(userBody);
  }

  /**
   * 批量创建用户 - Map里存 userId - 用户id nickName - 用户昵称
   * @param userList
   */
  public static void createUser(List<Map<String,String>> userList){
    List<IMUserBody> userBodies = new ArrayList<IMUserBody>();
    for(Map<String,String> map:userList){
      String userId = map.get("userId");
      String nickName = map.get("nickName");
      IMUserBody imUserBody = new IMUserBody(userId,"123456",nickName);
      userBodies.add(imUserBody);
    }
    EasemobRestAPIFactory factory = ClientContext.getInstance().init(ClientContext.INIT_FROM_PROPERTIES).getAPIFactory();
    IMUserAPI user = (IMUserAPI)factory.newInstance(EasemobRestAPIFactory.USER_CLASS);
    user.createNewIMUserBatch(userBodies);
  }
  /**
   * 创建群组
   * @param groupName 群组名称
   * @param desc 简介
   * @param owner 群主id
   */
  public static ResponseWrapper createGroup(String groupName,String desc,String owner){
    EasemobRestAPIFactory factory = ClientContext.getInstance().init(ClientContext.INIT_FROM_PROPERTIES).getAPIFactory();
    ChatGroupAPI chatgroup = (ChatGroupAPI)factory.newInstance(EasemobRestAPIFactory.CHATGROUP_CLASS);
    BodyWrapper chat = new ChatGroupBody(groupName,desc,false,(long)2000,false,owner,null);
    return (ResponseWrapper)chatgroup.createChatGroup(chat);
  }

  /**
   * 更改群组信息
   * @param groupId 群组id
   * @param groupName
   * @param groupDesc 群主id
   */
  public static ResponseWrapper updateGroup(String groupId,String groupName,String groupDesc){
    EasemobRestAPIFactory factory = ClientContext.getInstance().init(ClientContext.INIT_FROM_PROPERTIES).getAPIFactory();
    ChatGroupAPI chatgroup = (ChatGroupAPI)factory.newInstance(EasemobRestAPIFactory.CHATGROUP_CLASS);
    BodyWrapper chat = new ModifyChatRoomBody(groupName,groupDesc,(long)2000);
    return (ResponseWrapper)chatgroup.modifyChatGroup(groupId,chat);
  }

  /**
   * 添加用户到群组
   * @param groupId 群组id
   * @param userId 用户id
   * @return
   */
  public static ResponseWrapper addSingleUserToChatGroup(String groupId, String userId){
    EasemobRestAPIFactory factory = ClientContext.getInstance().init(ClientContext.INIT_FROM_PROPERTIES).getAPIFactory();
    ChatGroupAPI chatgroup = (ChatGroupAPI)factory.newInstance(EasemobRestAPIFactory.CHATGROUP_CLASS);
    return (ResponseWrapper)chatgroup.addSingleUserToChatGroup(groupId,userId);
  }

  /**
   * 批量添加用户到群组
   * @param groupId
   * @param userIds - 用户id
   * @return
   */
  public static ResponseWrapper addBatchUsersToChatGroup(String groupId,List<String> userIds){
    EasemobRestAPIFactory factory = ClientContext.getInstance().init(ClientContext.INIT_FROM_PROPERTIES).getAPIFactory();
    ChatGroupAPI chatgroup = (ChatGroupAPI)factory.newInstance(EasemobRestAPIFactory.CHATGROUP_CLASS);
    IMBatchUserBody imBatchUserBody = new IMBatchUserBody(userIds);
    return (ResponseWrapper)chatgroup.addBatchUsersToChatGroup(groupId,imBatchUserBody);
  }

  /**
   * 群组中移除用户
   * @param groupId 群组id
   * @param userId 用户id
   * @return
   */
  public static ResponseWrapper removeSingleUserFromChatGroup(String groupId,String userId){
    EasemobRestAPIFactory factory = ClientContext.getInstance().init(ClientContext.INIT_FROM_PROPERTIES).getAPIFactory();
    ChatGroupAPI chatgroup = (ChatGroupAPI)factory.newInstance(EasemobRestAPIFactory.CHATGROUP_CLASS);
    return (ResponseWrapper)chatgroup.removeSingleUserFromChatGroup(groupId,userId);
  }

  /**
   * 批量从群组中移除用户
   * @param groupId
   * @param userIds
   * @return
   */
  public static ResponseWrapper removeBatchUsersFromChatGroup(String groupId,List<String> userIds){
    EasemobRestAPIFactory factory = ClientContext.getInstance().init(ClientContext.INIT_FROM_PROPERTIES).getAPIFactory();
    ChatGroupAPI chatgroup = (ChatGroupAPI)factory.newInstance(EasemobRestAPIFactory.CHATGROUP_CLASS);
    return (ResponseWrapper)chatgroup.removeBatchUsersFromChatGroup(groupId,userIds.toArray(new String[userIds.size()]));
  }

  /**
   * 删除群组
   * @param groupId 群组id
   * @return
   */
  public static ResponseWrapper deleteChatGroup(String groupId){
    EasemobRestAPIFactory factory = ClientContext.getInstance().init(ClientContext.INIT_FROM_PROPERTIES).getAPIFactory();
    ChatGroupAPI chatgroup = (ChatGroupAPI)factory.newInstance(EasemobRestAPIFactory.CHATGROUP_CLASS);
    return (ResponseWrapper)chatgroup.deleteChatGroup(groupId);
  }

  /**
   * 群组转让
   * @param groupId
   * @param userId
   * @return
   */
  public static ResponseWrapper transferChatGroupOwner(String groupId,String userId){
    EasemobRestAPIFactory factory = ClientContext.getInstance().init(ClientContext.INIT_FROM_PROPERTIES).getAPIFactory();
    ChatGroupAPI chatgroup = (ChatGroupAPI)factory.newInstance(EasemobRestAPIFactory.CHATGROUP_CLASS);
    GroupOwnerTransferBody groupOwnerTransferBody = new GroupOwnerTransferBody(userId);
    return (ResponseWrapper)chatgroup.transferChatGroupOwner(groupId,groupOwnerTransferBody);
  }

}
