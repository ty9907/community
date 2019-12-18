package com.example.community.service;

import com.example.community.mapper.UserMapper;
import com.example.community.model.User;
import com.example.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());
        List<User> users=userMapper.selectByExample(userExample);    //查询数据库里是否有该用户
        if(users.size()==0){
            //数据库无该用户，插入用户
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else {
            //数据库有该用户，更新用户
            //dbUser.setId(user.getId()); user未设置id，导致数据库更新失败，拦截器查询token无用户，一直处于未登录状态
            User dbUser=users.get(0);
            User updateUser=new User();
            updateUser.setName(user.getName());
            updateUser.setGmtCreate(System.currentTimeMillis());
            updateUser.setGmtModified(dbUser.getGmtCreate());
            updateUser.setAvatarUrl(user.getAvatarUrl());
            updateUser.setToken(user.getToken());
            UserExample example=new UserExample();
            userExample.createCriteria().andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser,example);
        }
    }
}
