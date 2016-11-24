package com.fulaan.zouban.service;

import com.db.zouban.*;
import com.fulaan.zouban.dto.SubjectConfDTO;
import com.fulaan.zouban.dto.XuanKeDTO;
import com.pojo.app.IdValuePair;
import com.pojo.zouban.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by wangkaidong on 2016/6/1.
 * <p/>
 * 走班课分班Service
 */
@Service
public class CZFenbanService {
    private XuanKeConfDao xuanKeConfDao = new XuanKeConfDao();
    private ZouBanCourseDao zouBanCourseDao = new ZouBanCourseDao();
    private FenDuanDao fenDuanDao = new FenDuanDao();
    private StudentXuankeDao studentXuankeDao = new StudentXuankeDao();
    private ScoreDao scoreDao = new ScoreDao();
    @Autowired
    private XuanKeService xuanKeService;
    @Autowired
    private BianbanService bianbanService;


    /**
     * 第一步：把6门课分成三组，每组每学科一个班，总共分成18个班
     * 第二步：遍历学生，学生有三门课，共6种组合，随机取一种放到班中
     */
    public List<ZouBanCourseEntry> fenban(String year, String schoolId, String gradeId) {
        //第一步
        List<List<ZouBanCourseEntry>> group = arrangeClass(year, schoolId, gradeId);

        //查询学生选课信息
        XuankeConfEntry xuankeEntry = xuanKeConfDao.findXuanKeConf(year, new ObjectId(gradeId));
        List<StudentChooseEntry> studentChooseEntryList = studentXuankeDao.findStuXuanKeListByGradeId(xuankeEntry.getID());

        //第二步
        for (StudentChooseEntry studentChooseEntry : studentChooseEntryList) {
            List<ObjectId> advSubjectList = studentChooseEntry.getAdvancelist();
            int x = (int) (Math.random() * 6);
            Integer[] arr = permutate().get(x);
            for (int i = 0; i < 3; i++) {
                for (ZouBanCourseEntry zoubanCourseEntry : group.get(arr[i])) {
                    if (advSubjectList.get(i).toString().equals(zoubanCourseEntry.getSubjectId().toString())) {
                        List<ObjectId> studentList = zoubanCourseEntry.getStudentList();
                        studentList.add(studentChooseEntry.getUserId());
                        zoubanCourseEntry.setStudentList(studentList);
                        break;
                    }
                }
            }
        }

        List<ZouBanCourseEntry> result = new ArrayList<ZouBanCourseEntry>();
        for (List<ZouBanCourseEntry> list : group) {
            result.addAll(list);
        }

        return result;
    }

    //学生3门课6种组合
    private List<Integer[]> permutate() {
        List<Integer[]> arrList = new ArrayList<Integer[]>();
        Integer[] a1 = {0, 1, 2};
        Integer[] a2 = {0, 2, 1};
        Integer[] a3 = {1, 0, 2};
        Integer[] a4 = {1, 2, 0};
        Integer[] a5 = {2, 0, 1};
        Integer[] a6 = {2, 1, 0};
        arrList.add(a1);
        arrList.add(a2);
        arrList.add(a3);
        arrList.add(a4);
        arrList.add(a5);
        arrList.add(a6);
        return arrList;
    }

    /**
     * 把6个学科编成18个班
     */
    private List<List<ZouBanCourseEntry>> arrangeClass(String year, String schoolId, String gradeId) {
        //走班课学科信息
        XuanKeDTO xuanKeDTO = xuanKeService.findXuanKeConf(year, gradeId, 1, schoolId);
        List<SubjectConfDTO> subjectConfDTOList = xuanKeDTO.getSubConfList();

        List<ZouBanCourseEntry> group1 = new ArrayList<ZouBanCourseEntry>();
        List<ZouBanCourseEntry> group2 = new ArrayList<ZouBanCourseEntry>();
        List<ZouBanCourseEntry> group3 = new ArrayList<ZouBanCourseEntry>();
        ObjectId group1Id = new ObjectId();
        ObjectId group2Id = new ObjectId();
        ObjectId group3Id = new ObjectId();

        for (SubjectConfDTO subjectConfDTO : subjectConfDTOList) {
            ZouBanCourseEntry zouBanCourseEntry1 = new ZouBanCourseEntry(new ObjectId(schoolId), new ObjectId(subjectConfDTO.getSubjectId()), year,
                    new ObjectId(gradeId), null, null, subjectConfDTO.getSubjectName() + "1", subjectConfDTO.getAdvanceTime(), null, group1Id, 1);
            ZouBanCourseEntry zouBanCourseEntry2 = new ZouBanCourseEntry(new ObjectId(schoolId), new ObjectId(subjectConfDTO.getSubjectId()), year,
                    new ObjectId(gradeId), null, null, subjectConfDTO.getSubjectName() + "2", subjectConfDTO.getAdvanceTime(), null, group2Id, 1);
            ZouBanCourseEntry zouBanCourseEntry3 = new ZouBanCourseEntry(new ObjectId(schoolId), new ObjectId(subjectConfDTO.getSubjectId()), year,
                    new ObjectId(gradeId), null, null, subjectConfDTO.getSubjectName() + "3", subjectConfDTO.getAdvanceTime(), null, group3Id, 1);
            group1.add(zouBanCourseEntry1);
            group2.add(zouBanCourseEntry2);
            group3.add(zouBanCourseEntry3);
        }
        List<List<ZouBanCourseEntry>> group = new ArrayList<List<ZouBanCourseEntry>>();
        group.add(group1);
        group.add(group2);
        group.add(group3);

        return group;
    }


    //--------------------------------------------------按组合分班-----------------------------------------------------------


    /**
     * 第一步：查询每一种组合的学生
     * 第二步：遍历组合，给每种组合分配逻辑位置{1：检测三门课是否已经存在逻辑位置 2：如果存在，检查人数是否超过上限 3：超过人数上限和不存在逻辑位置的课新建位置}
     * 第三步：对逻辑位置分班
     */
    public List<ZouBanCourseEntry> fenban2(String year, String schoolId, String gradeId, ObjectId groupId,
                                           int advMax, int advMin, int simMax, int simMin, int classroomCount) {
        //走班课学科信息
        XuanKeDTO xuanKeDTO = xuanKeService.findXuanKeConf(year, gradeId, 1, schoolId);
        List<SubjectConfDTO> subjectConfDTOList = xuanKeDTO.getSubConfList();

        Map<String, SubjectConfDTO> subjectConfDTOMap = new HashMap<String, SubjectConfDTO>();
        for (SubjectConfDTO confDTO : subjectConfDTOList) {
            subjectConfDTOMap.put(confDTO.getSubjectId(), confDTO);
        }

        ObjectId xuankeId = new ObjectId(xuanKeDTO.getXuankeId());

        //获取分段
        List<ClassFengDuanEntry> fengDuanEntryList = new ArrayList<ClassFengDuanEntry>();
        if (groupId == null) {//全部段
            fengDuanEntryList.addAll(fenDuanDao.getClassFenduanList(xuankeId));
        } else {//某一段
            fengDuanEntryList.add(fenDuanDao.findFenDuanById(groupId));
        }

        List<ZouBanCourseEntry> result = new ArrayList<ZouBanCourseEntry>();
        for (ClassFengDuanEntry fengDuanEntry : fengDuanEntryList) {
            //查询学生选课信息
            List<StudentChooseEntry> studentChooseEntryList = studentXuankeDao.findStuXuanKeListByClassIds(xuankeId, fengDuanEntry.getClassIds());

            //type 1 : 等级考 2 : 合格考

            //等级考分班
            List<ZouBanCourseEntry> advCourseList = buildCourse(year, schoolId, gradeId, xuanKeDTO,
                    fengDuanEntry, studentChooseEntryList, subjectConfDTOMap, advMax, advMin, classroomCount, 1);
            if (advCourseList.size() == 0) {
                result.clear();
                //清空已经生成的走班课
                zouBanCourseDao.removeCourseByType(year, new ObjectId(gradeId), ZoubanType.ZOUBAN.getType());
                break;
            } else {
                boolean flag = false;
                //判断合格考是否全部为0课时
                for (SubjectConfDTO subjectConfDTO : subjectConfDTOMap.values()) {
                    if (subjectConfDTO.getSimpleTime() > 0) {
                        flag = true;
                    }
                }

                if (flag) {
                    //合格考分班
                    List<ZouBanCourseEntry> simCourseList = buildCourse(year, schoolId, gradeId, xuanKeDTO,
                            fengDuanEntry, studentChooseEntryList, subjectConfDTOMap, simMax, simMin, classroomCount, 2);
                    if (simCourseList.size() == 0) {
                        result.clear();
                        //清空已经生成的走班课
                        zouBanCourseDao.removeCourseByType(year, new ObjectId(gradeId), ZoubanType.ZOUBAN.getType());
                        break;
                    } else {
                        result.addAll(advCourseList);
                        result.addAll(simCourseList);
                    }
                } else {
                    result.addAll(advCourseList);
                }
            }
        }

        return result;
    }

    /**
     * 生成班级
     */
    private List<ZouBanCourseEntry> buildCourse(String year, String schoolId, String gradeId, XuanKeDTO xuanKeDTO,
                                                ClassFengDuanEntry fengDuanEntry, List<StudentChooseEntry> studentChooseEntryList,
                                                Map<String, SubjectConfDTO> subjectConfDTOMap, int max, int min, int classroomCount, int type) {
        //分班结果
        List<ZouBanCourseEntry> result = new ArrayList<ZouBanCourseEntry>();
        //学生选课结果组合
        Map<String, List<ObjectId>> studentChooseGroupList = getStudentChooseList(studentChooseEntryList, type, subjectConfDTOMap);
        //3个逻辑位置
        Map<Integer, Map<String, List<ObjectId>>> position = findResult(studentChooseEntryList, studentChooseGroupList, max, min, classroomCount, type, xuanKeDTO);

        if (position != null) {
            //将分配结果生成课
            List<ZouBanCourseEntry> group = new ArrayList<ZouBanCourseEntry>();
            ObjectId group1Id = new ObjectId();
            ObjectId group2Id = new ObjectId();
            ObjectId group3Id = new ObjectId();

            for (int i = 0; i < 3; i++) {
                for (Map.Entry<String, List<ObjectId>> entry : position.get(i).entrySet()) {
                    String subjectId = entry.getKey();
                    List<ObjectId> studentList = entry.getValue();

                    SubjectConfDTO subjectConfDTO = subjectConfDTOMap.get(subjectId);

                    ObjectId groupId = null;
                    switch (i) {
                        case 0:
                            groupId = group1Id;
                            break;
                        case 1:
                            groupId = group2Id;
                            break;
                        case 2:
                            groupId = group3Id;
                            break;
                        default:
                            break;
                    }


                    if (type == 1) {
                        //学生成绩
                        ScoreEntry scoreEntry = scoreDao.findScore(year, new ObjectId(gradeId), new ObjectId(subjectId));
                        if (scoreEntry != null && scoreEntry.getScoreList().size() > 0) {
                            List<IdValuePair> scoreList = scoreEntry.getScoreList();
                            Map<ObjectId, Integer> allScoreMap = new HashMap<ObjectId, Integer>();
                            for (IdValuePair ivp : scoreList) {
                                allScoreMap.put(ivp.getId(), (Integer) ivp.getValue());
                            }
                            Map<ObjectId, Integer> scoreMap = new HashMap<ObjectId, Integer>();
                            for (ObjectId stuId : studentList) {
                                scoreMap.put(stuId, allScoreMap.get(stuId));
                            }
                            if (scoreMap.size() > 0) {
                                //按照成绩排序
                                List<Map.Entry> mapList = new ArrayList<Map.Entry>(scoreMap.entrySet());
                                Collections.sort(mapList, new Comparator<Map.Entry>() {
                                    @Override
                                    public int compare(Map.Entry o1, Map.Entry o2) {
                                        int score1 = (Integer) o1.getValue();
                                        int score2 = (Integer) o2.getValue();
                                        return score1 - score2;
                                    }
                                });
                                List<ObjectId> sortedStuList = new ArrayList<ObjectId>();
                                for (Map.Entry map : mapList) {
                                    sortedStuList.add((ObjectId) map.getKey());
                                }
                            }
                        }


                        if (studentList.size() > max) {
                            List<ObjectId> stuList1 = studentList.subList(0, studentList.size() / 2);
                            List<ObjectId> stuList2 = studentList.subList(studentList.size() / 2, studentList.size());

                            ZouBanCourseEntry zouBanCourseEntry1 = new ZouBanCourseEntry(new ObjectId(schoolId), new ObjectId(subjectId), year,
                                    new ObjectId(gradeId), fengDuanEntry.getID(), subjectConfDTO.getSubjectName() + "等级" + fengDuanEntry.getGroup() + "-" + (i + 1) + "A", subjectConfDTO.getAdvanceTime(), stuList1, groupId, type);
                            ZouBanCourseEntry zouBanCourseEntry2 = new ZouBanCourseEntry(new ObjectId(schoolId), new ObjectId(subjectId), year,
                                    new ObjectId(gradeId), fengDuanEntry.getID(), subjectConfDTO.getSubjectName() + "等级" + fengDuanEntry.getGroup() + "-" + (i + 1) + "B", subjectConfDTO.getAdvanceTime(), stuList2, groupId, type);
                            group.add(zouBanCourseEntry1);
                            group.add(zouBanCourseEntry2);

                        } else {
                            ZouBanCourseEntry zouBanCourseEntry = new ZouBanCourseEntry(new ObjectId(schoolId), new ObjectId(subjectId), year,
                                    new ObjectId(gradeId), fengDuanEntry.getID(), subjectConfDTO.getSubjectName() + "等级" + fengDuanEntry.getGroup() + "-" + (i + 1), subjectConfDTO.getAdvanceTime(), studentList, groupId, type);
                            group.add(zouBanCourseEntry);
                        }
                    } else {
                        if (studentList.size() > max) {
                            List<ObjectId> stuList1 = studentList.subList(0, studentList.size() / 2);
                            List<ObjectId> stuList2 = studentList.subList(studentList.size() / 2, studentList.size());

                            ZouBanCourseEntry zouBanCourseEntry1 = new ZouBanCourseEntry(new ObjectId(schoolId), new ObjectId(subjectId), year,
                                    new ObjectId(gradeId), fengDuanEntry.getID(), subjectConfDTO.getSubjectName() + "合格" + fengDuanEntry.getGroup() + "-" + (i + 1) + "A", subjectConfDTO.getSimpleTime(), stuList1, groupId, type);
                            ZouBanCourseEntry zouBanCourseEntry2 = new ZouBanCourseEntry(new ObjectId(schoolId), new ObjectId(subjectId), year,
                                    new ObjectId(gradeId), fengDuanEntry.getID(), subjectConfDTO.getSubjectName() + "合格" + fengDuanEntry.getGroup() + "-" + (i + 1) + "B", subjectConfDTO.getSimpleTime(), stuList2, groupId, type);
                            group.add(zouBanCourseEntry1);
                            group.add(zouBanCourseEntry2);

                        } else {
                            ZouBanCourseEntry zouBanCourseEntry = new ZouBanCourseEntry(new ObjectId(schoolId), new ObjectId(subjectId), year,
                                    new ObjectId(gradeId), fengDuanEntry.getID(), subjectConfDTO.getSubjectName() + "合格" + fengDuanEntry.getGroup() + "-" + (i + 1), subjectConfDTO.getSimpleTime(), studentList, groupId, type);
                            group.add(zouBanCourseEntry);
                        }
                    }
                }
            }

            for (ZouBanCourseEntry entry : group) {
                zouBanCourseDao.addZouBanCourseEntry(entry);
                bianbanService.updateZBCourseClassId(entry.getID());
            }

            result.addAll(group);
        }


        return result;
    }

    /**
     * 找出分班可行解
     */
    private Map<Integer, Map<String, List<ObjectId>>> findResult(List<StudentChooseEntry> studentChooseEntryList,
                                                                 Map<String, List<ObjectId>> studentChooseGroupList,
                                                                 int max, int min, int classroomCount, int type, XuanKeDTO xuanKeDTO) {
        //选课结果中每个学科等级考/合格考总人数,用于检查学生是否分配完
        Map<ObjectId, Integer> studentChooseSubCount = new HashMap<ObjectId, Integer>();
        for (StudentChooseEntry studentChooseEntry : studentChooseEntryList) {
            List<ObjectId> stuIdList = new ArrayList<ObjectId>();
            if (type == 1) {
                stuIdList.addAll(studentChooseEntry.getAdvancelist());
            } else {
                stuIdList.addAll(studentChooseEntry.getSimplelist());
            }

            for (ObjectId subjectId : stuIdList) {
                int count = 1;
                if (studentChooseSubCount.containsKey(subjectId)) {
                    count = studentChooseSubCount.get(subjectId) + 1;
                }
                studentChooseSubCount.put(subjectId, count);
            }
        }

        Map<Integer, Map<String, Integer>> stuNumMap = new HashMap<Integer, Map<String, Integer>>();
        Map<String, Integer> dengji = new HashMap<String, Integer>();
        Map<String, Integer> hege = new HashMap<String, Integer>();
        List<SubjectConfDTO> subConfList = xuanKeDTO.getSubConfList();
        for (SubjectConfDTO subjectConfDTO : subConfList) {
            dengji.put(subjectConfDTO.getSubjectId(), subjectConfDTO.getAdvUserCount());
            hege.put(subjectConfDTO.getSubjectId(), subjectConfDTO.getSimUserCount());
        }
        stuNumMap.put(1, dengji);
        stuNumMap.put(2, hege);

        //分班结果
        Map<Integer, Map<String, List<ObjectId>>> position = null;
        boolean end = false;
        int x = 0;
        while (!end) {
            position = arrange(studentChooseGroupList, max, min, type, stuNumMap.get(type));

            boolean flag = true;

            //每个逻辑位置班级数上限
            int maxClassCount = 0;

            //检查是否排完所有组合(检查分班后每个学科的总人数是否与选课结果中每个学科的人数一致)
            //分班后每个学科总人数
            Map<ObjectId, Integer> subjectStuCount = new HashMap<ObjectId, Integer>();

            for (Map.Entry<Integer, Map<String, List<ObjectId>>> result : position.entrySet()) {
                Map<String, List<ObjectId>> subStudent = result.getValue();
                int classCount = 0;//班级数

                for (Map.Entry<String, List<ObjectId>> sub : subStudent.entrySet()) {
                    ObjectId subjectId = new ObjectId(sub.getKey());
                    List<ObjectId> stuList = sub.getValue();
                    int count = stuList.size();
                    if (stuList.size() < min || (stuList.size() > max && stuList.size() / 2 < min)) {// 剔除班级人数少于最少人数的结果
                        flag = false;
                        System.out.print("type=" + type + "：剔除班级人数少于最少人数的结果");
                        break;
                    } else {
                        if (subjectStuCount.containsKey(subjectId)) {
                            count += subjectStuCount.get(subjectId);
                        }
                        subjectStuCount.put(subjectId, count);

                        if (stuList.size() > max) {
                            classCount += 2;
                        } else {
                            classCount += 1;
                        }
                    }
                }
                if (classCount > maxClassCount) {
                    maxClassCount = classCount;
                }
            }

            for (Map.Entry<ObjectId, Integer> entry : subjectStuCount.entrySet()) {
                ObjectId subjectId = entry.getKey();
                int count = entry.getValue();
                if (count != studentChooseSubCount.get(subjectId)) {
                    flag = false;
                    System.out.print("type=" + type + "：剔除不能分配完所有组合的结果" + count + "/" + studentChooseSubCount.get(subjectId) + "    ");
                    break;
                }
            }

            if (maxClassCount > classroomCount) {//班级数不能超过教室数
                flag = false;
                System.out.print("    班级数超过教室数   ");
            }

            end = flag;
            System.out.println(x++);
            if (x > 100000) {//超过100000次排课失败
                return null;
            }
        }

        return position;
    }


    /**
     * 按组合分班
     */
    private Map<Integer, Map<String, List<ObjectId>>> arrange(Map<String, List<ObjectId>> studentChooseGroupList,
                                                              int max, int min, int type, Map<String, Integer> stuNumMap) {
        //3个逻辑位置
        Map<Integer, Map<String, List<ObjectId>>> position = new HashMap<Integer, Map<String, List<ObjectId>>>();
        position.put(0, new HashMap<String, List<ObjectId>>());
        position.put(1, new HashMap<String, List<ObjectId>>());
        position.put(2, new HashMap<String, List<ObjectId>>());


        //第一步：学生选课组合
        Map<String, List<ObjectId>> subjectCombo = combo(studentChooseGroupList);
        //第二步：遍历组合
        for (Map.Entry<String, List<ObjectId>> entry : subjectCombo.entrySet()) {
            //获得学科
            String subStr = entry.getKey();
            String[] subjectIds = subStr.split(",");
            List<String> subIds = Arrays.asList(subjectIds);
            List<ObjectId> stus = entry.getValue();

            Collections.shuffle(subIds);

            Map<String, Integer> subjectPosition = getSubjectPosition(subIds, position, max, min, stus.size(), stuNumMap);


            //将分配结果加入逻辑位置
            for (Map.Entry sp : subjectPosition.entrySet()) {
                String subject = (String) sp.getKey();
                int p = (Integer) sp.getValue();

                Map<String, List<ObjectId>> subjectMap = position.get(p);
                List<ObjectId> stuList = new ArrayList<ObjectId>();
                if (subjectMap.containsKey(subject)) {
                    stuList.addAll(subjectMap.get(subject));
                }
                stuList.addAll(stus);
                subjectMap.put(subject, stuList);
                position.put(p, subjectMap);
            }
        }


        return position;
    }

    /**
     * 计算组合排放位置
     *
     * @param subjectIds 组合的三个学科
     * @param position   已排好的位置信息
     * @param max        班级最大学生数
     * @param stuCount   组合学生数
     * @return
     */
    private Map<String, Integer> getSubjectPosition(List<String> subjectIds, Map<Integer, Map<String, List<ObjectId>>> position,
                                                    int max, int min, int stuCount, Map<String, Integer> stuNumMap) {
        Map<String, Integer> subjectPosition = new HashMap<String, Integer>();

        Map<Integer, Boolean> posAva = new HashMap<Integer, Boolean>();
        posAva.put(0, true);
        posAva.put(1, true);
        posAva.put(2, true);

        //遍历学科，查找学科逻辑位置
        for (String subjectId : subjectIds) {
            int maxNum = stuNumMap.get(subjectId) - min;
            if (stuNumMap.get(subjectId) <= max)
                maxNum = 10000;
            Boolean isOldPlaceAvailable = false;
            int minStuNo = 10000;
            int bestPosition = -1;//优先放在已有位置，多个已有位置优先放在人数较少位置
            //遍历3个逻辑位置，查找可用位置
            for (int i = 0; i < 3; i++) {
                if (!posAva.get(i))
                    continue;
                Map<String, List<ObjectId>> ePosition = position.get(i);

                //判断该位置是否可用
                if (ePosition.containsKey(subjectId)) {
                    int teacherCount = 2;//老师数
                    if (teacherCount * max < maxNum)
                        maxNum = teacherCount * max;
                    if (ePosition.get(subjectId).size() + stuCount <= maxNum) {//判断人数是否超过上限
                        if (ePosition.get(subjectId).size() < minStuNo) {
                            minStuNo = ePosition.get(subjectId).size();
                            bestPosition = i;
                        }

                        isOldPlaceAvailable = true;
                    }
                }
            }
            if (isOldPlaceAvailable) {
                subjectPosition.put(subjectId, bestPosition);
                posAva.put(bestPosition, false);
            } else {
                for (int i = 0; i < 3; i++) {
                    if (!posAva.get(i) || position.get(i).containsKey(subjectId))
                        continue;
                    Map<String, List<ObjectId>> ePosition = position.get(i);

                    //判断该位置是否可用
                    if (ePosition.size() < 4) {
                        subjectPosition.put(subjectId, i);
                        posAva.put(i, false);
                        break;
                    }
                }
            }
        }

        return subjectPosition;
    }

    /**
     * 获取学生选课结果组合
     *
     * @param studentChooseEntryList
     * @param type
     * @return
     */
    private Map<String, List<ObjectId>> getStudentChooseList(List<StudentChooseEntry> studentChooseEntryList, int type, Map<String, SubjectConfDTO> subjectConfDTOMap) {
        //学科-课时Map
        Map<ObjectId, Integer> subjectLessonCountMap = new HashMap<ObjectId, Integer>();
        for (Map.Entry<String, SubjectConfDTO> entry : subjectConfDTOMap.entrySet()) {
            String subjectId = entry.getKey();
            SubjectConfDTO subjectConfDTO = entry.getValue();

            if (type == 1) {
                subjectLessonCountMap.put(new ObjectId(subjectId), subjectConfDTO.getAdvanceTime());
            } else {
                subjectLessonCountMap.put(new ObjectId(subjectId), subjectConfDTO.getSimpleTime());
            }
        }

        //选课组合
        Map<String, List<ObjectId>> combo = new HashMap<String, List<ObjectId>>();
        for (StudentChooseEntry entry : studentChooseEntryList) {
            String subStr = "";
            List<ObjectId> subjectIdList = new ArrayList<ObjectId>();
            if (type == 1) {
                subjectIdList.addAll(entry.getAdvancelist());
            } else {
                subjectIdList.addAll(entry.getSimplelist());
            }

            for (ObjectId subjectId : subjectIdList) {
                if (subjectLessonCountMap.containsKey(subjectId) && subjectLessonCountMap.get(subjectId) > 0) {
                    subStr += subjectId.toString() + ",";
                }
            }

            if (combo.containsKey(subStr)) {
                combo.get(subStr).add(entry.getUserId());
            } else {
                List<ObjectId> studentIdList = new ArrayList<ObjectId>();
                studentIdList.add(entry.getUserId());
                combo.put(subStr, studentIdList);
            }
        }
        return combo;
    }


    /**
     * 遍历学生选课信息，计算组合
     */
    @SuppressWarnings("unchecked")
    private Map<String, List<ObjectId>> combo(Map<String, List<ObjectId>> combo) {
        //打乱重排(随机)
        Map<String, List<ObjectId>> shuffleCombo = new LinkedHashMap<String, List<ObjectId>>();
        List<Map.Entry> mapList = new ArrayList<Map.Entry>(combo.entrySet());
        Collections.shuffle(mapList);

        for (Map.Entry entry : mapList) {
            shuffleCombo.put((String) entry.getKey(), (ArrayList<ObjectId>) entry.getValue());
        }

        return shuffleCombo;
    }

}