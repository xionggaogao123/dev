package com.pojo.overallquality;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.DeleteState;
import org.bson.types.ObjectId;

/**
 *<pre>
 * collectionName:classOverallQuality
 * </pre>
 * <pre>
 {
 gn:商品名称
 ph:图片路径
 qk:七牛key
 gp:商品价格
 gs:商品库存
 si:学校ID
 cui:创建人ID
 cd:创建日期
 uui:修改人ID
 ud:修改日期
 slc: 售出量
 st: 删除标记
 }
 * Created by guojing on 2016/8/25.
 */
public class OverallQualityGoodsEntry extends BaseDBObject {
    private static final long serialVersionUID = 7936856358592747487L;

    public OverallQualityGoodsEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public OverallQualityGoodsEntry(
            String goodsName,
            String picPath,
            String qnKey,
            int goodsPrice,
            int goodsStock,
            ObjectId schoolId,
            ObjectId createUserId,
            long createDate
    ) {
        BasicDBObject baseEntry =new BasicDBObject()
                .append("gn",goodsName)
                .append("ph",picPath)
                .append("qk",qnKey)
                .append("gp",goodsPrice)
                .append("gs",goodsStock)
                .append("si", schoolId)
                .append("cui", createUserId)
                .append("cd", createDate)
                .append("uui", createUserId)
                .append("ud", createDate)
                .append("ud", createDate)
                .append("st", DeleteState.NORMAL.getState())
                ;
        setBaseEntry(baseEntry);
    }

    public String getGoodsName() {
        return getSimpleStringValue("gn");
    }

    public void setGoodsName(String goodsName) {
        setSimpleValue("gn", goodsName);
    }

    public String getPicPath() {
        return getSimpleStringValue("ph");
    }

    public void setPicPath(String picPath) {
        setSimpleValue("ph", picPath);
    }

    public String getQnKey() {
        return getSimpleStringValue("qk");
    }

    public void setQnKey(String qnKey) {
        setSimpleValue("qk", qnKey);
    }

    public int getGoodsPrice() {
        return getSimpleIntegerValue("gp");
    }

    public void setGoodsPrice(int goodsPrice) {
        setSimpleValue("gp", goodsPrice);
    }

    public int getGoodsStock() {
        return getSimpleIntegerValue("gs");
    }

    public void setGoodsStock(int goodsStock) {
        setSimpleValue("gs", goodsStock);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("si");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("si", schoolId);
    }

    public ObjectId getCreateUserId() {
        return getSimpleObjecIDValue("cui");
    }

    public void setCreateUserId(ObjectId createUserId) {
        setSimpleValue("cui", createUserId);
    }

    public long getCreateDate() {
        return getSimpleLongValue("cd");
    }

    public void setCreateDate(long createDate) {
        setSimpleValue("cd",createDate);
    }

    public ObjectId getUpdateUserId() {
        return getSimpleObjecIDValue("uui");
    }

    public void setUpdateUserId(ObjectId updateUserId) {
        setSimpleValue("uui", updateUserId);
    }

    public long getUpdateDate() {
        return getSimpleLongValue("ud");
    }

    public void setUpdateDate(long updateDate) {
        setSimpleValue("ud",updateDate);
    }

    public int getSoldCount() {
        return getSimpleIntegerValueDef("slc", 0);
    }

    public void setSoldCount(int soldCount) {
        setSimpleValue("slc", soldCount);
    }
}
