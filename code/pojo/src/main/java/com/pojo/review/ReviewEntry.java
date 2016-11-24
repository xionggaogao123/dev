package com.pojo.review;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

/**
 * @author cxy
 *         2015-7-26 17:50:03
 *         评课议课Entry类
 *         collectionName : review
 *         发布人id: puid(publishUserId)
 *         发布人name:puna(publishUserName)
 *         发布人学校id: scid(schoolId)
 *         发布人学校name: scna(schoolName)
 *         评课议课名称 : rna(reviewName)
 *         学段	   : ett(educationTermType)
 *         学科	   : es(educationSubject)
 *         教材版本 : tv(textbookVersion)
 *         年级 	   : eg(educationGrade)
 *         章  	   : ch(chapter)
 *         节	   : pat(part)
 *         发布时间 : pt(publishTime) long
 *         评课议课封面 : rc(reviewCover)
 *         课堂实录 : cr(classRecord)
 *         课件[] 	 : cs(courseware)
 *         教育局ID  ： edid(educationBureauId)
 */
public class ReviewEntry extends BaseDBObject {

    public ReviewEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public ReviewEntry(String reviewName, String educationTermType, String educationSubject, String textbookVersion, String educationGrade,
                       String chapter, String part, long publishTime, String reviewCover, ObjectId classRecord,
                       List<ObjectId> courseware, ObjectId publishUserId, String publishUserName, ObjectId schoolId, String schoolName,ObjectId educationBureauId) {
        super();

        BasicDBObject baseEntry = new BasicDBObject().append("rna", reviewName)
                .append("puid", publishUserId)
                .append("puna", publishUserName)
                .append("scid", schoolId)
                .append("scna", schoolName)
                .append("ett", educationTermType)
                .append("es", educationSubject)
                .append("tv", textbookVersion)
                .append("eg", educationGrade)
                .append("ch", chapter)
                .append("pat", part)
                .append("pt", publishTime)
                .append("rc", reviewCover)
                .append("cr", classRecord)
                .append("edid", educationBureauId)
                .append("cs", MongoUtils.convert(courseware));

        setBaseEntry(baseEntry);
    }

    public String getStringId() {
        return this.getID().toString();
    }

    public ObjectId getPublicUserId() {
        return getSimpleObjecIDValue("puid");
    }

    public String getPublishUserName() {
        return getSimpleStringValue("puna");
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("scid");
    }

    public String getSchoolName() {
        return getSimpleStringValue("scna");
    }

    public String getEducationTermType() {
        return getSimpleStringValue("ett");
    }

    public String getReviewName() {
        return getSimpleStringValue("rna");
    }

    public String getEducationSubject() {
        return getSimpleStringValue("es");
    }

    public String getTextbookVersion() {
        return getSimpleStringValue("tv");
    }

    public String getChapter() {
        return getSimpleStringValue("ch");
    }

    public String getPart() {
        return getSimpleStringValue("pat");
    }

    public String getReviewCover() {
        return getSimpleStringValue("rc");
    }

    public ObjectId getClassRecord() {
        return getSimpleObjecIDValue("cr");
    }

    public long getPublishTime() {
        return getSimpleLongValue("pt");
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
