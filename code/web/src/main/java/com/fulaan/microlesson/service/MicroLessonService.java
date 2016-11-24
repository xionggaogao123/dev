package com.fulaan.microlesson.service;

import com.db.educationbureau.EducationBureauDao;
import com.db.lesson.LessonDao;
import com.db.microlesson.MicroLessonDao;
import com.db.school.SchoolDao;
import com.db.user.UserDao;
import com.fulaan.microlesson.dto.*;
import com.fulaan.utils.QiniuFileUtils;
import com.fulaan.utils.StringUtil;
import com.fulaan.video.service.VideoService;
import com.mongodb.BasicDBObject;
import com.pojo.app.IdValuePair;
import com.pojo.educationbureau.EducationBureauEntry;
import com.pojo.lesson.LessonEntry;
import com.pojo.lesson.LessonWare;
import com.pojo.microlesson.*;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserEntry;
import com.pojo.user.UserInfoDTO;
import com.pojo.utils.MongoUtils;
import com.pojo.video.VideoEntry;
import com.pojo.video.VideoSourceType;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by wang_xinxin on 2015/8/21.
 */

@Service
public class MicroLessonService {

    private static final Logger logger= Logger.getLogger(MicroLessonService.class);

    private MicroLessonDao microLessonDao = new MicroLessonDao();

    private EducationBureauDao bureauDao = new EducationBureauDao();

    private  SchoolDao schoolDao=new SchoolDao();

    private UserDao userdao = new UserDao();

    private LessonDao lessondao = new LessonDao();

    private VideoService videoService =new VideoService();
    
    private UserDao userDao =new UserDao();

    /**
     * 发部比赛
     * @param microMatchEntry
     */
    public ObjectId addMicroMatch(MicroMatchEntry microMatchEntry) {
        return microLessonDao.addMicroMatch(microMatchEntry);
    }

    /**
     * 获取评委
     * @param id
     * @return
     */
    public List<MatchAddressDTO> microMatchAdress(String id) {
        EducationBureauEntry educationBureauEntry = bureauDao.selEducationByUserId(new ObjectId(id));
        List<SchoolEntry> SEList=schoolDao.getSchoolEntryList(educationBureauEntry.getSchoolIds());
        List<MatchAddressDTO> adslist = new ArrayList<MatchAddressDTO>();
        MatchAddressDTO addressdto = null;
        List<UserMatchDTO> users = null;
        for(SchoolEntry schoolEntry:SEList){
            users = new ArrayList<UserMatchDTO>();
            addressdto = new MatchAddressDTO();
            List<UserEntry> userlist = userdao.getTeacherEntryBySchoolId(schoolEntry.getID(), new BasicDBObject("nm",1));
            for (UserEntry user : userlist) {
                users.add(new UserMatchDTO(user));
            }
            addressdto.setSchoolname(schoolEntry.getName());
            addressdto.setUserlist(users);
            adslist.add(addressdto);
        }

        return adslist;
    }

    /**
     * 比赛列表
     * @param id
     * @return
     */
    public int selMicroMatchCount(ObjectId id) {
    return microLessonDao.selMicroMatchCount(id);

    }


    /**
     * 查询比赛列表
     * @param id
     * @param page
     * @param pageSize
     * @return
     */
    public List<MicroMatchDTO> selMicroMatch(ObjectId id, int page, int pageSize) {
        List<MicroMatchEntry> matchlist = microLessonDao.selMicroMatch(id,page < 1 ? 0 : ((page - 1) * pageSize),pageSize);
        
        
        List<ObjectId> userIds =MongoUtils.getFieldObjectIDs(matchlist, "bui");
        
        
        Map<ObjectId, UserEntry> userMap=userDao.getUserEntryMap(userIds, new BasicDBObject("nm",1).append("si", 1));
        
        List<ObjectId> schoolIds =MongoUtils.getFieldObjectIDs(userMap.values(), "si");
        
        
        Map<ObjectId, SchoolEntry> schoolMap=  schoolDao.getSchoolMap(schoolIds, new BasicDBObject("nm",1));

        
        
        
        
        
        
        
        
        List<MicroMatchDTO> microlist = new ArrayList<MicroMatchDTO>();
        MicroMatchDTO dto = null;
        
        UserEntry ue=null;
        SchoolEntry se =null;
        
        if(matchlist!=null && matchlist.size()!=0) {
            for (MicroMatchEntry match : matchlist) {
            	
            	try
            	{
	                dto = new MicroMatchDTO();
	                dto.setMatchname(match.getMatchname());
	                dto.setContent(match.getConntent());
	                dto.setPath(match.getPath());
	                dto.setBegintime(DateTimeUtils.convert(match.getBegintime(),DateTimeUtils.DATE_YYYY_MM_DD_B));
	                dto.setEndtime(DateTimeUtils.convert(match.getEndtime(),DateTimeUtils.DATE_YYYY_MM_DD_B));
	                dto.setScorebegintime(DateTimeUtils.convert(match.getScorebegintime(),DateTimeUtils.DATE_YYYY_MM_DD_B));
	                dto.setScoreendtime(DateTimeUtils.convert(match.getScoreendtime(),DateTimeUtils.DATE_YYYY_MM_DD_B));
	                dto.setId(match.getID().toString());
	                dto.setMatchtypes(match.getMatchtypelist());
	                ue=userMap.get(match.getBuserid());
	                if(null!=ue)
	                {
	                	dto.setUserName(ue.getUserName().replaceAll("[(0-9)]", ""));
	                	
	                	se=schoolMap.get(ue.getSchoolID());
	                	
	                	if(null!=se)
	                	{
	                		dto.setSchoolName(se.getName());
	                	}
	                }
	                microlist.add(dto);
            	}catch(Exception ex)
            	{
            		logger.error("", ex);
            	}
            }
        }
        return microlist;
    }

    /**
     * 比赛详情
     * @param matchid
     * @return
     */
    public MicroMatchDTO matchDetailInfo(String matchid,int type) {
        MicroMatchEntry match = microLessonDao.getMatchDetail(matchid,null);
        MicroMatchDTO dto = new MicroMatchDTO();
        if (match!=null) {
            dto.setMatchname(match.getMatchname());
            dto.setContent(match.getConntent());
            dto.setPath(match.getPath());
            if (type==1) {
                dto.setBegintime(DateTimeUtils.convert(match.getBegintime(),DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A));
                dto.setEndtime(DateTimeUtils.convert(match.getEndtime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A));
                dto.setScorebegintime(DateTimeUtils.convert(match.getScorebegintime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A));
                dto.setScoreendtime(DateTimeUtils.convert(match.getScoreendtime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A));
                List<ObjectId> userlist = match.getUserlist();
                List<UserInfoDTO> users = new ArrayList<UserInfoDTO>();
                List<UserEntry> user = userdao.getUserEntryList(userlist, null);
                UserInfoDTO userdto = new UserInfoDTO();
                for (UserEntry e:user) {
                    userdto = new UserInfoDTO();
                    userdto.setId(e.getID().toString());
                    userdto.setName(e.getUserName());
                    users.add(userdto);

                }
                dto.setUsers(users);
            } else {
                if (match.getBegintime()>new Date().getTime() || match.getEndtime()<((new Date().getTime()))) {
                    dto.setShowup(1);
                } else {
                    dto.setShowup(2);
                }
                dto.setEdtime(match.getScoreendtime());
                dto.setBegintime(DateTimeUtils.convert(match.getBegintime(), DateTimeUtils.DATE_YYYY_MM_DD_B));
                dto.setEndtime(DateTimeUtils.convert(match.getEndtime(),DateTimeUtils.DATE_YYYY_MM_DD_B));
                dto.setScorebegintime(DateTimeUtils.convert(match.getScorebegintime(), DateTimeUtils.DATE_YYYY_MM_DD_B));
                dto.setScoreendtime(DateTimeUtils.convert(match.getScoreendtime(),DateTimeUtils.DATE_YYYY_MM_DD_B));
            }

            dto.setId(match.getID().toString());
            List<IdValuePair> typelist = match.getMatchtypelist();
            dto.setMatchtypes(typelist);
            List<TypeValueDTO> tvs = new ArrayList<TypeValueDTO>();
            TypeValueDTO typeValueDTO = new TypeValueDTO();
            if (typelist!=null && typelist.size()!=0) {
                for (IdValuePair pair : typelist) {
                    typeValueDTO = new TypeValueDTO();
                    typeValueDTO.setId(pair.getId().toString());
                    typeValueDTO.setName(pair.getValue().toString());
                    tvs.add(typeValueDTO);
                }
            }
            dto.setTypes(tvs);
            List<TypeValueDTO> stvs = new ArrayList<TypeValueDTO>();
            List<ScoreTypeEntry> scoreTypeEntryList = match.getScoreTypeList();
            dto.setScoretypes(scoreTypeEntryList);
            if (scoreTypeEntryList!=null && scoreTypeEntryList.size()!=0) {
                for (ScoreTypeEntry score : scoreTypeEntryList) {
                    typeValueDTO = new TypeValueDTO();
                    typeValueDTO.setId(score.getName());
                    typeValueDTO.setName(String.valueOf(score.getScore()));
                    stvs.add(typeValueDTO);
                }
            }
            dto.setStypes(stvs);
        }
        return dto;
    }

    /**
     *
     * @param typeid
     * @param id
     */
    public void updateMicroMatch(String matchid,String typeid, ObjectId id,String name) {
        TypeLessonEntry pair = new TypeLessonEntry(typeid,id.toString());
        microLessonDao.updateMicroMatch(matchid,pair);
        List<LessonScoreEntry> scorelist = microLessonDao.selLessonScorelist(null, new ObjectId(matchid));
        int sort = 0;
        if (scorelist!=null && scorelist.size()!=0) {
            if (scorelist.get(scorelist.size()-1).getAverage()!=0) {
                sort = scorelist.get(scorelist.size()-1).getSort()+1;
            } else {
                sort = scorelist.get(scorelist.size()-1).getSort();
            }

        }
        LessonScoreEntry lessonScore = new LessonScoreEntry(id,new ObjectId(matchid),name,0,0d,sort,new ArrayList<ScoreTypeEntry>());
        microLessonDao.addLessonScore(lessonScore);

    }

    public List<LessonEntry> selMicroLesson(String type, String matchid) {
        MicroMatchEntry match = microLessonDao.getMatchDetail(matchid,null);
        List<LessonEntry> lessonList = getMicroMatchLessonList(type,match);
        return lessonList;
    }

    private List<LessonEntry> getMicroMatchLessonList(String type,MicroMatchEntry match){
        List<LessonEntry> lessonList = new ArrayList<LessonEntry>();
        if (match!=null) {
            List<TypeLessonEntry> vaules = match.getLessonlist();
            List<ObjectId> lessons = new ArrayList<ObjectId>();
            if (vaules!=null && vaules.size()!=0) {
                for (TypeLessonEntry vale : vaules) {
                    if ("全部".equals(type)) {
                        lessons.add(new ObjectId(vale.getValue().toString()));
                    } else {
                        if (type.equals(vale.getId())) {
                            lessons.add(new ObjectId(vale.getValue().toString()));
                        }
                    }
                }
            }
            lessonList = lessondao.selLessonList(lessons);
        }
        return lessonList;
    }

    /**
     * 删除课程
     * @param lessonid
     * @param matchid
     */
    public void deletelesson(String lessonid, String matchid) {
        MicroMatchEntry match = microLessonDao.getMatchDetail(matchid,null);
        if (match!=null) {
            List<TypeLessonEntry> vaules = match.getLessonlist();
            List<ObjectId> lessons = new ArrayList<ObjectId>();
            if (vaules!=null && vaules.size()!=0) {
                for (TypeLessonEntry vale : vaules) {
                    if (lessonid.equals(vale.getValue())) {
                        microLessonDao.deletematchlesson(vale, matchid);
                    }
                }
            }
        }
        lessondao.removeLessonEntry(new ObjectId(lessonid));
    }

    /**
     * 删除比赛
     * @param id
     */
    public void deleteMatch(ObjectId id) {
        microLessonDao.deleteMatch(id);
    }

    /**
     * 老师打分
     * @param teacherScore
     */
    public void addScoreInfo(TeacherScoreEntry teacherScore) {
        microLessonDao.addSocreInfo(teacherScore);
        LessonEntry lesson = lessondao.getLessonEntry(teacherScore.getLessonid(), null);
        List<TeacherScoreEntry> teacherScoreList = microLessonDao.getLessonScore(teacherScore.getLessonid());
        int sumScore = 0,count = 0;
        double avgScore = 0;
        Integer minScore = null, maxScore = null;
        List<ScoreTypeEntry> scoreTypeEntryList = new ArrayList<ScoreTypeEntry>();
        if (teacherScoreList!=null && teacherScoreList.size()!=0) {
            for (TeacherScoreEntry teacherscore : teacherScoreList) {
                Integer score = teacherscore.getScore();
                sumScore += score;
                count++;
                if (minScore == null || minScore > score) {
                    minScore = score;
                }
                if (maxScore == null || maxScore < score) {
                    maxScore = score;
                }
                List<ScoreTypeEntry> scorelist = teacherscore.getScoreTypeList();
                if (scoreTypeEntryList!=null && scoreTypeEntryList.size()!=0) {
                    if (scorelist != null && scorelist.size() != 0) {
                        for (int i = 0; i < scorelist.size(); i++) {
                            ScoreTypeEntry st = scorelist.get(i);
                            if (scoreTypeEntryList.get(i).getName().equals(st.getName())) {
                                scoreTypeEntryList.set(i, new ScoreTypeEntry(st.getName(), scoreTypeEntryList.get(i).getScore() + st.getScore()));
                            }
                        }
                    }
                } else {
                    scoreTypeEntryList = scorelist;
                }
            }
        }
        if (count>2) {
            avgScore = (sumScore - minScore - maxScore)/(count-2);
        }
        ObjectId matchid = lesson.getDirId();
        LessonScoreEntry lessonscoreentry = microLessonDao.selLessonScore(teacherScore.getLessonid(), matchid);
        LessonScoreEntry lessonScore = new LessonScoreEntry(teacherScore.getLessonid(),matchid,lesson.getName(),sumScore,avgScore,0,scoreTypeEntryList==null?new ArrayList<ScoreTypeEntry>():scoreTypeEntryList);
        if (lessonscoreentry!=null) {
            microLessonDao.updateLessonScore(lessonScore);
        } else {
            microLessonDao.addLessonScore(lessonScore);

        }
        List<LessonScoreEntry> scorelist = microLessonDao.selLessonScorelist(null, matchid);
        List<LessonScoreDTO> lsdtos = new ArrayList<LessonScoreDTO>();
        if (scorelist!=null && scorelist.size()!=0){
            for (LessonScoreEntry score :scorelist) {
                LessonScoreDTO so = new LessonScoreDTO(score,null);
                if (score.getLessonid().equals(teacherScore.getLessonid().toString())) {
                    so.setAverage((float)avgScore);
                }
                lsdtos.add(so);
            }
        }
        if (lsdtos!=null && lsdtos.size()!=0) {
            Collections.sort(lsdtos);
            Collections.reverse(lsdtos);
            int i = 0;
            float j = 0;
            for (LessonScoreDTO dto : lsdtos) {

                if (j == 0) {
                    j = dto.getAverage();
                    dto.setSort(1+i);
                } else {
                    if (j==dto.getAverage()) {
                        dto.setSort(1+i);
                    } else {
                        i++;
                        dto.setSort(1+i);
                        j = dto.getAverage();
                    }
                }
                microLessonDao.updateLessonScoreSort(new ObjectId(dto.getLessonid()),matchid,dto.getSort());

            }

        }

    }

    public LessonScoreEntry getlessonscore(ObjectId lessonid,ObjectId matchid) {
        LessonScoreEntry lessonscoreentry = microLessonDao.selLessonScore(lessonid, matchid);
        return lessonscoreentry;
    }


    public void selmatchscore(ObjectId lessonid,ObjectId matchid,Map<String,Object> model) {

            List<TeacherScoreEntry> scorelist =microLessonDao.getLessonScore(lessonid);
            List<TeacherScoreDTO> dto = new ArrayList<TeacherScoreDTO>();
            if (scorelist!=null && scorelist.size()!=0) {
                for (TeacherScoreEntry score : scorelist) {
                    dto.add(new TeacherScoreDTO(score));
                }
                model.put("type",1);
                model.put("scorelist",dto);
            } else {
                model.put("type",4);
            }



    }

    public MicroMatchEntry selMicroMatchByUserid(String id,ObjectId lessonid) {
        return microLessonDao.selMicroMatchByuserid(new ObjectId(id),lessonid);

    }

    public TeacherScoreEntry selTeacherScore(String userid, ObjectId lessonId) {
        return microLessonDao.selTeacherScore(new ObjectId(userid),lessonId);
    }

    public void getlessonScoreList(String matchid,Model model) {
        MicroMatchEntry match =  microLessonDao.getMatchDetail(matchid, null);
        List<TypeLessonEntry> lessonlist = match.getLessonlist();
        List<ObjectId> lst = new ArrayList<ObjectId>();
        if (lessonlist!=null && lessonlist.size()!=0) {
            for (TypeLessonEntry lesson : lessonlist) {
                lst.add(new ObjectId(lesson.getValue()));
            }
        }
        List<LessonScoreDTO> lsdtos = new ArrayList<LessonScoreDTO>();
        List<LessonScoreEntry> scorelist = microLessonDao.selLessonScorelist(lst, new ObjectId(matchid));
        List<String> namelist = new ArrayList<String>();
        if (match.getScoreTypeList()!=null && match.getScoreTypeList().size()!=0) {
            for (ScoreTypeEntry scoreTypeEntry : match.getScoreTypeList()) {
                namelist.add(scoreTypeEntry.getName());
            }
        }
        if (scorelist!=null && scorelist.size()!=0){
            for (LessonScoreEntry score :scorelist) {
                lsdtos.add(new LessonScoreDTO(score,namelist));
            }
        }

        model.addAttribute("namelist",namelist);
        model.addAttribute("scorelist",lsdtos);
        model.addAttribute("sbegtime", DateTimeUtils.convert(match.getScorebegintime(), DateTimeUtils.DATE_YYYY_MM_DD_B));
        model.addAttribute("sendtime",DateTimeUtils.convert(match.getScoreendtime(), DateTimeUtils.DATE_YYYY_MM_DD_B));
        model.addAttribute("matchname",match.getMatchname());


    }

    /**
     * 更新比赛
     * @param microMatchEntry
     */

    public void updatematch(MicroMatchEntry microMatchEntry,MicroMatchDTO micromatch) {
        MicroMatchEntry match = microLessonDao.getMatchDetail(micromatch.getId(), null);
        List<ObjectId> users = match.getUserlist();
        List<TypeLessonEntry> lessons = match.getLessonlist();

        String[] matchs = micromatch.getMatchtypelist();
        List<IdValuePair> pairs = match.getMatchtypelist();
        String[] tps = new String[matchs.length];
        String[] curtps = new String[pairs.size()];
        int z=0;
        if (matchs!=null && matchs.length!=0){
            for (int i=0;i<matchs.length;i++) {
                if (!StringUtils.isEmpty(matchs[i])) {
                    String[] arg = matchs[i].split(";");
                    if (arg[0].equals("eid")) {
                        IdValuePair pair = new IdValuePair(new ObjectId(),arg[1]);
                        microLessonDao.updateMatchType(micromatch.getId(),pair);
                    } else {
                        tps[z]=arg[0];
                        z++;
                    }
                }
            }
        }
        int j=0;
        for(IdValuePair idv : pairs) {
            curtps[j] = idv.getId().toString();
            j++;
        }
        List<String> missType = StringUtil.compare(tps, curtps);

        if (missType!=null && missType.size()!=0) {
            for (String type : missType) {
                for(IdValuePair idv : pairs) {
                    if (idv.getId().toString().equals(type)) {
                        microLessonDao.missMatchType(micromatch.getId(),idv);
                    }
                }

            }
        }
        if (lessons!=null && lessons.size()!=0) {
            for (String type : missType) {
                for(TypeLessonEntry idv : lessons) {
                    if (idv.getId().toString().equals(type)) {
                        microLessonDao.missMatchLessonType(micromatch.getId(),idv);
                    }
                }

            }
        }

        List<String> userList = new ArrayList<String>();
        if (users!=null && users.size()!=0) {
            for(ObjectId uid : users) {
               userList.add(uid.toString());
            }
        }
        String[] userAry = new String[userList.size()];
        if (userList!=null && userList.size()!=0) {
            for (int i=0;i<userAry.length;i++) {
                userAry[i] = userList.get(i);
            }
        }
        List<String> missUser = StringUtil.compare(micromatch.getUserlist(), userAry);
        List<String> addUser = StringUtil.compare(userAry, micromatch.getUserlist());
        if (addUser!=null && addUser.size()!=0) {
            for (String userid : addUser) {
                if (!StringUtils.isEmpty(userid)) {
                    microLessonDao.updateMatchUser(micromatch.getId(), userid, 1);
                }
            }
        }
        if (missUser!=null && missUser.size()!=0) {
            for (String userid : missUser) {
                if (!StringUtils.isEmpty(userid)) {
                    microLessonDao.updateMatchUser(micromatch.getId(), userid, 2);
                }
            }
        }
        List<ScoreTypeEntry> scoretypes = new ArrayList<ScoreTypeEntry>();
        String[] scoreTypeList = micromatch.getScoretypelist();
        if (scoreTypeList!=null && scoreTypeList.length!=0) {
            for (String type : scoreTypeList) {
                if (!StringUtils.isEmpty(type)) {
                    ScoreTypeEntry pair = new ScoreTypeEntry(type.split(";")[0],Integer.valueOf(type.split(";")[1]));
                    scoretypes.add(pair);
                }
            }
        }
        microMatchEntry.setScoreTypeList(scoretypes);
        microLessonDao.updatematch(microMatchEntry, new ObjectId(micromatch.getId()));
    }

    public void downAllFile(String matchid, HttpServletRequest request, HttpServletResponse response) throws IOException, IllegalParamException{
        MicroMatchEntry match = microLessonDao.getMatchDetail(matchid, null);
        List<ObjectId> videIds =new ArrayList<ObjectId>();
        List<ObjectId> userIds =new ArrayList<ObjectId>();
        List<LessonEntry> lessonList = getMicroMatchLessonList("全部",match);
        String realPath=request.getServletContext().getRealPath("/WEB-INF/download");
        String fileName=realPath+"/"+getFileName(request, match.getMatchname())+".zip";
        for(LessonEntry entry:lessonList){
            if(!entry.getVideoIds().isEmpty()) {
                videIds.addAll(entry.getVideoIds());
            }
            userIds.add(entry.getUserId());
        }

        Map<ObjectId, UserEntry> uMaps= userdao.getUserEntryMap(userIds, Constant.FIELDS);
        List<ObjectId> schoolIds =new ArrayList<ObjectId>();
        for (UserEntry ue : uMaps.values()) {
            schoolIds.add(ue.getSchoolID());
        }
        Map<ObjectId, SchoolEntry> sMaps=schoolDao.getSchoolMap(schoolIds, Constant.FIELDS);

        Map<ObjectId, VideoEntry> vMaps=videoService.getVideoEntryMap(videIds, Constant.FIELDS);
        List<DownFileDTO> fileDtos=new ArrayList<DownFileDTO>();
        for(LessonEntry entry:lessonList){
            UserEntry userEntry=uMaps.get(entry.getUserId());

            SchoolEntry schoolEntry=sMaps.get(userEntry.getSchoolID());

            DateTimeUtils timeUtils=new DateTimeUtils();
            if(!entry.getVideoIds().isEmpty()){
                for(ObjectId videoId:entry.getVideoIds()){
                    VideoEntry ve = vMaps.get(videoId);
                    if(ve!=null) {
                        DownFileDTO dto = new DownFileDTO();
                        dto.setFileType(1);
                        dto.setUserName(userEntry.getUserName());
                        dto.setSchoolName(schoolEntry.getName());
                        dto.setFileName(ve.getName());
                        Long time = ve.getID().getTime();
                        String dateStr = timeUtils.getLongToStrTime(time);
                        dto.setUploadDate(dateStr);
                        String timeStr = timeUtils.getLongToStrTimeThree(time);
                        dto.setUploadTime(timeStr);
                        String filePath = "";
                        if (ve.getVideoSourceType() == VideoSourceType.USER_VIDEO.getType()) {
                            filePath = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_USER_VIDEO, ve.getBucketkey());
                        } else if (ve.getVideoSourceType() == VideoSourceType.VIDEO_CLOUD_CLASS.getType()) {
                            filePath = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_VIDEO, ve.getBucketkey());
                        } else if (ve.getVideoSourceType() == VideoSourceType.SWF_CLOUD_CLASS.getType()) {
                            filePath = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_FLASH, ve.getBucketkey());
                        }
                        dto.setFilePath(filePath);
                        fileDtos.add(dto);
                    }
                }
            }

            if(!entry.getLessonWareList().isEmpty())
            {
                for(LessonWare ware:entry.getLessonWareList()) {
                    DownFileDTO dto=new DownFileDTO();
                    dto.setFileType(2);
                    dto.setUserName(userEntry.getUserName());
                    dto.setSchoolName(schoolEntry.getName());
                    String warePath= ware.getPath();
                    String suffix=warePath.substring(warePath.lastIndexOf("."));
                    dto.setFileName(ware.getName()+suffix);
                    Long time=ware.getId().getTime();
                    String dateStr=timeUtils.getLongToStrTime(time);
                    dto.setUploadDate(dateStr);
                    String timeStr=timeUtils.getLongToStrTimeThree(time);
                    dto.setUploadTime(timeStr);

                    String path = "";
                    if(ware.getPath().contains("upload")){//兼容老的数据
                        path = ware.getPath();
                    }else {
                        path = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, ware.getPath());
                    }
                    dto.setFilePath(path);
                    fileDtos.add(dto);
                }
            }
        }

        execute(fileName,fileDtos,response);
    }

    /**
     * 获取导出文件的名称
     *
     * @param request
     * @param fileName
     * @return
     * @throws UnsupportedEncodingException
     */
    private String getFileName(HttpServletRequest request, String fileName) throws UnsupportedEncodingException {
        String agent = request.getHeader("User-Agent");
        if (agent != null && agent.toLowerCase().indexOf("firefox") >= 0) {
            fileName = new String(fileName.getBytes(Constant.UTF_8), Constant.ISO);
        } else {
            fileName = java.net.URLEncoder.encode(fileName, Constant.UTF_8);
        }
        return fileName;
    }

    /**
     * 生成的ZIP文件打包压缩
     */
    public String execute(String tmpFileName, List<DownFileDTO> fileDtos, HttpServletResponse response) {
        try {
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));

            response.setContentType("application/x-download");// 设置response内容的类型
            String zipName=tmpFileName.substring(tmpFileName.lastIndexOf("/") + 1);
            zipName=new String(zipName.getBytes("iso8859-1"),"UTF-8");
            response.setHeader("Content-disposition","attachment; filename="+new String(zipName.getBytes("gb2312"),"iso8859-1"));// 设置头部信息

            byte[] buffer = new byte[8192];
            int len = 0;
            for (DownFileDTO fileDto:fileDtos) {
                try {
                    String fileName=fileDto.getFileName();
                    String suffix=fileName.substring(fileName.lastIndexOf("."));
                    String prefix=fileName.substring(0,fileName.lastIndexOf("."));
                    String filePath="";
                    InputStream in =null;
                    if(fileDto.getFileType()==1){
                        filePath=fileDto.getFilePath();
                        InputStream subIn =QiniuFileUtils.downFileByUrl(filePath);
                        int i=0;
                        String filePathsStr="";
                        boolean flag = false;
                        while ((i = subIn.read()) != -1) {
                            String sbuStr = (char) i + "";
                            if ("\n".equals(sbuStr) && flag) {
                                filePathsStr += ",";
                                flag = false;
                            }
                            if ("h".equals(sbuStr)||"/".equals(sbuStr)|| flag) {
                                flag = true;
                                filePathsStr += sbuStr;
                            }
                        }
                        String[] filePaths = filePathsStr.split(",");
                        if(filePaths[0].contains("http://7sb")){
                            in = QiniuFileUtils.downFileByUrl(filePaths[0]);
                        }else{
                            in = QiniuFileUtils.downFileByUrl("http://7sbrbl.com1.z0.glb.clouddn.com/"+filePaths[0]);
                        }

                        if (filePaths.length > 1) {
                            for (int k = 1; k < filePaths.length; k++) {
                                String tempPath="";
                                if(filePaths[k].contains("http://7sb")){
                                    tempPath=filePaths[k];
                                }else{
                                    tempPath="http://7sbrbl.com1.z0.glb.clouddn.com/"+filePaths[k];
                                }
                                InputStream in2 = QiniuFileUtils.downFileByUrl(tempPath);
                                in = new SequenceInputStream(in, in2);
                            }
                        }
                    }else{
                        filePath=fileDto.getFilePath();
                        in = QiniuFileUtils.downFileByUrl(filePath);
                    }
                    if (in == null) {
                        continue;
                        //throw new IllegalParamException("未找到文件信息");
                    }

                    String targetPath=fileDto.getSchoolName()+File.separator+fileDto.getUserName()+"_"+fileDto.getUploadDate()+File.separator
                            +prefix+"_"+fileDto.getUploadTime()+suffix;

                    ZipEntry zipEntry=new ZipEntry(targetPath);
                    out.putNextEntry(zipEntry);
                    //设置压缩文件内的字符编码，不然会变成乱码
                    //out.setEncoding("UTF-8");
                    logger.debug("文件"+ targetPath);
                    BufferedInputStream bis = new BufferedInputStream(in);
                    while ((len = bis.read(buffer))!=-1) {
                        out.write(buffer, 0, len);
                    }
                    bis.close();
                    out.closeEntry();
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("文件下载出错", e);
                }

            }
            out.close();
            //this.downFile(response, tmpFileName);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("文件下载出错", e);
        }
        return null;
    }

    /**
     * 文件下载
     * @param response
     * @param path
     */
/*    private void downFile(HttpServletResponse response, String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                InputStream ins = new FileInputStream(path);
                BufferedInputStream bins = new BufferedInputStream(ins);// 放到缓冲流里面
                OutputStream outs = response.getOutputStream();// 获取文件输出IO流
                BufferedOutputStream bouts = new BufferedOutputStream(outs);
                response.setContentType("application/x-download");// 设置response内容的类型

                //String filename=URLEncoder.encode(path.substring(path.lastIndexOf("/")+1), "UTF-8");
                String filename=path.substring(path.lastIndexOf("/") + 1);
                filename=new String(filename.getBytes("iso8859-1"),"UTF-8");
                //response.setHeader("Content-disposition","attachment;filename=" + filename);// 设置头部信息
                response.setHeader("Content-disposition","attachment; filename="+new String(filename.getBytes("gb2312"),"iso8859-1"));// 设置头部信息

                int bytesRead = 0;
                byte[] buffer = new byte[2014];
                // 开始向网络传输文件流
                while ((bytesRead = bins.read(buffer, 0, buffer.length)) != -1) {
                    bouts.write(buffer, 0, bytesRead);
                }
                bouts.flush();// 这里一定要调用flush()方法
                ins.close();
                bins.close();
                outs.close();
                bouts.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("文件下载出错", e);
        }
    }*/

    public String m3u8DownLoad(String fileUrl, HttpServletRequest request) throws IOException, IllegalParamException{
        String realPath=request.getServletContext().getRealPath("/WEB-INF/download");
        String filePath = fileUrl.split("#")[0];
        String fileName=filePath.substring(filePath.lastIndexOf("/")+1);
        String suffix=fileName.substring(fileName.lastIndexOf("."));
        String name = fileUrl.split("#")[1];
        //String prefix=fileName.substring(0,fileName.lastIndexOf("."));
        if(".m3u8".equals(suffix)) {
            InputStream in = null;
            InputStream subIn = QiniuFileUtils.downFileByUrl(filePath);
            int i = 0;
            String filePathsStr = "";
            boolean flag = false;
            while ((i = subIn.read()) != -1) {
                String sbuStr = (char) i + "";
                if ("\n".equals(sbuStr) && flag) {
                    filePathsStr += ",";
                    flag = false;
                }
                if ("h".equals(sbuStr)||"/".equals(sbuStr)|| flag) {
                    flag = true;
                    filePathsStr += sbuStr;
                }
            }
            String[] filePaths = filePathsStr.split(",");
            if(filePaths[0].contains("http://7sbrbl.com1.z0.glb.clouddn.com")){
                in = QiniuFileUtils.downFileByUrl(filePaths[0]);
            }else{
                in = QiniuFileUtils.downFileByUrl("http://7sbrbl.com1.z0.glb.clouddn.com/"+filePaths[0]);
            }

            if (filePaths.length > 1) {
                for (int k = 1; k < filePaths.length; k++) {
                    String tempPath="";
                    if(filePaths[k].contains("http://7sbrbl.com1.z0.glb.clouddn.com")){
                        tempPath=filePaths[k];
                    }else{
                        tempPath="http://7sbrbl.com1.z0.glb.clouddn.com/"+filePaths[k];
                    }
                    //tempPath=filePaths[k];
                    InputStream in2 = QiniuFileUtils.downFileByUrl(tempPath);
                    in = new SequenceInputStream(in, in2);
                }
            }

            String path=realPath+"/"+name+".mp4";

            File file=new File(path);
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer, 0, 1024)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            in.close();
            String url=request.getRequestURL().toString();
            url=url.substring(0,url.lastIndexOf(request.getRequestURI()));
            path=path.substring(path.lastIndexOf("download"));
            return url+"/"+path;
        }
        return "";
    }
}
