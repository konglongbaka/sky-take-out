package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        //判断用户是否买过商品
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.selectByShoppingCart(shoppingCart);
        //如果买过，则修改数量
        if (!shoppingCarts.isEmpty()){
            shoppingCart.setNumber(shoppingCarts.get(0).getNumber() + 1);
            shoppingCartMapper.update(shoppingCart);
        }
        //如果没买过，则新增
        else {
            if (shoppingCart.getSetmealId()!=null){
                Setmeal setmeal = setmealMapper.getById(shoppingCart.getSetmealId());
                shoppingCart.setAmount(setmeal.getPrice());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
            }
            else {
                Dish dish = dishMapper.getById(shoppingCart.getDishId());
                shoppingCart.setAmount(dish.getPrice());
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setDishFlavor(shoppingCartDTO.getDishFlavor());
            }
            shoppingCart.setNumber(1);
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    @Override
    public List<ShoppingCart> getByUserId() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(BaseContext.getCurrentId());
        return shoppingCartMapper.selectByShoppingCart(shoppingCart);
    }

    @Override
    public void clean() {
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.deleteByUserId(userId);
    }

    @Override
    @Transactional
    public void delete(ShoppingCartDTO shoppingCartDTO) {
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        shoppingCart.setUserId(userId);
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.selectByShoppingCart(shoppingCart);
        //如果数量大于1
        if (shoppingCarts.get(0).getNumber()>1){
            shoppingCart.setNumber(shoppingCarts.get(0).getNumber() - 1);
            shoppingCartMapper.update(shoppingCart);
        }
        else {
            shoppingCartMapper.deleteByDishOrSetmealId(shoppingCart);
        }

    }

}
