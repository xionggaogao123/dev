package com.pojo.emarket;

import com.sys.utils.DateTimeUtils;

import java.io.Serializable;
import java.util.Date;

public class WithdrawCashInfo implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 8398826453111164099L;
    public WithdrawCashInfo(){

    }
    public WithdrawCashInfo(WithDrawEntry entry)
    {
        this.id = entry.getID().toString();
        this.userid = entry.getUserid().toString();
        this.paypalAccount = entry.getPaypalAccount();
        this.cash = entry.getCash();
        this.createtime = new Date(entry.getCreateTime());
        this.isPay = entry.getIsPay();
        if(entry.getUpdateTime()>0){
            this.updatetime = new Date(entry.getUpdateTime());
        }
        this.username = entry.getUsername();
        this.openbank = entry.getOpenBank();
        this.phone = entry.getPhone();
        this.paytype = entry.getPayType();
        this.password = entry.getPassword();
		this.cardnum = entry.getCardNum()==null?"":entry.getCardNum();
		this.status = entry.getStatus();
		this.beiZhu = entry.getBeiZhu()==null?"":entry.getBeiZhu();


    }

	private String id;
	
	private String userid;
	
	private String paypalAccount;
	
	private double cash;
	
	private Date createtime;
	
	private int isPay;
	
	private Date updatetime;

	private String username;

	private String openbank;

	private String phone;

	private int paytype;

	private String password;

	private String schoolname;

	private String name;

	private String time;

	private String cardnum;

	private int status;

	private String beiZhu;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPaypalAccount() {
		return paypalAccount;
	}

	public void setPaypalAccount(String paypalAccount) {
		this.paypalAccount = paypalAccount;
	}

	public double getCash() {
		return cash;
	}

	public void setCash(double cash) {
		this.cash = cash;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

    public String getCreatetimeStr() {
        if(getCreatetime()!=null){
            return DateTimeUtils.dateToStrLong(getCreatetime(),DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H);
        }else{
            return "";
        }
    }

	public int getIsPay() {
		return isPay;
	}

	public void setIsPay(int isPay) {
		this.isPay = isPay;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

    public String getUpdatetimeStr() {
        if(getUpdatetime()!=null){
            return DateTimeUtils.dateToStrLong(getUpdatetime(),DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H);
        }else{
            return "";
        }
    }

	public String getUsername() {
		return username;
	}

	public String getOpenbank() {
		return openbank;
	}

	public String getPhone() {
		return phone;
	}

	public int getPaytype() {
		return paytype;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setOpenbank(String openbank) {
		this.openbank = openbank;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setPaytype(int paytype) {
		this.paytype = paytype;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSchoolname() {
		return schoolname;
	}

	public void setSchoolname(String schoolname) {
		this.schoolname = schoolname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getCardnum() {
		return cardnum;
	}

	public void setCardnum(String cardnum) {
		this.cardnum = cardnum;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getBeiZhu() {
		return beiZhu;
	}

	public void setBeiZhu(String beiZhu) {
		this.beiZhu = beiZhu;
	}

	/** 从当前传入的DTO产生Entry
     * @return
     */
    public WithDrawEntry buildWithDrawEntry(String newOrder){
        long createtime=this.getCreatetime()==null?0:this.getCreatetime().getTime();
        long updatetime=this.getUpdatetime()==null?0:this.getUpdatetime().getTime();
        WithDrawEntry entry = new WithDrawEntry(
                this.getUserid(),
                this.getPaypalAccount(),
                this.getCash(),
                createtime,
                this.getIsPay(),
                updatetime,
                this.getUsername(),
                this.getPassword(),
                this.getOpenbank(),
                this.getPhone(),
                this.getPaytype(),
				newOrder,
				this.cardnum
        );
        return entry;
    }
}
