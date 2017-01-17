package com.fulaan.connect;

import com.fulaan.ConnectException;
import com.fulaan.model.UserInfo;

/**
 * Created by moslpc on 2017/1/16.
 */
public interface Auth {

    String getAuthUrl();

    UserInfo getUserInfo(String uniqueId) throws ConnectException;
}
