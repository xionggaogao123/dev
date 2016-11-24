package com.fulaan.zouban.service;

import com.db.zouban.ZoubanModeDao;
import com.fulaan.myschool.controller.GradeView;
import com.fulaan.school.service.SchoolService;
import com.fulaan.zouban.dto.ZoubanModeDTO;
import com.pojo.app.IdNameValuePair;
import com.pojo.app.IdNameValuePairDTO;
import com.pojo.app.NameValuePair;
import com.pojo.app.NameValuePairDTO;
import com.pojo.zouban.ZoubanModeEntry;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangkaidong on 2016/7/13.
 * <p/>
 * 走班模式配置
 */

@Service
public class ZoubanModeService {
    ZoubanModeDao zoubanModeDao = new ZoubanModeDao();

    @Autowired
    SchoolService schoolService;

    /**
     * 新增
     *
     * @param zoubanModeDTO
     * @return
     */
    public String add(ZoubanModeDTO zoubanModeDTO) {
        List<GradeView> gradeViewList = schoolService.findGradeList(zoubanModeDTO.getSchoolId());

        List<IdNameValuePairDTO> gradeList = new ArrayList<IdNameValuePairDTO>();
        for (GradeView gv : gradeViewList) {
            gradeList.add(new IdNameValuePairDTO(gv.getId(), gv.getName(), zoubanModeDTO.getMode().get(0).getValue()));
        }
        zoubanModeDTO.setGradeList(gradeList);
        ObjectId id = zoubanModeDao.add(zoubanModeDTO.exportEntry());
        return id.toString();
    }


    /**
     * 更新学校走班模式
     *
     * @param zoubanModeDTO
     */
    public void updateSchool(ZoubanModeDTO zoubanModeDTO) {
        List<NameValuePair> modeList = new ArrayList<NameValuePair>();
        for (NameValuePairDTO dto : zoubanModeDTO.getMode()) {
            modeList.add(new NameValuePair(dto.getName(), dto.getValue()));
        }
        zoubanModeDao.updateSchool(new ObjectId(zoubanModeDTO.getId()), modeList);
    }

    /**
     * 更新年级走班模式
     *
     * @param id
     * @param gradeId
     * @param mode
     */
    public void updateGrade(String id, String gradeId, int mode) {
        zoubanModeDao.updateGrade(new ObjectId(id), new ObjectId(gradeId), mode);
    }

    /**
     * 删除学校
     *
     * @param id
     */
    public void delete(String id) {
        zoubanModeDao.delete(new ObjectId(id));
    }

    /**
     * 查询总数（分页）
     *
     * @return
     */
    public int count() {
        return zoubanModeDao.count();
    }

    /**
     * 分页查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    public List<ZoubanModeDTO> findZoubanModeList(int page, int pageSize) {
        List<ZoubanModeDTO> dtoList = new ArrayList<ZoubanModeDTO>();
        List<ZoubanModeEntry> entryList = zoubanModeDao.findZoubanModeList((page - 1) * pageSize, pageSize);
        for (ZoubanModeEntry entry : entryList) {
            dtoList.add(new ZoubanModeDTO(entry));
        }
        return dtoList;
    }

    /**
     * 根据id查询学校走班模式
     *
     * @param id
     * @return
     */
    public ZoubanModeDTO findZoubanMode(String id) {
        ZoubanModeEntry entry = zoubanModeDao.findZoubanMode(new ObjectId(id));
        return new ZoubanModeDTO(entry);
    }

    /**
     * 根据学校名称模糊查询
     *
     * @param key
     * @param page
     * @param pageSize
     * @return
     */
    public List<ZoubanModeDTO> findZoubanModeList(String key, int page, int pageSize) {
        List<ZoubanModeDTO> dtoList = new ArrayList<ZoubanModeDTO>();
        List<ZoubanModeEntry> entryList = zoubanModeDao.findZoubanModeList(key, (page - 1) * pageSize, pageSize);
        for (ZoubanModeEntry entry : entryList) {
            dtoList.add(new ZoubanModeDTO(entry));
        }
        return dtoList;
    }

    /**
     * 判断学校是否已存在
     *
     * @param schoolId
     * @return
     */
    public boolean checkSchoolExist(String schoolId) {
        return zoubanModeDao.findSchoolMode(new ObjectId(schoolId)) != null;
    }


    /**
     * 查询年级走班模式
     *
     * @param schoolId
     * @param gradeId
     * @return
     */
    public int getGradeMode(String schoolId, String gradeId) {
        return zoubanModeDao.findZoubanMode(new ObjectId(schoolId), new ObjectId(gradeId));
    }


}
