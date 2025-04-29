package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {


    SetmealVO getById(Long id);


    PageResult page(SetmealPageQueryDTO setmealPageQueryDTO);

    void updateStatus(Integer status, Long id);

    void save(SetmealDTO setmealDTO);

    void update(SetmealDTO setmealDTO);

    void deleteByIds(List<Long> ids);

    List<Setmeal> selectByCategoryWithStatus(Setmeal setmeal);

    List<DishItemVO> getDishItemById(Long id);
}
