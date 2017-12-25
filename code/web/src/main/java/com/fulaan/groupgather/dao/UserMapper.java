package com.fulaan.groupgather.dao;

import com.fulaan.groupgather.pojo.User;

/**
 * Created by scott on 2017/12/22.
 */

public interface UserMapper {

    public void save(User user);

    public User findByUserId(String userId);
}
