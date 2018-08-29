package com.pojo.backstage;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by taoto.chan on 2018年8月22日16:56:14
 *
 * 用户角色权限设置页面
 *
 * id		主键                         ObjectId
 * class	类别 分为 系统设置 学校设置 运营管理 订单管理 String
 * parentId  父节点的id       ObjectId
 * name          tree节点的名    String
 * value         tree节点的值    String
 * 下面暂时没添加
// * createBy          创建人            ObjectId
// * creationDate      创建时间         String
// * lastUpdateBy      最新更新人       ObjectId
// * lastUpdateDate    最新更新时间     String
 *
 */
public class JurisdictionTreeEntry extends BaseDBObject {

    public JurisdictionTreeEntry(DBObject dbObject) {
        setBaseEntry((BasicDBObject) dbObject);
    }


    public String getParentId() {
        return "".equals(getSimpleStringValue("parentId")) ? "" : getSimpleObjecIDValue("parentId").toString();
    }

    public String getName() {
        return getSimpleStringValue("name");
    }

    public String getClas(){
        return getSimpleStringValue("class");
    }
}
