package com.fulaan.quality.controller;

import com.fulaan.base.controller.BaseController;
import com.fulaan.myschool.controller.GradeView;
import com.fulaan.quality.dto.TeacherCheckDTO;
import com.fulaan.quality.dto.TeacherPlainDTO;
import com.fulaan.quality.service.TeachService;
import com.fulaan.school.service.SchoolService;
import com.fulaan.school.service.TeacherClassSubjectService;
import com.fulaan.teachermanage.dto.TeacherInfoDTO;
import com.fulaan.user.service.UserService;
import com.fulaan.zouban.service.CommonService;
import com.mongodb.BasicDBObject;
import com.pojo.app.IdNameValuePairDTO;
import com.pojo.app.UserGroupInfo;
import com.pojo.docflow.IdUserFilePairDTO;
import com.pojo.school.SchoolEntry;
import com.pojo.school.Subject;
import com.pojo.school.TeacherClassSubjectEntry;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by wang_xinxin on 2016/11/10.
 */

@Controller
@RequestMapping("/teach")
public class TeachController extends BaseController{
    private static final Logger logger = Logger.getLogger(TeachController.class);

    @Autowired
    private TeachService teachService;

    private TeacherClassSubjectService tcsService =new TeacherClassSubjectService();

    private SchoolService schoolService =new SchoolService();

    private UserService userService =new UserService();

    /**
     * 教师计划页面
     * @return
     */
    @RequestMapping("/plan")
    public String indexPage(Model model){
        List<String> termList = getTermList();
        model.addAttribute("termList",termList);
        model.addAttribute("userId","");
        return "/teacherevaluation/teaPlan";
    }

    /**
     * 教师计划页面
     * @return
     */
    @RequestMapping("/planUser")
    public String indexUserPage(Model model,String userId,String term){
        List<String> termList = getTermList();
        model.addAttribute("termList",termList);
        model.addAttribute("userId",userId);
        model.addAttribute("term",term);
        return "/teacherevaluation/teaPlan";
    }

    /**
     * 教师检查页面
     * @return
     */
    @RequestMapping("/check")
    public String checkPage(Model model){
        List<String> termList = getTermList();
        model.addAttribute("termList",termList);
        return "/teacherevaluation/teaCheck";
    }

    /**
     * 添加教学计划
     * @param teacherPlainDTO
     * @return
     */
    @RequestMapping("/addUdpTeachPlan")
    @ResponseBody
    public Map addUdpTeachPlan(TeacherPlainDTO teacherPlainDTO) {
        Map map = new HashMap();
        try {
            teacherPlainDTO.setTeacherId(getSessionValue().getId());
            teacherPlainDTO.setSchoolId(getSessionValue().getSchoolId());
            List<IdUserFilePairDTO> docList = generalFileList(teacherPlainDTO.getDocNames(), teacherPlainDTO.getDocAddress(), "", "");
            teacherPlainDTO.setDocList(docList);
            teachService.addUdpTeachPlan(teacherPlainDTO);
            map.put("code",200);
            if (teacherPlainDTO.getType()==0) {
                map.put("message","添加成功！");
            } else {
                map.put("message","编辑成功！");
            }

        }catch (Exception e) {
            map.put("code",500);
            if (teacherPlainDTO.getType()==0) {
                map.put("message", "添加失败！");
            } else {
                map.put("message","编辑失败！");
            }
        }
        return map;
    }

    @RequestMapping("/selTeachPlanList")
    @ResponseBody
    public Map selTeachPlanList(String term,String planName,String userId) {
        Map map = new HashMap();
        ObjectId uid = null;
        if (StringUtils.isEmpty(userId)) {
            uid = getUserId();
        } else {
            uid = new ObjectId(userId);
        }
        map.put("rows",teachService.selTeachPlanList(term, planName, uid));
        map.put("code",200);
        return map;
    }

    /**
     * 查询单条计划
     * @param id
     * @return
     */
    @RequestMapping("/selSinglePlan")
    @ResponseBody
    public Map selSinglePlan(@RequestParam String id) {
        Map map = new HashMap();
        map.put("rows",teachService.selSinglePlan(new ObjectId(id)));
        map.put("code",200);
        return map;
    }

    /**
     * 删除计划
     * @param id
     * @return
     */
    @RequestMapping("/delPlan")
    @ResponseBody
    public Map delPlan(@RequestParam String id) {
        Map map = new HashMap();
        try {
            teachService.delPlan(new ObjectId(id),getUserId());
            map.put("code",200);
            map.put("message","删除教学计划成功！");
        }catch (Exception e) {
            map.put("code",500);
            map.put("message","删除教学计划失败！");
        }
        return map;
    }


    /**
     * 生成IdUserFilePairDTO列表
     *
     * @param docNames
     * @param docValues
     * @return
     */
    public List<IdUserFilePairDTO> generalFileList(String docNames, String docValues, String docIds, String userIds) {
        List<IdUserFilePairDTO> docList = new ArrayList<IdUserFilePairDTO>();
        ObjectId userId = getUserId();
        if (StringUtils.isNotBlank(docNames)) {
            String[] fileIdArr = new String[docNames.length()];
            for (int i = 0; i < fileIdArr.length; i++) {
                fileIdArr[i] = "-1";
            }
            String[] fileUserIdArr = new String[docNames.length()];
            if (StringUtils.isNotBlank(docIds)) {
                fileIdArr = StringUtils.split(docIds, Constant.COMMA);
                fileUserIdArr = StringUtils.split(userIds, Constant.COMMA);
            }
            String[] filenameArr = StringUtils.split(docNames, Constant.COMMA);
            String[] fileValueArr = StringUtils.split(docValues, Constant.COMMA);
            for (int i = 0; i < filenameArr.length; i++) {
                try {
                    String str = filenameArr[i];
                    String fileId = fileIdArr[i];
                    if (fileId.equals("-1")) {
                        IdUserFilePairDTO docPair = new IdUserFilePairDTO(new ObjectId().toString());
                        docPair.setUserId(userId.toString());
                        docPair.setValue(fileValueArr[i]);
                        docPair.setName(str);
                        docList.add(docPair);
                    } else {
                        IdUserFilePairDTO docPair = new IdUserFilePairDTO(fileId);
                        docPair.setUserId(fileUserIdArr[i]);
                        docPair.setValue(fileValueArr[i]);
                        docPair.setName(str);
                        docList.add(docPair);
                    }
                } catch (Exception ex) {
                    logger.error("", ex);
                }
            }
        }
        return docList;
    }


    /**
     * 获取学年列表
     * @return
     */
    public List<String> getTermList(){
        List<String> termList = new ArrayList<String>();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int startYear = 2015;
        for (int i = startYear; i <= year; i++) {
            if (year!=i) {
                termList.add(i + "-" + (i + 1) + "学年第一学期");
                termList.add(i + "-" + (i + 1) + "学年第二学期");
            } else {
                if (month < 8 && month >= 2) {
                    termList.add(i + "-" + (i + 1) + "学年第一学期");
                    termList.add(i + "-" + (i + 1) + "学年第二学期");
                } else if (month >= 8) {
                    termList.add(i + "-" + (i + 1) + "学年第一学期");
                } else {
                    termList.add(i + "-" + (i + 1) + "学年第一学期");
                }
            }

        }
        Collections.reverse(termList);
        return termList;
    }


    /**
     *
     * @param userName
     * @return
     * @throws IllegalParamException
     */
    @RequestMapping("/subject/users")
    @ResponseBody
    public Map subjectUsers(String userName,int page,int pageSize) throws IllegalParamException
    {
        Map map = new HashMap();
        List<TeacherCheckDTO> teacherCheckDTOs = new ArrayList<TeacherCheckDTO>();
        SchoolEntry se =schoolService.getSchoolEntry(getSchoolId(), new BasicDBObject("subs",1));
        Map<ObjectId,UserGroupInfo<IdNameValuePairDTO>> userGroupMap=new HashMap<ObjectId, UserGroupInfo<IdNameValuePairDTO>>();
        for(Subject sub:se.getSubjects())
        {
            List<ObjectId> userIds = sub.getUserIds();
            if (UserRole.isHeadmaster(getSessionValue().getUserRole()) || (UserRole.isLeaderOfSubject(getSessionValue().getUserRole())&&userIds!=null && userIds.size()!=0 && userIds.contains(getUserId()))) {
                IdNameValuePairDTO dto =new IdNameValuePairDTO();
                dto.setId(sub.getSubjectId());
                dto.setValue(sub.getName());
                UserGroupInfo<IdNameValuePairDTO> info =new UserGroupInfo<IdNameValuePairDTO>(dto, new ArrayList<IdNameValuePairDTO>());
                userGroupMap.put(sub.getSubjectId(), info);
            }
        }
        List<TeacherClassSubjectEntry> tcsList=tcsService.findTeacherClassSubjectBySubjectIds(userGroupMap.keySet(),new BasicDBObject("ti",1).append("sui", 1));
        //teacher->subject
        Map<ObjectId,ObjectId> teacherSubjectMap =new HashMap<ObjectId, ObjectId>();

        for(TeacherClassSubjectEntry tcs:tcsList)
        {
            teacherSubjectMap.put(tcs.getTeacherId(), tcs.getSubjectInfo().getId());
        }
        Map<ObjectId, UserEntry> userMap=userService.getUserEntryMapByUserIds(teacherSubjectMap.keySet(), userName, new BasicDBObject("nm", 1));
        for(Map.Entry<ObjectId, UserEntry> userEntry:userMap.entrySet())
        {
            IdNameValuePairDTO dto =new IdNameValuePairDTO();
            dto.setId(userEntry.getKey());
            dto.setValue(userEntry.getValue().getUserName());
            ObjectId subjectId=teacherSubjectMap.get(userEntry.getKey());
            if(null!=subjectId)
            {
                UserGroupInfo<IdNameValuePairDTO> info =userGroupMap.get(subjectId);

                if(null!=info)
                {
                    info.getList().add(dto);
                }
            }
        }
        List<UserGroupInfo<IdNameValuePairDTO>> userGroupInfos = new ArrayList<UserGroupInfo<IdNameValuePairDTO>>(userGroupMap.values());
        if (userGroupInfos!=null && userGroupInfos.size()!=0) {
            for (UserGroupInfo<IdNameValuePairDTO> list : userGroupInfos) {
                if (list.getList()!=null && list.getList().size()!=0) {
                    for (IdNameValuePairDTO dto : list.getList()) {
                        TeacherCheckDTO teacherCheckDTO = new TeacherCheckDTO();
                        teacherCheckDTO.setSubjectId(list.getT().getId().toString());
                        teacherCheckDTO.setSubjectName(list.getT().getValue().toString());
                        teacherCheckDTO.setUserId(dto.getId().toString());
                        teacherCheckDTO.setUserName(dto.getValue().toString());
                        teacherCheckDTOs.add(teacherCheckDTO);
                    }
                }
            }
        }
        map.put("page", page);
        map.put("pageSize", pageSize);
        List<TeacherCheckDTO> resultList=listImitatePage(teacherCheckDTOs,page,pageSize);
        map.put("rows",resultList);
        map.put("total", teacherCheckDTOs.size());
        return map;
    }

    /**
     * 模拟对list分页查询
     * @param list
     * @param page
     * @param pageSize
     * @return
     */
    private List<TeacherCheckDTO> listImitatePage(List<TeacherCheckDTO> list,int page,int pageSize) {
        int totalCount =list.size();
        int pageCount=0;
        int m=totalCount%pageSize;
        if(m>0){
            pageCount=totalCount/pageSize+1;
        } else {
            pageCount=totalCount/pageSize;
        }
        List<TeacherCheckDTO> subList=new ArrayList<TeacherCheckDTO>();
        if(list!=null&&list.size()>0) {
            if (m == 0) {
                subList = list.subList((page - 1) * pageSize, pageSize * (page));
            } else {
                if (page == pageCount) {
                    subList = list.subList((page - 1) * pageSize, totalCount);
                } else {
                    subList = list.subList((page - 1) * pageSize, pageSize * (page));
                }
            }
        }
        return subList;
    }

    @RequestMapping("/selSingleTeacherCheck")
    @ResponseBody
    public Map selSingleTeacherCheck(String term,String subjectId,String userId) {
        Map map = new HashMap();
        teachService.selSingleTeacherCheck(term, subjectId, userId, map);
        return map;
    }

    /**
     * 更新评分项的值
     * @param id
     * @param value
     * @param type
     * @param projectName
     * @return
     */
    @RequestMapping("/updateProjectValue")
    @ResponseBody
    public Map updateProjectValue(String id,String value,int type,String projectName) {
        Map map = new HashMap();
        try {
            teachService.updateProjectValue(new ObjectId(id),value,type,projectName);
            map.put("code",200);
            map.put("message","更新评分项成功！");
        } catch (Exception e) {
            map.put("code",500);
            map.put("message","更新评分项失败！");
        }
        return map;
    }
}
