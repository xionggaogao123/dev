
package com.db.temp;

import org.bson.types.ObjectId;

import com.db.school.ClassDao;
import com.db.user.UserDao;
import com.pojo.school.ClassEntry;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.MD5Utils;

public class ClassStudentPwdUpdate {
    //561f46dd0cf2c94b6a2ec406 888888

    public static void main(String[] args) throws IllegalParamException {
//		ClassDao cDao =new ClassDao();
//		UserDao uDao =new UserDao();
//		
//		ClassEntry ce=cDao.getClassEntryById(new ObjectId("561f46dd0cf2c94b6a2ec406"), Constant.FIELDS);
//		
//		for(ObjectId id:ce.getStudents())
//		{
//			uDao.update(id, "pw", MD5Utils.getMD5String("888888"), false);
//		}


        System.out.println(MD5Utils.getMD5String("888888"));
    }

}
