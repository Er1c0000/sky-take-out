package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单相关定时任务，定时处理订单状态
 */
@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理超时订单
     */
    @Scheduled(cron = "0 0/1 * * * ?")
//    @Scheduled(cron = "0/5 * * * * ?")
    public void processTimeOutOrder() {
        // 这里可以添加处理订单状态的逻辑
        log.info("定时处理超时订单：{}", LocalDateTime.now());
        LocalDateTime time = LocalDateTime.now().minusMinutes(15);
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, time);
        if(ordersList != null && ordersList.size() > 0) {
            for (Orders order : ordersList) {
                // 更新订单状态为已取消
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason("超时未支付，已自动取消");
                order.setCancelTime(LocalDateTime.now());
                orderMapper.update(order);
                log.info("订单号：{} 已取消", order.getId());
            }
        } else {
            log.info("没有超时订单需要处理");
        }
    }

    /**
     * 处理一直处于派送中状态订单
     */
    @Scheduled(cron = "0 0 1 * * ? ") // 每天凌晨1点执行
//    @Scheduled(cron = "0/5 * * * * ?") // 每30分钟执行
    public void processPendingOrders() {
        log.info("定时处理派送中订单：{}", LocalDateTime.now());

        LocalDateTime time = LocalDateTime.now().minusMinutes(60);

        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, time);
        if (ordersList != null && !ordersList.isEmpty()) {
            for (Orders order : ordersList) {
                // 更新订单状态为已完成
                order.setStatus(Orders.COMPLETED);
                order.setDeliveryTime(LocalDateTime.now());
                orderMapper.update(order);
                log.info("订单号：{} 已完成", order.getId());
            }
        } else {
            log.info("没有派送中订单需要处理");
        }

    }
}
