package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealSeviceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Override
    public SetmealVO getById(Long id) {
        SetmealVO setmealVO = new SetmealVO();

        // 1. 获取套餐基本信息
        Setmeal setmeal = setmealMapper.getById(id);
        if (setmeal == null) {
            throw new RuntimeException("套餐不存在");
        }
        BeanUtils.copyProperties(setmeal, setmealVO);

        // 2. 获取套餐包含的菜品信息
        List<SetmealDish> setmealDishes = setmealDishMapper.listBySetmealId(id);
        setmealVO.setSetmealDishes(setmealDishes);

        // 3. 获取分类名称
        Category category = categoryMapper.selectById(setmeal.getCategoryId());
        String categoryName = (category == null) ? "未知" : category.getName();
        setmealVO.setCategoryName(categoryName);

        return setmealVO;
    }

    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> setmealPage = setmealMapper.page(setmealPageQueryDTO);
        Long total = setmealPage.getTotal();
        final String[] categoryName = {null};
        List<SetmealVO> records = setmealPage.getResult();
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

        return new PageResult(total,records);
    }

    @Override
    public void updateStatus(Integer status, Long id) {
        Setmeal setmeal = new Setmeal();
        setmeal.setStatus(status);
        setmeal.setId(id);
        setmealMapper.update(setmeal);

    }

    @Override
    @Transactional
    public void save(SetmealDTO setmealDTO) {
        // 1. 保存套餐基本信息
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.insert(setmeal);

        // 2. 获取生成的套餐ID
        Long setmealId = setmeal.getId();

        // 3. 保存套餐菜品关联关系
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            setmealDishes.forEach(dish -> {
                dish.setSetmealId(setmealId);  // 设置关联的套餐ID// 设置默认排序值
            });
            setmealDishMapper.batchInsert(setmealDishes);  // 批量插入
        }
    }

    @Override
    @Transactional
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal =new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        Long setmealId = setmeal.getId();
        setmealDishMapper.deleteAll(setmealId);
        for (SetmealDish dish : setmealDishes) {
            dish.setSetmealId(setmealId);
            setmealDishMapper.updateDish(dish);
        }
        setmealMapper.update(setmeal);
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        List<Setmeal> setmealIdBool1 = setmealMapper.selectById(ids);
        setmealIdBool1.forEach(setmeal -> {
            if (setmeal.getStatus() == 1) {
                throw new DeletionNotAllowedException("套餐正在售卖中，无法删除");
            }
        });
        setmealMapper.deleteByIds(ids);
    }

    @Override
    public List<Setmeal> selectByCategoryWithStatus(Setmeal setmeal) {
        return setmealMapper.selectByCategoryWithStatus(setmeal);
    }

    @Override
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealDishMapper.selectDishBySetmealId(id);
    }
}



