package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {

    void insert(Orders order);

    Page<OrderVO> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderVO getById(Long orderId);

    void update(Orders order);
}
