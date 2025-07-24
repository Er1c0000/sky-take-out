package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    /**
     * 用户下单
     * @param orderSubmitDTO
     * @return
     */
    @Override
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO orderSubmitDTO) {
        AddressBook addressBook = addressBookMapper.getById(orderSubmitDTO.getAddressBookId());
        if(addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        if(list == null || list.size() == 0) {
            throw new AddressBookBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        //向订单表插入1条数据
        Orders order = new Orders();
        BeanUtils.copyProperties(orderSubmitDTO, order);
        order.setOrderTime(LocalDateTime.now());
        order.setPayStatus(Orders.UN_PAID);
        order.setStatus(Orders.PENDING_PAYMENT);
        order.setNumber(String.valueOf(System.currentTimeMillis()));
        order.setPhone(addressBook.getPhone());
        order.setConsignee(addressBook.getConsignee());
        order.setUserId(userId);

        orderMapper.insert(order);

        List<OrderDetail> orderDetailList = new ArrayList<>();
        //向订单明细表插入多条数据
        for (ShoppingCart cart : list) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);

            orderDetail.setOrderId(order.getId());

            orderDetailList.add(orderDetail);
        }
        orderDetailMapper.insertBatch(orderDetailList);

        //清空当前用户购物车数据
        shoppingCartMapper.deleteByUserId(userId);

        //封装返回结果
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(order.getId())
                .orderNumber(order.getNumber())
                .orderAmount(order.getAmount())
                .orderTime(order.getOrderTime())
                .build();

        return orderSubmitVO;

    }

    /**
     * 历史订单查询
     * @return
     */
    @Override
    public PageResult history(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        // 设置当前用户ID
        Long userId = BaseContext.getCurrentId();
        ordersPageQueryDTO.setUserId(userId);
        // 执行分页查询
        Page<OrderVO> page = orderMapper.pageQuery(ordersPageQueryDTO);
        // 封装分页结果
        PageResult pageResult = new PageResult(page.getTotal(), page.getResult());
        // 设置订单明细
        for (OrderVO orderVO : page.getResult()) {
            orderVO.setOrderDetailList(orderDetailMapper.listByOrderId(orderVO.getId()));
        }

        return pageResult;

    }

    /**
     * 订单详情查询
     * @param orderId
     * @return
     */
    @Override
    public OrderVO getOrderDetail(Long orderId) {
        // 查询订单信息
        OrderVO orderVO = orderMapper.getById(orderId);

        // 查询订单明细
        List<OrderDetail> orderDetailList = orderDetailMapper.listByOrderId(orderId);
        orderVO.setOrderDetailList(orderDetailList);
        return orderVO;
    }


    /**
     * 取消订单
     * @param id
     */
    @Override
    public void cancelOrder(Long id) {
        // 查询订单信息
        Orders order = orderMapper.getById(id);
        if (order == null) {
            throw new AddressBookBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        // 更新订单状态为已取消
        order.setStatus(Orders.CANCELLED);
        order.setCancelTime(LocalDateTime.now());
        orderMapper.update(order);

    }

    /**
     * 再来一单
     * @param id
     */
    @Override
    public void repeatOrder(Long id) {
        // 查询订单信息
        Orders order = orderMapper.getById(id);
        if (order == null) {
            throw new AddressBookBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        // 查询订单明细
        List<OrderDetail> orderDetailList = orderDetailMapper.listByOrderId(id);
        if (orderDetailList == null || orderDetailList.isEmpty()) {
            throw new AddressBookBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Long userId = BaseContext.getCurrentId();

        List<ShoppingCart> shoppingCartList = orderDetailList.stream()
                .map(orderDetail -> ShoppingCart.builder()
                        .userId(userId)
                        .dishId(orderDetail.getDishId())
                        .setmealId(orderDetail.getSetmealId())
                        .number(orderDetail.getNumber())
                        .amount(orderDetail.getAmount())
                        .name(orderDetail.getName())
                        .image(orderDetail.getImage())
                        .build())
                .collect(Collectors.toList());

        shoppingCartMapper.insertBatch(shoppingCartList);


    }
}
