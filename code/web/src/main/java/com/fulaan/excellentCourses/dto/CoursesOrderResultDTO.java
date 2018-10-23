package com.fulaan.excellentCourses.dto;

import com.pojo.excellentCourses.CoursesOrderResultEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-10-19.
 */
public class CoursesOrderResultDTO {
    /** id                                               id
     * orderTime            下单日期                    otm
     * userId               用户id                      uid
     * schoolId             学校id                      sid
     * schoolName           学校名称                    snm
     * coursesId            课程id                      cid
     * coursesName          课程名称                    cnm
     * type                 1 收入   2 支出             typ
     * List<ObjectId>       购买课节id                  clt
     * price                购买金额                    pri
     * order                订单号                      ord
     * source               来源                        sou*/

     private String id;
    private String orderTime;
    private String userId;
    private String schoolId;
    private String schoolName;
    private String coursesId;
    private String coursesName;
    private int type;
    private List<String> classList = new ArrayList<String>();
    private double price;
    private String order;
    private int source;
    private String userName;

    public CoursesOrderResultDTO(){

    }

    public CoursesOrderResultDTO(CoursesOrderResultEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            if(e.getOrderTime()!=0){
                this.orderTime = DateTimeUtils.getLongToStrTimeTwo(e.getOrderTime());
            }else{
                this.orderTime = "";
            }
            this.userId = e.getUserId()==null?"":e.getUserId().toString();
            this.schoolId = e.getSchoolId()==null?"":e.getSchoolId().toString();
            this.schoolName = e.getSchoolName();
            this.coursesId = e.getCoursesId()==null?"":e.getCoursesId().toString();
            this.coursesName = e.getCoursesName();
            this.type = e.getType();
            if(e.getClassList()!=null){
                for(ObjectId cid: e.getClassList()){
                    this.classList.add(cid.toString());
                }
            }
            this.price = e.getPrice();
            this.order = e.getOrder();
            this.source = e.getSource();
        }else{
            new CoursesOrderResultDTO();
        }

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getCoursesId() {
        return coursesId;
    }

    public void setCoursesId(String coursesId) {
        this.coursesId = coursesId;
    }

    public String getCoursesName() {
        return coursesName;
    }

    public void setCoursesName(String coursesName) {
        this.coursesName = coursesName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getClassList() {
        return classList;
    }

    public void setClassList(List<String> classList) {
        this.classList = classList;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }
}
