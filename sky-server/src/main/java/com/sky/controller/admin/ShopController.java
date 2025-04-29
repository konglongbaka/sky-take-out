package com.sky.controller.admin;

import com.sky.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
public class ShopController {
    @Autowired
    private RedisTemplate redisTemplate;

    private static final String SHOP_STATUS = "shop_status";

    @GetMapping("/status")
    public Result<Object> getStatus() {
        String status = (String) redisTemplate.opsForValue().get(SHOP_STATUS);
        if (status == null) {
            redisTemplate.opsForValue().set(SHOP_STATUS, "1");
            status = "1";
        }
        return Result.success(Integer.parseInt(status));
    }
    @PutMapping("/{status}")
    public Result<String> updateStatus(@PathVariable String status) {
        status = "1";
        redisTemplate.opsForValue().set(SHOP_STATUS, status);
        return Result.success();
    }
}
