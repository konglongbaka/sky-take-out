package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> dishPage = dishMapper.pageSelect(dishPageQueryDTO);
        Long total = dishPage.getTotal();
        List<DishVO> records = dishPage.getResult();
        final String[] categoryName = {null};
        records.forEach(record->{
            Category category = categoryMapper.selectById(record.getCategoryId());
            if (category == null) {
                categoryName[0] = "未知";
            }
            else {
                categoryName[0] = category.getName();
            }
            record.setCategoryName(categoryName[0]);
        });
        return new PageResult(total, records);

    }

    @Override
    public List<Dish> listSelect(Long categoryId) {
        return dishMapper.selectByCategoryId(categoryId);
    }

    @Override
    public DishVO getById(Long id) {
        DishVO dishVO = new DishVO();
        Dish dish = dishMapper.getById(id);
        BeanUtils.copyProperties(dish, dishVO);
        List<DishFlavor> flavors = dishFlavorMapper.getByDishId(id);
        Category categroy = categoryMapper.selectById(dish.getCategoryId());
        String categroyName = null;
        if (categroy == null) {
            categroyName = "未知";
        }
        else {
            categroyName = categroy.getName();
        }
        dishVO.setCategoryName(categroyName);
        dishVO.setFlavors(flavors);
        return dishVO;
    }

    @Override
    @Transactional
    public void addDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.addDish(dish);
        Long dish_id = dish.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        flavors.forEach(
                flavor -> flavor.setDishId(dish_id)
        );
//        for (DishFlavor flavor : flavors) {
//            flavor.setDishId(dish_id);
//        }
        dishFlavorMapper.addDishFlavors(flavors);

    }

    @Override
    @Transactional
    public void deleteDish(List<Long> ids) {

        List<SetmealDish> dishIdBool1 = setmealMapper.selectByDishID(ids);
        Long dishIdBool2 = dishMapper.selectDishStatus(ids);

        if (!dishIdBool1.isEmpty()) {
            throw new DeletionNotAllowedException("删除失败，菜品有关联套餐正在被使用");
        } else if (dishIdBool2 !=0) {
            throw new DeletionNotAllowedException("删除失败，菜品正在被售卖");
        }
        dishMapper.deleteDish(ids);
        ids.forEach(id->{
            dishFlavorMapper.deleteByDishId(id);
        });

    }

    @Override
    @Transactional
    public void updateDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.updateDish(dish);
        List<DishFlavor> flavors = dishDTO.getFlavors();
        Long dishId = dish.getId();
        dishFlavorMapper.deleteByDishId(dishId);
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishId);
            dishFlavorMapper.updateFlavor(flavor);
        }

    }

    @Override
    public void updateDishStatus(Integer status, Integer id) {
        Dish dish = new Dish();
        dish.setStatus(status);
        dish.setId(id.longValue());
        dishMapper.updateDish(dish);
    }

    @Override
    public List<DishVO> listWithFlavor(Dish dish) {
        List<DishVO> dishVOS = dishMapper.selectByCategoryIdWithStatus(dish);
        dishVOS.forEach(dishVO -> {
            Category categroy = categoryMapper.selectById(dishVO.getCategoryId());
            String categroyName = null;
            if (categroy == null) {
                categroyName = "未知";
            }
            else {
                categroyName = categroy.getName();
            }
            dishVO.setCategoryName(categroyName);
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(dishVO.getId());
            dishVO.setFlavors(flavors);
        });
        return dishVOS;

    }

}
