package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {
    PageResult pageSelectCategory(CategoryPageQueryDTO categoryPageQueryDTO);

    void addCategory(CategoryDTO categoryDTO);

    List<Category> selectByCategoryType(Integer categoryType);

    void updateCategoryStatus(Integer status, Long id);

    void deleteCategory(Long id);

    void updateCategory(CategoryDTO category);
}
