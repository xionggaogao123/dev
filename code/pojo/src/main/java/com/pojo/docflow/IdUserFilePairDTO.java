package com.pojo.docflow;

import com.pojo.app.IdNameValuePair;
import com.pojo.app.IdValuePair;
import com.pojo.school.ClassEntry;
import com.pojo.school.DepartmentEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by qiangm on 2015/8/25.
 */
public class IdUserFilePairDTO {
    private String id;
    private String userId;
    private String userName;
    private String name;
    private String value;



    public IdUserFilePairDTO()
    {
    }


    public IdUserFilePairDTO(String id,String userId,String userName,String name,String value)
    {
        this.id=id;
        this.userId=userId;
        this.userName=userName;
        this.name=name;
        this.value=value;
    }


    public IdUserFilePairDTO(String id)
    {
        this.id=id;
    }

    public IdUserFilePairDTO(IdUserFilePair pair)
    {
        this.id=pair.getId().toString();
        this.userId=pair.getUserId().toString();
        this.name=pair.getName();
        this.value=pair.getValue().toString();
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    public IdUserFilePair export()
    {
        IdUserFilePair pair=new IdUserFilePair(new ObjectId(this.getId()),new ObjectId(this.userId),
                this.getName(),this.getValue());
        return pair;
    }

}
