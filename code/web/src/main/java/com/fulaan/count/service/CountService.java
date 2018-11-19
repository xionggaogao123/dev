package com.fulaan.count.service;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.backstage.TeacherApproveDao;
import com.db.fcommunity.MemberDao;
import com.db.fcommunity.NewVersionCommunityBindDao;
import com.db.jiaschool.HomeSchoolDao;
import com.db.jiaschool.SchoolCommunityDao;
import com.fulaan.count.dto.JxmCountDto;
import com.fulaan.jiaschool.dto.HomeSchoolDTO;
import com.fulaan.jiaschool.service.HomeSchoolService;
import com.pojo.jiaschool.HomeSchoolEntry;

@Service
public class CountService {
    
    
    private SchoolCommunityDao schoolCommunityDao = new SchoolCommunityDao();
    
    private MemberDao memberDao = new MemberDao();
    
    private TeacherApproveDao teacherApproveDao = new TeacherApproveDao();
    
    private NewVersionCommunityBindDao newVersionCommunityBindDao = new NewVersionCommunityBindDao();
    
    private HomeSchoolDao homeSchoolDao = new HomeSchoolDao();
    
    
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

    public JxmCountDto jxmCount(String schooleId) {
        JxmCountDto jxmCountDto = new JxmCountDto();
        if (schooleId != null) {
            //社区数
            List<ObjectId> communityIdList = schoolCommunityDao.getCommunityIdsListBySchoolId(new ObjectId(schooleId));
            jxmCountDto.setCommunityCount(communityIdList.size());
            //成员id
            List<ObjectId> memberList = memberDao.getAllGroupIdsMembers(communityIdList);
            jxmCountDto.setUserCount(memberList.size());
            //大V老师的id
            List<ObjectId> objectIdList = teacherApproveDao.selectMap(memberList);
            jxmCountDto.setTeacherCount(objectIdList.size());
            
            //家长数量
            jxmCountDto.setParentCount(memberList.size() - objectIdList.size());
            //学生数量
            Integer stuNum = 0;
            for (ObjectId cId : communityIdList) {
                stuNum += newVersionCommunityBindDao.countStudentIdListByCommunityId(cId);
            }
            jxmCountDto.setStudentCount(stuNum);
            
        }
        
        return jxmCountDto;
    }
}
