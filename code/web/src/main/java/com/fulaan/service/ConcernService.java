package com.fulaan.service;

import com.db.fcommunity.ConcernDao;
import com.fulaan.dto.ConcernDTO;
import com.fulaan.user.service.UserService;
import com.pojo.fcommunity.ConcernEntry;
import com.pojo.user.UserEntry;
import com.sys.utils.AvatarUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/11/7.
 */
@Service
public class ConcernService {

    private ConcernDao concernDao = new ConcernDao();
    @Autowired
    private UserService userService;

    public void pushConcern(ObjectId userId, ObjectId concernId) {
        ConcernEntry concernEntry = new ConcernEntry(userId, concernId);
        concernDao.save(concernEntry);
    }

    public void pullConcern(ObjectId id) {
        concernDao.pullConcern(id);
    }

    public ConcernDTO getConcernById(ObjectId id) {
        ConcernEntry concernEntry = concernDao.getConcernEntry(id);
        if (null != concernEntry) {
            return new ConcernDTO(concernEntry);
        }
        return null;
    }

    public List<ConcernDTO> getConcernByUserId(ObjectId id, int page, int pageSize) {
        List<ConcernDTO> concernDTOs = new ArrayList<ConcernDTO>();
        List<ConcernEntry> concernEntries = concernDao.getConcernByUserId(id, page, pageSize);
        for (ConcernEntry concernEntry : concernEntries) {
            ConcernDTO concernDTO = new ConcernDTO(concernEntry);
            UserEntry userEntry = userService.findById(new ObjectId(concernDTO.getConcernId()));
            concernDTO.setAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(),userEntry.getSex()));
            concernDTO.setNickName(StringUtils.isNotBlank(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName());
            concernDTOs.add(concernDTO);

        }
        return concernDTOs;
    }

    public int countConcernList(ObjectId userId) {
        return concernDao.countConcernByUserId(userId);
    }

    public ConcernEntry getConcernData(ObjectId userId, ObjectId concernId, int remove) {
        return concernDao.getConcernData(userId, concernId, remove);
    }

    public void setConcernData(ObjectId userId, ObjectId concernId, int remove) {
        concernDao.setConcernData(userId, concernId, remove);
    }
}
