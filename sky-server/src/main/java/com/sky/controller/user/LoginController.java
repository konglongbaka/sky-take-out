package com.sky.controller.user;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/user")
@Slf4j
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProperties jwtProperties;
    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO){
        User user =  userService.login(userLoginDTO);
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setId(user.getId());
        userLoginVO.setOpenid(user.getOpenid());
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
        userLoginVO.setToken(token);
        log.info("该用户分配的jwt:{}",token);
        return Result.success(userLoginVO);
    }
}
