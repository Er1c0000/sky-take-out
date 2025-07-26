package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {
    OrderSubmitVO submitOrder(OrdersSubmitDTO orderSubmitDTO);

    PageResult history(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderVO getOrderDetail(Long orderId);

    void cancelOrder(Long id);

    void repeatOrder(Long id);

    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderStatisticsVO statistics();

    void confirm(OrdersConfirmDTO ordersConfirmDTO);

//    void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception;

    void cancel(OrdersCancelDTO ordersCancelDTO) throws Exception;

    void delivery(Long id);

    void complete(Long id);
}
