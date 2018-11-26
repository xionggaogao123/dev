package com.fulaan.count.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.backstage.TeacherApproveDao;
import com.db.fcommunity.GroupDao;
import com.db.fcommunity.MemberDao;
import com.db.fcommunity.NewVersionCommunityBindDao;
import com.db.jiaschool.HomeSchoolDao;
import com.db.jiaschool.SchoolCommunityDao;
import com.db.operation.AppCommentDao;
import com.db.user.UserDao;
import com.fulaan.count.dto.JxmCountDto;
import com.fulaan.jiaschool.dto.HomeSchoolDTO;
import com.fulaan.jiaschool.service.HomeSchoolService;
import com.pojo.jiaschool.HomeSchoolEntry;

@Service
public class CountService {
    
    
    private SchoolCommunityDao schoolCommunityDao = new SchoolCommunityDao();
    
    private GroupDao groupDao = new GroupDao();
    
    private MemberDao memberDao = new MemberDao();
    
    private TeacherApproveDao teacherApproveDao = new TeacherApproveDao();
    
    private NewVersionCommunityBindDao newVersionCommunityBindDao = new NewVersionCommunityBindDao();
    
    private HomeSchoolDao homeSchoolDao = new HomeSchoolDao();
    
    private UserDao userDao = new UserDao();
    //作业
    private AppCommentDao appCommentDao;
    
    
    /**
     * 
     *〈简述〉获取学校列表
     *〈详细描述〉
     * @author Administrator
     * @return
     */
    public List<HomeSchoolDTO> getSimpleSchoolList(){
        List<HomeSchoolDTO> homeSchoolDTOs = new ArrayList<HomeSchoolDTO>();
        List<HomeSchoolEntry> entries = homeSchoolDao.getSchoolList();
        for(HomeSchoolEntry homeSchoolEntry : entries){
            if(homeSchoolEntry.getName()!=null){
                homeSchoolDTOs.add(new HomeSchoolDTO(homeSchoolEntry));
            }
        }
        return homeSchoolDTOs;
    }

    /**
     * 
     *〈简述〉家校美统计
     *〈详细描述〉
     * @author Administrator
     * @param schooleId
     * @return
     */
    public JxmCountDto jxmCount(String schooleId) {
        JxmCountDto jxmCountDto = new JxmCountDto();
        if (schooleId != null) {
            //社区数
            List<ObjectId> communityIdList = schoolCommunityDao.getCommunityIdsListBySchoolId(new ObjectId(schooleId));
            jxmCountDto.setCommunityCount(communityIdList.size());
            //通过社区id查找群组
            List<ObjectId> groupIdList = groupDao.getCommunitysIdsList(communityIdList);
            //成员id
            List<ObjectId> memberList = memberDao.getAllGroupIdsMembers(groupIdList);
            jxmCountDto.setUserCount(memberList.size());
            //大V老师的id
            List<ObjectId> objectIdList = teacherApproveDao.selectMap(memberList);
            jxmCountDto.setTeacherCount(objectIdList.size());
            
            //家长数量
            jxmCountDto.setParentCount(memberList.size() - objectIdList.size());
            //学生数量
            Integer stuNum = 0;
            for (ObjectId cId : communityIdList) {
                List<ObjectId> userId = newVersionCommunityBindDao.getStudentListByCommunityId(cId);
                stuNum += userDao.getStudentNum(userId);
            }
            jxmCountDto.setStudentCount(stuNum);
            //本日发布作业数量
            
        }
        
        return jxmCountDto;
    }
    
    /*public Map<Integer, Long> getTimePointOneDay() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DATE);
        
        
    }*/
}
