package com.dao.mapper;


import com.entity.UserCookieEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserCookieMapper {

    List<UserCookieEntity> queryById(Map<String,Object> parm);

}
