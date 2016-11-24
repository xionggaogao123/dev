package com.fulaan.yunying.service;

import cn.jpush.api.report.UsersResult;
import com.db.elect.ElectDao;
import com.db.microblog.MicroBlogDao;
import com.db.questionnaire.QuestionnaireDao;
import com.db.school.SchoolDao;
import com.db.user.UserDao;
import com.db.user.UserLogDao;
import com.db.withdrawCash.WithdrawCashDao;
import com.db.yunying.YunyingDao;
import com.fulaan.experience.dto.ExperienceLogDTO;
import com.fulaan.experience.dto.UserExperienceLogDTO;
import com.fulaan.experience.service.ExperienceService;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.user.service.UserService;
import com.pojo.elect.ElectEntry;
import com.pojo.emarket.WithDrawEntry;
import com.pojo.emarket.WithdrawCashInfo;
import com.pojo.microblog.MicroBlogEntry;
import com.pojo.questionnaire.QuestionnaireEntry;
import com.pojo.salary.SalaryDto;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserEntry;
import com.pojo.user.UserLogEntry;
import com.pojo.user.UserRole;
import com.pojo.utils.DeleteState;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.ResultTooManyException;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sql.dao.UserBalanceDao;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by wang_xinxin on 2016/1/5.
 */

@Service
public class YunYingService {

    private QuestionnaireDao questionnaireDao = new QuestionnaireDao();
    private MicroBlogDao microBlogDao = new MicroBlogDao();

    private WithdrawCashDao withdrawCashDao = new WithdrawCashDao();

    private UserDao userDao = new UserDao();

    private SchoolDao schoolDao = new SchoolDao();

    private UserBalanceDao userBalanceDao = new UserBalanceDao();

    private YunyingDao yunyingDao = new YunyingDao();

    private UserLogDao userLogDao = new UserLogDao();

    private ElectDao electDao = new ElectDao();

    @Autowired
    private UserService userService;

    @Autowired
    private ClassService classService;

    @Autowired
    private ExperienceService experienceService;
    /**
     * 好友微博数
     * @param userid
     * @param hottype
     * @param blogtype
     * @return
     */
    public int selBlogCount(ObjectId userid, int hottype, int blogtype,String schoolid,String theme,int seachType,String startDate,String endDate,int formtype,String keyword,String choose1,String choose2,int btype) {
        long btime = 0;
        long etime = 0;
        if (StringUtils.isNotEmpty(startDate)) {
            btime = DateTimeUtils.stringToDate(startDate,
                    DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A).getTime();
        }
        if (StringUtils.isNotEmpty(endDate)) {
            etime = DateTimeUtils.stringToDate(endDate,
                    DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A).getTime();
        }
        List<ObjectId> userids = new ArrayList<ObjectId>();
        if (StringUtils.isNotEmpty(keyword)) {
            userids = userDao.findIdListByUserName(keyword);
        }
        return microBlogDao.getBlogCount(userid, hottype, blogtype, schoolid, theme, seachType,btime,etime,formtype,keyword,userids,choose1,choose2,btype);
    }

    /**
     * 查询微博
     * @param userid
     * @param hottype
     * @param blogtype
     * @param page
     * @param pageSize
     * @return
     * @throws com.sys.exceptions.ResultTooManyException
     */
    public List<MicroBlogEntry> selBlogInfo(ObjectId userid,int hottype, int blogtype, String schoolid, int page, int pageSize,int seachtype,String theme,int seachType,String startDate,String endDate,int formtype,String keyword,String choose1,String choose2,int btype) throws ResultTooManyException {
        List<MicroBlogEntry> bloglist = new ArrayList<MicroBlogEntry>();
        long btime = 0;
        long etime = 0;
        if (StringUtils.isNotEmpty(startDate)) {
            btime = DateTimeUtils.stringToDate(startDate,
                    DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A).getTime();
        }
        if (StringUtils.isNotEmpty(endDate)) {
            etime = DateTimeUtils.stringToDate(endDate,
                    DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A).getTime();
        }
        List<ObjectId> userids = new ArrayList<ObjectId>();
        if (StringUtils.isNotEmpty(keyword)) {
            userids = userDao.findIdListByUserName(keyword);
        }
        if (hottype==3) {
            bloglist = microBlogDao.getBlogEntryList(userid, DeleteState.NORMAL, null, null, 0, hottype, null, schoolid, page < 1 ? 0 : ((page - 1) * pageSize), pageSize, theme, seachType,btime,etime,formtype,keyword,userids,choose1,choose2,btype);
        } else {
            bloglist = microBlogDao.getBlogEntryList(null, DeleteState.NORMAL, null, null, blogtype, hottype, null, schoolid, page < 1 ? 0 : ((page - 1) * pageSize), pageSize, theme, seachType,btime,etime,formtype,keyword,userids,choose1,choose2,btype);
        }
        return bloglist;
    }

    /**
     * 提现记录
     * @return
     */
    public List<WithdrawCashInfo> withDrawEntryList(List<String> userids,ObjectId dslId, ObjectId delId,int page, int pageSize) {
        List<WithdrawCashInfo> withdrawCashInfoList = new ArrayList<WithdrawCashInfo>();
        List<WithDrawEntry> withDrawEntries = withdrawCashDao.selWithDrawEntryList(userids,dslId,delId,page < 1 ? 0 : ((page - 1) * pageSize),pageSize);
        if (withDrawEntries!=null &&withDrawEntries.size()!=0) {
            for (WithDrawEntry withDrawEntry : withDrawEntries) {
                WithdrawCashInfo withdrawCashInfo = new WithdrawCashInfo(withDrawEntry);
                UserEntry user = userDao.getUserEntry(new ObjectId(withDrawEntry.getUserid()), null);
                if (user!=null) {
                    withdrawCashInfo.setSchoolname(schoolDao.getSchoolEntry(user.getSchoolID(),Constant.FIELDS).getName());
                    withdrawCashInfo.setName(user.getUserName());
                    withdrawCashInfo.setTime(DateTimeUtils.convert(withDrawEntry.getID().getTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS));
                }
                withdrawCashInfoList.add(withdrawCashInfo);
            }
        }
        return withdrawCashInfoList;
    }

    /**
     * k6kt增加经验值
     * @param userid
     * @param exp
     */
    public void updateExperenceValue(ObjectId userid,int exp,String content) {
        userDao.updateExperenceValue(userid,exp);
        userLogDao.addUserLogLog(new UserLogEntry(userid,content,String.valueOf(exp),1));
    }

    /**
     * k6kt增加余额
     * @param userid
     * @param balance
     */
    @Transactional
    public void modifyBalance(String userid, int balance,String content) {
        userBalanceDao.subMoneyFromBalance(userid, Double.valueOf(balance));
        userLogDao.addUserLogLog(new UserLogEntry(new ObjectId(userid),content,String.valueOf(balance),2));
    }

    /**
     *
     * @return
     */
    public int withDrawEntryCount(List<String> userids,ObjectId dslId, ObjectId delId) {
        return withdrawCashDao.withDrawEntryCount(userids,dslId,delId);
    }

    public void delteAllMicroBlog(List<String> list) {
        List<ObjectId> objList=new ArrayList<ObjectId>();
        for(int i=0;i<list.size();i++)
        {
            objList.add(new ObjectId(list.get(i)));
        }
        microBlogDao.delteAllMicroBlog(objList);

    }

    /**
     *
     * @param list
     */
    public void canceltop(List<ObjectId> list) {
        microBlogDao.canceltop(list);
    }

    /**
     * 查询微博
     * @param userid
     * @param hottype
     * @param blogtype
     * @return
     * @throws com.sys.exceptions.ResultTooManyException
     */
    public void exportTeacherBlogExcel(ObjectId userid,int hottype, int blogtype, String schoolid,String theme,int seachType,String startDate,String endDate,int formtype,String keyword,HttpServletResponse response,String choose1,String choose2,int btype) throws ResultTooManyException {
        List<MicroBlogEntry> bloglist = new ArrayList<MicroBlogEntry>();
        long btime = 0;
        long etime = 0;
        if (StringUtils.isNotEmpty(startDate)) {
            btime = DateTimeUtils.stringToDate(startDate,
                    DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A).getTime();
        }
        if (StringUtils.isNotEmpty(endDate)) {
            etime = DateTimeUtils.stringToDate(endDate,
                    DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A).getTime();
        }
        List<ObjectId> userids = new ArrayList<ObjectId>();
        if (StringUtils.isNotEmpty(keyword)) {
            userids = userDao.findIdListByUserName(keyword);
        }
        if (hottype==3) {
            bloglist = microBlogDao.getBlogEntryList(userid, DeleteState.NORMAL, null, null, 0, hottype, null, schoolid, 0, 0, theme, seachType,btime,etime,formtype,keyword,userids,choose1,choose2,btype);
        } else {
            bloglist = microBlogDao.getBlogEntryList(null, DeleteState.NORMAL, null, null, blogtype, hottype, null, schoolid, 0, 0, theme, seachType,btime,etime,formtype,keyword,userids,choose1,choose2,btype);
        }
        HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("微校家园列表");
        //为sheet1生成第一行，用于放表头信息
        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("用户名");
        cell = row.createCell(1);
        cell.setCellValue("内容");
        cell = row.createCell(2);
        cell.setCellValue("学校");
        cell = row.createCell(3);
        cell.setCellValue("发布时间");
        cell = row.createCell(4);
        cell.setCellValue("班级");
        cell = row.createCell(5);
        cell.setCellValue("是否有图片");
        cell = row.createCell(6);
        cell.setCellValue("是否有视频");
        Set<ObjectId> userSet =new HashSet<ObjectId>();
        Set<ObjectId> schollSet =new HashSet<ObjectId>();
        for(MicroBlogEntry me:bloglist)
        {
            userSet.add(me.getUserId());
            schollSet.add(me.getSchoolID());
        }
        Map<ObjectId, UserEntry> userMap =userDao.getUserEntryMap2(userSet, Constant.FIELDS);
        Map<ObjectId, SchoolEntry> schollMap =schoolDao.getSchoolMap(schollSet, Constant.FIELDS);
        int page=0;
        for(int i=0; i<bloglist.size(); i++) {
            MicroBlogEntry blog = bloglist.get(i);
            if (userMap.get(blog.getUserId())!=null) {
                page++;
                row = sheet.createRow(page);
                cell = row.createCell(0);
                cell.setCellValue(userMap.get(blog.getUserId()) != null ? userMap.get(blog.getUserId()).getUserName() : "");
                cell = row.createCell(1);
                cell.setCellValue(blog.getContent());
                cell = row.createCell(2);
                if (schollMap.get(blog.getSchoolID()) != null) {
                    cell.setCellValue(schollMap.get(blog.getSchoolID()).getName());
                } else {
                    cell.setCellValue("");
                }

                cell = row.createCell(3);
                cell.setCellValue((DateTimeUtils.convert(blog.getPublishTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS)));
                cell = row.createCell(4);
                String classname = "";

                if (UserRole.isStudent(userMap.get(blog.getUserId()).getRole())) {
                    classname = classService.findMainClassNameByUserId(blog.getUserId().toString());
                }
                cell.setCellValue(classname);
                cell = row.createCell(5);
                cell.setCellValue((blog.getImageList() != null && blog.getImageList().size() > 0) ? "是" : "否");
                cell = row.createCell(6);
                cell.setCellValue((blog.getVideoIds() != null && blog.getVideoIds().size() > 0) ? "是" : "否");
            }
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
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("微校家园.xls", "UTF-8"));
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

    public List<QuestionnaireEntry> findPlatformSurveys(int page,int size) throws Exception {
        List<QuestionnaireEntry> surveyList = yunyingDao.getPlatformSurveies((page-1)*size, size);
        return surveyList;
    }

    /** 读取当前用户可见的调查数量
     * @return 数量
     * @throws Exception
     */
    public int getPlatFormQuestionnaireCount()throws Exception
    {
        return yunyingDao.getPlatformQuestionnaireCount();
    }

    /**
     *
     * @param userid
     */
    public int isJinyan(String userid) {
        UserEntry userEntry = userDao.getUserEntry(new ObjectId(userid), null);
        int jinyan = 0;
        if (userEntry.getJinyan()==0) {
            jinyan = 1;
        } else {
            jinyan = 0;
        }
        userDao.isJinyan(new ObjectId(userid),jinyan);
        return jinyan;
    }

    /**
     *
     * @param begintime
     * @param endtime
     * @param tname
     * @param response
     */
    public void exportWithdraw(String begintime, String endtime, String tname, HttpServletResponse response) {
        List<String> userids =new ArrayList<String>();
        if (!StringUtils.isEmpty(tname)) {
            UserEntry user = userService.searchUserByUserName(tname);
            userids.add(user.getID().toString());
        }
        List<WithDrawEntry> withDrawEntryList = withdrawCashDao.selWithDrawEntryList(userids, !StringUtils.isEmpty(begintime) ? new ObjectId(DateTimeUtils.stringToDate(begintime, DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A)) : null, !StringUtils.isEmpty(endtime) ? new ObjectId(DateTimeUtils.stringToDate(endtime, DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A)) : null);
        if (withDrawEntryList!=null && withDrawEntryList.size()!=0) {
            HSSFWorkbook wb = new HSSFWorkbook();
            //生成一个sheet1
            HSSFSheet sheet = wb.createSheet("提现列表");
            //为sheet1生成第一行，用于放表头信息
            HSSFRow row = sheet.createRow(0);

            HSSFCell cell = row.createCell(0);
            cell.setCellValue("用户名");
            cell = row.createCell(1);
            cell.setCellValue("学校");
            cell = row.createCell(2);
            cell.setCellValue("金额");
            cell = row.createCell(3);
            cell.setCellValue("账号");
            cell = row.createCell(4);
            cell.setCellValue("账号姓名");
            cell = row.createCell(5);
            cell.setCellValue("身份证");
            cell = row.createCell(6);
            cell.setCellValue("手机号");
            cell = row.createCell(7);
            cell.setCellValue("开户行");
            cell = row.createCell(8);
            cell.setCellValue("时间");
            int page=0;
            for(int i=0; i<withDrawEntryList.size(); i++) {
                WithDrawEntry with = withDrawEntryList.get(i);
                UserEntry user = userDao.getUserEntry(new ObjectId(with.getUserid()), null);
                page++;
                row = sheet.createRow(page);
                cell = row.createCell(0);
                cell.setCellValue(user.getUserName());
                cell = row.createCell(1);
                cell.setCellValue(schoolDao.getSchoolEntry(user.getSchoolID(),Constant.FIELDS).getName());
                cell = row.createCell(2);
                cell.setCellValue(with.getCash());
                cell = row.createCell(3);
                cell.setCellValue(with.getPaypalAccount());
                cell = row.createCell(4);
                cell.setCellValue(with.getUsername());
                cell = row.createCell(5);
                cell.setCellValue(with.getCardNum()==null?"":with.getCardNum());
                cell = row.createCell(6);
                cell.setCellValue(with.getPhone());
                cell = row.createCell(7);
                cell.setCellValue(with.getOpenBank());
                cell = row.createCell(8);
                cell.setCellValue(DateTimeUtils.convert(with.getID().getTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS));
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
                response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("提现列表.xls", "UTF-8"));
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

    public void updateBeiZhu(String id, String beiZhu) {
        withdrawCashDao.updateBeiZhu(new ObjectId(id),beiZhu);
    }

    public void updateStatus(String id, int status) {
        withdrawCashDao.updateStatus(new ObjectId(id), status);
    }

    public Page<ExperienceLogDTO> getBalanceInfo(String userId, Pageable pageable) {
        //取得分页页码
        int page = pageable.getOffset()-pageable.getPageSize();
        //取得分页每页的size
        int pageSize = pageable.getPageSize();
        int count = userLogDao.getUserBalanceLogCount(new ObjectId(userId));
        //取得用户积分日志信息
        List<UserLogEntry> userLogEntryList=userLogDao.getUserBalanceLogEntry(new ObjectId(userId), page, pageSize);

        //取得用户增减积分的明细信息
        List<ExperienceLogDTO> expLogDTOList=new ArrayList<ExperienceLogDTO>();
        if (userLogEntryList!=null && userLogEntryList.size()!=0) {
            for (UserLogEntry userLog : userLogEntryList) {
                ExperienceLogDTO experienceLogDTO = new ExperienceLogDTO();
                experienceLogDTO.setExperiencename(userLog.getConnent());
                experienceLogDTO.setExperience(Integer.valueOf(userLog.getValue()));
                experienceLogDTO.setCreatetime(DateTimeUtils.longToDate(userLog.getID().getTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
                expLogDTOList.add(experienceLogDTO);
            }
        }


        return new PageImpl<ExperienceLogDTO>(expLogDTOList, pageable, count);
    }

    /**
     *
     * @param id
     * @throws IllegalParamException
     */
    public void shouCangMicroblog(String id) throws IllegalParamException {
        if(!ObjectId.isValid(id))
        {
            throw new IllegalParamException("the id ["+id+" ] is valid!!");
        }
        microBlogDao.shouCangMicroblog(new ObjectId(id));
    }

    /** 查询选举 ， 按照学校id和所在班级id列表
     * @param schoolId 学校id
     * @param classIds 老师或学生所在的班级列表
     * @param page 页数，0开始。
     * @param size 每页数量
     * @return 所有当前用户可见的选举信息列表
     * @throws Exception
     */
    public List<ElectEntry> findElectsBySchoolId(String schoolId,List<ObjectId> classIds,ObjectId userId,int page,int size) throws Exception
    {
        if(!ObjectId.isValid(schoolId))
        {
            throw new IllegalParamException("the id ["+schoolId+" ] is valid!!");
        }

        List<ElectEntry> elects = yunyingDao.getElectEntryBySchoolId(new ObjectId(schoolId),classIds,page*size,size);

        //处理数据，将可显示的publish置为0，不可显示的置为1
        for (ElectEntry electEntry:elects)
        {
            Calendar currentDate = new GregorianCalendar();
            currentDate.set(Calendar.HOUR_OF_DAY, 0);
            currentDate.set(Calendar.MINUTE, 0);
            currentDate.set(Calendar.SECOND, 0);
            long time=currentDate.getTimeInMillis()-100000;
            if(electEntry.getEndTime()>=time)//未结束，若为发起人，publish为0，其余为1
            {
                if(electEntry.getPublish()==0)
                {
                    electEntry.setPublish(0);
                }
                else {
                    if (electEntry.getPublisherId().equals(userId)) {
                        electEntry.setPublish(10);
                    }
                    else
                    {
                        electEntry.setPublish(11);
                    }
                }
            }
            else//已结束,若公布，publish不变；若为发起人，publish为0，其余为1
            {
                if(electEntry.getPublish()==0)
                {
                    electEntry.setPublish(0);
                }
                else if(electEntry.getPublish()==1 && electEntry.getPublisherId().equals(userId))
                {
                    electEntry.setPublish(10);
                }
                else
                {
                    electEntry.setPublish(11);
                }
            }
        }

        if(elects.isEmpty())
        {
//            logger.info("can not find ElectEntry;schoolId=" + schoolId);
        }
        return elects;
    }

    /** 读取当前用户可见的选举数量
     * @param schoolId 学校id
     * @param classIds 老师或学生所在的班级列表
     * @return 数量
     * @throws Exception
     */
    public int getElectCountBySchoolId(String schoolId, List<ObjectId> classIds)throws Exception
    {
        if(!ObjectId.isValid(schoolId))
        {
            throw new IllegalParamException("the id ["+schoolId+" ] is valid!!");
        }

        return electDao.getElectCountbySchoolId(new ObjectId(schoolId),classIds);
    }

    public void updateUserExp(SalaryDto dto) {
        UserEntry userEntry = userDao.searchUserByUserName(dto.getUserName());
        if (userEntry!=null) {
            ObjectId userId = userEntry.getID();
            userLogDao.addUserLogLog(new UserLogEntry(userId,dto.getRemark(),String.valueOf((int)dto.getSs()),1));
            userDao.updateExperenceValue(userId,(int)dto.getSs());
            ExperienceLogDTO experienceLog = new ExperienceLogDTO();
            experienceLog.setExperience((int)dto.getSs());
            experienceLog.setExperiencename(dto.getRemark());
            experienceLog.setCreatetime(new Date());
            experienceService.addUserExperience(userId.toString(),experienceLog);
        }

    }
}
