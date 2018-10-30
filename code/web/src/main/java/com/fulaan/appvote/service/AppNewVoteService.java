package com.fulaan.appvote.service;

import com.db.appvote.AppNewVoteDao;
import com.db.appvote.AppVoteOptionDao;
import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.MemberDao;
import com.db.indexPage.IndexContentDao;
import com.db.indexPage.IndexPageDao;
import com.fulaan.appvote.dto.AppNewVoteDTO;
import com.fulaan.indexpage.dto.IndexContentDTO;
import com.fulaan.indexpage.dto.IndexPageDTO;
import com.fulaan.instantmessage.service.RedDotService;
import com.fulaan.integral.service.IntegralSufferService;
import com.fulaan.picturetext.runnable.PictureRunNable;
import com.pojo.appvote.AppNewVoteEntry;
import com.pojo.appvote.AppVoteOptionEntry;
import com.pojo.fcommunity.AttachmentEntry;
import com.pojo.fcommunity.CommunityEntry;
import com.pojo.fcommunity.VideoEntry;
import com.pojo.indexPage.IndexContentEntry;
import com.pojo.indexPage.IndexPageEntry;
import com.pojo.instantmessage.ApplyTypeEn;
import com.pojo.integral.IntegralType;
import com.pojo.newVersionGrade.CommunityType;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by James on 2018-10-29.
 */
@Service
public class AppNewVoteService {

    private AppNewVoteDao appNewVoteDao = new AppNewVoteDao();

    private IndexPageDao indexPageDao = new IndexPageDao();

    private CommunityDao communityDao = new CommunityDao();

    private IndexContentDao indexContentDao = new IndexContentDao();

    private MemberDao memberDao = new MemberDao();

    private AppVoteOptionDao appVoteOptionDao = new AppVoteOptionDao();
    @Autowired
    private RedDotService redDotService;
    @Autowired
    private IntegralSufferService integralSufferService;

    public String saveNewAppVote(AppNewVoteDTO appNewVoteDTO,ObjectId userId){
        AppNewVoteEntry appNewVoteEntry = appNewVoteDTO.buildAddEntry();
        appNewVoteDao.saveAppVote(appNewVoteEntry);
        //添加选项
        String option = appNewVoteDTO.getOption();
        if(appNewVoteDTO.getType()==1 && option!=null && !option.equals("")){
            String[] strings =  option.split(",");
            for(String s:strings){
                AppVoteOptionEntry appVoteOptionEntry = new AppVoteOptionEntry(
                        appNewVoteEntry.getID(),
                        s,
                        userId,
                        Constant.ONE,
                        Constant.ONE,
                        new ArrayList<AttachmentEntry>(),
                        new ArrayList<AttachmentEntry>(),
                        new ArrayList<VideoEntry>(),
                        new ArrayList<AttachmentEntry>(),
                        Constant.ZERO,
                        new ArrayList<ObjectId>());
                appVoteOptionDao.saveAppVote(appVoteOptionEntry);
            }
        }
        //添加附加信息
        if(appNewVoteDTO.getVoteTypeList().contains(new Integer("2")) ||
                appNewVoteDTO.getApplyTypeList().contains(new Integer("2")) ||
                appNewVoteDTO.getVoteTypeList().contains(new Integer("3")) ||
                appNewVoteDTO.getApplyTypeList().contains(new Integer("3"))){ //投票或报名 有 家长或老师
            //发送通知
            List<ObjectId> communityIds2 = appNewVoteEntry.getCommunityList();
            List<CommunityEntry> communityEntries = communityDao.findByObjectIds(communityIds2);
            List<String> cids = new ArrayList<String>();
            StringBuffer sb = new StringBuffer();
            List<ObjectId> groupIds = new ArrayList<ObjectId>();
            List<ObjectId> communityIds = new ArrayList<ObjectId>();
            for(CommunityEntry communityEntry : communityEntries){
                sb.append(communityEntry.getCommunityName());
                cids.add(communityEntry.getID().toString());
                groupIds.add(communityEntry.getGroupId());
                communityIds.add(communityEntry.getID());
            }
            if(communityIds.size()>0){
                for(ObjectId communityId : communityIds){
                    PictureRunNable.addTongzhi(communityId.toString(), appNewVoteDTO.getUserId(), 5);
                    cids.add(communityId.toString());
                }
            }
            sb.substring(0,sb.length()-1);
            //新首页记录
            IndexPageDTO dto1 = new IndexPageDTO();
            dto1.setType(CommunityType.allNotice.getType());
            dto1.setUserId(appNewVoteDTO.getUserId());
            dto1.setReceiveIdList(cids);
            dto1.setCommunityId(null);
            dto1.setContactId(appNewVoteEntry.getID().toString());
            IndexPageEntry entry = dto1.buildAddEntry();
            indexPageDao.addEntry(entry);
            IndexContentDTO indexContentDTO = new IndexContentDTO(
                    appNewVoteDTO.getSubjectName(),
                    "通知-投票",
                    appNewVoteDTO.getTitle(),
                    appNewVoteDTO.getVideoList(),
                    appNewVoteDTO.getImageList(),
                    appNewVoteDTO.getAttachements(),
                    appNewVoteDTO.getVoiceList(),
                    sb.toString(),
                    "");
            Set<ObjectId> members=memberDao.getAllCommunityMemberIds(groupIds);
            IndexContentEntry indexContentEntry = indexContentDTO.buildEntry(appNewVoteDTO.getUserId(),appNewVoteDTO.getSubjectId(), null,null,1);
            indexContentEntry.setReadList(new ArrayList<ObjectId>());
            indexContentEntry.setContactId(appNewVoteEntry.getID());
            indexContentEntry.setContactType(7);
            indexContentEntry.setAllCount(members.size());
            indexContentDao.addEntry(indexContentEntry);
            //添加红点
            redDotService.addOtherEntryList(communityIds, new ObjectId(appNewVoteDTO.getUserId()), ApplyTypeEn.piao.getType(), 1);
        }
        int score = integralSufferService.addIntegral(new ObjectId(appNewVoteDTO.getUserId()), IntegralType.vote,4,1);
        return score+"";
    }



}
