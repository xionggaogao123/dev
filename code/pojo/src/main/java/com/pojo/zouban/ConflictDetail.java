package com.pojo.zouban;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**冲突详情
 * {
 *     id:---------------->id
 *     cid:课程id------------>courseId
 *     stu:学生冲突----------->studnet:List<ObjectId>
 *     tea:教师是否冲突----------->teacher
 *     clr:教室是否冲突----------->classroom
 * }
 * Created by qiangm on 2015/10/15.
 */
public class ConflictDetail extends BaseDBObject{
    public ConflictDetail()
    {
        super();
    }
    public ConflictDetail(BasicDBObject basicDBObject)
    {
        super(basicDBObject);
    }

    public ConflictDetail(ObjectId id,ObjectId courseId,List<ObjectId> students,int teacher,int classroom)
    {
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("id",id)
                .append("cid",courseId)
                .append("stu",MongoUtils.convert(students))
                .append("tea", teacher)
                .append("clr",classroom);
        setBaseEntry(basicDBObject);
    }

    public ObjectId getId()
    {
        return getSimpleObjecIDValue("id");
    }
    public void setId(ObjectId id)
    {
        setSimpleValue("id",id);
    }

    public ObjectId getCourseId()
    {
        return getSimpleObjecIDValue("cid");
    }
    public void setCourseId(ObjectId courseId)
    {
        setSimpleValue("cid",courseId);
    }

    public List<ObjectId> getStudentConflict()
    {
        List<ObjectId> studentList = new ArrayList<ObjectId>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("stu");
        if (list != null && !list.isEmpty()) {
            for (Object o : list) {
                studentList.add((ObjectId) o);
            }
        }
        return studentList;
    }
    public void setStudentConflict(List<ObjectId> studentList)
    {
        setSimpleValue("stu", MongoUtils.convert(studentList));
    }

    public int getTeacherConflict()
    {
        return getSimpleIntegerValue("tea");
    }
    public void setTeacherConflict(int teacher)
    {
        setSimpleValue("tea",teacher);
    }

    public int getClassroomConflict()
    {
        return getSimpleIntegerValue("clr");
    }
    public void setClassroomConflict(int classroom)
    {
        setSimpleValue("clr",classroom);
    }
}
