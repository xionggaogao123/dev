package com.fulaan.systemMessage.service;

import com.db.indexPage.IndexPageDao;
import com.db.operation.AppNoticeDao;
import com.db.wrongquestion.SubjectClassDao;
import com.fulaan.base.BaseService;
import com.fulaan.indexpage.dto.IndexPageDTO;
import com.fulaan.operation.dto.AppCommentDTO;
import com.fulaan.operation.dto.AppNoticeDTO;
import com.pojo.indexPage.IndexPageEntry;
import com.pojo.newVersionGrade.CommunityType;
import com.pojo.wrongquestion.SubjectClassEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

/**
 * Created by James on 2018-05-22.
 */
@Service
public class SystemMessageService extends BaseService {
    //老师社群
   /* private static final String TEACHERCOMMUNIY = "5ae993953d4df93f01b11a36";
    private static final String TEACHERGROUP = "5ae993963d4df93f01b11a38";*/
    //线上
    private static final String TEACHERCOMMUNIY = "5ae993953d4df93f01b11a36";
    private static final String TEACHERGROUP = "5ae993963d4df93f01b11a38";
    //家长社群
   /* private static final String PARENTCOMMUNIY = "5acecca9bf2e792210a70583";
    private static final String PARENTGROUP = "5aceccaabf2e792210a70585";*/
    //线上
    private static final String PARENTCOMMUNIY = "5b04d9f53d4df9273f5c775a";
    private static final String PARENTGROUP = "5b04d9f53d4df9273f5c775c";
    //学生社群
   /* private static final String STUDENTCOMMUNIY = "5abaf547bf2e791a5457a584";
    private static final String STUDENTGROUP = "5abaf548bf2e791a5457a586";*/
    //线上
    private static final String STUDENTCOMMUNIY = "5b04d9eb3d4df9273f5c7747";
    private static final String STUDENTGROUP = "5b04d9eb3d4df9273f5c7749";

    private AppNoticeDao appNoticeDao = new AppNoticeDao();

    private IndexPageDao indexPageDao = new IndexPageDao();

    private SubjectClassDao subjectClassDao = new SubjectClassDao();

    //保存系统消息
    public  void  addEntry(ObjectId userId,AppCommentDTO dto){
        String coStr = dto.getComList();
        String[] strings = coStr.split(",");
        SubjectClassEntry subjectClassEntry = subjectClassDao.getList().get(0);
        String s = "";
        for(String str :strings){
            if(str.equals(TEACHERCOMMUNIY)){
                s = s + "老师"+"/";
            }else if(str.equals(PARENTCOMMUNIY)){
                s = s + "家长"+"/";
            }else if(str.equals(STUDENTCOMMUNIY)){
                s = s + "孩子"+"/";
            }
        }
        if(!s.equals("")){
            s = s.substring(0,s.length()-1);
        }
        for(String str:strings){
            if(str.equals(TEACHERCOMMUNIY)){//发送老师
                AppNoticeDTO appNoticeDTO=new AppNoticeDTO(
                        subjectClassEntry.getID().toString(),
                        s,
                        dto.getTitle(),
                        dto.getDescription(),
                        TEACHERGROUP,
                        str,
                        Constant.ONE,
                        dto.getVideoList(),
                        dto.getImageList(),
                        dto.getAttachements(),
                        dto.getVoiceList(),
                        dto.getSubject(),
                        dto.getSubjectId());
                appNoticeDTO.setUserId(userId.toString());
                ObjectId oid = appNoticeDao.saveAppNoticeEntry(appNoticeDTO.buildEntry());
                //添加临时记录表
                IndexPageDTO dto1 = new IndexPageDTO();
                dto1.setType(CommunityType.appNotice.getType());
                dto1.setUserId(userId.toString());
                dto1.setCommunityId(str);
                dto1.setContactId(oid.toString());
                IndexPageEntry entry = dto1.buildAddEntry();
                indexPageDao.addEntry(entry);
            }else if(str.equals(PARENTCOMMUNIY)){//发送家长
                AppNoticeDTO appNoticeDTO=new AppNoticeDTO(
                        subjectClassEntry.getID().toString(),
                        s,
                        dto.getTitle(),
                        dto.getDescription(),
                        PARENTGROUP,
                        str,
                        Constant.ONE,
                        dto.getVideoList(),
                        dto.getImageList(),
                        dto.getAttachements(),
                        dto.getVoiceList(),
                        dto.getSubject(),
                        dto.getSubjectId());
                appNoticeDTO.setUserId(userId.toString());
                ObjectId oid = appNoticeDao.saveAppNoticeEntry(appNoticeDTO.buildEntry());
                //添加临时记录表
                IndexPageDTO dto1 = new IndexPageDTO();
                dto1.setType(CommunityType.appNotice.getType());
                dto1.setUserId(userId.toString());
                dto1.setCommunityId(str);
                dto1.setContactId(oid.toString());
                IndexPageEntry entry = dto1.buildAddEntry();
                indexPageDao.addEntry(entry);

            }else if(str.equals(STUDENTCOMMUNIY)){//发送学生
                AppNoticeDTO appNoticeDTO=new AppNoticeDTO(
                        subjectClassEntry.getID().toString(),
                        s,
                        dto.getTitle(),
                        dto.getDescription(),
                        STUDENTGROUP,
                        str,
                        Constant.THREE,
                        dto.getVideoList(),
                        dto.getImageList(),
                        dto.getAttachements(),
                        dto.getVoiceList(),
                        dto.getSubject(),
                        dto.getSubjectId());
                appNoticeDTO.setUserId(userId.toString());
                appNoticeDao.saveAppNoticeEntry(appNoticeDTO.buildEntry());
            }
        }
    }


}
