package com.sky.mapper;

import com.sky.entity.Dish;
import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface SetmealDishMapper {

    /**
     * 根据菜品id查询套餐id
     * @param dishIds
     * @return
     */
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);

    /**
     * 新增菜品和套餐关系
     * @param setmealDishs
     * @return
     */
    void insertBatch(List<SetmealDish> setmealDishs);

    /**
     * 根据套餐id查询菜品
     * @param setmealId
     * @return
     */
    List<Dish> getBySetmealId(Long setmealId);

    /**
     * 根据id查询套餐内菜品
     * @param setmealId
     * @return
     */
    List<SetmealDish> getSetmealDishByDishIds(Long setmealId);

    /**
     * 根据id删除套餐内菜品
     * @param id
     */
    void deleteBySetmealId(Long id);
}
