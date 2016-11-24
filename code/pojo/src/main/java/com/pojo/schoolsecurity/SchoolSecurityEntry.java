package com.pojo.schoolsecurity;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.Platform;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.DeleteState;
import com.pojo.utils.HandleState;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 微博
 * <pre>
 * collectionName:schoolSecurity
 * </pre>
 * <pre>
 {
     ui:用户ID
     con:内容
     pf：平台;参见Platform
     pbt:发布时间,long
     si:学校ID
     ili:图片；参见SchoolSecurityImage
     [

     ]
     ih:是否处理
     oui:操作人id
     ot:操作时间
     st:是否删除
 }
 * </pre>
 * @author jing.guo
 */
public class SchoolSecurityEntry extends BaseDBObject {
    private static final long serialVersionUID = 7933557018592747487L;

    public SchoolSecurityEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public SchoolSecurityEntry(
            ObjectId userId,
            String content,
            Platform pf,
            ObjectId schoolID,
            List<SchoolSecurityImage> imageList
    ) {
        this(
                userId,
                content,
                pf,
                new Date().getTime(),
                schoolID,
                imageList,
                HandleState.UNTREATED,
                userId,
                new Date().getTime(),
                DeleteState.NORMAL
        );
    }

    public SchoolSecurityEntry(
            ObjectId userId,
            String content,
            Platform pf,
            long publishTime,
            ObjectId schoolID,
            List<SchoolSecurityImage> imageList,
            HandleState hs,
            ObjectId operateUserId,
            long operateTime,
            DeleteState ds
    ) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("ui", userId)
                .append("con", content)
                .append("pf", pf.getType())
                .append("pbt", publishTime)
                .append("si",schoolID)
                .append("ili", MongoUtils.convert(MongoUtils.fetchDBObjectList(imageList)))
                .append("ih", hs.getState())
                .append("oui", operateUserId)
                .append("ot", operateTime)
                .append("st", ds.getState());
        setBaseEntry(baseEntry);
    }

    public ObjectId getUserId() {
        return getSimpleObjecIDValue("ui");
    }

    public void setUserId(ObjectId userId) {
        setSimpleValue("ui", userId);
    }

    public String getContent() {
        return getSimpleStringValue("con");
    }

    public void setContent(String content) {
        setSimpleValue("con", content);
    }

    public int getPlatformType() {
        return getSimpleIntegerValue("pf");
    }

    public void setPlatformType(int platformType) {
        setSimpleValue("pf", platformType);
    }

    public long getPublishTime() {return getSimpleLongValue("pbt");}

    public void setPublishTime(long publishTime) {setSimpleValue("pbt",publishTime);}

    public ObjectId getSchoolID() {
        return getSimpleObjecIDValue("si");
    }

    public void setSchoolID(ObjectId schoolID) {
        setSimpleValue("si", schoolID);
    }

    public List<SchoolSecurityImage> getImageList() {
        List<SchoolSecurityImage> retList =new ArrayList<SchoolSecurityImage>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("ili");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add(new SchoolSecurityImage((BasicDBObject)o));
            }
        }
        return retList;
    }

    public void setImageList(List<SchoolSecurityImage> imageList) {
        List<DBObject> list =MongoUtils.fetchDBObjectList(imageList);
        setSimpleValue("ili", MongoUtils.convert(list));
    }

    public int getHandleState() {
        return getSimpleIntegerValue("ih");
    }

    public void setHandleState(int handleState) {
        setSimpleValue("ih", handleState);
    }

    public ObjectId getOperateUserId() {
        return getSimpleObjecIDValue("oui");
    }

    public void setOperateUserId(ObjectId operateUserId) {
        setSimpleValue("oui", operateUserId);
    }

    public long getOperateTime() {return getSimpleLongValue("ot");}

    public void setOperateTime(long operateTime) {setSimpleValue("ot",operateTime);}

    public int getDeleteState() {
        return getSimpleIntegerValue("st");
    }

    public void setDeleteState(int deleteState) {
        setSimpleValue("st", deleteState);
    }
}
