package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.anno.AutoFillInsert;
import com.sky.anno.AutoFillUpdate;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface SetmealMapper {



    List<SetmealDish> selectByDishID(List<Long> dishId);

    Page<SetmealVO> page(SetmealPageQueryDTO setmealPageQueryDTO);
    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);
    @AutoFillInsert
    void insert(Setmeal setmeal);
    @AutoFillUpdate
    void update(Setmeal setmeal);

    List<Setmeal> selectById(List<Long> ids);

    void deleteByIds(List<Long> ids);
    @Select("select * from setmeal where category_id = #{categoryId} and status = #{status}")
    List<Setmeal> selectByCategoryWithStatus(Setmeal setmeal);
    @Select("select * from setmeal where category_id = #{categoryId}")
    List<Setmeal> selectByCategoryId(Long categoryId);
    @Select("select count(*) from setmeal")
    Integer countByMap(Map map);
}
