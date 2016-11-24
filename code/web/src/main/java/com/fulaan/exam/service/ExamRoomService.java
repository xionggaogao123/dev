package com.fulaan.exam.service;

import java.util.ArrayList;
import java.util.List;

import com.pojo.exam.ExamRoomDTO;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.exam.ExamRoomDao;
import com.db.factory.MongoFacroty;
import com.fulaan.repair.service.RepairService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.exam.ExamRoomEntry;
import com.sys.constants.Constant;

/**
 * 考场资源service类
 *
 * @author cxy
 */
@Service
public class ExamRoomService {
    private static final Logger logger = Logger.getLogger(RepairService.class);

    private ExamRoomDao examRoomDao = new ExamRoomDao();

    /**
     * 添加一条考场资源信息
     *
     * @param e
     * @return
     */
    public ObjectId addExamRoomEntry(ExamRoomEntry e) {
        return examRoomDao.addExamRoomEntry(e);
    }

    /**
     * 根据Id查询一个特定的考场资源信息
     *
     * @param id
     * @return
     */
    public ExamRoomEntry getExamRoomEntry(ObjectId id) {
        return examRoomDao.getExamRoomEntry(id);
    }

    /**
     * 删除一条考场资源
     *
     * @param id
     */
    public void deleteExamRoom(ObjectId id) {
        examRoomDao.deleteExamRoom(id);
    }

    /**
     * 根据ID更新一条考场资源信息
     */
    public void updateExamRoom(ObjectId id, String examRoomNumber, String examRoomName, int examRoomSitNumber, String examRoomPostscript) {

        examRoomDao.updateExamRoom(id, examRoomNumber, examRoomName, examRoomSitNumber, examRoomPostscript);

    }

    /**
     * 查询本校所有的考场资源记录
     *
     * @return
     */
    public List<ExamRoomDTO> queryExamRoomsBySchoolId(ObjectId schoolId) {
        List<ExamRoomEntry> roomEntries = examRoomDao.queryExamRoomsBySchoolId(schoolId);
        List<ExamRoomDTO> formatList = new ArrayList<ExamRoomDTO>();
        for (ExamRoomEntry one : roomEntries) {
            formatList.add(new ExamRoomDTO(one));
        }
        return formatList;
    }
    
    /**
     * 查询本场考试所用的激活的所有的考场记录信息
     *
     * @return
     */
    public List<ExamRoomDTO> queryExamRoomsByExamId(ObjectId examId) {
        return examRoomDao.queryExamRoomsByExamId(examId);
    }
}
