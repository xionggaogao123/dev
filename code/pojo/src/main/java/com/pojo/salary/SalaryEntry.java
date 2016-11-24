package com.pojo.salary;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

/**
 * 2015-07-18 15:54:35
 * <p/>
 * 工资Entry类
 * <pre>
 * collectionName:salary
 * </pre>
 * <pre>
 * {
 *  sid: 学校编码
 *  uid: 用户ID
 *  unm：用户名字
 *  y：年
 *  m: 月
 *  ym:年月
 *  tm:发放日期
 *  ix:当月中的发放次数
 *  na:发放次数名称
 *  money:[
 *    {in: 'xxx', it: '-/+', m: 5000.0},
 *    {in: 'xxx', it: '-/+', m: 5000.0},
 *    {in: 'xxx', it: '-/+', m: 5000.0}
 *  ]  工资对于条目量
 *  ss: 应发合计
 *  ms: 扣发合计
 *  as: 实发合计
 *  dl: 是否删除
 *  rmk:remark备注
 * }
 * </pre>
 *
 * @author yangling
 *         2015-07-18 15:54:35
 */
public class SalaryEntry extends BaseDBObject {
    private final DecimalFormat decimalFormat = new DecimalFormat("0.##");
    private static final long serialVersionUID = 2004296173128260124L;


    public SalaryEntry(DBObject dbo) {
        this((BasicDBObject) dbo);
    }

    public SalaryEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public SalaryEntry(
            String uid, String unm,
            int y, int m, int n, String schoolId, String timesName,
            List<SalaryItemDto> moneyList,String remark) {
        super();
        double ss = 0;
        double ms = 0;
        double as = 0;
        BasicDBList list = new BasicDBList();
        for (int i = 0; i < moneyList.size(); i++) {
            BasicDBObject e = new BasicDBObject();
            e.append("in", moneyList.get(i).getItemName());
            e.append("it", moneyList.get(i).getType());
            e.append("m", moneyList.get(i).getM());
            if (SalaryItemDto.ADD.equals(moneyList.get(i).getType())) {
                ss += moneyList.get(i).getM();
            } else if (SalaryItemDto.SUB.equals(moneyList.get(i).getType())) {
                ms += moneyList.get(i).getM();
            }
            list.add(e);
        }

        as = ss - ms;

        BasicDBObject baseEntry = new BasicDBObject().append("sid", new ObjectId(schoolId))
                .append("uid", new ObjectId(uid)).append("unm", unm)
                .append("y", y).append("m", m).append("n", n)
                .append("money", list).append("na", timesName).append("ym", Integer.parseInt(Constant.EMPTY + y + (m < 10 ? "0" + m : m)))
                .append("ss", ss).append("ms", ms).append("as", as).append("dl", 0).append("tm", DateTimeUtils.getCurrDate()).append("rmk",remark);

        setBaseEntry(baseEntry);
    }

    public String getSalaryDate(){
        return getSimpleStringValue("tm");
    }
    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }

    public String getTimesName() {
        return getSimpleStringValue("na");
    }

    public int getDeleteFlag() {
        return getSimpleIntegerValue("dl");
    }

    public String getYearMonth() {
        return getSimpleStringValue("ym");
    }

    public String getId() {
        return getSimpleStringValue(Constant.ID);
    }

    public String getUserId() {
        return getSimpleStringValue("uid");
    }

    public String getUserName() {
        return getSimpleStringValue("unm");
    }

    public String getRemark() {
        return getSimpleStringValue("rmk");
    }

    public int getYear() {
        return getSimpleIntegerValue("y");
    }

    public int getMonth() {
        return getSimpleIntegerValue("m");
    }

    public int getNumber() {
        return getSimpleIntegerValue("n");
    }

    public double getS() {
        return getSimpleDoubleValue("ss");
    }

    public double getM() {
        return getSimpleDoubleValue("ms");
    }

    public double getA() {
        return getSimpleDoubleValue("as");
    }

    public List<SalaryItemDto> getMoney() {
        List<SalaryItemDto> retList = new ArrayList<SalaryItemDto>();
        BasicDBList list = (BasicDBList) getSimpleObjectValue("money");
        if (null != list && !list.isEmpty()) {
            for (Object o : list) {
                if (o instanceof BasicDBObject) {
                    SalaryItemDto obj = new SalaryItemDto();
                    obj.setItemName(((BasicDBObject) o).getString("in"));
                    obj.setType(((BasicDBObject) o).getString("it"));
                    obj.setM(((BasicDBObject) o).getDouble("m"));
                    obj.setmStr(decimalFormat.format(((BasicDBObject) o).getDouble("m")));
                    retList.add(obj);
                }
            }
        }
        return retList;
    }
}
