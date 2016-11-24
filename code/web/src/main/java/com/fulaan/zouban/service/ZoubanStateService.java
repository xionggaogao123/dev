package com.fulaan.zouban.service;

import com.db.zouban.*;
import com.pojo.zouban.XuankeConfEntry;
import com.pojo.zouban.ZouBanCourseEntry;
import com.pojo.zouban.ZoubanState;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by qiangm on 2015/10/15.
 */
@Service
public class ZoubanStateService {
    private ZoubanStateDao zoubanStateDao = new ZoubanStateDao();
    private ZouBanCourseDao zouBanCourseDao = new ZouBanCourseDao();
    @Autowired
    private ZoubanModeService zoubanModeService;

    /**
     * 获取走班进度值
     *
     * @param term
     * @param schoolId
     * @param gradeId
     * @return
     */
    public int getZoubanState(String term, String schoolId, String gradeId) {
        int mode = zoubanModeService.getGradeMode(schoolId, gradeId);
        ZoubanState zoubanState = zoubanStateDao.findZoubanState(new ObjectId(gradeId), term);
        if (zoubanState != null) {
            if (mode == 0) {//非走班模式
                int state = zoubanState.getState();
                return state > 4 ? state : 4;
            } else {
                int state = zoubanState.getState();
                return state > 2 ? state : 2;
            }
        } else {
            ZoubanState zoubanState1 = new ZoubanState();
            zoubanState1.setTerm(term);
            zoubanState1.setGradeId(new ObjectId(gradeId));
            zoubanState1.setSchoolId(new ObjectId(schoolId));

            int state = 2;
            if (mode == 0) {//非走班模式，不需要第二步第三步
                state = 4;
            }
            zoubanState1.setState(state);
            zoubanState1.setState2(3);
            zoubanStateDao.addState(zoubanState1);
            return state;
        }
    }

    /**
     * 获取进度2(分段编班进度)
     *
     * @param term
     * @param gradeId
     * @return
     */
    public int getSubState(String term, String gradeId) {
        ZoubanState zoubanState = zoubanStateDao.findZoubanState(new ObjectId(gradeId), term);
        int state = 1;
        if (zoubanState != null) {
            state = zoubanState.getState2();
        }
        return state;
    }

    /**
     * 设置年级的进度
     *
     * @param term
     * @param gradeId
     * @param state
     */
    public void setZoubanSubState(String term, String gradeId, int state) {
        zoubanStateDao.setGradeSubState(new ObjectId(gradeId), term, state);
    }

    /**
     * 设置年级的进度
     *
     * @param term
     * @param gradeId
     * @param state
     */
    public void setZoubanState(String term, String gradeId, int state) {
        zoubanStateDao.setGradeState(new ObjectId(gradeId), term, state);
    }

}
