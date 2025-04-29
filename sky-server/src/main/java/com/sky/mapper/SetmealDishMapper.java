package com.sky.mapper;

import com.sky.entity.SetmealDish;
import com.sky.vo.DishItemVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    @Select("select * from setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> listBySetmealId(Long id);

    void batchInsert(List<SetmealDish> setmealDishes);
    @Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
    void deleteAll(Long setmealId);
    @Insert("insert into setmeal_dish (setmeal_id, dish_id, name, price, copies) values (#{setmealId},#{dishId},#{name},#{price},#{copies})")
    void updateDish(SetmealDish dish);

// 根据套餐id查询套餐
    List<SetmealDish> selectBySetmealId(List<Long> ids);
    @Select("select * from setmeal_dish where setmeal_id = #{id}")
    List<DishItemVO> selectDishBySetmealId(Long id);
}
