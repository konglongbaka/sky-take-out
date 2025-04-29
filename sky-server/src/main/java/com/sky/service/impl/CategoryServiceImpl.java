package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public PageResult pageSelectCategory(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        Page<Category> categoryPage = categoryMapper.pageSelectCategory(categoryPageQueryDTO);
        return new PageResult(categoryPage.getTotal(),categoryPage.getResult());
    }

    @Override
    public void addCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        categoryMapper.addCategory(category);
    }


    @Override
    public List<Category> selectByCategoryType(Integer type) {
        List<Category> list;
        if (type == null) {
            list = categoryMapper.selectAllWithStatus();
        }
        else{
            list = categoryMapper.selectByCategoryType(type);
        }
        return list;
    }

    @Override
    public void updateCategoryStatus(Integer status, Long id) {
        Category category =  Category.builder()
                .id(id)
                .status(status)
                .build();
        categoryMapper.updateCategory(category);
    }

    @Override
    public void deleteCategory(Long id) {
        //判断是否关联菜品
        List<Dish> categoryBoole1 = dishMapper.selectByCategoryId(id);
        Category categories = categoryMapper.selectById(id);
        if (!categoryBoole1.isEmpty()) {
            throw new DeletionNotAllowedException("该分类下有关联菜品，无法删除");
        }
        if (categories.getStatus() == 1){
            throw new DeletionNotAllowedException("该分类正在售卖，无法删除");
        }
        categoryMapper.deleteCategory(id);
    }

    @Override
    public void updateCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        categoryMapper.updateCategory(category);
    }
}
