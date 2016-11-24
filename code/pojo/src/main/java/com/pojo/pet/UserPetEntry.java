package com.pojo.pet;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by guojing on 2015/3/26.
 * 用户宠物表
 * <pre>
 * collectionName:userpets
 * </pre>
 * <pre>
 * {
 *  uid:用户id
 *  pets
 *  [
 *     * {
 *  id :唯一性标示
 *  pid:宠物id
 *  pnm:宠物名称
 *  st:是否是选中宠物
 *  crt:创建时间,long
 *  ih:是否孵化
 *  upt:修改时间,long
 *
 * }
 *  ]:宠物列表
 *
 * }
 * </pre>
  * @author guojing
 */
public class UserPetEntry extends BaseDBObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7876153141098391570L;

	public UserPetEntry(DBObject dbo) {
        this((BasicDBObject)dbo);
    }

    public UserPetEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public UserPetEntry(ObjectId userid,List<PetInfo> petInfos){
        super();
        BasicDBList pets =null ;
        if(petInfos !=null && petInfos.size()>0)
        {
            pets = new BasicDBList();
            for(PetInfo petInfo : petInfos)
            {
                pets.add(petInfo.getBaseEntry());
            }
        }

        BasicDBObject dbo =new BasicDBObject()
                .append("uid", userid)
                .append("pets", pets);
        setBaseEntry(dbo);
    }

	public ObjectId getUserid() {
		return getSimpleObjecIDValue("uid");
	}

	public void setUserid(ObjectId userid) {
        setSimpleValue("uid", userid);
	}

    public List<PetInfo> getPetInfos(){
        List<PetInfo> petInfos =null;
        BasicDBList list =(BasicDBList)getSimpleObjectValue("pets");
        if(null!=list && !list.isEmpty())
        {
            petInfos =new ArrayList<PetInfo>();
            for(Object o:list)
            {
                petInfos.add(new PetInfo((BasicDBObject)o));
            }
        }
        return petInfos;
    }

    public void setPetInfos(List<PetInfo> petInfos) {
        BasicDBList list = null;
        if(petInfos !=null && petInfos.size()>0)
        {
            list = new BasicDBList();
            for(PetInfo petInfo : petInfos)
            {
                list.add(petInfo.getBaseEntry());
            }
        }

        setSimpleValue("pets", list);
    }

}
