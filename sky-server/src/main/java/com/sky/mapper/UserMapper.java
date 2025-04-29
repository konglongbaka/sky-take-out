package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface UserMapper {

    User selectByOpenid(String openid);


    void saveUser(User user);
    @Select("select * from user where id = #{id}")
    User getById(Long userId);
    @Select("select count(*) from user")
    Integer countByMap(Map map);
}
