package com.pojo.achievement;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * @author cxy
 *         2015-7-26 17:50:03
 *         科研成果Entry类
 *         collectionName : achievement
 *         科研成果名称 : ana(achievementName)
 *         科研成果类别  : at(achievementType)
 *         发布人编码 puid(publishUserId)
 *         发布人名称 puna(publishUserName)
 *         发布时间 : pt(publishTime) long
 *         封面 rc（reviewCover） String
 *         课件[] : cs(courseware)
 */
public class AchievementEntry extends BaseDBObject {

    public AchievementEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public AchievementEntry(String achievementName, int achievementType, String content,
                            long publishTime, ObjectId cover, List<ObjectId> courseware
            , ObjectId publishUserId, String publishUserName, ObjectId schoolId, String schoolName) {
        super();
        BasicDBObject baseEntry = new BasicDBObject().append("ana", achievementName)
                .append("at", achievementType)
                .append("pt", publishTime)
                .append("cs", courseware)
                .append("rc", cover)
                .append("puid", publishUserId)
                .append("sid", schoolId)
                .append("sna", schoolName)
                .append("ct", content)
                .append("puna", publishUserName);
        setBaseEntry(baseEntry);
    }

    public String getAchievementName() {
        return getSimpleStringValue("ana");
    }

    public int getAchievementType() {
        return getSimpleIntegerValue("at");
    }

    public ObjectId getPublishUserId() {
        return getSimpleObjecIDValue("puid");
    }

    public String getPublishUserName() {
        return getSimpleStringValue("puna");
    }

    public ObjectId getReviewCover() {
        return getSimpleObjecIDValue("rc");
    }

    public String getContent() {
        return getSimpleStringValue("ct");
    }

    public long getPublishTime() {
        return getSimpleLongValue("pt");
    }

    public String getSchoolName() {
        return getSimpleStringValue("sna");
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }

    public List<ObjectId> getCoursewareList() {
        List<ObjectId> resultList = new ArrayList<ObjectId>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("cs");
        if (null != list && !list.isEmpty()) {
            for (Object o : list) {
                resultList.add((ObjectId) o);
            }
        }
        return resultList;
    }
}
