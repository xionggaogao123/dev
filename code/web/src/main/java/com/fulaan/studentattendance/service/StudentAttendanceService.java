package com.fulaan.studentattendance.service;

import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.studentattendance.StudentAttendanceDao;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.ExportUtil;
import com.pojo.school.ClassEntry;
import com.pojo.school.ClassInfoDTO;
import com.pojo.studentattendance.StudentAttendanceDTO;
import com.pojo.studentattendance.StudentAttendanceEntry;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

@Service
public class StudentAttendanceService {

	@Autowired
	private ClassService classService;
	
	@Autowired
	private UserService userService;
	
	private StudentAttendanceDao attendanceDao = new StudentAttendanceDao();
	
	/**
	 * 保存或更新考勤信息
	 * @param entry
	 * @return
	 */
	public ObjectId saveOrUpdateAttendanceInfo(StudentAttendanceEntry entry) {
		return attendanceDao.saveStudentAttendance(entry);
	}
	
	/**
	 * 根据id获取考勤详细信息
	 * @param id
	 * @return
	 */
	public StudentAttendanceEntry getEntryById(ObjectId id) {
		return attendanceDao.getStuAttendanceEntry(id, Constant.FIELDS);
	}
	
	/**
	 * 根据id删除考勤信息
	 * @param id
	 */
	public void removeById(ObjectId id) {
		attendanceDao.deleteById(id);
	}
	
	/**
	 * 校领导进行考勤信息查询
	 * @param gradeIds
	 * @param clazzIds
	 * @param clazzId
	 * @param startDate
	 * @param endDate
	 * @param stuName
	 * @param type
	 * @return
	 * @throws Exception 
	 */
	public List<StudentAttendanceDTO> getStudentAttendanceInfo(
			List<ObjectId> gradeIds, List<ObjectId> clazzIds,
			String startDate, String endDate, 
			String stuName, int type) throws Exception {
		
		List<StudentAttendanceDTO> attendanceDtoList = new ArrayList<StudentAttendanceDTO>();
		
		List<String> dateList = bulidSearchDateList(startDate, endDate, "yyyy/MM/dd");
		List<StudentAttendanceEntry> attendanceEntryList = 
				attendanceDao.getAttendanceInfo(gradeIds, clazzIds, dateList, stuName, type);
		
		for(StudentAttendanceEntry entry : attendanceEntryList) {
			ObjectId clazzId = entry.getClazzId();
			ObjectId stuId = entry.getStudentId();
			ClassEntry clazzEntry = classService.getClassEntryById(clazzId, Constant.FIELDS);
			String clazzName = clazzEntry.getName();
			UserDetailInfoDTO stuInfoDto = userService.findUserInfoHasCityName(stuId.toString());
			String stuNumber = stuInfoDto.getStudentNum() == null ? "" : stuInfoDto.getStudentNum();
			
			StudentAttendanceDTO dto = new StudentAttendanceDTO(entry);
			dto.setClazzName(clazzName);
			dto.setStudentNumber(stuNumber);
			
			attendanceDtoList.add(dto);
		}
		
		Collections.sort(attendanceDtoList, new StuNameComparator());
		
		return attendanceDtoList;
	}
	
	public List<StudentAttendanceDTO> getStudentAttendanceInfo(
			String clazzId, String startDate, 
			String endDate, String stuName, int type) throws Exception {
		
		ClassEntry clazzEntry = classService.getClassEntryById(new ObjectId(clazzId), Constant.FIELDS);
		String clazzName = clazzEntry.getName();
		
		List<StudentAttendanceDTO> attendanceDtoList = new ArrayList<StudentAttendanceDTO>();
		
		List<String> dateList = bulidSearchDateList(startDate, endDate, "yyyy/MM/dd");
		List<StudentAttendanceEntry> attendanceEntryList = 
				attendanceDao.getAttendanceInfo(new ObjectId(clazzId), dateList, stuName, type);
		
		/**
		 * 查询全部状态时需要获取班级全部学生
		 * */
		List<UserDetailInfoDTO> stuDetailInfoList = new ArrayList<UserDetailInfoDTO>();
		if(type == -1) {
			ClassInfoDTO clazzInfoDto = classService.findClassInfoByClassId(clazzId);
			List<ObjectId> studentIdList = clazzInfoDto.getStudentIds();
			List<String> strIdList = MongoUtils.convertToStringList(studentIdList);
			stuDetailInfoList = userService.findUserInfoByUserIds(strIdList);
			
			if(StringUtils.isNotBlank(stuName)) { // 如果查询某个学生，去除其他学生
				Iterator<UserDetailInfoDTO> detailInfoItor = stuDetailInfoList.iterator();
				while(detailInfoItor.hasNext()) {
					UserDetailInfoDTO detailInfo = detailInfoItor.next();
					if(!detailInfo.getUserName().contains(stuName)) {
						detailInfoItor.remove();
					}
				}
			}
			
			Map<String, Map<ObjectId, StudentAttendanceDTO>> dateStudentAttenMap = 
					new HashMap<String, Map<ObjectId,StudentAttendanceDTO>>();
			
			for(String date : dateList) {
				Map<ObjectId, StudentAttendanceDTO> studentAttendMap = 
						new HashMap<ObjectId, StudentAttendanceDTO>();
				StudentAttendanceDTO attendanceDto = null;
				for(UserDetailInfoDTO userInfoDto : stuDetailInfoList) {
					String studentId = userInfoDto.getId();
					ObjectId stuObjId = new ObjectId(studentId);
					attendanceDto = new StudentAttendanceDTO();
					attendanceDto.setStudentName(userInfoDto.getUserName());
					attendanceDto.setStudentId(studentId);
					attendanceDto.setClazzId(clazzId);
					attendanceDto.setClazzName(clazzName);
					// 学号可能为null
					String stuNumber = userInfoDto.getStudentNum() == null ? "" : userInfoDto.getStudentNum();
					attendanceDto.setStudentNumber(stuNumber);
					attendanceDto.setAttendanceDate(date);
					attendanceDto.setAttendanceStatus(0); // 考勤状态正常
					studentAttendMap.put(stuObjId, attendanceDto);
				}
				dateStudentAttenMap.put(date, studentAttendMap);
			}
			
			for(StudentAttendanceEntry entry : attendanceEntryList) {
				String attendanceDate = entry.getAttendanceDate(); // 考勤日期
				ObjectId studentId = entry.getStudentId();
				Map<ObjectId, StudentAttendanceDTO> attendanceDtoMap = dateStudentAttenMap.get(attendanceDate);
				if(attendanceDtoMap != null) {
					StudentAttendanceDTO dtoInMap = attendanceDtoMap.get(studentId);
					if(dtoInMap != null) {
						dtoInMap.setAttendanceStatus(entry.getStuAttendanceStatus());
						dtoInMap.setRemark(entry.getRemark());
						dtoInMap.setClazzName(clazzName);
						dtoInMap.setId(entry.getID().toString());
					}
				}
			}
			
			for(String date : dateList) {
				Map<ObjectId, StudentAttendanceDTO> dtoMap = dateStudentAttenMap.get(date);
				Collection<StudentAttendanceDTO> dtoColl = dtoMap.values();
				List<StudentAttendanceDTO> dtoList = new ArrayList<StudentAttendanceDTO>(dtoColl);
				Collections.sort(dtoList, new StuNameComparator());
				attendanceDtoList.addAll(dtoList);
			}
		} else {
			for(StudentAttendanceEntry entry : attendanceEntryList) {
				StudentAttendanceDTO dto = new StudentAttendanceDTO(entry);
				String studentId = dto.getStudentId();
				UserDetailInfoDTO stuInfoDto = userService.getUserInfoById(studentId);
				dto.setClazzName(clazzName);
				dto.setStudentNumber(stuInfoDto.getStudentNum() == null ? "" : stuInfoDto.getStudentNum());
				attendanceDtoList.add(dto);
			}
			Collections.sort(attendanceDtoList, new StuNameComparator());
		}
		
		return attendanceDtoList;
	}
	
	/**
	 * 根据学生姓名进行排序
	 * @author xusy
	 */
	private class StuNameComparator implements Comparator<StudentAttendanceDTO> {
		
		private Comparator chComparator = Collator.getInstance(Locale.CHINA);
		
		@Override
		public int compare(StudentAttendanceDTO o1, StudentAttendanceDTO o2) {
			String stuName1 = o1.getStudentName();
			String stuName2 = o2.getStudentName();
			return chComparator.compare(stuName1, stuName2);
		}
	}
	
	/**
	 * 根据开始日期和结束日期获取连续的日期list，
	 * 1.开始日期和结束日期都为null，返回当前时间
	 * 2.若其中一个为null则返回包含不为空的日期list
	 * 3.都不为null则返回连续的日期list
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @return
	 * @throws ParseException 
	 */
	private static List<String> bulidSearchDateList(String startDate, 
			String endDate, String dateFormat) throws ParseException {
		
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		List<String> dateList = new ArrayList<String>();
		
		if(StringUtils.isBlank(startDate) 
				&& StringUtils.isBlank(endDate)) { // 当前时间
			String currentDate = format.format(new Date());
			dateList.add(currentDate);
		} else if(!StringUtils.isBlank(startDate) 
				&& !StringUtils.isBlank(endDate)) {
			Date start = format.parse(startDate);
			Date end = format.parse(endDate);
			
			if(start.getTime() > end.getTime()) { // 开始时间大于结束时间
				return dateList;
			}
			
			Date tempDate = start;
			Calendar calendar = Calendar.getInstance();
			dateList.add(format.format(tempDate));
			while(!tempDate.equals(end)) {
				calendar.setTime(tempDate);
				int day = calendar.get(Calendar.DATE);
				calendar.set(Calendar.DATE, day + 1);
				tempDate = calendar.getTime();
				dateList.add(format.format(tempDate));
			}
		} else if(StringUtils.isBlank(startDate) 
				|| StringUtils.isBlank(endDate)) {
			String date = StringUtils.isBlank(startDate) ? endDate : startDate;
			dateList.add(format.format(new Date(date)));
		}
		
		return dateList;
	}
	
	/**
	 * 导出学生考勤信息表
	 * @param util
	 * @param attendanceDtoList
	 */
	public void exportAttendanceInfo(final ExportUtil util, List<StudentAttendanceDTO> attendanceDtoList) {
		
		List<String> dataList = new ArrayList<String>();
		SXSSFWorkbook workbook = util.getBook();
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 制定单元格垂直居中
		cellStyle.setBorderTop((short) 1);
		cellStyle.setBorderBottom((short) 1);
		cellStyle.setBorderLeft((short) 1);
		cellStyle.setBorderRight((short) 1);
		
		dataList.clear();
		dataList.add("学号");
		dataList.add("姓名");
		dataList.add("考勤日期");
		dataList.add("班级");
		dataList.add("考勤状态");
		dataList.add("备注");
		util.appendRow(cellStyle, dataList.toArray());
		
		for(StudentAttendanceDTO dto : attendanceDtoList) {
			dataList.clear();
			dataList.add(dto.getStudentNumber());
			dataList.add(dto.getStudentName());
			dataList.add(dto.getAttendanceDate());
			dataList.add(dto.getClazzName());
			int attendStatus = dto.getAttendanceStatus();
			String status = "";
			switch (attendStatus) {
			case 0:
				status = "正常";
				break;
			case 1:
				status = "事假";
				break;
			case 2:
				status = "病假";
				break;
			case 9:
				status = "其他";
				break;
			default:
				break;
			}
			dataList.add(status);
			dataList.add(dto.getRemark());
			util.appendRow(cellStyle, dataList.toArray());
		}
		
		util.setFileName("学生考勤信息表.xlsx");
	}
	
	public static void main(String[] args) throws ParseException {
		List<String> dateList = bulidSearchDateList("2016/11/11", "2016/11/11", "yyyy/MM/dd");
		for(String s : dateList) {
			System.out.println(s);
		}
	}
}
