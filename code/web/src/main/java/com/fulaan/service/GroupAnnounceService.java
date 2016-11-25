package com.fulaan.service;

import com.db.fcommunity.GroupAnnounceDao;
import com.fulaan.dto.CommunityDetailDTO;
import com.fulaan.pojo.Attachement;
import com.fulaan.pojo.GroupAnnounceDTO;
import com.fulaan.pojo.PageModel;
import com.fulaan.user.service.UserService;
import com.fulaan.util.DateUtils;
import com.pojo.fcommunity.GroupAnnounceEntry;
import com.pojo.user.AvatarType;
import com.pojo.user.UserEntry;
import com.sys.utils.AvatarUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moslpc on 2016/11/8.
 */

@Service
public class GroupAnnounceService {

    private GroupAnnounceDao groupAnnounceDao = new GroupAnnounceDao();
    @Autowired
    private UserService userService;


    public void save(ObjectId groupId, ObjectId userId, String title, String content, List<String> images) {
        ObjectId _id = new ObjectId();
        GroupAnnounceEntry groupAnnounceEntry = new GroupAnnounceEntry(_id, groupId, userId, title, content, images);
        groupAnnounceDao.save(groupAnnounceEntry);
    }

    public void save(ObjectId groupId, String title, String content, ObjectId userId, List<String> images) {
        ObjectId _id = new ObjectId();
        GroupAnnounceEntry groupAnnounceEntry = new GroupAnnounceEntry(_id, groupId, userId, title, content, images);
        groupAnnounceDao.save(groupAnnounceEntry);
    }

    public GroupAnnounceDTO getEarlyAnnounce(ObjectId groupId) {
        GroupAnnounceEntry entry = groupAnnounceDao.getEarlyOne(groupId);
        if (entry != null) {
            return new GroupAnnounceDTO(entry);
        }
        return null;
    }

    public PageModel<CommunityDetailDTO> getGroupAnnounceByMessage(ObjectId groupId, int page, int pageSize) {
        int totalCount = groupAnnounceDao.count(groupId);
        int totalPages = (int) Math.ceil(totalCount / pageSize) + 1;
        if (page > totalPages) {
            page = 1;
        }

        if (page < 1) {
            page = 1;
        }

        List<GroupAnnounceEntry> list = groupAnnounceDao.getByPage(groupId, page, pageSize);
        List<CommunityDetailDTO> groupAnnounceDTOs = new ArrayList<CommunityDetailDTO>();

        for (GroupAnnounceEntry entry : list) {
            CommunityDetailDTO communityDetailDTO = new CommunityDetailDTO();
            communityDetailDTO.setTitle(entry.getTitle());
            communityDetailDTO.setContent(entry.getAnnounce());
            UserEntry userEntry = userService.find(entry.getUserId());
            communityDetailDTO.setImageUrl(AvatarUtils.getAvatar(userEntry.getAvatar(), AvatarType.MIN_AVATAR.getType()));
            if (StringUtils.isNotBlank(userEntry.getNickName())) {
                communityDetailDTO.setNickName(userEntry.getNickName());
            } else {
                communityDetailDTO.setNickName(userEntry.getUserName());
            }

            List<Attachement> images = new ArrayList<Attachement>();
            for (String image : entry.getImages()) {
                Attachement attachement = new Attachement();
                attachement.setUrl(image);
                images.add(attachement);
            }
            communityDetailDTO.setImages(images);
            communityDetailDTO.setTime(DateUtils.timeStampToStr(entry.getID().getTimestamp()));
            groupAnnounceDTOs.add(communityDetailDTO);
        }


        PageModel<CommunityDetailDTO> pageModel = new PageModel<CommunityDetailDTO>();
        pageModel.setResult(groupAnnounceDTOs);
        pageModel.setTotalCount(totalCount);
        pageModel.setTotalPages(totalPages);
        pageModel.setPageSize(pageSize);
        pageModel.setPage(page);

        return pageModel;
    }


}
