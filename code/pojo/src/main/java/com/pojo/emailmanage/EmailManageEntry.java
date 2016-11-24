package com.pojo.emailmanage;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;

/**
 * collectionName:emailmanage
 * <pre>
 {
 ems:邮箱地址
 ty: 1:日志报表用
 udf: 自定义用
 soip:服务器公网地址
 siip:服务器内网地址
 fl:是否有效 1有效，0无效
 }
 * </pre>
 * Created by guojing on 2016/5/19.
 */
public class EmailManageEntry extends BaseDBObject {
    private static final long serialVersionUID = 7936859648592747487L;

    public EmailManageEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public EmailManageEntry(
            String emails,
            int type,
            int userDef,
            String serverOuterIp,
            String serverInnerIp

    ) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("ems", emails)
                .append("soip", serverOuterIp)
                .append("siip", serverInnerIp)
                .append("ty", type)
                .append("udf", userDef)
                .append("fl", Constant.ONE);
        setBaseEntry(baseEntry);
    }

    public String getEmails() {
        return getSimpleStringValue("ems");
    }

    public void setEmails(String emails) {
        setSimpleValue("ems", emails);
    }

    public String getServerOuterIp() {
        return getSimpleStringValue("soip");
    }

    public void setServerOuterIp(String serverOuterIp) {
        setSimpleValue("soip", serverOuterIp);
    }

    public String getServerInnerIp() {
        return getSimpleStringValue("siip");
    }

    public void setServerInnerIp(String serverInnerIp) {
        setSimpleValue("siip", serverInnerIp);
    }

    public int getType() {
        return getSimpleIntegerValue("ty");
    }

    public void setType(int type) {
        setSimpleValue("ty", type);
    }

    public int getUserDef() {
        return getSimpleIntegerValueDef("udf",1);
    }

    public void setUserDef(int userDef) {
        setSimpleValue("udf", userDef);
    }

}
