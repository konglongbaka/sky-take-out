package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.anno.AutoFillInsert;
import com.sky.anno.AutoFillUpdate;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {
    /**
     * 分页查询菜品分类
     * @param categoryPageQueryDTO
     * @return
     */
    Page<Category> pageSelectCategory(CategoryPageQueryDTO categoryPageQueryDTO);
    @AutoFillInsert
    @Insert("insert into category (type, name, sort, status, create_time, update_time, create_user, update_user) values(#{type}, #{name}, #{sort}, #{status}, now(), now(), #{createUser}, #{updateUser})")
    void addCategory(Category category);
    @Select("select * from category where type = #{categoryType}")
    List<Category> selectByCategoryType(Integer categoryType);

    @Select("select * from category where id = #{id}")
    Category selectById(Long id);

    @AutoFillUpdate
    void updateCategory(Category category);
    @Delete("delete from category where id = #{id}")
    void deleteCategory(Long id);

    @Select("select * from category where status = 1")
    List<Category> selectAllWithStatus();
}
