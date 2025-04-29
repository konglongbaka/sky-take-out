package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public Result<String> addShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        shoppingCartService.addShoppingCart(shoppingCartDTO);
        return Result.success();
    }

    @GetMapping("list")
    public Result<List<ShoppingCart>> list() {

        List<ShoppingCart> shoppingCartList = shoppingCartService.getByUserId();
        return Result.success(shoppingCartList);
    }
    @DeleteMapping("clean")
    public  Result<String> clean(){
        shoppingCartService.clean();
        return Result.success();
    }
    @PostMapping("/sub")
    public Result<String> sub(@RequestBody ShoppingCartDTO shoppingCartDTO){
        shoppingCartService.delete(shoppingCartDTO);
        return Result.success();
    }
}
