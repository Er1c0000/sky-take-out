package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * 批量插入菜品对应的口味数据
     * @param flavors
     */
    void insertBatch(List<DishFlavor> flavors);

    /**
     * 根据菜品id查询对应的口味数据
     * @param dishIds
     * @return
     */
//    @Delete("delete from dish_flavor where dish_id = #{id}")
//    void deleteByDishId(Long dishId);

    void deleteByDishIds(List<Long> dishIds);

    /**
     * 根据菜品id查询对应的口味数据
     * @param id
     * @return
     */
    List<DishFlavor> getByDishId(Long id);

    void deleteByDishId(Long dishId);
}
