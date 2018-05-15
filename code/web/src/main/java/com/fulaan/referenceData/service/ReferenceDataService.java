package com.fulaan.referenceData.service;

import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.CommunityDetailDao;
import com.db.fcommunity.MemberDao;
import com.db.referenceData.ReferenceDataDao;
import com.db.user.NewVersionBindRelationDao;
import com.db.user.UserDao;
import com.db.wrongquestion.SubjectClassDao;
import com.fulaan.community.dto.CommunityDTO;
import com.fulaan.instantmessage.service.RedDotService;
import com.fulaan.integral.service.IntegralSufferService;
import com.fulaan.newVersionBind.service.NewVersionBindService;
import com.fulaan.picturetext.runnable.PictureRunNable;
import com.fulaan.picturetext.service.CheckTextAndPicture;
import com.fulaan.pojo.Attachement;
import com.fulaan.referenceData.dto.ReferenceDataDTO;
import com.fulaan.service.CommunityService;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.pojo.fcommunity.*;
import com.pojo.instantmessage.ApplyTypeEn;
import com.pojo.integral.IntegralType;
import com.pojo.referenceData.ReferenceDataEntry;
import com.pojo.user.UserEntry;
import com.pojo.wrongquestion.SubjectClassEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by James on 2018-04-23.
 */

@Service
public class ReferenceDataService {
    @Autowired
    private RedDotService redDotService;
    @Autowired
    private IntegralSufferService integralSufferService;
    @Autowired
    private CommunityService communityService;

    private NewVersionBindService newVersionBindService = new NewVersionBindService();


    private ReferenceDataDao referenceDataDao = new ReferenceDataDao();

    private SubjectClassDao subjectClassDao = new SubjectClassDao();

    private UserDao userDao = new UserDao();

    private CommunityDetailDao communityDetailDao = new CommunityDetailDao();

    private CommunityDao  communityDao = new CommunityDao();

    private MemberDao  memberDao = new MemberDao();

    private NewVersionBindRelationDao newVersionBindRelationDao = new NewVersionBindRelationDao();



    public String  addReferenceDataEntry(ReferenceDataDTO dto)throws Exception{

        //文本检测
        Map<String,Object> flag = CheckTextAndPicture.checkText(dto.getContent() + "-----------" + dto.getTitle(), new ObjectId(dto.getUserId()));
        String f = (String)flag.get("bl");
        if(f.equals("1")){
            return (String)flag.get("text");
        }
        if(dto.getCommunityIds()==null){
            return "0";
        }
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        Attachement attachement = dto.getAttachements().get(0);
        if(attachement!=null){
            String flnm = attachement.getUrl();
            if(flnm!=null){
                dto.setSuffix(getType(flnm.substring(flnm.lastIndexOf(".")+1)));
            }
        }
       // dto.setType(checkType(dto.getSuffix()));
        dto.setContent("附件");
        for(String str : dto.getCommunityIds()){
            dto.setCommunityId(str);
            ReferenceDataEntry entry = dto.buildAddEntry();
            objectIdList.add(new ObjectId(str));
            String oid = referenceDataDao.addEntry(entry);
            //向下兼容
            communityDetailDao.save(getOldEntry(entry));
            //发送通知
            PictureRunNable.addTongzhi(str,dto.getUserId().toString(),4);
            /*//图片检测
        List<Attachement> alist = dto.getAttachements();
        if(dto.getType()==4){
            if(alist != null && alist.size()>0){
                for(Attachement entry5 : alist){
                    PictureRunNable.send(oid, dto.getUserId(), PictureType.operationImage.getType(), 1, entry5.getUrl());
                }
            }
        }*/
        }
        //红点
        redDotService.addEntryList(objectIdList,new ObjectId(dto.getUserId()), ApplyTypeEn.text.getType(),4);
        //积分
        int score = integralSufferService.addIntegral(new ObjectId(dto.getUserId()), IntegralType.book,4,1);
        return score+"";
    }



    public Map<String,Object> getReferenceData(String keyword,int page,int pageSize,int type,ObjectId userId){
        //清除红点
        redDotService.cleanOtherResult(userId, 4);
        Map<String,Object> map = new HashMap<String, Object>();
        List<CommunityDTO> communityDTOList = communityService.getCommunitys2(userId, 1, 100);
        List<ObjectId>  objectIdList = new ArrayList<ObjectId>();
        Map<String,CommunityDTO> map3 = new HashMap<String, CommunityDTO>();
        if(communityDTOList.size() >0){
            for(CommunityDTO dto : communityDTOList){
                if(!dto.getName().equals("复兰社区")){
                    objectIdList.add(new ObjectId(dto.getId()));
                    map3.put(dto.getId(),dto);
                }
            }
        }
        List<ReferenceDataEntry> entries = referenceDataDao.selectDateListPage(keyword, page, pageSize, type,objectIdList);
        int count = referenceDataDao.countDateListPage(keyword, type,objectIdList);
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        userIds.add(userId);
        List<ObjectId> subjectIds = new ArrayList<ObjectId>();
        for(ReferenceDataEntry entry: entries){
            userIds.add(entry.getUserId());
            subjectIds.add(entry.getSubjectId());
        }
        Map<ObjectId, SubjectClassEntry> subjectClassEntryMap = subjectClassDao.getSubjectClassEntryMap(subjectIds);
        Map<ObjectId, UserEntry> mainUserEntryMap = userDao.getUserEntryMap(userIds, Constant.FIELDS);
        List<ReferenceDataDTO>  dtos = new ArrayList<ReferenceDataDTO>();
        List<ObjectId> communityIds = new ArrayList<ObjectId>();
        List<ObjectId> objectIds = new ArrayList<ObjectId>();
        for(ReferenceDataEntry entry: entries){
            ReferenceDataDTO dataDTO = new ReferenceDataDTO(entry);
            CommunityDTO communityDTO = map3.get(dataDTO.getCommunityId());
            if(communityDTO!=null){
                String str = communityDTO.getName();
                if(str!=null && str.equals("复兰大学")){
                    str = "复兰教育";
                }
                dataDTO.setCommunityName(str);
            }
            SubjectClassEntry  subjectClassEntry = subjectClassEntryMap.get(entry.getSubjectId());
            if(subjectClassEntry!=null){
                dataDTO.setSubjectName(subjectClassEntry.getName());
            }
            UserEntry userEntry = mainUserEntryMap.get(entry.getUserId());
            if(userEntry!=null){
                dataDTO.setUserName(userEntry.getNickName());
            }
            dataDTO.setLongTime(entry.getCreateTime());
            dtos.add(dataDTO);
            communityIds.add(entry.getCommunityId());
            objectIds.add(entry.getUserId());
        }
        List<ObjectId> groupIdList = new ArrayList<ObjectId>();
        Map<ObjectId, ObjectId> groupIds = communityDao.getGroupIds(communityIds);
        for (Map.Entry<ObjectId, ObjectId> item : groupIds.entrySet()) {
            groupIdList.add(item.getValue());
        }
        objectIds.add(userId);
        Map<String, MemberEntry> memberMap = memberDao.getGroupNick(groupIdList, objectIds);
        for(ReferenceDataDTO dataDTO2 :dtos){
            ObjectId groupId = groupIds.get(new ObjectId(dataDTO2.getCommunityId()));
            MemberEntry entry1 = memberMap.get(groupId + "$" + new ObjectId(dataDTO2.getUserId()));
            MemberEntry entry5 = memberMap.get(groupId + "$" + userId);
            if(entry1!=null && entry5!=null){
                if(entry5.getRole() > entry1.getRole()){
                    dataDTO2.setOperation(1);//权限压制，可删除
                }else if(dataDTO2.getUserId().equals(userId.toString())){
                    dataDTO2.setOperation(1);//发布人相同，可删除
                }else{
                    dataDTO2.setOperation(0);//不可删除
                }
            }else{
                dataDTO2.setOperation(0);//不可删除
            }
            if(entry5!=null && entry1==null){//发送人已出去
                if(entry5.getRole()==1|| (entry5.getRole()==2)){
                    dataDTO2.setOperation(1);//可删除
                }
            }
        }
        map.put("list",dtos);
        map.put("count",count);
        map.put("page",page);
        map.put("pageSize",pageSize);
        return map;
    }


    public Map<String,Object> getStudentReferenceData(String keyword,int page,int pageSize,int type,ObjectId studentId){
        Map<String,Object> map = new HashMap<String, Object>();
        List<ObjectId> objectIdList = newVersionBindService.getCommunityIdsByUserId(studentId);
        if(objectIdList.size()==0){
            map.put("list",new ArrayList<ReferenceDataDTO>());
            map.put("count",0);
            map.put("page",page);
            map.put("pageSize",pageSize);
            return map;
        }
        List<ReferenceDataEntry> entries = referenceDataDao.selectDateListPage(keyword, page, pageSize, type,objectIdList);
        int count = referenceDataDao.countDateListPage(keyword, type,objectIdList);
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        List<ObjectId> subjectIds = new ArrayList<ObjectId>();
        for(ReferenceDataEntry entry: entries){
            userIds.add(entry.getUserId());
            subjectIds.add(entry.getSubjectId());
        }
        Map<ObjectId, SubjectClassEntry> subjectClassEntryMap = subjectClassDao.getSubjectClassEntryMap(subjectIds);
        Map<ObjectId, UserEntry> mainUserEntryMap = userDao.getUserEntryMap(userIds, Constant.FIELDS);
        Map<ObjectId, CommunityEntry> map3 = communityDao.findMapInfo(objectIdList);
        List<ReferenceDataDTO>  dtos = new ArrayList<ReferenceDataDTO>();
        List<ObjectId> communityIds = new ArrayList<ObjectId>();
        List<ObjectId> objectIds = new ArrayList<ObjectId>();
        for(ReferenceDataEntry entry: entries){
            ReferenceDataDTO dataDTO = new ReferenceDataDTO(entry);
            CommunityEntry communityDTO = map3.get(new ObjectId(dataDTO.getCommunityId()));
            if(communityDTO!=null){
                dataDTO.setCommunityName(communityDTO.getCommunityName());
            }
            SubjectClassEntry  subjectClassEntry = subjectClassEntryMap.get(entry.getSubjectId());
            if(subjectClassEntry!=null){
                dataDTO.setSubjectName(subjectClassEntry.getName());
            }
            UserEntry userEntry = mainUserEntryMap.get(entry.getUserId());
            if(userEntry!=null){
                dataDTO.setUserName(userEntry.getNickName());
            }
            dataDTO.setLongTime(entry.getCreateTime());
            dataDTO.setOperation(0);
            dtos.add(dataDTO);
            communityIds.add(entry.getCommunityId());
            objectIds.add(entry.getUserId());
        }
        List<ObjectId> groupIdList = new ArrayList<ObjectId>();
        Map<ObjectId, ObjectId> groupIds = communityDao.getGroupIds(communityIds);
        for (Map.Entry<ObjectId, ObjectId> item : groupIds.entrySet()) {
            groupIdList.add(item.getValue());
        }
       // Map<String, MemberEntry> memberMap = memberDao.getGroupNick(groupIdList, objectIds);
        /*for(ReferenceDataDTO dataDTO2 :dtos){
            ObjectId groupId = groupIds.get(new ObjectId(dataDTO2.getCommunityId()));
            MemberEntry entry1 = memberMap.get(groupId + "$" + new ObjectId(dataDTO2.getUserId()));
            MemberEntry entry5 = memberMap.get(groupId + "$" + userId);
            if(entry1!=null && entry5!=null){
                if(entry5.getRole() > entry1.getRole()){
                    dataDTO2.setOperation(1);//权限压制，可删除
                }else if(dataDTO2.getUserId().equals(userId.toString())){
                    dataDTO2.setOperation(1);//发布人相同，可删除
                }else{
                    dataDTO2.setOperation(0);//不可删除
                }
            }else{
                dataDTO2.setOperation(0);//不可删除
            }
        }*/
        map.put("list",dtos);
        map.put("count",count);
        map.put("page",page);
        map.put("pageSize",pageSize);
        return map;
    }

    public String transReferenceData(ObjectId id,String communityIds,ObjectId userId){
        ReferenceDataEntry referenceDataEntry = referenceDataDao.getEntry(id);
        int score = 0;
        if(referenceDataEntry!=null){
            String[] strings = communityIds.split(",");
            List<ObjectId> objectIdList = new ArrayList<ObjectId>();
            long current = System.currentTimeMillis();
            for(String str:strings){
                referenceDataEntry.setCommunityId(new ObjectId(str));
                referenceDataEntry.setUserId(userId);
                referenceDataEntry.setID(null);
                referenceDataEntry.setCreateTime(current);
                String oid = referenceDataDao.addEntry(referenceDataEntry);
                communityDetailDao.save(getOldEntry(referenceDataEntry));
                //发送通知
                PictureRunNable.addTongzhi(str,userId.toString(),4);
                objectIdList.add(new ObjectId(str));
            }
            //红点
            redDotService.addEntryList(objectIdList,userId, ApplyTypeEn.text.getType(),4);
            //积分
            score = integralSufferService.addIntegral(userId, IntegralType.book,4,1);
        }else{

        }

        return score+"";
    }

    public void delReferenceData(ObjectId id,ObjectId userId){
        ReferenceDataEntry referenceDataEntry = referenceDataDao.getEntry(id);
        if(referenceDataEntry!=null){
            referenceDataDao.delEntry(id);
            communityDetailDao.delOldEntry(id.toString());
        }
    }

    public void updateEntry(){
        int page = 1;
        int pageSize = 10;
        boolean flage = true;
        while(flage){
            List<CommunityDetailEntry> communityDetailEntries = communityDetailDao.getDetailEntries(4, page, pageSize);
            List<ReferenceDataEntry> referenceDataEntries = new ArrayList<ReferenceDataEntry>();
            if(communityDetailEntries.size()<10){
                flage = false;
            }
            for(CommunityDetailEntry communityDetailEntry: communityDetailEntries){

                ReferenceDataEntry entry = new ReferenceDataEntry();
                entry.setCommunityId(new ObjectId(communityDetailEntry.getCommunityId()));
                entry.setUserId(communityDetailEntry.getCommunityUserId());
                entry.setContent(communityDetailEntry.getCommunityContent());
                entry.setSuffix(getType(communityDetailEntry));
                if(entry.getSuffix().equals("")){
                }else{
                    entry.setCreateTime(communityDetailEntry.getCreateTime());
                    entry.setIsRemove(communityDetailEntry.getRemove());
                    entry.setSize("");
                    entry.setTitle(communityDetailEntry.getCommunityTitle());
                    entry.setSubjectId(communityDetailEntry.getID());
                    entry.setType(checkType2(entry.getSuffix()));
                    List<AttachmentEntry> attachmentList  = getAttenment(communityDetailEntry);
                    if(attachmentList.size()>0){
                        BasicDBList attachmentDbList = new BasicDBList();
                        for(AttachmentEntry attachmentEntry:attachmentList){
                            attachmentDbList.add(attachmentEntry.getBaseEntry());
                        }
                        entry.setAttachmentList(attachmentDbList);
                        referenceDataEntries.add(entry);
                    }
                }
            }

            this.addRedDotEntryBatch(referenceDataEntries);
            page++;
        }


    }

    /**
     * 批量增加参考资料
     * @param list
     */
    public void addRedDotEntryBatch(List<ReferenceDataEntry> list) {
        List<DBObject> dbList = new ArrayList<DBObject>();
        for (int i = 0; list != null && i < list.size(); i++) {
            ReferenceDataEntry si = list.get(i);
            dbList.add(si.getBaseEntry());
        }
        //导入新纪录
        if(dbList.size()>0) {
            referenceDataDao.addBatch(dbList);
        }
    }


    /**
     * 判断文件类型
     * //0 全部   1 文档  2 视频  3 音频  4 图片 5 其他
     */
    public int checkType(String type) {
        //截取最后的后缀
       // String type = str.substring(str.lastIndexOf("."));
        if(type.equalsIgnoreCase("jpg") ||type.equalsIgnoreCase("jpg")||type.equalsIgnoreCase("jpeg")||type.equalsIgnoreCase("png") ||type.equalsIgnoreCase("gif") ||type.equalsIgnoreCase("bmp")){//图片
            return 4;
        }else if(type.equalsIgnoreCase("avi") ||type.equalsIgnoreCase("mp4")||type.equalsIgnoreCase("m3u8")||type.equalsIgnoreCase("mov")||type.equalsIgnoreCase("rmvb")||type.equalsIgnoreCase("rm")||type.equalsIgnoreCase("3gp")||type.equalsIgnoreCase("wmv")||type.equalsIgnoreCase("mkv")){//视频
            return 2;
        }else if(type.equalsIgnoreCase("doc") ||type.equalsIgnoreCase("docx")||type.equalsIgnoreCase("pdf")||type.equalsIgnoreCase("ppt")||type.equalsIgnoreCase("xls")||type.equalsIgnoreCase("xlsx")||type.equalsIgnoreCase("txt") ||type.equalsIgnoreCase("pptx")){//文档
            return 1;
        }else if(type.equalsIgnoreCase("mp3") || type.equalsIgnoreCase("flac")|| type.equalsIgnoreCase("amr")){//音频
            return 3;
        }else{
            return 5;
        }

    }

    /**
     * 判断文件类型
     * //0 全部   1 文档  2 视频  3 音频  4 图片 5 其他
     */
    public int checkType2(String type) {
        //截取最后的后缀
        // String type = str.substring(str.lastIndexOf("."));
        if(type.equalsIgnoreCase("image")){//图片
            return 4;
        }else if(type.equalsIgnoreCase("video")){//视频
            return 2;
        }else if(type.equalsIgnoreCase("doc") ||type.equalsIgnoreCase("pdf")||type.equalsIgnoreCase("ppt")||type.equalsIgnoreCase("xls")||type.equalsIgnoreCase("txt")){//文档
            return 1;
        }else if(type.equalsIgnoreCase("mp3")){//音频
            return 3;
        }else{
            return 5;
        }

    }

    /**
     * 返回文件后缀
     * //0 全部   1 文档  2 视频  3 音频  4 图片 5 其他
     */
    public String getType(CommunityDetailEntry communityDetailEntry) {
        String str = "";
        if(communityDetailEntry.getAttachmentList().size()>0){
            AttachmentEntry attachement = communityDetailEntry.getAttachmentList().get(0);
            str = attachement.getUrl();
        }else if(communityDetailEntry.getImageList().size()>0){
            AttachmentEntry attachement = communityDetailEntry.getImageList().get(0);
            str = attachement.getUrl();
        }else if(communityDetailEntry.getVideoList().size()>0){
            VideoEntry attachement = communityDetailEntry.getVideoList().get(0);
            str = attachement.getVideoUrl();
        }else if(communityDetailEntry.getVoiceList().size()>0){
            AttachmentEntry attachement = communityDetailEntry.getVoiceList().get(0);
            str = attachement.getUrl();
        }else{
            return "";
        }
        //截取最后的后缀
        String type = str.substring(str.lastIndexOf(".")+ 1);
        if(type.equalsIgnoreCase("jpg") ||type.equalsIgnoreCase("jpg")||type.equalsIgnoreCase("jpeg")||type.equalsIgnoreCase("png") ||type.equalsIgnoreCase("gif") ||type.equalsIgnoreCase("bmp")){//图片
            type= "image";
        }else if(type.equalsIgnoreCase("avi") ||type.equalsIgnoreCase("mp4")||type.equalsIgnoreCase("m3u8")||type.equalsIgnoreCase("mov")||type.equalsIgnoreCase("rmvb")||type.equalsIgnoreCase("rm")||type.equalsIgnoreCase("3gp")||type.equalsIgnoreCase("wmv")||type.equalsIgnoreCase("mkv")){//视频
            type= "video";
        }else if(type.equalsIgnoreCase("txt")){//文档
            type= "txt";
        }else if(type.equalsIgnoreCase("doc") || type.equalsIgnoreCase("docx")){
            type= "doc";
        }else if(type.equalsIgnoreCase("pdf")){
            type= "pdf";
        }else if(type.equalsIgnoreCase("ppt") || type.equalsIgnoreCase("pptx")){
            type= "ppt";
        }else if(type.equalsIgnoreCase("xls") || type.equalsIgnoreCase("xlsx")){
            type= "xls";
        }else if(type.equalsIgnoreCase("mp3") || type.equalsIgnoreCase("flac") || type.equalsIgnoreCase("amr")){
            type= "mp3";
        }else{
            type="other";
        }
        return type;
    }

    public String getType(String type){
        if(type.equalsIgnoreCase("jpg") ||type.equalsIgnoreCase("jpg")||type.equalsIgnoreCase("jpeg")||type.equalsIgnoreCase("png") ||type.equalsIgnoreCase("gif") ||type.equalsIgnoreCase("bmp")){//图片
            type= "image";
        }else if(type.equalsIgnoreCase("avi") ||type.equalsIgnoreCase("mp4")||type.equalsIgnoreCase("m3u8")||type.equalsIgnoreCase("mov")||type.equalsIgnoreCase("rmvb")||type.equalsIgnoreCase("rm")||type.equalsIgnoreCase("3gp")||type.equalsIgnoreCase("wmv")||type.equalsIgnoreCase("mkv")){//视频
            type= "video";
        }else if(type.equalsIgnoreCase("txt")){//文档
            type= "txt";
        }else if(type.equalsIgnoreCase("doc") || type.equalsIgnoreCase("docx")){
            type= "doc";
        }else if(type.equalsIgnoreCase("pdf")){
            type= "pdf";
        }else if(type.equalsIgnoreCase("ppt") || type.equalsIgnoreCase("pptx")){
            type= "ppt";
        }else if(type.equalsIgnoreCase("xls") || type.equalsIgnoreCase("xlsx")){
            type= "xls";
        }else if(type.equalsIgnoreCase("mp3") || type.equalsIgnoreCase("flac") || type.equalsIgnoreCase("amr")){
            type= "mp3";
        }else{
            type="other";
        }
        return type;
    }

    /**
     * 返回文件后缀
     * //0 全部   1 文档  2 视频  3 音频  4 图片 5 其他
     */
    public List<AttachmentEntry> getAttenment(CommunityDetailEntry communityDetailEntry) {
        List<AttachmentEntry> attachement= new ArrayList<AttachmentEntry>();
        if(communityDetailEntry.getAttachmentList().size()>0){
            attachement = communityDetailEntry.getAttachmentList();

        }else if(communityDetailEntry.getImageList().size()>0){
            attachement = communityDetailEntry.getImageList();

        }else if(communityDetailEntry.getVideoList().size()>0){
            List<VideoEntry> videoList = communityDetailEntry.getVideoList();
           //VideoEntryString url, String fileName, long time, ObjectId userId
            if(videoList.size()>0){
                VideoEntry videoEntry = videoList.get(0);
                if(videoEntry!=null){
                    AttachmentEntry attachmentEntry = new AttachmentEntry(videoEntry.getVideoUrl(),"",0l,null);
                    attachement.add(attachmentEntry);
                }
            }
        }else if(communityDetailEntry.getVoiceList().size()>0){
            attachement = communityDetailEntry.getVoiceList();

        }

        return attachement;
    }


    public  CommunityDetailEntry  getOldEntry(ReferenceDataEntry entry){
        List<AttachmentEntry> attachmentEntries = new ArrayList<AttachmentEntry>();
        List<VideoEntry> videoEntries = new ArrayList<VideoEntry>();
        CommunityDetailEntry communityDetailEntry = new CommunityDetailEntry(entry.getCommunityId(),entry.getUserId(),entry.getTitle(),entry.getContent()
                                            ,4,new ArrayList<ObjectId>(),entry.getAttachmentList(),attachmentEntries,attachmentEntries,entry.getID().toString(),"","","","",0,-1,0,videoEntries);
        return communityDetailEntry;
    }

    public void addNewEntry(CommunityDetailEntry communityDetailEntry){
        referenceDataDao.addEntry(getNewEntry(communityDetailEntry));
    }

    public  ReferenceDataEntry  getNewEntry(CommunityDetailEntry communityDetailEntry){
        ReferenceDataEntry entry = new ReferenceDataEntry();
        entry.setCommunityId(new ObjectId(communityDetailEntry.getCommunityId()));
        entry.setUserId(communityDetailEntry.getCommunityUserId());
        entry.setContent(communityDetailEntry.getCommunityContent());
        entry.setSuffix(getType(communityDetailEntry));
        if(entry.getSuffix().equals("")){
        }else{
            entry.setCreateTime(communityDetailEntry.getCreateTime());
            entry.setIsRemove(communityDetailEntry.getRemove());
            entry.setSize("");
            entry.setTitle(communityDetailEntry.getCommunityTitle());
            entry.setSubjectId(communityDetailEntry.getID());
            entry.setType(checkType2(entry.getSuffix()));
            List<AttachmentEntry> attachmentList  = getAttenment(communityDetailEntry);
            if(attachmentList.size()>0){
                BasicDBList attachmentDbList = new BasicDBList();
                for(AttachmentEntry attachmentEntry:attachmentList){
                    attachmentDbList.add(attachmentEntry.getBaseEntry());
                }
                entry.setAttachmentList(attachmentDbList);
            }
        }
        return entry;
    }
}
