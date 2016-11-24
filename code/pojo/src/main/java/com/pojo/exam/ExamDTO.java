package com.pojo.exam;

import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by Caocui on 2015/7/22.
 * 考试信息视图对象
 */
public class ExamDTO {
    public static final String[] examTypeNames = new String[]{"期中", "期末", "其他", "区域联考"};
    private String id;
    private String name;
    private String date;
    private String gradeId;
    private String gradeName;
    private int examType;
    private String examTypeName;
    private String remark;
    private boolean open;
    private int isArrangeComplate;
    private int isLock;
    private String schoolId;
    private String schoolYear;
    private Map<String, Map<String, Object>> roomUsed;
    private int isGra;
    private List<String> classList = new ArrayList<String>();
    private List<ExamSubjectDTO> examSubjectDTO = new ArrayList<ExamSubjectDTO>();
    private int type;//1：普通考试     2:3+3考试

    public ExamDTO() {
    }

    public ExamDTO(ExamEntry entry) {
        this.id = entry.getID().toString();
        this.name = entry.getName();
        this.date = DateTimeUtils.convert(entry.getExamDate(), DateTimeUtils.DATE_YYYY_MM_DD);
        this.gradeId = entry.getGradeId().toString();
        this.gradeName = entry.getGradeName();
        this.examType = entry.getExamType();
        this.examTypeName = examTypeNames[entry.getExamType()];
        this.remark = entry.getRemark();
        this.isArrangeComplate = entry.isArrangeComplate();
        this.isLock = entry.isLock();
        this.schoolId = entry.getSchoolId().toString();
        this.isGra = entry.getIsGra();
        this.schoolYear = entry.getSchoolYear();
        for (ObjectId id : entry.getCList()) {
            classList.add(id.toString());
        }
        for (ExamSubjectEntry subject : entry.getExamSubject()) {
            examSubjectDTO.add(new ExamSubjectDTO(subject));
        }

        if (entry.getRoomUsed() != null) {
            roomUsed = entry.getRoomUsed();
        }
        this.type = entry.getType();
    }

    public Map<String, Map<String, Object>> getRoomUsed() {
        return roomUsed;
    }

    public void setRoomUsed(Map<String, Map<String, Object>> roomUsed) {
        this.roomUsed = roomUsed;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }

    public int getIsGra() {
        return isGra;
    }

    public void setIsGra(int isGra) {
        this.isGra = isGra;
    }

    public List<String> getClassList() {
        return classList;
    }

    public void setClassList(List<String> classList) {
        this.classList = classList;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getExamTypeName() {
        return examTypeName;
    }

    public void setExamTypeName(String examTypeName) {
        this.examTypeName = examTypeName;
    }

    public List<ExamSubjectDTO> getExamSubjectDTO() {
        return examSubjectDTO;
    }

    public void setExamSubjectDTO(List<ExamSubjectDTO> examSubjectDTO) {
        this.examSubjectDTO = examSubjectDTO;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public int getExamType() {
        return examType;
    }

    public void setExamType(int examType) {
        this.examType = examType;
        this.examTypeName = examTypeNames[examType];
    }

    public int getIsArrangeComplate() {
        return isArrangeComplate;
    }

    public void setIsArrangeComplate(int isArrangeComplate) {
        this.isArrangeComplate = isArrangeComplate;
    }

    public int getIsLock() {
        return isLock;
    }

    public void setIsLock(int isLock) {
        this.isLock = isLock;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ExamEntry getEntry() {
        List<ExamSubjectEntry> examSubjectEntries = new ArrayList<ExamSubjectEntry>(this.getExamSubjectDTO().size());
        List<ObjectId> subjectList = new ArrayList<ObjectId>(this.getExamSubjectDTO().size());
        for (ExamSubjectDTO examSubjectDTO : this.getExamSubjectDTO()) {
            examSubjectEntries.add(new ExamSubjectEntry(
                    new ObjectId(examSubjectDTO.getSubjectId()),
                    examSubjectDTO.getSubjectName(),
                    examSubjectDTO.getFullMarks(),
                    examSubjectDTO.getYouXiuScore(),
                    examSubjectDTO.getFailScore(),
                    examSubjectDTO.getDiFenScore(),
                    examSubjectDTO.getTime(),
                    examSubjectDTO.getExamDate(),
                    examSubjectDTO.getWeekDay(),
                    examSubjectDTO.getOpenStatus(),
                    examSubjectDTO.getOpenBeginTime(),
                    examSubjectDTO.getOpenEndTime()));
            subjectList.add(new ObjectId(examSubjectDTO.getSubjectId()));
        }
        List<ObjectId> classL = new ArrayList<ObjectId>(this.classList.size());
        for (String string : this.classList) {
            classL.add(new ObjectId(string));
        }
        ExamEntry examEntry =  new ExamEntry(this.name,
                DateTimeUtils.stringToDate(this.date,
                        DateTimeUtils.DATE_YYYY_MM_DD).getTime(),
                new ObjectId(this.gradeId),
                this.gradeName,
                this.examType,
                this.remark,
                examSubjectEntries,
                0,
                0,
                new ObjectId(this.schoolId),
                genSchoolYear(),
                this.getIsGra(),
                classL,
                subjectList);
        examEntry.setType(type == 2 ? 2 : 1);
        return examEntry;
    }

    private String genSchoolYear() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String schoolYear;
        if (month < 9 && month >= 2) {
            schoolYear = (year - 1) + "-" + year + "学年第二学期";
        } else if (month > 9) {
            schoolYear = year + "-" + (year + 1) + "学年第一学期";
        } else {
            schoolYear = (year - 1) + "-" + year + "学年第一学期";
        }
        return schoolYear;
    }

    public void setOpenFlag(Set set) {
        //考试信息是否可编辑取决于内部课程的开放设置
        for (ExamSubjectDTO dto : this.getExamSubjectDTO()) {
            if (set.contains(dto.getSubjectId())) {
                this.open = this.open || dto.isCanEdit();
            }
            //只要存在可编辑信息，立即停止
            if (this.open) {
                break;
            }
        }
    }

    public class ExamUseRoom {
        private String id;
        private String name;
        private int useSeat;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getUseSeat() {
            return useSeat;
        }

        public void setUseSeat(int useSeat) {
            this.useSeat = useSeat;
        }

        public ExamUseRoom(String id, String name, int useSeat) {

        }
    }
}
