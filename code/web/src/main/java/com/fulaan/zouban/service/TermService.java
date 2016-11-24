package com.fulaan.zouban.service;

import com.db.zouban.TermDao;
import com.db.zouban.TimeTableDao;
import com.fulaan.zouban.dto.TermDTO;
import com.pojo.zouban.TermEntry;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by qiangm on 2015/11/5.
 */
@Service
public class TermService {
    //设置教学周

    private TimeTableDao timeTableDao = new TimeTableDao();
    private TermDao termDao = new TermDao();


    /**
     * 获取教学周，附带共多少周
     *
     * @param year
     * @param schoolId
     * @return
     */
    public TermDTO findTermDTO(String year, ObjectId schoolId) {
        TermEntry termEntry = termDao.findTermEntry(schoolId, year);

        if (termEntry != null) {
            TermDTO termDTO = new TermDTO(termEntry);
            termDTO.setFweek(calWeek(termEntry.getFirstTermEnd(), termEntry.getFirstTermStart()));
            termDTO.setSweek(calWeek(termEntry.getSecondTermEnd(), termEntry.getSecondTermStart()));
            return termDTO;
        } else {
            //不存在则默认添加一个
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String firstYear = year.substring(0, 4);
            String secondYear = year.substring(5, 9);
            try {
                termEntry = new TermEntry(schoolId, year, sdf.parse(firstYear + "-09-01").getTime(),
                        sdf.parse(secondYear + "-01-24").getTime(), sdf.parse(secondYear + "-03-01").getTime(), sdf.parse(secondYear + "-07-24").getTime());
                termDao.addTerm(termEntry);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            TermDTO termDTO = new TermDTO(termEntry);
            return termDTO;
        }
    }

    /**
     * 获取当前学期总共多少周
     *
     * @param term
     * @param schoolId
     * @return
     */
    public int getAllWeekByTerm(String term, ObjectId schoolId) {
        TermDTO termDTO = findTermDTO(term.substring(0, 11), schoolId);
        if (term.contains("一")) {
            return termDTO.getFweek();
        } else {
            return termDTO.getSweek();
        }
    }


    /**
     * 更新教学周
     */
    public void updateTerm(TermDTO termDTO) {
        termDao.updateTermEntry(termDTO.exportEntry());
    }


    /**
     * 计算有几周
     *
     * @param fte
     * @param fts
     * @return
     */
    private int calWeek(long fte, long fts) {
        Date d1 = new Date(fte);
        Date d2 = new Date(fts);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d2);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        int days = daysBetween(d2, d1) + w;
        int week = days / 7 + 1;
        return week;
    }

    /**
     * 判断当天是第几周
     *
     * @param schoolId
     * @return
     */
    public int findWeekIndex(String schoolId, String year) {
        Date date = new Date();
        return findWeekIndexByDate(schoolId, year, date);
    }

    /**
     * 计算该日期处于第几周
     *
     * @param schoolId
     * @param year
     * @return
     */
    public int findWeekIndexByDate(String schoolId, String year, Date date) {
        long nowTime = date.getTime();
        TermEntry termEntry = termDao.findTermEntry(new ObjectId(schoolId), year);
        if (termEntry.getFirstTermStart() < nowTime && termEntry.getFirstTermEnd() > nowTime) {//第一学期
            Long startDay = termEntry.getFirstTermStart();
            Date dt = new Date(startDay);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dt);
            int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
            if (w < 0)
                w = 0;
            int days = daysBetween(dt, date) + w;
            int week = days / 7 + 1;
            return week;
        } else if (termEntry.getFirstTermStart() < nowTime && termEntry.getSecondTermEnd() > nowTime) {//第二学期
            Long startDay = termEntry.getSecondTermStart();
            Date dt = new Date(startDay);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dt);
            int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
            if (w < 0)
                w = 0;
            int days = daysBetween(dt, date) + w;
            int week = days / 7 + 1;
            return week;
        }
        return 0;
    }

    /**
     * 某学期周数
     *
     * @param schoolId
     * @param year     学年
     * @param term     1：第一学期  2：第二学期
     * @return
     */
    public int findPassedWeeks(String schoolId, String year, int term) throws Exception {
        Date date = new Date();
        long nowTime = date.getTime();
        TermEntry termEntry = termDao.findTermEntry(new ObjectId(schoolId), year);
        if (termEntry == null) {
            throw new Exception("本学年还未设置教学周，请联系管理员到  办公OA/导入课表  中设置");
        }
        if (term == 1 && termEntry.getFirstTermStart() < nowTime && termEntry.getFirstTermEnd() > nowTime)//第一学期
        {
            Long startDay = termEntry.getFirstTermStart();
            Date dt = new Date(startDay);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dt);
            int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
            if (w < 0)
                w = 0;
            int days = daysBetween(dt, date) + w;
            int week = days / 7 + 1;
            return week;
        } else if (term == 1 && nowTime > termEntry.getFirstTermEnd()) {
            return calWeek(termEntry.getFirstTermEnd(), termEntry.getFirstTermStart());
        } else if (termEntry.getFirstTermStart() < nowTime && termEntry.getSecondTermEnd() > nowTime) {//第二学期
            Long startDay = termEntry.getSecondTermStart();
            Date dt = new Date(startDay);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dt);
            int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
            if (w < 0)
                w = 0;
            int days = daysBetween(dt, date) + w;
            int week = days / 7 + 1;
            return week;
        }
        return calWeek(termEntry.getSecondTermEnd(), termEntry.getSecondTermStart());
    }


    /**
     * 计算两天相差的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    private int daysBetween(Date date1, Date date2) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(date2);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }


    /**
     * 获取当前学期以及教学周
     *
     * @param schoolId
     * @return
     */
    public Map<String, Object> getCurrentTerm(String schoolId) {
        Map<String, Object> map = new HashMap<String, Object>();
        Date now = new Date();
        //第一学期
        TermEntry termEntry = termDao.findFirstTermByDate(new ObjectId(schoolId), now.getTime());
        if (termEntry != null) {
            map.put("term", termEntry.getYear() + "第一学期");
            map.put("dto", new TermDTO(termEntry));
            map.put("year", termEntry.getYear());
            map.put("allweek", calWeek(termEntry.getFirstTermEnd(), termEntry.getFirstTermStart()));
            map.put("curweek", findWeekIndex(schoolId, termEntry.getYear()));
            map.put("startDate", termEntry.getFirstTermStart());
            map.put("endDate", termEntry.getFirstTermEnd());
        } else {
            //第二学期
            termEntry = termDao.findSecondTermByDate(new ObjectId(schoolId), now.getTime());
            if (termEntry != null) {
                map.put("term", termEntry.getYear() + "第二学期");
                map.put("dto", new TermDTO(termEntry));
                map.put("year", termEntry.getYear());
                map.put("allweek", calWeek(termEntry.getSecondTermEnd(), termEntry.getSecondTermStart()));
                map.put("curweek", findWeekIndex(schoolId, termEntry.getYear()));
                map.put("startDate", termEntry.getSecondTermStart());
                map.put("endDate", termEntry.getSecondTermEnd());
            } else {
                //判断是否处于两学期之间
                termEntry = termDao.findDuringTermByDate(new ObjectId(schoolId), now.getTime());
                if (termEntry != null) {
                    map.put("term", termEntry.getYear() + "第二学期");
                    map.put("dto", new TermDTO(termEntry));
                    map.put("year", termEntry.getYear());
                    map.put("allweek", calWeek(termEntry.getSecondTermEnd(), termEntry.getSecondTermStart()));
                    map.put("curweek", 1);
                    map.put("startDate", termEntry.getSecondTermStart());
                    map.put("endDate", termEntry.getSecondTermEnd());
                } else {
                    //寻找比当前时间大的第一学期
                    List<TermEntry> termEntries = termDao.findFirstTermGteNow(new ObjectId(schoolId), now.getTime());
                    if (termEntries.isEmpty()) {
                        //没有，自动初始化一个
                        Calendar a = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String currentYear = Integer.toString(a.get(Calendar.YEAR));
                        String lastYear = Integer.toString(a.get(Calendar.YEAR) - 1);
                        String nextYear = Integer.toString(a.get(Calendar.YEAR) + 1);
                        TermEntry termEntry1 = null;
                        try {
                            if (a.getTime().getTime() < sdf.parse(currentYear + "-07-24").getTime()) {
                                termEntry1 = new TermEntry(new ObjectId(schoolId), Integer.toString(a.get(Calendar.YEAR) - 1) + "-" + Integer.toString(a.get(Calendar.YEAR)) + "学年",
                                        sdf.parse(lastYear + "-09-01").getTime(),
                                        sdf.parse(currentYear + "-01-24").getTime(), sdf.parse(currentYear + "-03-01").getTime(), sdf.parse(currentYear + "-07-24").getTime());
                            } else {
                                termEntry1 = new TermEntry(new ObjectId(schoolId), Integer.toString(a.get(Calendar.YEAR)) + "-" + Integer.toString(a.get(Calendar.YEAR) + 1) + "学年",
                                        sdf.parse(currentYear + "-09-01").getTime(),
                                        sdf.parse(nextYear + "-01-24").getTime(), sdf.parse(nextYear + "-03-01").getTime(), sdf.parse(nextYear + "-07-24").getTime());
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        termDao.addTerm(termEntry1);
                        if (a.getTime().getTime() <= termEntry1.getFirstTermEnd()) {
                            map.put("term", termEntry1.getYear() + "第一学期");
                            map.put("dto", new TermDTO(termEntry1));
                            map.put("year", termEntry1.getYear());
                            map.put("allweek", calWeek(termEntry1.getFirstTermEnd(), termEntry1.getFirstTermStart()));
                            map.put("curweek", findWeekIndex(schoolId, termEntry1.getYear()));
                            map.put("startDate", termEntry1.getFirstTermStart());
                            map.put("endDate", termEntry1.getFirstTermEnd());
                        } else {
                            map.put("term", termEntry1.getYear() + "第二学期");
                            map.put("dto", new TermDTO(termEntry1));
                            map.put("year", termEntry1.getYear());
                            map.put("allweek", calWeek(termEntry1.getSecondTermEnd(), termEntry1.getSecondTermStart()));
                            map.put("curweek", findWeekIndex(schoolId, termEntry1.getYear()));
                            map.put("startDate", termEntry1.getSecondTermStart());
                            map.put("endDate", termEntry1.getSecondTermEnd());
                        }
                    } else {
                        //寻找时间最近的一个
                        TermEntry best = termEntries.get(0);
                        for (int i = 1; i < termEntries.size(); i++) {
                            if (termEntries.get(i).getFirstTermStart() > best.getFirstTermStart()) {
                                best = termEntries.get(i);
                            }
                        }
                        map.put("term", best.getYear() + "第一学期");
                        map.put("dto", new TermDTO(best));
                        map.put("year", best.getYear());
                        map.put("allweek", calWeek(best.getFirstTermEnd(), best.getFirstTermStart()));
                        map.put("curweek", 1);
                        map.put("startDate", best.getFirstTermStart());
                        map.put("endDate", best.getFirstTermEnd());
                    }
                }
            }
        }
        return map;
    }

    /**
     * 获取当前日期是星期几,星期一返回1，星期日返回0
     *
     * @param dt
     * @return 当前日期是星期几
     */
    public int getWeekOfDate(Date dt) {
        //String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        /*if (w == 0)
            w = 7;*/
        return w;
    }
}
