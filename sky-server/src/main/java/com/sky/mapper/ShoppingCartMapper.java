package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface ShoppingCartMapper {
    
    List<ShoppingCart> selectByShoppingCart(ShoppingCart shoppingCart);

    void update(ShoppingCart shoppingCart);
    @Insert("insert into shopping_cart (name, image, user_id, dish_id, setmeal_id, dish_flavor, number, amount, create_time) values (#{name}, #{image}, #{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{number}, #{amount}, now())")
    void insert(ShoppingCart shoppingCart);
    @Delete("delete from shopping_cart where user_id = #{userId}")
    void deleteByUserId(Long userId);

    void deleteByDishOrSetmealId(ShoppingCart shoppingCart);
    @Select("select * from shopping_cart where user_id = #{id}")
    List<ShoppingCart> selectByUserId(Long id);
    @Delete("delete from shopping_cart where id = #{id}")
    void deleteById(Long id);

}
