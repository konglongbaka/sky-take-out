package com.sky.controller.admin;

import com.sky.constant.CacheNames;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminSetMealController")
@RequestMapping("/admin/setmeal")
public class SetMealController {

    @Autowired
    private SetmealService setMealService;
    @GetMapping("page")
//    @Cacheable(cacheNames = CacheNames.ADMINSETMEAL, key = "'pageSelect'")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO)
    {
        PageResult pageResult = setMealService.page(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    public Result<SetmealVO> get(@PathVariable Long id) {
        SetmealVO setmealVO = setMealService.getById(id);
        return Result.success(setmealVO);
    }

    @PostMapping
    @CacheEvict(cacheNames = CacheNames.USERSETMEAL,key = "#setmealDTO.categoryId")
    public Result<String> save(@RequestBody SetmealDTO setmealDTO) {
        setMealService.save(setmealDTO);
        return Result.success();
    }

    @PostMapping("status/{status}")
    @CacheEvict(cacheNames = CacheNames.USERSETMEAL,allEntries = true)
    public Result<String> updateStatus(@PathVariable Integer status,Long id) {
        setMealService.updateStatus(status,id);
        return Result.success("修改成功");
    }

    @PutMapping
    @CacheEvict(cacheNames = CacheNames.USERSETMEAL,key = "#setmealDTO.categoryId")
    public Result<String> update(@RequestBody SetmealDTO setmealDTO){
        setMealService.update(setmealDTO);
        return Result.success("修改成功");
    }
    @DeleteMapping
    @CacheEvict(cacheNames = CacheNames.USERSETMEAL,allEntries = true)
    public Result<String> delete(@RequestParam List<Long> ids) {
        setMealService.deleteByIds(ids);
        return Result.success("删除成功");
    }
}
