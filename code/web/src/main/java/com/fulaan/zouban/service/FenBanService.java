package com.fulaan.zouban.service;

import com.db.zouban.SchoolSubjectGroupDao;
import com.db.zouban.StudentXuankeDao;
import com.db.zouban.SubjectConfDao;
import com.db.zouban.XuanKeConfDao;
import com.fulaan.examresult.controller.IdNameDTO;
import com.pojo.zouban.*;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by wangkaidong on 2016/8/16.
 */
public class FenBanService {

    private SchoolSubjectGroupDao schoolSubjectGroupDao = new SchoolSubjectGroupDao();
    private StudentXuankeDao studentXuankeDao = new StudentXuankeDao();
    private XuanKeConfDao xuanKeConfDao = new XuanKeConfDao();
    private SubjectConfDao subjectConfDao = new SubjectConfDao();

    /**
     * 组合-学生信息
     * */
    class SubjectGroupStu {
        private String groupName;
        private List<ObjectId> advSubjectList = new ArrayList<ObjectId>();
        private List<ObjectId> simSubjectList = new ArrayList<ObjectId>();
        private List<IdNamePair> studentList = new ArrayList<IdNamePair>();

        public SubjectGroupStu() {

        }

        public SubjectGroupStu(String groupName, List<ObjectId> advSubjectList, List<ObjectId> simSubjectList, List<IdNamePair> studentList) {
            this.groupName = groupName;
            this.advSubjectList.addAll(advSubjectList);
            this.simSubjectList.addAll(simSubjectList);
            this.studentList.addAll(studentList);
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public List<ObjectId> getAdvSubjectList() {
            return advSubjectList;
        }

        public void setAdvSubjectList(List<ObjectId> advSubjectList) {
            this.advSubjectList = advSubjectList;
        }

        public List<ObjectId> getSimSubjectList() {
            return simSubjectList;
        }

        public void setSimSubjectList(List<ObjectId> simSubjectList) {
            this.simSubjectList = simSubjectList;
        }

        public List<IdNamePair> getStudentList() {
            return studentList;
        }

        public void setStudentList(List<IdNamePair> studentList) {
            this.studentList = studentList;
        }
    }


    /**
     * 按组合分班(虚拟班)
     */
    class VirtualClass {
        private String className; //虚拟班名称
        private String groupName; //虚拟班组合名称（形如：物化生+物化历...）
        private List<SubjectGroupStu> subjectGroupStuList = new ArrayList<SubjectGroupStu>(); //组合及学生
        private int groupCount; //虚拟班总人数


        public VirtualClass(String className, String groupName, int groupCount, List<SubjectGroupStu> subjectGroupStuList) {
            this.className = className;
            this.groupName = groupName;
            this.groupCount = groupCount;
            this.subjectGroupStuList = subjectGroupStuList;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public int getGroupCount() {
            return groupCount;
        }

        public void setGroupCount(int groupCount) {
            this.groupCount = groupCount;
        }

        public List<SubjectGroupStu> getSubjectGroupStuList() {
            return subjectGroupStuList;
        }

        public void setSubjectGroupStuList(List<SubjectGroupStu> subjectGroupStuList) {
            this.subjectGroupStuList = subjectGroupStuList;
        }
    }


    public static void main(String[] args) {
        FenBanService fenBanService = new FenBanService();

        String schoolId = "56e68e7e0cf2a5c70a1cc136";
        String term = "2015-2016学年";
        String gradeId = "57beb51571f0566f48906bc9";

        fenBanService.fenban(schoolId, term, gradeId, 42, 12);
    }

    public List<VirtualClass> fenban(String schoolId, String term, String gradeId, int max, int count) {
        XuankeConfEntry xuankeConfEntry = xuanKeConfDao.findXuanKeConf(term, new ObjectId(gradeId));

        //学科配置
        List<SubjectConfEntry> subjectConfEntryList = subjectConfDao.findSubjectConf(xuankeConfEntry.getID(), ZoubanType.ZOUBAN.getType());
        Collections.sort(subjectConfEntryList, new Comparator<SubjectConfEntry>() {
            @Override
            public int compare(SubjectConfEntry o1, SubjectConfEntry o2) {
                return o2.getAdvUsers().size() - o1.getAdvUsers().size();
            }
        });
        List<ObjectId> subjectIdList = new ArrayList<ObjectId>();
        for (SubjectConfEntry subjectConfEntry : subjectConfEntryList) {
            subjectIdList.add(subjectConfEntry.getSubjectId());
        }

        //学生选课组合
        List<SubjectGroupStu> subjectGroupStus = getGroupStudentList(schoolId, term, gradeId, xuankeConfEntry.getID());

        int groupIndex = 1;
        for (SubjectGroupStu subjectGroupStu : subjectGroupStus) {
            System.out.println(groupIndex++ +  ":" + subjectGroupStu.getGroupName() + ":" + subjectGroupStu.getStudentList().size());
        }

        System.out.println("----------------------------------------------------------------------");

        int min = max - 15;
        for (int i = 0; i < 1000; i++) {
            List<VirtualClass> classList = new ArrayList<VirtualClass>();
            classList.addAll(arrange(subjectGroupStus, subjectIdList, max, min));

            if (classList.size() == count) {
                int all = 0;
                for (VirtualClass virtualClass : classList) {
                    all += virtualClass.getGroupCount();
                    System.out.println(virtualClass.getClassName() + ":" + virtualClass.getGroupCount());
                }

                System.out.println("\n已运算" + (i + 1) +"次\n已分配人数：" + all + "，班级数：" + count);
                return classList;
            }
        }
        return null;
    }

    /**
     * 获取选课组合并按照人数多少排序
     *
     * @param schoolId
     * @param term
     * @param gradeId
     * @param xuanKeId
     * @return
     */
    private List<SubjectGroupStu> getGroupStudentList(String schoolId, String term, String gradeId, ObjectId xuanKeId) {
        SchoolSubjectGroupEntry schoolSubjectGroupEntry = schoolSubjectGroupDao.getEntry(new ObjectId(schoolId), term, new ObjectId(gradeId));
        List<SchoolSubjectGroupEntry.SubjectGroup> subjectGroups = schoolSubjectGroupEntry.getSubjectGroups();

        List<SubjectGroupStu> subjectGroupStuList = new ArrayList<SubjectGroupStu>();
        for (SchoolSubjectGroupEntry.SubjectGroup subjectGroup : subjectGroups) {
            if (subjectGroup.getIsPublic()) {
                SubjectGroupStu subjectGroupStu = new SubjectGroupStu();
                subjectGroupStu.setAdvSubjectList(subjectGroup.getAdvSubjects());
                subjectGroupStu.setSimSubjectList(subjectGroup.getSimSubjects());
                subjectGroupStu.setGroupName(subjectGroup.getName());

                List<StudentChooseEntry> studentChooseEntries = studentXuankeDao.getStudentBySubjectGroup(subjectGroup.getAdvSubjects(), xuanKeId);
                List<IdNamePair> studentList = new ArrayList<IdNamePair>();
                for (StudentChooseEntry studentChooseEntry : studentChooseEntries) {
                    studentList.add(new IdNamePair(studentChooseEntry.getUserId(), studentChooseEntry.getUserName()));
                }

                subjectGroupStu.setStudentList(studentList);

                if (studentList.size() > 0) {
                    subjectGroupStuList.add(subjectGroupStu);
                }
            }
        }

        return subjectGroupStuList;
    }

    /**
     * 分班
     *
     * @param subjectGroupStus
     * @param subjectIdList
     * @param max
     * @param min
     * @return
     */
    private List<VirtualClass> arrange(final List<SubjectGroupStu> subjectGroupStus, final List<ObjectId> subjectIdList, int max, int min) {
        //1.先把3门等级考一样且人数在min和max之间或是倍数关系的组合(人数 % max > min)分成单一组合的虚拟班；
        //2.再从剩下的组合中找两门等级考一样的组合凑成虚拟班（班级人数在min~max之间）

        List<VirtualClass> classList = new ArrayList<VirtualClass>();
        List<Integer> usedGroup = new ArrayList<Integer>();

        final String className = "组合"; //虚拟班名称前缀
        int classNameIndex = 1;

        for (int x = 0; x < subjectGroupStus.size(); x++) {
            if (!usedGroup.contains(x)) {
                SubjectGroupStu subjectGroupStu = subjectGroupStus.get(x);
                final String groupName = subjectGroupStu.getGroupName();
                final int stuCount = subjectGroupStu.getStudentList().size();
                List<IdNamePair> studentList = subjectGroupStu.getStudentList();

                if (stuCount % max > min) {//组合人数在min和max之间或是倍数关系的组合，形成单一组合虚拟班
                    for (int i = 0; i < stuCount / max + 1; i++) {
                        List<IdNamePair> stuList = new ArrayList<IdNamePair>();
                        if (i == stuCount / max) {
                            stuList.addAll(studentList.subList(i * max, studentList.size()));
                        } else {
                            stuList.addAll(studentList.subList(i * max, (i + 1) * max));
                        }

                        SubjectGroupStu subjectGroupStu1 = new SubjectGroupStu(groupName, subjectGroupStu.getAdvSubjectList(), subjectGroupStu.getSimSubjectList(), stuList);
                        List<SubjectGroupStu> subjectGroupStuList = new ArrayList<SubjectGroupStu>();
                        subjectGroupStuList.add(subjectGroupStu1);
                        classList.add(new VirtualClass(className + classNameIndex + "班", groupName, 1, subjectGroupStuList));
                        classNameIndex++;
                    }
                    continue;
                }

                final int count = stuCount / max + 1;
                /*for (int i = 0; i < count; i++) {
                    if (i < count - 1) {
                        String name = count == 2 ? className : (className + "-" + (i + 1));
                        classList.add(new VirtualClass(name, 1, subjectGroupStu.getStudentList().subList(i * max, (i + 1) * max), subjectGroupStu.getSubjectList()));
                    } else {
                        //班级名称
                        StringBuilder sb = new StringBuilder();
                        sb.append(className);
                        //班级学生
                        List<IdNameDTO> students = new ArrayList<IdNameDTO>(subjectGroupStu.getStudentList().subList(i * max, stuCount));
                        //班级学科
                        Set<ObjectId> subjectIds = new HashSet<ObjectId>();
                        //班级组合数
                        int groupCount = 1;

                        //根据选课结果每个学科人数多少对本组合学科排序
                        List<ObjectId> targetSubjects = new ArrayList<ObjectId>();
                        for (ObjectId subjectId : subjectIdList) {
                            if (subjectGroupStu.getSubjectList().contains(subjectId)) {
                                targetSubjects.add(subjectId);
                            }
                        }
                        //两两组合
                        List<List<ObjectId>> subjectCombo = new ArrayList<List<ObjectId>>();
                        List<Integer[]> combo = cmn(new Integer[]{0, 1, 2}, 2);
                        for (Integer[] c : combo) {
                            List<ObjectId> sList = new ArrayList<ObjectId>();
                            sList.add(targetSubjects.get(c[0]));
                            sList.add(targetSubjects.get(c[1]));
                            subjectCombo.add(sList);
                        }

                        int subjectIndex = 0;
                        while (students.size() < min && subjectIndex < subjectCombo.size()) {
                            for (int j = x + 1; j < subjectGroupStus.size(); j++) {
                                SubjectGroupStu group = subjectGroupStus.get(j);
                                if (!(group.getStudentList().size() < max && group.getStudentList().size() > min)) {//跳过可以组成一个班的组合
                                    List<ObjectId> groupSubjects = new ArrayList<ObjectId>(group.getSubjectList());
                                    groupSubjects.retainAll(subjectCombo.get(subjectIndex));

                                    if (groupSubjects.size() == 2 && (students.size() + group.getStudentList().size() <= max) && !usedGroup.contains(j)) {
                                        sb.append("+").append(group.getGroupName());
                                        students.addAll(group.getStudentList());
                                        subjectIds.addAll(group.getSubjectList());
                                        groupCount++;
                                        usedGroup.add(j);
                                    }
                                }
                            }
                            subjectIndex++;
                        }

                        classList.add(new VirtualClass(sb.toString(), groupCount, students, new ArrayList<ObjectId>(subjectIds)));
                    }
                }*/
            }
        }

        //按照组合数从少到多排序
        Collections.sort(classList, new Comparator<VirtualClass>() {
            @Override
            public int compare(VirtualClass o1, VirtualClass o2) {
                return o1.getGroupCount() - o2.getGroupCount();
            }
        });

        return classList;
    }

    /**
     * 三选二
     * @param source
     * @param n
     * @return
     */
    private List<Integer[]> cmn(Integer[] source, int n) {
        List<Integer[]> result = new ArrayList<Integer[]>();
        if (n == 1) {
            for (int i = 0; i < source.length; i++) {
                result.add(new Integer[]{source[i]});

            }
        } else if (source.length == n) {
            result.add(source);
        } else {
            Integer[] psource = new Integer[source.length - 1];
            for (int i = 0; i < psource.length; i++) {
                psource[i] = source[i];
            }
            result = cmn(psource, n);
            List<Integer[]> tmp = cmn(psource, n - 1);
            for (int i = 0; i < tmp.size(); i++) {
                Integer[] rs = new Integer[n];
                for (int j = 0; j < n - 1; j++) {
                    rs[j] = tmp.get(i)[j];
                }
                rs[n - 1] = source[source.length - 1];
                result.add(rs);
            }
        }
        return result;
    }

}

