package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    PageResult page(DishPageQueryDTO dishPageQueryDTO);

    List<Dish> listSelect(Long categoryId);

    DishVO getById(Long id);

    void addDish(DishDTO dishDTO);

    void deleteDish(List<Long> ids);

    void updateDish(DishDTO dishDTO);

    void updateDishStatus(Integer status, Integer id);

    List<DishVO> listWithFlavor(Dish dish);
}
