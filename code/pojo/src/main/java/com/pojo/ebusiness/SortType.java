package com.pojo.ebusiness;

import com.mongodb.BasicDBObject;
import com.sys.constants.Constant;

/**排序方式
 * Created by fl on 2016/1/13.
 */
public enum SortType {

    TIME_ASC(1, Constant.MONGO_SORTBY_ASC),//时间升序
    TIME_DESC(2, Constant.MONGO_SORTBY_DESC),//时间降序
    PRICE_ASC(3, new BasicDBObject("pr", 1)),//价格升序
    PRICE_DESC(4, new BasicDBObject("pr", -1)),//价格降序
    SALES_ASC(5, new BasicDBObject("sc", 1)),//销量升序
    SALES_DESC(6, new BasicDBObject("sc", -1)),//销量降序
    POPULAR_ASC(7, new BasicDBObject("pl", 1)),//人气升序
    POPULAR_DESC(8, new BasicDBObject("pl", -1)),//人气降序
    UPDATE_ASC(9, new BasicDBObject("upt", -1)) //最新时间降序
    ;

    private int type;
    private BasicDBObject sort;

    private SortType(int type, BasicDBObject sort){
        this.type = type;
        this.sort = sort;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public BasicDBObject getSort() {
        return sort;
    }

    public void setSort(BasicDBObject sort) {
        this.sort = sort;
    }

    public static SortType getSortType(int type){
        for(SortType sortType : SortType.values()){
            if(sortType.getType() == type){
                return sortType;
            }
        }
        return null;
    }
}
