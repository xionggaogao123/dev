package com.fulaan.moralculture.service;

import com.db.moralculture.MoralCultureManageDao;
import com.fulaan.moralculture.dto.MoralCultureManageDTO;
import com.pojo.moralculture.MoralCultureManageEntry;
import com.pojo.utils.DeleteState;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by guojing on 2015/7/2.
 */
@Service
public class MoralCultureManageService {

    private static final Logger logger= Logger.getLogger(MoralCultureManageService.class);

    private MoralCultureManageDao moralCultureManageDao = new MoralCultureManageDao();

    /**
     * 添加德育项目
     * @param moralCultureManageDTO
     * @return
     */
    public ObjectId addMoralCultureProject(MoralCultureManageDTO moralCultureManageDTO) {
        return moralCultureManageDao.addMoralCultureProject(moralCultureManageDTO.buildMoralCultureManageEntry());
    }

    /**
     * 删除德育项目
     * @param id
     * @param userId
     * @return
     */
    public void delMoralCultureProject(String id,ObjectId userId) {
        moralCultureManageDao.delMoralCultureProject(new ObjectId(id), userId, new Date().getTime());
    }

    /**
     * 修改德育项目
     * @param id
     * @param moralCultureName
     * @param userId
     * @return
     */
    public void updMoralCultureProject(String id, String moralCultureName, ObjectId userId) {
        moralCultureManageDao.updMoralCultureProject(new ObjectId(id), moralCultureName, userId, new Date().getTime());
    }

    /**
     * 德育项目列表
     * @param schoolId
     * @param state
     * @return
     */
    public List<MoralCultureManageDTO> selMoralCultureProjectList(String schoolId, DeleteState state) {
        List<MoralCultureManageEntry> list=moralCultureManageDao.selMoralCultureProjectList(new ObjectId(schoolId), state);
        List<MoralCultureManageDTO> dTOList = new ArrayList<MoralCultureManageDTO>();
        for (MoralCultureManageEntry entry : list) {
            MoralCultureManageDTO schoolSecurityDTO = new MoralCultureManageDTO(entry);
            dTOList.add(schoolSecurityDTO);
        }
        return dTOList;
    }

    /**
     * 查询单条德育项目
     * @param id
     * @return
     */
    public MoralCultureManageDTO selMoralCultureProjectInfo(String id) {
        MoralCultureManageEntry entry = moralCultureManageDao.selMoralCultureProjectInfo(id);
        MoralCultureManageDTO dTO = new MoralCultureManageDTO(entry);
        return dTO;
    }
}
