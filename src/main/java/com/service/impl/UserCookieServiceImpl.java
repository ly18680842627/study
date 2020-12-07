package com.service.impl;

import com.dao.mapper.UserCookieMapper;
import com.entity.UserCookieEntity;
import com.service.UserCookieService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class UserCookieServiceImpl implements UserCookieService {
    @Autowired
    UserCookieMapper userCookieMapper;

    @Override
    public List<UserCookieEntity> queryById(Map<String, Object> parm) {
        return userCookieMapper.queryById(parm);
    }
}
