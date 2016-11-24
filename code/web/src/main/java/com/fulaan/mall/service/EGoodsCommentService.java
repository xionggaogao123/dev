package com.fulaan.mall.service;

import com.db.ebusiness.EGoodsCommentDao;
import com.db.ebusiness.EGoodsDao;
import com.fulaan.user.service.UserService;
import com.pojo.ebusiness.EGoodCommentDTO;
import com.pojo.ebusiness.EGoodsCommentEntry;
import com.pojo.user.UserDetailInfoDTO;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by fl on 2016/1/14.
 */
@Service
public class EGoodsCommentService {
    private EGoodsCommentDao eGoodsCommentDao = new EGoodsCommentDao();
    private EGoodsDao eGoodsDao = new EGoodsDao();
    @Autowired
    private UserService userService;
    @Autowired
    private EGoodsService eGoodsService;

    /**
     * 评价列表
     *
     * @param goodsId
     * @param page
     * @param pageSize
     * @return
     */
    public List<EGoodCommentDTO> getEGoodComments(ObjectId goodsId, int onlyImg, int page, int pageSize) {
        List<EGoodsCommentEntry> entries = eGoodsCommentDao.getEGoodsCommentEntrys(null, goodsId, onlyImg, (page - 1) * pageSize, pageSize);
        List<EGoodCommentDTO> eGoodCommentDTOs = new ArrayList<EGoodCommentDTO>();
        List<ObjectId> userIds = new ArrayList<ObjectId>();

        if (null != entries) {
            for (EGoodsCommentEntry entry : entries) {
                userIds.add(entry.getUserId());
                EGoodCommentDTO dto = new EGoodCommentDTO(entry);
                long time = entry.getID().getTime();
                String date = DateTimeUtils.convert(time, DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H);
                dto.setDate(date);
                eGoodCommentDTOs.add(dto);
            }
            //构建用户Map
            List<UserDetailInfoDTO> userList = userService.findUserInfoByIds(userIds);
            Map<String, Object> userMap = new HashMap<String, Object>();
            if (null != userList) {
                for (UserDetailInfoDTO user : userList) {
                    userMap.put(user.getId(), user);
                }
            }

            for (EGoodCommentDTO dto : eGoodCommentDTOs) {
                UserDetailInfoDTO user = (UserDetailInfoDTO) userMap.get(dto.getUserId());
                if (null != user) {
                    dto.setUserName(user.getUserName());
                    dto.setUserAvatar(user.getImgUrl());
                } else {
                    dto.setUserName("匿名");
                    dto.setUserAvatar("http://7xiclj.com1.z0.glb.clouddn.com/head-default-head.jpg");
                }

            }

        }
        return eGoodCommentDTOs;
    }

    public int getCommentCount(ObjectId goodsId) {
        return eGoodsCommentDao.getCommentsCount(null, goodsId);
    }

    public ObjectId addComment(EGoodCommentDTO eGoodCommentDTO) {
        eGoodsDao.updateCommentSummary(new ObjectId(eGoodCommentDTO.getGoodsId()), String.valueOf(eGoodCommentDTO.getScore()), 1);
        return eGoodsCommentDao.addEGoodsCommentEntry(eGoodCommentDTO.exportEntry());
    }
}
