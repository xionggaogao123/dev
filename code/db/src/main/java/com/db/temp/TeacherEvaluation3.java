package com.db.temp;

import com.db.teacherevaluation.*;
import com.pojo.app.IdValuePair;
import com.pojo.teacherevaluation.MemberGroupEntry;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**教师评价改版数据库脚本
 * 1、设置评价默认姓名
 * 1.1 老师列表由List<ObjectId>改成List<IdValuePair>
 * 2、设置setting关联到评价id
 * 3、设置proportion关联到评价id
 * 4、设置element关联到评价id
 * 5、设置item关联到评价id
 * Created by fl on 2016/7/29.
 */
public class TeacherEvaluation3 {

    private MemberGroupDao memberGroupDao = new MemberGroupDao();
    private SettingDao settingDao = new SettingDao();
    private ProportionDao proportionDao = new ProportionDao();
    private ElementDao elementDao = new ElementDao();
    private EvaluationItemDao evaluationItemDao = new EvaluationItemDao();

    public static void main(String[] args){
        new TeacherEvaluation3().change();
    }

    private void change(){
        List<MemberGroupEntry> memberGroupEntries = getMemberGroupEntries();
        for(MemberGroupEntry memberGroupEntry : memberGroupEntries){
            setDefaultName(memberGroupEntry);
            changeSetting(memberGroupEntry);
            changeProportion(memberGroupEntry);
            changeElement(memberGroupEntry);
            changeItem(memberGroupEntry);
        }
        System.out.println(memberGroupEntries.size());
    }

    private List<MemberGroupEntry> getMemberGroupEntries(){//剔除重复数据
        List<MemberGroupEntry> memberGroupEntries = memberGroupDao.getAll();
        List<MemberGroupEntry> list = new ArrayList<MemberGroupEntry>();
        Map<String, MemberGroupEntry> map = new HashMap<String, MemberGroupEntry>();
        for(MemberGroupEntry memberGroupEntry : memberGroupEntries){
            String key = memberGroupEntry.getSchoolId().toString() + memberGroupEntry.getYear();
            if(map.get(key) == null){
                map.put(key, memberGroupEntry);
                list.add(memberGroupEntry);
            } else {
                memberGroupDao.removeMemberGroup(memberGroupEntry.getID());
            }
        }
System.out.println(memberGroupEntries.size() + "==============" + list.size());
        return list;
    }

    private void setDefaultName(MemberGroupEntry memberGroupEntry){
        memberGroupEntry.setName(memberGroupEntry.getYear() + "学年 教师评价");
        memberGroupEntry.setIsRemoved(0);
        List<MemberGroupEntry.TeacherGroup> teacherGroups = memberGroupEntry.getTeacherGroups();
        List<MemberGroupEntry.TeacherGroup> teacherGroupsnew = new ArrayList<MemberGroupEntry.TeacherGroup>();
        for(MemberGroupEntry.TeacherGroup teacherGroup : teacherGroups){
            List<ObjectId> objectIds = teacherGroup.getGroupTeachersOld();
            teacherGroup.setGroupTeachers(format(objectIds));
            teacherGroupsnew.add(teacherGroup);
        }
        memberGroupEntry.setTeacherGroups(teacherGroupsnew);
        memberGroupDao.saveMemberGroup(memberGroupEntry);
    }

    private List<IdValuePair> format(List<ObjectId> teacherIds){
        List<IdValuePair> idValuePairs = new ArrayList<IdValuePair>();
        for(ObjectId objectId : teacherIds){
            idValuePairs.add(new IdValuePair(objectId, 1));
        }
        return idValuePairs;
    }


    private void changeSetting(MemberGroupEntry memberGroupEntry){
        settingDao.updateEvaluationId(memberGroupEntry.getSchoolId(), memberGroupEntry.getYear(), memberGroupEntry.getID());
    }

    private void changeProportion(MemberGroupEntry memberGroupEntry) {
        proportionDao.updateEvaluationId(memberGroupEntry.getSchoolId(), memberGroupEntry.getYear(), memberGroupEntry.getID());
    }

    private void changeElement(MemberGroupEntry memberGroupEntry){
       elementDao.updateEvaluationId(memberGroupEntry.getSchoolId(), memberGroupEntry.getYear(), memberGroupEntry.getID());
    }

    private void changeItem(MemberGroupEntry memberGroupEntry){
        evaluationItemDao.updateEvaluationId(memberGroupEntry.getSchoolId(), memberGroupEntry.getYear(), memberGroupEntry.getID());
    }




}
