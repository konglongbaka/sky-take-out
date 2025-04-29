package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    void addDishFlavors(List<DishFlavor> dishFlavor);

//    void deleteDishFlavors(List<Long> dishId);

    List<DishFlavor> selectByDishID(List<Long> dishId);
    @Select("select * from dish_flavor where dish_id = #{id}")
    List<DishFlavor> getByDishId(Long id);
    @Insert("insert into dish_flavor (dish_id, name, value)values(#{dishId},#{name},#{value})")
    void updateFlavor(DishFlavor flavor);
    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void deleteByDishId(Long dishId);
}
