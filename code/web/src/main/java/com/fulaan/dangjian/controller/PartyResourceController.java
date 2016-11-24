package com.fulaan.dangjian.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.base.service.DirService;
import com.fulaan.dangjian.service.PartyResourceService;
import com.fulaan.utils.QiniuFileUtils;
import com.pojo.app.IdNameValuePair;
import com.pojo.app.IdNameValuePairDTO;
import com.pojo.dangjian.PartyResourceDTO;
import com.pojo.dangjian.PartyUserDTO;
import com.pojo.lesson.DirEntry;
import com.pojo.lesson.DirType;
import com.pojo.teachermanage.ResumeEntry;
import com.pojo.user.UserRole;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.apache.commons.io.FilenameUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * Created by fl on 2016/3/23.
 */
@Controller
@RequestMapping("/party")
public class PartyResourceController extends BaseController{

    @Autowired
    private PartyResourceService partyResourceService;
    @Autowired
    private DirService dirService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(){
        return "dangjian/partyresource";
    }

    @RequestMapping(value = "/partyusers", method = RequestMethod.GET)
    public String users(){
        return "dangjian/partyusers";
    }

    @UserRoles(value = {UserRole.TEACHER, UserRole.HEADMASTER, UserRole.ADMIN})
    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public String upload(Map<String, Object> model){
        PartyUserDTO partyUserDTO = partyResourceService.getPartyUserDTO(getUserId(), getSchoolId());
        model.put("partyManger", partyUserDTO.getIsPartySecretary() == 1);
        model.put("partyMember", partyUserDTO.getIsPartyMember() == 1);
        model.put("teacher", UserRole.isTeacher(getSessionValue().getUserRole()));
        return "dangjian/upload";
    }

    @UserRoles(value = {UserRole.TEACHER, UserRole.HEADMASTER, UserRole.ADMIN})
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable @ObjectIdType ObjectId id, Map<String, Object> model){
        PartyUserDTO partyUserDTO = partyResourceService.getPartyUserDTO(getUserId(), getSchoolId());
        model.put("id", id);
        model.put("partyManger", partyUserDTO.getIsPartySecretary() == 1);
        model.put("partyMember", partyUserDTO.getIsPartyMember() == 1);
        model.put("teacher", UserRole.isTeacher(getSessionValue().getUserRole()));
        return "dangjian/upload";
    }

    @RequestMapping(value = "/resources", method = RequestMethod.POST)
    @ResponseBody
    public RespObj add(@RequestBody PartyResourceDTO partyResourceDTO){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        if(partyResourceDTO.getId() == null){
            partyResourceDTO.setTerm(getCurrentTerm());
            partyResourceDTO.setUserId(getUserId().toString());
            partyResourceDTO.setId("");
        }
        partyResourceService.addPartyResource(partyResourceDTO);
        respObj.setMessage("添加成功");
        return respObj;
    }

    private String getCurrentTerm() {
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

    @UserRoles(value = {UserRole.TEACHER, UserRole.HEADMASTER, UserRole.ADMIN})
    @RequestMapping(value = "/resources", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getResources(@RequestParam(required = false) @ObjectIdType ObjectId directoryId,
                                            DirType dirType, String term, int page, int pageSize){
        Map<String, Object> model = new HashMap<String, Object>();
        int count = 0;
        List<PartyResourceDTO> resourceDTOs = new ArrayList<PartyResourceDTO>();
        PartyUserDTO partyUserDTO = partyResourceService.getPartyUserDTO(getUserId(), getSchoolId());
        Map<String, Boolean> map = checkUserRole(dirType, partyUserDTO);
        if(map.get("flag")) {
            resourceDTOs = partyResourceService.getResources(getDirectoryIds(directoryId, dirType), term, page, pageSize, getUserId().toString(), map.get("isAll"));
            if (partyUserDTO.getIsPartySecretary() == 1) {
                for (PartyResourceDTO resourceDTO : resourceDTOs) {
                    resourceDTO.setIsMine(1);
                }
            }
            ObjectId userId = map.get("isAll") ? null : getUserId();
            count = partyResourceService.countResources(getDirectoryIds(directoryId, dirType), term, userId);
        }
        model.put("dtos", resourceDTOs);
        model.put("count", count);
        model.put("page", page);
        model.put("pageSize", pageSize);
        model.put("flag", map.get("flag"));
        return model;
    }

    private List<ObjectId> getDirectoryIds(ObjectId directoryId, DirType dirType){
        List<ObjectId> directoryIds = new ArrayList<ObjectId>();
        if(directoryId == null){
            //todo 查找该类型下所有目录
            List<ObjectId> owners = new ArrayList<ObjectId>();
            owners.add(new ObjectId(getSessionValue().getSchoolId()));
            List<DirEntry> dirEntries = dirService.getDirEntryList(owners, Constant.FIELDS, dirType.getType());
            List<ObjectId> ids = MongoUtils.getFieldObjectIDs(dirEntries);
            directoryIds.addAll(ids);
        } else {
            //todo 查找本目录及子目录
            Set<ObjectId> ids =dirService.getSelfAndChildDirs(new ObjectId(getSessionValue().getSchoolId()), directoryId);
            directoryIds.addAll(ids);
        }
        return directoryIds;
    }

    private Map<String, Boolean> checkUserRole(DirType dirType, PartyUserDTO partyUserDTO){
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        Boolean flag = false;//是否拥有本栏目权限
        Boolean isAll = true;//是否拥有本栏目所有资源的权限
        int type = 0;
        switch (dirType.getType()){
            case 8:
            case 10:
            case 13:
            case 14:
                type = 1;//所有老师
                break;
            case 11:
            case 12:
            case 15:
                type = 2;//党员
                break;
            case 9:
            case 16:
                type = 3;//党支书
                break;
            case 7:
                type = 4;//党员和中心组
                break;
            default:
                type = 0;
                break;
        }

        if(type == 1){
            flag = true;
        } else if(type == 2){
            flag = partyUserDTO.getIsPartyMember()==1;
        } else if(type == 3){
            flag = true;
            isAll = partyUserDTO.getIsPartySecretary()==1;
        } else if(type == 4){
            flag = partyUserDTO.getIsPartyMember()==1 || partyUserDTO.getIsCenterMember()==1;
        }
        map.put("isAll", isAll);
        map.put("flag", flag);
        return map;
    }



    @RequestMapping(value = "/resources/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public RespObj delete(@PathVariable @ObjectIdType ObjectId id){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            partyResourceService.deletePartyResource(id);
            respObj.setCode("200");
            respObj.setMessage("删除成功");
        } catch (Exception e){
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }

    @RequestMapping(value = "/resources/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public RespObj update(@PathVariable @ObjectIdType ObjectId id, @RequestBody PartyResourceDTO partyResourceDTO){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            partyResourceService.editPartyResource(partyResourceDTO);
            respObj.setCode("200");
            respObj.setMessage("编辑成功");
        } catch (Exception e){
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }

    @RequestMapping(value = "/resources/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getResource(@PathVariable @ObjectIdType ObjectId id){
        Map<String, Object> model = new HashMap<String, Object>();
        try {
            PartyResourceDTO dto = partyResourceService.getResourceById(id);
            DirEntry dirEntry = dirService.getDirEntry(new ObjectId(dto.getDirectory()), new ObjectId(getSessionValue().getSchoolId()));
            dto.setDirType(dirEntry.getType());
            model.put("dto", dto);
            model.put("code", "200");
            model.put("msg", "成功");
        } catch (Exception e){
            model.put("code", "500");
            model.put("msg", e.getMessage());
        }
        return model;
    }

    @RequestMapping(value = "/resources/{id}/{fileId}", method = RequestMethod.GET)
    public void downLoadFile(@PathVariable @ObjectIdType ObjectId id, @PathVariable String fileId, HttpServletResponse response){
        try {
            PartyResourceDTO dto = partyResourceService.getResourceById(id);
            List<IdNameValuePairDTO> files = dto.getSrcs();
            String fileName = "";
            for(IdNameValuePairDTO file : files){
                if(file.getId().toString().equals(fileId)){
                    fileName = file.getName();
                }
            }

            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=" + new String(fileName.getBytes("utf-8"), "ISO8859-1" ));

            String fileKey = fileId + Constant.POINT+ FilenameUtils.getExtension(fileName);
            String qiniuPath = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, fileKey);
            try {
                InputStream inputStream = QiniuFileUtils.downFileByUrl(qiniuPath);
                OutputStream os = response.getOutputStream();
                byte[] b = new byte[2048];
                int length;
                while ((length = inputStream.read(b)) > 0) {
                    os.write(b, 0, length);
                }
                os.close();
                inputStream.close();
            }  catch (IOException ex) {
            }

        } catch (Exception e){

        }
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @ResponseBody
    public RespObj getPartyUsers(int page, int pageSize){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);

        Map<String, Object> model = new HashMap<String, Object>();
        List<PartyUserDTO> partyUserDTOs = partyResourceService.getPartyUserDTOs(getSchoolId(), page, pageSize);
        model.put("dtos", partyUserDTOs);
        model.put("count", partyResourceService.countTeacher_Manager_HeaderMaster(getSchoolId()));
        model.put("page", page);
        model.put("pageSize", pageSize);
        respObj.setMessage(model);
        return respObj;
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.POST)
    @ResponseBody
    public RespObj updatePartyUser(int isPartyMember, int isCenterMember, int isPartySecretary, @PathVariable(value = "id") @ObjectIdType ObjectId id){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        partyResourceService.updatePartyUser(isPartyMember, isCenterMember, isPartySecretary, id);
        return respObj;
    }



    //
}
