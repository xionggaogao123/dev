package com.fulaan.learningcenter.service;

import com.db.school.HomeWorkDao;
import com.db.school.SchoolDao;
import com.db.user.UserDao;
import com.mongodb.BasicDBObject;
import com.pojo.app.IdNameValuePair;
import com.pojo.school.HomeWorkEntry;
import com.pojo.school.StudentSubmitHomeWork;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.exceptions.ResultTooManyException;
import com.sys.utils.DateTimeUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.bson.types.ObjectId;

import javax.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * 作业service
 * @author fourer
 *
 */
public class HomeWorkService {

	private HomeWorkDao dao =new HomeWorkDao();
    private UserDao userDao = new UserDao();
    SchoolDao schoolDao =new SchoolDao();

    /**
     * 得到当前学期
     * @return
     */
    public String getCurrentTerm() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String schoolYear;
        if (month < 9 && month >= 2) {
            schoolYear = (year - 1) + "-" + year + "学年第二学期";
        } else if(month >= 9){
            schoolYear = year + "-" + (year + 1) + "学年第一学期";
        } else {
            schoolYear = (year - 1) + "-" + year + "学年第一学期";
        }
        return schoolYear;
    }
	
	/**
	 * 老师添加一个作业
	 * @param e
	 * @return
	 */
	public ObjectId addHomeWork(HomeWorkEntry e)
	{
		return dao.addHomeWorkEntry(e);
	}
	
	/**
	 * 查询老师布置的作业
	 * @param teacherid
	 * @param
	 * @return
	 * @throws ResultTooManyException 
	 */
	public List<HomeWorkEntry> getHomeWorkEntrys(ObjectId teacherid) throws ResultTooManyException
	{
		//return dao.getHomeWorkEntrys(teacherid, null);
        return null;
	}
	

    /**
     * 老师作业dto形式----修改部分：增加判断是否为校长，校长可查看全部老师的作业
     * @param teacherid 老师ID
     * @param classId 班级ID
     * @param page 页数
     * @param size 每页条数
     * @param type  0：课前 1：课后 2：其他 3：全部
     * @param subjectId
     * @param term 学期  0 ：本学期  1：全部学期
     * @param contentType 0：全部 1：有视频 2：有附件 3：有进阶练习 4：有语音
     * @return
     * @throws ResultTooManyException
     */
	public List<HomeWorkEntry> getHomeWorkEntrys(ObjectId teacherid,ObjectId classId,int page,int size, int type, ObjectId subjectId, int term, int contentType) throws ResultTooManyException
	{

        String currentTerm;
        if(term == 0) {
            currentTerm = getCurrentTerm();
        } else {
            currentTerm = null;
        }
        //如果teacherId是校长，则获取全部老师的作业
        List<HomeWorkEntry> list = new ArrayList<HomeWorkEntry>();
        if(teacherid==null)//学生
        {
                list.addAll(dao.findHomeWorkEntrys(teacherid, classId, page * size, size, type, subjectId, currentTerm, contentType));
        }
        else {
//            UserEntry userEntry = userDao.getUserEntry(teacherid, Constant.FIELDS);
//            UserRole[] roleList = {UserRole.HEADMASTER};
//            if (UserRole.isInRoles(userEntry.getRole(), roleList))//校长
//            {
//                ClassEntry classEntry = classDao.getClassEntryById(classId, Constant.FIELDS);
//                List<ObjectId> allTeacherId = new ArrayList<ObjectId>();
//                if (classEntry != null) {
//                    allTeacherId = classEntry.getTeachers();
//                } else {
//                    InterestClassEntry interestClassEntry = interestClassDao.findEntryByClassId(classId);
//                    if (interestClassEntry != null) {
//                        allTeacherId.add(interestClassEntry.getTeacherId());
//                    }
//                }
//                list = dao.getHomeWorkEntrys(allTeacherId, classId, page * size, size, type, subjectId, getCurrentTerm());
//            } else {
                list.addAll(dao.findHomeWorkEntrys(teacherid, classId, page * size, size, type, subjectId, currentTerm, contentType));
//            }
        }
    	return list;
	}

    /**
     * 老师作业dto形式----修改部分：增加判断是否为校长，校长可查看全部老师的作业
     * @param teacherid 老师ID
     * @param classId 班级ID
     * @param page 页数
     * @param size 每页条数
     * @param type  0：课前 1：课后 2：其他 3：全部
     * @param subjectId
     * @param term 学期  0 ：本学期  1：全部学期
     * @param contentType 0：全部 1：有视频 2：有附件 3：有进阶练习 4：有语音
     * @return
     * @throws ResultTooManyException
     */
    public List<HomeWorkEntry> getHomeWorkEntrys(ObjectId teacherid, String classId, int page, int size, int type, ObjectId subjectId, int term, int contentType) throws ResultTooManyException {
        String[] classIds = classId.split(",");

        String currentTerm;
        if (term == 0) {
            currentTerm = getCurrentTerm();
        } else {
            currentTerm = null;
        }
        //如果teacherId是校长，则获取全部老师的作业
        List<HomeWorkEntry> list = new ArrayList<HomeWorkEntry>();
        for (int i = 0; i < classIds.length; i++) {
        	if(StringUtils.isNotBlank(classIds[i]) && ObjectId.isValid(classIds[i]))
        	{
              list.addAll(dao.findHomeWorkEntrys(teacherid, new ObjectId(classIds[i]), page * size, size, type, subjectId, currentTerm, contentType));
        	}
        }
        return list;
    }

    /**
     * 作业列表
     * @param classId
     * @param subjectId
     * @param page
     * @param pageSize
     * @return
     */
    public List<HomeWorkEntry> getHomeWorkEntryList(ObjectId classId, ObjectId subjectId, int page, int pageSize) {
        List<HomeWorkEntry> homeWorkEntryList = new ArrayList<HomeWorkEntry>();
        homeWorkEntryList = dao.findHomeWorkEntryList(classId, subjectId, page * pageSize, pageSize, null, getCurrentTerm());
        return homeWorkEntryList;
    }


    /**
     * 查询作业数量
     * @param teacherid 老师ID
     * @param classId 班级ID
     * @param type 类型 0：课前 1：课后 2：其他 3：全部
     * @param subjectId 科目ID
     * @param term 学期 0：本学期 1：全部
     * @param contentType 0：全部 1：有视频 2：有附件 3：有进阶练习 4：有语音
     * @return
     * @throws ResultTooManyException
     */
    public int countHomeWorkEntrys(ObjectId teacherid,ObjectId classId,int type, ObjectId subjectId, int term, int contentType) throws ResultTooManyException
    {
        String currentTerm;
        if(term == 0) {
            currentTerm = getCurrentTerm();
        } else {
            currentTerm = null;
        }
        if(teacherid==null)//学生
        {
            return dao.findHomeWorkEntrysCount(teacherid, classId, type, subjectId, currentTerm, contentType);
        }
//        UserEntry userEntry=userDao.getUserEntry(teacherid,Constant.FIELDS);
//        UserRole[] roleList={UserRole.HEADMASTER};
//        if(UserRole.isInRoles(userEntry.getRole(), roleList))//校长,获取全部老师作业
//        {
//            ClassEntry classEntry=classDao.getClassEntryById(classId,Constant.FIELDS);
//            List<ObjectId> allTeacherId=new ArrayList<ObjectId>();
//            if(classEntry!=null)
//            {
//                allTeacherId=classEntry.getTeachers();
//            }
//            else
//            {
//                InterestClassEntry interestClassEntry= interestClassDao.findEntryByClassId(classId);
//                if(interestClassEntry!=null)
//                {
//                    allTeacherId.add(interestClassEntry.getTeacherId());
//                }
//            }
//            return dao.countAllHomeWorkEntrys(allTeacherId, classId);
//        }
        return dao.findHomeWorkEntrysCount(teacherid, classId, type, subjectId, currentTerm, contentType);

    }

    /**
     * 查询作业数量
     * @param teacherid 老师ID
     * @param classId 班级ID
     * @param type 类型 0：课前 1：课后 2：其他 3：全部
     * @param subjectId 科目ID
     * @param term 学期 0：本学期 1：全部
     * @param contentType 0：全部 1：有视频 2：有附件 3：有进阶练习 4：有语音
     * @return
     * @throws ResultTooManyException
     */
    public int countHomeWorkEntrys(ObjectId teacherid,String classId,int type, ObjectId subjectId, int term, int contentType) throws ResultTooManyException
    {
        String[] classIds = classId.split(",");
        int count = 0;
        String currentTerm;
        if(term == 0) {
            currentTerm = getCurrentTerm();
        } else {
            currentTerm = null;
        }
        for (int i = 0; i < classIds.length; i++) {
        	if(StringUtils.isNotBlank(classIds[i]) && ObjectId.isValid(classIds[i]))
        	{
              count += dao.findHomeWorkEntrysCount(teacherid, new ObjectId(classIds[i]), type, subjectId, currentTerm, contentType);
        	}
        }

        return count;

    }

    /**
     * 删除作业
     * @param hwId 作业ID
     */
    public void removeHomework(ObjectId hwId){

        dao.removeHomeworkEntry(hwId);
    }
	
	/**
	 * 作业详情
	 * @param hwId 作业ID
	 * @return
	 */
	public HomeWorkEntry getHomeWorkEntry(ObjectId hwId)
	{
		return dao.findHomeWorkEntry(hwId);
	}

    /**
     * 分页查询学生提交作业
     * @param hwId  作业ID
     * @param classId  班级ID
     * @param skip
     * @param limit
     * @return
     */
	public List<StudentSubmitHomeWork> getStudentSubmitHomeWorks(ObjectId hwId,ObjectId classId,int skip,int limit, ObjectId userId)
	{
		return dao.findStudentSubmitHomeWorks(hwId, classId, skip, limit, userId);
	}

    /**
     * 查询学生提交的所有作业
     * @param hwId 作业ID
     * @param classId 班级ID
     * @return
     */
    public List<StudentSubmitHomeWork> getStudentSubmitHomeWorks(ObjectId hwId,ObjectId classId)
    {
        return dao.findStudentSubmitHomeWorks(hwId, classId);
    }

    public int countStudentSubmitHomeWorks(ObjectId id){
        return dao.findStudentSubmitHomeWorksCount(id);
    }
	
	
	/**
	 * 学生提交作业
	 * @param hwId 作业ID
	 * @param ssh 提交内容
	 */
	public void submitHomeWork(ObjectId hwId, StudentSubmitHomeWork ssh)
	{
		dao.updateSubmitHomeWork(hwId, ssh);
	}

    /**
     * 查询作业访问数
     * @param usIds
     * @param dslId
     * @param delId
     * @return
     */
    public int selHomeWorkCount(List<ObjectId> usIds, ObjectId dslId, ObjectId delId) {
        return dao.findSelHomeWorkCount(usIds, dslId, delId);
    }

    /**
     * 获取统计对象上传的作业信息
     * @param usIds
     * @param dslId
     * @param delId
     * @param skip
     * @param limit
     * @param orderBy
     * @return
     */
    public List<HomeWorkEntry> getHomeworkUploadByParamList(List<ObjectId> usIds, ObjectId dslId, ObjectId delId, int skip, int limit, String orderBy) {
        List<HomeWorkEntry> list= dao.findHomeworkUploadByParamList(usIds, dslId, delId, skip, limit, Constant.FIELDS, orderBy);
        return list;
    }

    /**
     * 获取统计对象上传的作业信息
     * @param usIds
     * @param dslId
     * @param delId
     * @param skip
     * @param limit
     * @param orderBy
     * @return
     */
    public List<HomeWorkEntry> getHomeworkUploadByParamList(List<ObjectId> usIds, ObjectId dslId, ObjectId delId, int skip, int limit, BasicDBObject fields, String orderBy) {
        List<HomeWorkEntry> list= dao.findHomeworkUploadByParamList(usIds, dslId, delId, skip, limit, fields, orderBy);
        return list;
    }

    /**
     * 获取统计对象作业完成信息
     * @param usIds
     * @param dsl
     * @param del
     * @param orderBy
     * @return
     */
    public List<HomeWorkEntry> getJobCompletionByParamList(List<ObjectId> usIds, long dsl, long del, String orderBy) {
        List<HomeWorkEntry> list= dao.findJobCompletionByParamList(usIds, dsl, del, orderBy);
        return list;
    }


    /**
     * 更新文档图像
     * @param docId
     * @param url
     */
    public void updateDocImage(ObjectId docId,String url){
        HomeWorkEntry homeWorkEntry = dao.findHomeworkEntryByDocId(docId);
        for(StudentSubmitHomeWork studentSubmitHomeWork:homeWorkEntry.getSubmitList()){
            for(IdNameValuePair idNameValuePair:studentSubmitHomeWork.getDocFile()){
                if(idNameValuePair.getId().equals(docId)){
                    idNameValuePair.setValue(url);
                }
            }
        }

        dao.removeHomeworkEntry(homeWorkEntry.getID());
        dao.addHomeWorkEntry(homeWorkEntry);
    }

    /**
     * 编辑作业
     * @param homeWorkEntry
     */
    public void editHomeWork(HomeWorkEntry homeWorkEntry) {
        dao.updateHomeWork(homeWorkEntry);
    }

    /**
     * 批阅
     * @param homeworkId 作业ID
     * @param studentId 学生ID
     * @param time 学生提交作业时间
     */
    public void correctHomework(ObjectId homeworkId, ObjectId studentId, long time){
        dao.updateHomeworkCorrectFiled(homeworkId, studentId, time);
    }

    /**
     * 老师回复学生
     * @param homeworkId 作业ID
     * @param studentId 学生ID
     * @param time 学生提交作业时间
     * @param sshw 回复内容
     */
    public void teacherReply(ObjectId homeworkId, ObjectId studentId, long time, StudentSubmitHomeWork sshw){
        List<StudentSubmitHomeWork> sshwList = new ArrayList<StudentSubmitHomeWork>();
        HomeWorkEntry homeWorkEntry = getHomeWorkEntry(homeworkId);
        List<StudentSubmitHomeWork> studentSubmitHomeWorkList = homeWorkEntry.getSubmitList();
        for(StudentSubmitHomeWork studentSubmitHomeWork : studentSubmitHomeWorkList){
            if(studentSubmitHomeWork.getTime()==time && studentSubmitHomeWork.getStudentId().equals(studentId)) {
                sshwList = studentSubmitHomeWork.getHF();
                sshwList.add(sshw);
            }
        }

        dao.updateTeacherReply(homeworkId, studentId, time, sshwList);
    }

    /**
     * 查找所有的老作业，用于转换成新作业
     * @return
     */
    public List<HomeWorkEntry> findAllOldHomeworkEntry(){
        return dao.findAllOldHomeworkEntry();
    }
    public List<HomeWorkEntry> findAllHomeworkEntry(){
        return dao.findAllHomeworkEntry();
    }

    /**
     * 删除学生提交的作业
     * @param homeworkId
     * @param userId
     * @param time
     */
    public void deleteStuSubmitHW(ObjectId homeworkId, ObjectId userId, long time)
    {
        dao.deleteStuSubmitHW(homeworkId, userId, time);
    }

    /**
     * 12月份作业统计
     * @param response
     */
    public void exportHomeWorkExcel(HttpServletResponse response) {
        List<HomeWorkEntry> homeWorkEntryList = dao.findHomeWorkList();
        HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("作业统计列表");
        //为sheet1生成第一行，用于放表头信息
        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("上传教师姓名");
        cell = row.createCell(1);
        cell.setCellValue("教师所属学校名称");
        cell = row.createCell(2);
        cell.setCellValue("作业标题");
        cell = row.createCell(3);
        cell.setCellValue("作业内容");
        cell = row.createCell(4);
        cell.setCellValue("作业回复次数");
        cell = row.createCell(5);
        cell.setCellValue("日期时间");
        Set<ObjectId> userSet =new HashSet<ObjectId>();
        Set<ObjectId> schollSet =new HashSet<ObjectId>();
        for(HomeWorkEntry me:homeWorkEntryList)
        {
            userSet.add(me.getTeacherId());
//            schollSet.add(me);
        }
        Map<ObjectId, UserEntry> userMap =userDao.getUserEntryMap(userSet, Constant.FIELDS);
//        Map<ObjectId, SchoolEntry> schollMap =schoolDao.getSchoolMap(schollSet, Constant.FIELDS);
        int page=0;
        for(int i=0; i<homeWorkEntryList.size(); i++) {
            HomeWorkEntry blog = homeWorkEntryList.get(i);
            page++;
            row = sheet.createRow(page);

            cell = row.createCell(0);
            cell.setCellValue(userMap.get(blog.getTeacherId()).getUserName());
            cell = row.createCell(1);
            cell.setCellValue(schoolDao.getSchoolEntry(userMap.get(blog.getTeacherId()).getSchoolID(),Constant.FIELDS).getName());
            cell = row.createCell(2);
            cell.setCellValue(blog.getName());
            cell = row.createCell(3);
            cell.setCellValue(blog.getContent());
            cell = row.createCell(4);
            cell.setCellValue(countStudentSubmitHomeWorks(blog.getID()));
            cell = row.createCell(5);
            cell.setCellValue(DateTimeUtils.convert(blog.getID().getTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS));
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("作业统计.xls", "UTF-8"));
            response.setContentLength(content.length);
            outputStream.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(outputStream != null){
                try {
                    outputStream.close();
                    outputStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
