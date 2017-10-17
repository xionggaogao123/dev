package com.fulaan.indexpage.service;

import com.db.indexPage.IndexPageDao;
import com.db.operation.AppCommentDao;
import com.db.operation.AppNoticeDao;
import com.fulaan.community.dto.CommunityDTO;
import com.fulaan.indexpage.dto.IndexPageDTO;
import com.fulaan.operation.dto.AppCommentDTO;
import com.fulaan.operation.dto.AppNoticeDTO;
import com.fulaan.service.CommunityService;
import com.fulaan.user.service.UserService;
import com.pojo.appnotice.AppNoticeEntry;
import com.pojo.indexPage.IndexPageEntry;
import com.pojo.newVersionGrade.CommunityType;
import com.pojo.operation.AppCommentEntry;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import com.sys.utils.TimeChangeUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by James on 2017/9/28.
 */
@Service
public class IndexPageService {
    private AppCommentDao appCommentDao = new AppCommentDao();

    private AppNoticeDao appNoticeDao = new AppNoticeDao();
    @Autowired
    private CommunityService communityService;
    @Autowired
    private UserService userService;

    private IndexPageDao indexPageDao = new IndexPageDao();



    public List<Map<String,Object>> getIndexList(ObjectId userId,int page,int pageSize){
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        List<CommunityDTO> communityDTOList =communityService.getCommunitys(userId, 1, 100);
        List<ObjectId>  dlist = new ArrayList<ObjectId>();
        if(communityDTOList.size() >0){
            for(CommunityDTO dto : communityDTOList){
                dlist.add(new ObjectId(dto.getId()));
            }
        }
        List<IndexPageEntry> entrys = indexPageDao.getPageList(dlist, page, pageSize);
        //作业
        List<ObjectId> appList = new ArrayList<ObjectId>();
        Map<String,Object> appMap =new HashMap<String, Object>();
        //通知
        List<ObjectId> noList = new ArrayList<ObjectId>();
        if(entrys.size()>0){
            for(IndexPageEntry entry : entrys){
                if(entry.getType()==1){
                    //作业
                    appList.add(entry.getContactId());
                }else if(entry.getType()==2){
                    //通知
                    noList.add(entry.getContactId());
                }
            }
        }
        //作业
        List<String> uids = new ArrayList<String>();
        if(appList.size()>0){
            List<AppCommentDTO> dtos = new ArrayList<AppCommentDTO>();
            List<AppCommentEntry> appCommentEntries = appCommentDao.getEntryListByIds(appList);
            if(appCommentEntries.size()>0){
                for(AppCommentEntry en : appCommentEntries){
                    AppCommentDTO dto3 = new AppCommentDTO(en);
                    if(dto3.getAdminId() != null && dto3.getAdminId().equals(userId.toString())){

                    }else{
                        dto3.setType(2);
                        uids.add(dto3.getAdminId());
                        dtos.add(dto3);
                    }
                }
            }

            List<UserDetailInfoDTO> udtos = userService.findUserInfoByUserIds(uids);
            Map<String,UserDetailInfoDTO> map = new HashMap<String, UserDetailInfoDTO>();
            if(udtos != null && udtos.size()>0){
                for(UserDetailInfoDTO dto4 : udtos){
                    map.put(dto4.getId(),dto4);
                }
            }
            for(AppCommentDTO dto5 : dtos){
                dto5.setAdminName(map.get(dto5.getAdminId()).getUserName());
                dto5.setAdminUrl(map.get(dto5.getAdminId()).getImgUrl());
            }
            for(AppCommentDTO dto6 : dtos){
                Map<String,Object> ob1 = new HashMap<String, Object>();
                ob1.put("label", CommunityType.appComment.getDes());
                ob1.put("communityName",dto6.getRecipientName());
                ob1.put("userName",dto6.getAdminName());
                ob1.put("userImg",dto6.getAdminUrl());
                ob1.put("title","作业");
                ob1.put("time", TimeChangeUtils.getChangeStringTime(dto6.getCreateTime()));
                ob1.put("description",dto6.getDescription());
                ob1.put("imageUrl",dto6.getImageList());
                ob1.put("talkNumber",dto6.getTalkNumber());
                ob1.put("videoList","");
                ob1.put("attachmentEntries","");
                list.add(ob1);
            }
        }
        //通知
        if(noList.size()>0){
           List<AppNoticeEntry> appNoticeEntries = appNoticeDao.getAppNoticeEntriesByIds(noList);
            List<ObjectId> userIds=new ArrayList<ObjectId>();
            for(AppNoticeEntry entry:appNoticeEntries){
                userIds.add(entry.getUserId());
            }
            Map<ObjectId,UserEntry> userEntryMap=userService.getUserEntryMap(userIds, Constant.FIELDS);
            for(AppNoticeEntry entry:appNoticeEntries){
                AppNoticeDTO dto8=new AppNoticeDTO(entry);
                UserEntry userEntry=userEntryMap.get(entry.getUserId());
                if(null!=userEntry){
                    dto8.setAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(), userEntry.getSex()));
                    dto8.setUserName(userEntry.getNickName());
                }
                Map<String,Object> ob1 = new HashMap<String, Object>();
                ob1.put("label", CommunityType.appNotice.getDes());
                ob1.put("communityName",dto8.getGroupName());
                ob1.put("userName",dto8.getUserName());
                ob1.put("userImg",dto8.getAvatar());
                ob1.put("title",dto8.getTitle());
                ob1.put("time",TimeChangeUtils.getChangeStringTime(dto8.getTime()));
                ob1.put("description",dto8.getContent());
                ob1.put("imageUrl",dto8.getImageList());
                ob1.put("talkNumber",dto8.getCommentCount());
                ob1.put("videoList",dto8.getVideoList());
                ob1.put("attachmentEntries",dto8.getAttachements());
                list.add(ob1);

            }

        }



        return list;
    }
    public void addIndexPage(String communityId,String contactId,int type){
        //添加临时记录表
        IndexPageDTO dto1 = new IndexPageDTO();
        dto1.setType(type);
        dto1.setCommunityId(communityId);
        dto1.setContactId(contactId);
        IndexPageEntry entry = dto1.buildAddEntry();
        indexPageDao.addEntry(entry);
    }

}
