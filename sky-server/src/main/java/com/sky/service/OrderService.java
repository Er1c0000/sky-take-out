package com.sky.service;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {
    OrderSubmitVO submitOrder(OrdersSubmitDTO orderSubmitDTO);

    PageResult history(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderVO getOrderDetail(Long orderId);

    void cancelOrder(Long id);

    void repeatOrder(Long id);
}
