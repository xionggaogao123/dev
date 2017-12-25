package com.fulaan.groupgather.serviceImpl;

import com.fulaan.groupgather.dao.UserMapper;
import com.fulaan.groupgather.pojo.User;
import com.fulaan.groupgather.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by scott on 2017/12/22.
 */
@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public void saveUser(User user) {
        userMapper.save(user);
    }

    @Override
    public User findUserByUserId(String userId) {
        return userMapper.findByUserId(userId);
    }
}
