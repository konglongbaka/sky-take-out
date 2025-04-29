package com.sky.controller.admin;

import com.sky.constant.CacheNames;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminDishController")
@RequestMapping("/admin/dish")
public class DishController {


    @Autowired
    private DishService dishService;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 分页查询菜品
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
//    @Cacheable(cacheNames = CacheNames.ADMINDISH, key = "'pageSelect'")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        return Result.success(dishService.page(dishPageQueryDTO));
    }

    /**
     * 根据分类查询
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    public Result<List<Dish>> list(Long categoryId) {
        List<Dish> dishList = dishService.listSelect(categoryId);
        return Result.success(dishList);
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<DishVO> get(@PathVariable Long id) {
        return Result.success(dishService.getById(id));
    }

    @PostMapping
    @CacheEvict(cacheNames =CacheNames.USERDISH,key = "#dishDTO.getCategoryId()")
    public  Result<String > addDish(@RequestBody DishDTO dishDTO){
        dishService.addDish(dishDTO);
        return Result.success();
    }

    @DeleteMapping
    @CacheEvict(cacheNames = CacheNames.USERDISH,allEntries = true)
    public Result<String> delete(@RequestParam List<Long> ids){
        dishService.deleteDish(ids);
        return Result.success();
    }

    @PutMapping
    @CacheEvict(cacheNames = CacheNames.USERDISH,key = "#dishDTO.getCategoryId()")
    public Result<String> update(@RequestBody DishDTO dishDTO){
        dishService.updateDish(dishDTO);
        return Result.success();
    }
    @PostMapping("status/{status}")
    @CacheEvict(cacheNames = CacheNames.USERDISH,allEntries = true)
    public Result<String> updateStatus(@PathVariable Integer status, Integer id){
        dishService.updateDishStatus(status,id);
        return Result.success();
    }
}
