package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.anno.AutoFillInsert;
import com.sky.anno.AutoFillUpdate;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DishMapper {

    Page<DishVO> pageSelect(DishPageQueryDTO dishPageQueryDTO);

    @Select("SELECT * FROM dish WHERE category_id = #{categoryId} order by update_time desc")
    List<Dish> selectByCategoryId(Long categoryId);

    @Select("SELECT * FROM dish WHERE id = #{id}")
    Dish getById(Long id);

    @AutoFillInsert
    void addDish(Dish dish);


    void deleteDish(List<Long> ids);

    Long selectDishStatus(List<Long> ids);
    @AutoFillUpdate
    void updateDish(Dish dish);
    @Select("SELECT * FROM dish WHERE category_id = #{categoryId} AND status = #{status} order by update_time desc")
    List<DishVO> selectByCategoryIdWithStatus(Dish dish);
    @Select("SELECT COUNT(*) FROM dish")
    Integer countByMap(Map map);
}
