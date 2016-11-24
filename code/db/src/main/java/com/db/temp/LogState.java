package com.db.temp;

import com.db.log.LogDao;
import com.db.school.ClassDao;
import com.db.school.SchoolDao;
import com.db.user.UserDao;
import com.pojo.log.LogEntry;
import com.pojo.school.ClassEntry;
import com.pojo.school.Grade;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by guojing on 2015/10/28.
 */
public class LogState {
    public static void main(String[] args) {

//        LogDao dao=new LogDao();
//
//        int logCount=dao.findAllCount();
//        int pageSize=10000;
//        int pageCount=1;
//        int m=logCount%pageSize;
//        if(m>0){
//            pageCount=logCount/pageSize+1;
//        } else {
//            pageCount=logCount/pageSize;
//        }
//        System.out.println("pageCount="+pageCount);
//       
//        System.out.println("#########用户登录日志修改开始" + DateTimeUtils.getCurrDate(DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H) + "##############");
//        for(int page=101;page<=pageCount;page++) {
//        	 System.out.println("page="+page);
//            List<LogEntry> list = dao.findLogEntry((page - 1) * pageSize, pageSize);
//            Set<ObjectId> userIds = new HashSet<ObjectId>();
//            for (LogEntry entry : list) {
//                userIds.add(entry.getUserId());
//            }
//
//            UserDao userDao = new UserDao();
//            List<UserEntry> users = userDao.getUserEntryList(userIds, null);
//
//            ClassDao classDao = new ClassDao();
//            SchoolDao schoolDao = new SchoolDao();
//            Map<ObjectId, ObjectId> scMap = new HashMap<ObjectId, ObjectId>();
//            Map<ObjectId, Set<ObjectId>> grMap = new HashMap<ObjectId, Set<ObjectId>>();
//            Map<ObjectId, Set<Integer>> gtMap = new HashMap<ObjectId, Set<Integer>>();
//            Map<ObjectId, Set<ObjectId>> clMap = new HashMap<ObjectId, Set<ObjectId>>();
//            Map<ObjectId, Integer> urMap = new HashMap<ObjectId, Integer>();
//            for (UserEntry ue : users) {
//                scMap.put(ue.getID(), ue.getSchoolID());
//                Set<ObjectId> gradeIds = new HashSet<ObjectId>();
//                Set<ObjectId> classIds = new HashSet<ObjectId>();
//                Set<Integer> gradeTys = new HashSet<Integer>();
//                ObjectId userId = ue.getID();
//                urMap.put(userId, ue.getRole());
//                if (UserRole.isStudentOrParent(ue.getRole())) {
//                    if (UserRole.isParent(ue.getRole())) {
//                        userId = ue.getConnectId();
//                    }
//                    ClassEntry classEntry = classDao.getClassEntryByStuId(userId, Constant.FIELDS);
//                    if (classEntry != null) {
//                        SchoolEntry se = schoolDao.getSchoolEntry(ue.getSchoolID());
//                        gradeIds.add(classEntry.getGradeId());
//                        classIds.add(classEntry.getID());
//
//                        Map<ObjectId, Grade> gradeMap = new HashMap<ObjectId, Grade>();
//                        for (Grade g : se.getGradeList()) {
//                            gradeMap.put(g.getGradeId(), g);
//                        }
//                        Grade g = gradeMap.get(classEntry.getGradeId());
//                        if (null != g) {
//                            gradeTys.add(g.getGradeType());
//                        }
//                    }
//                } else if (UserRole.isNotStudentAndParent(ue.getRole())) {
//                    List<ClassEntry> classEntryList = classDao.getClassEntryByTeacherId(userId, Constant.FIELDS);
//                    SchoolEntry se = schoolDao.getSchoolEntry(ue.getSchoolID());
//                    for (ClassEntry dto : classEntryList) {
//                        gradeIds.add(dto.getGradeId());
//                        classIds.add(dto.getID());
//
//                        Map<ObjectId, Grade> gradeMap = new HashMap<ObjectId, Grade>();
//                        if (se != null && se.getGradeList() != null) {
//                            for (Grade g : se.getGradeList()) {
//                                gradeMap.put(g.getGradeId(), g);
//                            }
//                        }
//
//                        Grade g = null;
//                        for (ClassEntry ce : classEntryList) {
//                            g = gradeMap.get(ce.getGradeId());
//                            if (null != g) {
//                                gradeTys.add(g.getGradeType());
//                            }
//                        }
//
//                    }
//                }
//                grMap.put(ue.getID(), gradeIds);
//                gtMap.put(ue.getID(), gradeTys);
//                clMap.put(ue.getID(), classIds);
//            }
//            System.out.println("************************用户登录日志第"+page+"页修改开始" + DateTimeUtils.getCurrDate(DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H) + "*******************************");
//            for (LogEntry entry : list) {
//                    entry.setSchoolId(scMap.get(entry.getUserId()));
//                    entry.setGradeIds(grMap.get(entry.getUserId()));
//                    entry.setGradeTys(gtMap.get(entry.getUserId()));
//                    entry.setClassIds(clMap.get(entry.getUserId()));
//                    int userRole = urMap.get(entry.getUserId()) == null ? 0 : urMap.get(entry.getUserId());
//                    if (userRole > UserRole.PARENT.getRole()) {
//                        userRole = UserRole.TEACHER.getRole();
//                    }
//                    entry.setUserRole(userRole);
//                    dao.editLog(entry.getID(), entry);
//            }
//            System.out.println("************************用户登录日志第"+page+"页修改结束" + DateTimeUtils.getCurrDate(DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H) + "*******************************");
//        }
//        System.out.println("#########用户登录日志修改结束" + DateTimeUtils.getCurrDate(DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H) + "##############");
    }
}
