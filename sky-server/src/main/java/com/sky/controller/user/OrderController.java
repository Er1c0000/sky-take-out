package com.sky.controller.user;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController("userOrderController")
@RequestMapping("/user/order")
@Api(tags = "用户端订单接口")
public class OrderController {
    @Autowired
    private OrderService orderService;


    /**
     * 用户下单
     * @param orderSubmitDTO
     * @return
     */
    @PostMapping("/submit")
    @ApiOperation("用户下单")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO orderSubmitDTO) {
        // 订单提交逻辑
        log.info("用户提交订单，参数为: {}", orderSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(orderSubmitDTO);
        // 返回结果
        return Result.success(orderSubmitVO);
    }

    /**
     * 历史订单查询
     * @param ordersPageQueryDTO
     * @return
     */
    @GetMapping("/historyOrders")
    @ApiOperation("历史订单查询")
    public Result<PageResult> history(OrdersPageQueryDTO ordersPageQueryDTO) {
        // 历史订单查询逻辑
        log.info("用户查询历史订单");
        PageResult pageResult = orderService.history(ordersPageQueryDTO);
        // 返回结果
        return Result.success(pageResult);
    }

    /**
     * 订单详情查询
     * @param orderId
     * @return
     */
    @GetMapping("orderDetail/{id}")
    @ApiOperation("订单详情查询")
    public Result orderDetail(@PathVariable("id") Long orderId) {
        // 订单详情查询逻辑
        log.info("用户查询订单详情，订单ID: {}", orderId);
        OrderVO orderDetail = orderService.getOrderDetail(orderId);
        // 返回结果
        return Result.success(orderDetail);
    }

    /**
     * 取消订单
     * @param id
     * @return
     */
    @PutMapping("cancel/{id}")
    @ApiOperation("取消订单")
    public Result cancel(@PathVariable("id") Long id) {
        // 取消订单逻辑
        log.info("用户取消订单，订单ID: {}", id);
        orderService.cancelOrder(id);
        // 返回结果
        return Result.success();
    }

    /**
     * 再来一单
     * @param id
     * @return
     */
    @PostMapping("repetition/{id}")
    @ApiOperation("再来一单")
    public Result repetition(@PathVariable("id") Long id) {
        // 再来一单逻辑，将原订单中的商品，重新添加到购物车中
        log.info("用户再来一单，订单ID: {}", id);
        orderService.repeatOrder(id);
        // 返回结果
        return Result.success();
    }

}
