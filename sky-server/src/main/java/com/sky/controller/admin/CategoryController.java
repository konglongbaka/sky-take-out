package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminCategoryController")
@Slf4j
@RequestMapping("/admin/category")
//400代表没有接受到参数
//500代表程序报错
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 分页查询菜品分类
     *
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
//    @Cacheable(cacheNames = CacheNames.ADMINCATEGORY, key = "'pageSelect'")
    public Result<PageResult> pageSelectCategory(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageResult pageResult = categoryService.pageSelectCategory(categoryPageQueryDTO);
        return Result.success(pageResult);
    }


    /**
     * 新增菜品分类
     *
     * @param categoryDTO
     * @return
     */
    @PostMapping
    public Result<String> addCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.addCategory(categoryDTO);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<Category>> selectByCategory(Integer type) {
        List<Category> result =  categoryService.selectByCategoryType(type);
            return Result.success(result);
    }

    /**
     * 更改商品状态
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    public Result<String> updateCategoryStatus(@PathVariable Integer status, Long id) {
        categoryService.updateCategoryStatus(status, id);
        return Result.success();
    }
    /**
     * 修改菜品分类
     *
     * @param categoryDTO
     * @return
     */
    @PutMapping
    public Result<String> updateCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.updateCategory(categoryDTO);
        return Result.success();
    }

    /**
     * 删除菜品分类
     *
     * @param id
     * @return
     **/
    @DeleteMapping
    public Result<String> deleteCategory(Long id) {
        categoryService.deleteCategory(id);
        return Result.success();
    }
}