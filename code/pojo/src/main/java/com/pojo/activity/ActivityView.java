package com.pojo.activity;


import java.io.Serializable;

/**
 * Created by Hao on 2014/10/22.
 */
public class ActivityView implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1893793139822197404L;
	private Activity act;
    private String guestIds;
    private String message;
    
    public Activity getAct() {
        return act;
    }

    public void setAct(Activity act) {
        this.act = act;
    }


	public String getGuestIds() {
		return guestIds;
	}

	public void setGuestIds(String guestIds) {
		this.guestIds = guestIds;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ActivityView [act=" + act + ", guestIds=" + guestIds
				+ ", message=" + message + "]";
	}
    
    
}
