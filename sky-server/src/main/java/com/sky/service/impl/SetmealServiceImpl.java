package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.BaseException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 新增套餐
     * @param setmealDTO
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.insert(setmeal);
        // 保存套餐和菜品的关联关系

        //获得套餐id
        Long setmealId = setmeal.getId();
        List<SetmealDish> setmealDishs = setmealDTO.getSetmealDishes();
        setmealDishs.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });

        //保存套餐和菜品的关联关系
        setmealDishMapper.insertBatch(setmealDishs);


    }

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 设置套餐起售，停售状态
     * @param status
     */
    @Override
    public void startOrStop(Integer status, Long setmealId) {


        //如果要status是起售，则要判断套餐内菜品是否都为起售状态，如果不是则报错
        if(status== StatusConstant.ENABLE){
            //查询套餐内菜品是否都为起售状态
            //根据setmealId查询setmealDish表和Dish，如果status为o的菜品，则套餐无法起售
            List<Dish> dishList = setmealDishMapper.getBySetmealId(setmealId);
            if(dishList!= null && dishList.size()>0){
                for (Dish dish : dishList) {
                    if(dish.getStatus()== StatusConstant.DISABLE){
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                }
            }

        }

        Setmeal setmeal = Setmeal.builder()
                .status(status)
                .id(setmealId)
                .build();
        setmealMapper.update(setmeal);
    }


    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @Override
    public SetmealVO getByIdWithDish(Long id) {
        //根据id查询套餐
        Setmeal setmeal = setmealMapper.getById(id);
        //根据id查询套餐内的菜品
        List<SetmealDish> setmealDishs = setmealDishMapper.getSetmealDishByDishIds(id);
        //封装到setmealVO
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishs);
        return setmealVO;
    }


    /**
     * 修改套餐
     * @param setmealDTO
     */
    @Override
    @Transactional
    public void updateWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);

        Long setmealId = setmealDTO.getId();
        //删除原有的套餐和菜品的关联关系
        setmealDishMapper.deleteBySetmealId(setmealId);
        //保存新的套餐和菜品的关联关系
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if(setmealDishes != null || setmealDishes.size() != 0) {
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(setmealId);
            });
            setmealDishMapper.insertBatch(setmealDishes);
        }
    }

    /**
     * 根据id删除套餐
     * @param ids
     */
    @Override
    public void deleteByIds(List<Long> ids) {
        //判断套餐是否可以删除
        for (Long id : ids) {
            Setmeal setmeal = setmealMapper.getById(id);
            if(setmeal.getStatus()== StatusConstant.ENABLE){
                throw new BaseException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        //删除套餐
        setmealMapper.deleteByIds(ids);
    }
}
