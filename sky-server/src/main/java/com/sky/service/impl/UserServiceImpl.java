package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatProperties weChatProperties;
    private final static String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";
    @Override
    public User login(UserLoginDTO userLoginDTO) {
        log.info("通过code获取openid");
        String openid = getOpenid(userLoginDTO);

        if(openid == null){
            throw new RuntimeException("openid为空");
        }

        User user = userMapper.selectByOpenid(openid);

        if(user == null){
            log.info("用户不存在，创建用户");
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.saveUser(user);
        }
        return user;
    }

    private String getOpenid(UserLoginDTO userLoginDTO) {

        Map<String, String> map = new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code", userLoginDTO.getCode());
        map.put("grant_type","authorization_code");
        String data = HttpClientUtil.doGet(WX_LOGIN, map);
        JSONObject jsonObject = JSON.parseObject(data);
        String openid = jsonObject.getString("openid");
        return openid;
    }
}
