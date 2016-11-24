package com.pojo.activity;


import com.pojo.activity.enums.ActIvtStatus;
import org.bson.types.ObjectId;

/**
 * Created by Hao on 2014/10/22.
 */
public class ActInvitation {
    private String id;
    private String actId;
    private String guestId;
    private String msg;
    private ActIvtStatus status;
    public ActInvitation(){}
    public ActInvitation(ActInvitationEntry invitationEntry) {
        if(invitationEntry!=null){
            this.id=invitationEntry.getID().toString();
            this.actId=invitationEntry.getActId().toString();
            this.guestId=invitationEntry.getGuestId().toString();
            this.msg=invitationEntry.getMsg();
            this.status= ActIvtStatus.values()[invitationEntry.getStatus()];
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActId() {
        return actId;
    }

    public void setActId(String actId) {
        this.actId = actId;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ActIvtStatus getStatus() {
        return status;
    }

    public void setStatus(ActIvtStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActInvitation that = (ActInvitation) o;

        if (actId != null ? !actId.equals(that.actId) : that.actId != null) return false;
        if (guestId != null ? !guestId.equals(that.guestId) : that.guestId != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (msg != null ? !msg.equals(that.msg) : that.msg != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (actId != null ? actId.hashCode() : 0);
        result = 31 * result + (guestId != null ? guestId.hashCode() : 0);
        result = 31 * result + (msg != null ? msg.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

	@Override
	public String toString() {
		return "ActInvitation [id=" + id + ", actId=" + actId + ", guestId="
				+ guestId + ", msg=" + msg + ", status=" + status + "]";
	}


    public ActInvitationEntry exportEntry() {
        ActInvitationEntry actInvitationEntry=new ActInvitationEntry();

        actInvitationEntry.setActId(new ObjectId(this.getActId()));
        actInvitationEntry.setGuestId(new ObjectId(this.getGuestId()));
        actInvitationEntry.setMsg(this.getMsg());
        actInvitationEntry.setStatus(this.status.getState());

        return actInvitationEntry;
    }
}
