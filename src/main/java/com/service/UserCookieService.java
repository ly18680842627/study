package com.service;

import com.entity.UserCookieEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface UserCookieService {

    List<UserCookieEntity> queryById(Map<String,Object> parm);

}
