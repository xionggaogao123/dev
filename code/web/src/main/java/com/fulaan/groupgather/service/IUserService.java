package com.fulaan.groupgather.service;

import com.fulaan.groupgather.pojo.User;

/**
 * Created by scott on 2017/12/22.
 */
public interface IUserService {

    public void saveUser(User user);

    public User findUserByUserId(String userId);
}
