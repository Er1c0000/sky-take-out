package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    /**
     * 查询购物车
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    void updateNumberById(ShoppingCart cart);

    void insert(ShoppingCart shoppingCart);

    void deleteByUserId(Long userId);

    void deleteById(Long id);

    void insertBatch(List<ShoppingCart> shoppingCartList);
}
