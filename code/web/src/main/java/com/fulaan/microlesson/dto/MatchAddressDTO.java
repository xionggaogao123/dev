package com.fulaan.microlesson.dto;

import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;

import java.util.List;

/**
 * Created by wang_xinxin on 2015/8/22.
 */
public class MatchAddressDTO {

    private String schoolname;

    private List<UserMatchDTO> userlist;

    public String getSchoolname() {
        return schoolname;
    }

    public List<UserMatchDTO> getUserlist() {
        return userlist;
    }

    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname;
    }

    public void setUserlist(List<UserMatchDTO> userlist) {
        this.userlist = userlist;
    }
}
